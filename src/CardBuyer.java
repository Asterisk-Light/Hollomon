import java.io.*;

public class CardBuyer {

    private final PrintWriter out;
    private final BufferedReader in;

    public CardBuyer(PrintWriter out, BufferedReader in) {
        this.out = out;
        this.in = in;
    }
    public boolean buyCard(String cardId) throws Exception {
        out.println("BUY " + cardId);

        String response = in.readLine();
        if (response == null) {
            System.err.println("No response from server after BUY command.");
            return false;
        }

        if (response.equals("OK")) {
            System.out.println("Purchase successful for card ID: " + cardId);
            return true;
        } else if (response.equals("ERROR")) {
            System.out.println("Purchase failed for card ID: " + cardId);
            return false;
        } else {
            System.out.println("Unexpected response: " + response);
            return false;
        }
    }
}
