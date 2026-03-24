import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.List;
import java.util.Scanner;

public class SimpleLoginClient {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        String serverAddress = "netsrv.cim.rhul.ac.uk";
        int serverPort = 1812;

        System.out.print("Username: ");
        String username = scanner.nextLine().trim();
        if (username.isEmpty()) {
            System.err.println("Username cannot be empty. Exiting.");
            return;
        }

        System.out.print("Password: ");
        String password = scanner.nextLine().trim();
        if (password.isEmpty()) {
            System.err.println("Password cannot be empty. Exiting.");
            return;
        }

        try (Socket socket = new Socket(serverAddress, serverPort);
             PrintWriter out = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()), true);
             BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()))) {

            // Send login credentials
            out.println(username.toLowerCase());
            out.println(password);

            // Read server responses after login attempt
            String responseLine;
            boolean loginSuccess = false;

            while ((responseLine = in.readLine()) != null) {
                System.out.println("Server: " + responseLine);

                // Adjust these conditions based on your server's actual messages
                String lowerResp = responseLine.toLowerCase();
                if (lowerResp.contains("welcome") || lowerResp.contains("success")) {
                    loginSuccess = true;
                    break;
                } else if (lowerResp.contains("fail") || lowerResp.contains("error") || lowerResp.contains("invalid")) {
                    System.err.println("Login failed: " + responseLine);
                    return;  // Exit on failure
                }

                // If server sends other info or prompts, continue reading
            }

            if (!loginSuccess) {
                System.err.println("Login failed: No success message received.");
                return;
            }

            System.out.println("Login successful!");

            boolean exit = false;
            while (!exit) {
                System.out.println("\nUser Menu:");
                System.out.println("1. list my cards");
                System.out.println("2. show my credits");
                System.out.println("3. list available cards");
                System.out.println("4. buy card");
                System.out.println("5. sell card");
                System.out.println("6. auto-trading [optional]");
                System.out.println("7. exit");
                System.out.print("Please enter your choice: ");

                String choice = scanner.nextLine();

                switch (choice) {
                    case "1":
                        System.out.println("Listing your cards...");
                        List<Card> cards = CardUtils.requestCards(out, in);
                        System.out.println("Your cards:");
                        for (Card c : cards) {
                            System.out.println(c);
                        }
                        // Example: out.println("LIST_CARDS");
                        break;
                    case "2":
                        System.out.println("Showing your credits...");
                        // Example: out.println("SHOW_CREDITS");
                        break;
                    case "3":
                        System.out.println("Listing available cards...");
                        // Example: out.println("LIST_AVAILABLE_CARDS");
                        break;
                    case "4":
                        System.out.println("Buying a card...");
                        // Example: out.println("BUY_CARD");
                        break;
                    case "5":
                        System.out.println("Selling a card...");
                        // Example: out.println("SELL_CARD");
                        break;
                    case "6":
                        System.out.println("Auto-trading feature not implemented.");
                        break;
                    case "7":
                        System.out.println("Exiting...");
                        exit = true;
                        break;
                    default:
                        System.out.println("Invalid choice. Please try again.");
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
