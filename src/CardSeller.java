import java.io.*;

public class CardSeller {

    public static boolean sellCard(PrintWriter out, BufferedReader in, String cardId, int price) throws IOException {
        String command = String.format("SELL %s %d", cardId, price);
        out.println(command);

        String response = in.readLine();
        if (response == null) {
            System.err.println("No response from server after SELL command.");
            return false;
        }

        if (response.equals("OK")) {
            System.out.println("Successfully placed sell order for card " + cardId + " at price " + price);
            return true;
        } else if (response.equals("ERROR")) {
            System.out.println("Failed to place sell order for card " + cardId);
            return false;
        } else {
            System.out.println("Unexpected response after SELL command: " + response);
            return false;
        }
    }
}
