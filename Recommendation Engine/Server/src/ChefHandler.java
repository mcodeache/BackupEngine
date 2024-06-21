import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ChefHandler {
    private final int userId;
    private PrintWriter out;
    private BufferedReader in;
    private String username;
    private Database database;


    public ChefHandler(PrintWriter out, BufferedReader in, String username, Database database, int userId) {
        this.out = out;
        this.in = in;
        this.username = username;
        this.database = database;
        this.userId = userId;
    }

    public void handle() throws IOException, SQLException {
        sendmenu();
        while (true) {
            String request = in.readLine();

            if ("menu".equals(request)) {
                sendMenu();
            } else if ("4".equals(request)) {
                out.println("Exiting...");
                out.println("END_OF_MESSAGE");
                out.flush();
                break;
            } else {
                handleOperation(request);
                sendMenu();
            }
        }

    }

    private void sendMenu() {
        out.println("Employee Menu: 1) View Food Menu 2) Roll Out Next Day Menu 3) View Monthly Report 4) Exit");
        out.flush();
    }

    private void handleOperation(String choice) throws IOException, SQLException {
            switch (choice) {
                case "1":
                    viewFoodMenu();
                    break;
                case "2":
                    rollOutNextDayMenu();
                    break;
                case "3":
                    viewMonthlyReport();
                    break;
                case "4":
                    return;
                default:
                    out.println("Invalid choice. Please try again.");
                    break;
            }
    }

    private void sendmenu() {
        out.println("Chef Menu: 1) View Food Menu 2) Roll Out Next Day Menu 3) View Monthly Report 4) Exit");
        out.flush();
    }

    private void viewFoodMenu() throws SQLException {
        ResultSet menuItems = database.fetchMenuItems();
        boolean hasItems = false;

        while (menuItems.next()) {
            hasItems = true;
            String itemName = menuItems.getString("item_name");
            double price = menuItems.getDouble("price");
            boolean availability = menuItems.getBoolean("availability");
            out.printf("%s: $%.2f - %s%n", itemName, price, availability ? "Available" : "Not Available");
        }

        if (!hasItems) {
            out.println("No menu items available.");
        }

        out.println("END_OF_MESSAGE");
        out.flush();
    }


    private int[] getFeedbackIdsForItems(int[] breakfastIds, int[] lunchIds, int[] dinnerIds) throws SQLException {
        int[] feedbackIds = new int[breakfastIds.length + lunchIds.length + dinnerIds.length];
        int index = 0;
        for (int id : breakfastIds) {
            feedbackIds[index++] = database.getFeedbackIdByItemId(id);
        }
        for (int id : lunchIds) {
            feedbackIds[index++] = database.getFeedbackIdByItemId(id);
        }
        for (int id : dinnerIds) {
            feedbackIds[index++] = database.getFeedbackIdByItemId(id);
        }
        return feedbackIds;
    }


    private void rollOutNextDayMenu() throws IOException, SQLException {
//        String numberOfItems = in.readLine();
//        int items_size = Integer.parseInt(numberOfItems);

        int breakfastItems = Integer.parseInt(in.readLine());
        String breakfast = in.readLine();
        int[] breakfastIds = parseItemIds(breakfast);

        int lunchItems = Integer.parseInt(in.readLine());
        String lunch = in.readLine();
        int[] lunchIds = parseItemIds(lunch);

        int dinnerItems = Integer.parseInt(in.readLine());
        String dinner = in.readLine();
        int[] dinnerIds = parseItemIds(dinner);

        int[] feedbackIds = getFeedbackIdsForItems(breakfastIds, lunchIds, dinnerIds);

        boolean success = database.rollOutNextDayMenu(userId, breakfastIds, lunchIds, dinnerIds, feedbackIds);
        if (success) {
            out.println("Next day menu rolled out successfully.");
        } else {
            out.println("Failed to roll out next day menu.");
        }

        out.flush();
    }

    private int[] parseItemIds(String items) {
        String[] itemArray = items.split(",");
        int[] itemIds = new int[itemArray.length];
        for (int i = 0; i < itemArray.length; i++) {
            itemIds[i] = Integer.parseInt(itemArray[i]);
        }
        return itemIds;
    }

    private void viewMonthlyReport() throws SQLException {
        ResultSet rs = database.fetchMonthlyReport();
        StringBuilder report = new StringBuilder("Monthly Report:\n");
        while (rs.next()) {
            report.append("Date: ").append(rs.getDate("report_date"))
                    .append(", Total Sales: $").append(rs.getDouble("total_sales")).append("\n");
        }
        out.println(report.toString());
        rs.close();
    }
}