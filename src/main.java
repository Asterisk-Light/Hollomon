import java.io.*;
import java.net.Socket;
import java.util.List;
import java.util.Scanner;

public class main {

    private static List<Card> requestOffers(PrintWriter out, BufferedReader in) throws Exception {
        out.println("OFFERS");
        return CardUtils.readCards(in);
    }


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

            // sends login to server
            out.println(username.toLowerCase());
            out.println(password);


            String responseLine;
            boolean loginSuccess = false;

            while ((responseLine = in.readLine()) != null) {
                System.out.println("Server: " + responseLine);

                String lowerResp = responseLine.toLowerCase();
                if (lowerResp.contains("welcome") || lowerResp.contains("success")) {
                    loginSuccess = true;
                    break;
                } else if (lowerResp.contains("fail") || lowerResp.contains("error") || lowerResp.contains("invalid")) {
                    System.err.println("Login failed: " + responseLine);
                    return;
                }

            }

            if (!loginSuccess) {
                System.err.println("Login failed: No success message received.");
                return;
            }

            System.out.println("Login successful!");
            CardBuyer cardBuyer = new CardBuyer(out, in);

            boolean exit = false;
            while (!exit) {
                Thread.sleep(100);

                while (in.ready()) {
                    in.readLine();
                }

                System.out.println("\nUser Menu:");
                System.out.println("1. list my cards");
                System.out.println("2. show my credits");
                System.out.println("3. list available cards");
                System.out.println("4. buy card");
                System.out.println("5. sell card");
                System.out.println("6. auto-trading");
                System.out.println("7. exit");
                System.out.print("Please enter your choice: ");

                String choice = scanner.nextLine();

                switch (choice) {
                    case "1":
                        System.out.println("Listing your cards...");
                        List<Card> cards = CardUtils.requestCards(out, in);
                        cards.sort(CardUtils.CARD_COMPARATOR);
                        System.out.println("Your cards:");
                        for (Card c : cards) {
                            System.out.println(c);
                        }
                        break;
                    case "2":
                        System.out.println("Showing your credits...");
                        int credits = CreditsUtil.requestCredits(out, in);
                        if (credits >= 0) {
                            System.out.println("Your current credits: " + credits);
                        } else {
                            System.out.println("Failed to retrieve credits.");
                        }
                        break;
                    case "3":
                        System.out.println("Listing available cards...");
                        // Example: out.println("LIST_AVAILABLE_CARDS");
                        List<Card> offers = requestOffers(out, in);
                        offers.sort(CardUtils.CARD_COMPARATOR);
                        System.out.println("Available cards for sale:");
                        for (Card card : offers) {
                            System.out.println(card);
                        }
                        break;
                    case "4":
                        System.out.print("Enter the card ID to buy: ");
                        String cardIdToBuy = scanner.nextLine().trim();
                        if (!cardIdToBuy.isEmpty()) {
                            try {
                                cardBuyer.buyCard(cardIdToBuy);
                            } catch (Exception e) {
                                System.err.println("Error while buying card: " + e.getMessage());
                            }
                        } else {
                            System.out.println("Card ID cannot be empty.");
                        }
                        break;

                    case "5":
                        System.out.println("Selling a card...");

                        System.out.print("Enter card ID to sell: ");
                        String cardId = scanner.nextLine().trim();

                        System.out.print("Enter price to sell for: ");
                        int price = Integer.parseInt(scanner.nextLine().trim());

                        try {
                            boolean success = CardSeller.sellCard(out, in, cardId, price);
                            if (success) {
                                System.out.println("Sell order placed successfully.");
                            } else {
                                System.out.println("Sell order failed.");
                            }
                        } catch (IOException e) {
                            System.err.println("Error communicating with server: " + e.getMessage());
                        }

                        break;
                    case "6":
                        System.out.println("Auto-trading (Not working right now)");
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
