import java.sql.ResultSet;
import java.sql.SQLException;

public class Users {
    private Database database;

    public Users(Database database) {
        this.database = database;
    }

    public boolean authenticate(String employee_id, String password) {
        try {
            database.connect();
            ResultSet resultSet = database.fetchUser(employee_id);

            if (resultSet.next()) {
                String storedPassword = resultSet.getString("password_hash");
                return storedPassword.equals(password);
            } else {
                return false; // User does not exist
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        } finally {
            try {
                database.disconnect();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}
