import java.util.Scanner;

public class Main {
    public static void main(String[] args) {

        String dbUrl = "jdbc:mysql://localhost:3306/RecommendationEngine";
        String dbUsername = "root";
        String dbPassword = "ITT@1234";

        Database database = new Database(dbUrl, dbUsername, dbPassword);
        Users users = new Users(database);

        Scanner userInput = new Scanner(System.in);
        System.out.print("Enter User Name: ");
        String inputUserName = userInput.nextLine();
        System.out.print("Enter password: ");
        String inputPassword = userInput.nextLine();

        String userName = inputUserName;
        String password = inputPassword;
        boolean isAuthenticated = users.authenticate(userName, password);

        if (isAuthenticated) {
            System.out.println("Authentication successful!");
        } else {
            System.out.println("Authentication failed.");
        }
    }
}
