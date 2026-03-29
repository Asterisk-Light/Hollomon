import java.io.*;

public class CreditsUtil {

    public static int requestCredits(PrintWriter out, BufferedReader in) throws Exception {
        out.println("CREDITS");

        String line;
        String creditsLine = null;

        while ((line = in.readLine()) != null) {
            System.out.println("Server (credits response): " + line);
            if (line.equals("OK")) {
                break;
            }
            if (creditsLine == null) {
                creditsLine = line;
            }
        }

        if (creditsLine == null) {
            System.err.println("No credits amount received.");
            return -1;
        }

        try {
            return Integer.parseInt(creditsLine.trim());
        } catch (NumberFormatException e) {
            System.err.println("Invalid credits format: " + creditsLine);
            return -1;
        }
    }

}
