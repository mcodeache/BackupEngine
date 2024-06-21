import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class AdminHandler {
    private PrintWriter out;
    private BufferedReader in;
    private BufferedReader stdIn;

    public AdminHandler(PrintWriter out, BufferedReader in, BufferedReader stdIn) {
        this.out = out;
        this.in = in;
        this.stdIn = stdIn;
    }

    public void handleUserOperations() throws IOException {
        while (true) {
            String menuOption = in.readLine();
            System.out.println(menuOption); // Display the menu option received from the server

            // Prompt user to enter their choice
            System.out.print("Enter your choice: ");
            String choice = stdIn.readLine();
            out.println(choice); // Send user choice back to server
            String serverResponse = "";

            switch (choice) {
                case "1":
                    serverResponse = readServerResponse();
                    System.out.println(serverResponse);
                    break;
                case "2":
                    addItem();
                    break;
                case "3":
                    updateItem();
                    break;
                case "4":
                    deleteItem();
                    break;
                case "5":
                    System.out.println("Logging out...");
                    return;
                default:
                    System.out.println("Invalid choice. Please enter a number from 1 to 5.");
                    break;
            }
        }
    }

    private void addItem() throws IOException {
        System.out.print("Enter item name: ");
        String itemName = stdIn.readLine();
        out.println(itemName);

        System.out.print("Enter item price: ");
        String itemPrice = stdIn.readLine();
        out.println(itemPrice);

        System.out.println("Enter Item Type(Breakfast, Lunch, Dinner)");
        String itemType = stdIn.readLine();
        out.println(itemType);

        String serverResponse = readServerResponse();
        System.out.println(serverResponse);
    }

    private void updateItem() throws IOException {
        System.out.print("Enter item name to update: ");
        String itemName = stdIn.readLine();
        out.println(itemName);

        System.out.print("Enter new price: ");
        String newItemPrice = stdIn.readLine();
        out.println(newItemPrice);

        System.out.println("Enter Yes/No for Availability");
        String availability = stdIn.readLine();
        out.println(availability);

        String serverResponse = readServerResponse();
        System.out.println(serverResponse);
    }

    private void deleteItem() throws IOException {
        System.out.print("Enter item name to delete: ");
        String itemName = stdIn.readLine();
        out.println(itemName);

        String serverResponse = readServerResponse();
        System.out.println(serverResponse);
    }

    private String readServerResponse() throws IOException {
        StringBuilder response = new StringBuilder();
        String line;
        while ((line = in.readLine()) != null) {
            if (line.equals("END_OF_MESSAGE")) {
                break;
            }
            response.append(line).append("\n");
        }
        return response.toString().trim(); // Trim to remove extra new line at the end
    }
}
