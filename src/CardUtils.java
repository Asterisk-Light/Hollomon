import java.io.*;
import java.util.*;

public class CardUtils {

    // Define rank order map here
    private static final Map<String, Integer> rankOrder = new HashMap<>();
    static {
        rankOrder.put("UNIQUE", 1);
        rankOrder.put("RARE", 2);
        rankOrder.put("UNCOMMON", 3);
        rankOrder.put("COMMON", 4);
        // Add other ranks as needed, assign higher numbers for lower priority
    }

    // Comparator for sorting cards by rank, name, and numeric ID
    public static final Comparator<Card> CARD_COMPARATOR = (c1, c2) -> {
        int rank1 = rankOrder.getOrDefault(c1.getRank(), Integer.MAX_VALUE);
        int rank2 = rankOrder.getOrDefault(c2.getRank(), Integer.MAX_VALUE);

        int rankCompare = Integer.compare(rank1, rank2);
        if (rankCompare != 0) {
            return rankCompare;
        }

        int nameCompare = c1.getName().compareTo(c2.getName());
        if (nameCompare != 0) {
            return nameCompare;
        }

        int id1 = Integer.parseInt(c1.getId());
        int id2 = Integer.parseInt(c2.getId());
        return Integer.compare(id1, id2);
    };

    /**
     * Reads cards from the server input stream until "OK" line is received.
     * Each card is expected to be sent as 5 lines:
     * "CARD", id, name, rank, lastSalePrice
     */
    public static List<Card> readCards(BufferedReader in) throws IOException {
        List<Card> cards = new ArrayList<>();
        String line;

        while ((line = in.readLine()) != null) {
            if ("OK".equals(line)) {
                break; // End of card list
            }

            if ("CARD".equals(line)) {
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

    /**
     * Requests cards from the server, sorts them by rank, name, and ID,
     * then prints them out.
     */
    public static void printSortedCards(PrintWriter out, BufferedReader in) throws IOException {
        List<Card> cards = requestCards(out, in);

        // Sort cards using the predefined comparator
        Collections.sort(cards, CARD_COMPARATOR);

        // Print sorted cards
        for (Card card : cards) {
            System.out.println(card);
        }
    }
}
