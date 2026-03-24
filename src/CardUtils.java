import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

class Card {
    String id;
    String name;
    String rank;
    int lastSalePrice;

    public Card(String id, String name, String rank, int lastSalePrice) {
        this.id = id;
        this.name = name;
        this.rank = rank;
        this.lastSalePrice = lastSalePrice;
    }

    @Override
    public String toString() {
        return String.format("Card ID: %s, Name: %s, Rank: %s, Last Sale Price: %d", id, name, rank, lastSalePrice);
    }
}

public class CardUtils {

    /**
     * Reads cards from the server until "OK" line is received.
     * Assumes the BufferedReader is positioned right after login success or after sending "CARDS" command.
     */
    public static List<Card> readCards(BufferedReader in) throws IOException {
        List<Card> cards = new ArrayList<>();
        String line;

        while ((line = in.readLine()) != null) {
            if (line.equals("OK")) {
                break; // End of card list
            }

            if (line.equals("CARD")) {
                // Read next 4 lines for card details
                String id = in.readLine();
                String name = in.readLine();
                String rank = in.readLine();
                String priceStr = in.readLine();
                int price = 0;
                try {
                    price = Integer.parseInt(priceStr);
                } catch (NumberFormatException e) {
                    System.err.println("Invalid price format for card " + id);
                }

                Card card = new Card(id, name, rank, price);
                cards.add(card);
            } else {
                // Unexpected line, you can log or ignore
                System.err.println("Unexpected line while reading cards: " + line);
            }
        }

        return cards;
    }

    /**
     * Sends the "CARDS" command to the server and reads the card list.
     */
    public static List<Card> requestCards(PrintWriter out, BufferedReader in) throws IOException {
        out.println("CARDS");
        return readCards(in);
    }
}
