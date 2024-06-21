import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;

public class Client {
    public static void main(String[] args) {
        try (Socket socket = new Socket("localhost", 12345);
             PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
             BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
             BufferedReader stdIn = new BufferedReader(new InputStreamReader(System.in))) {

            System.out.print("Enter username: ");
            String username = stdIn.readLine();
            System.out.print("Enter password: ");
            String password = stdIn.readLine();

            out.println(username);
            out.println(password);

            String response = in.readLine();
            System.out.println("Server response: " + response);

            if ("Authentication successful".equals(response)) {
                // Determine user's role
                String role = in.readLine();
                switch (role.toLowerCase()) {
                    case "admin":
                        handleAdmin(out, in, stdIn);
                        break;
                    case "chef":
                        handleChef(out, in, stdIn);
                        break;
                    case "employee":
                        handleEmployee(out, in, stdIn);
                        break;
                    default:
                        System.out.println("Unknown role.");
                        break;
                }
            }

        } catch (UnknownHostException e) {
            System.err.println("Don't know about host: localhost");
            e.printStackTrace();
        } catch (IOException e) {
            System.err.println("Couldn't get I/O for the connection to: localhost");
            e.printStackTrace();
        }
    }

    private static void handleAdmin(PrintWriter out, BufferedReader in, BufferedReader stdIn) throws IOException {
        AdminHandler adminHandler = new AdminHandler(out, in, stdIn);
        adminHandler.handleUserOperations();
    }

    private static void handleChef(PrintWriter out, BufferedReader in, BufferedReader stdIn) throws IOException {

         ChefHandler chefHandler = new ChefHandler(out, in, stdIn);
         chefHandler.handleUserOperations();
    }

    private static void handleEmployee(PrintWriter out, BufferedReader in, BufferedReader stdIn) throws IOException {
         EmployeeHandler employeeHandler = new EmployeeHandler(out, in, stdIn);
         employeeHandler.handleUserOperations();
    }
}
