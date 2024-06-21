import java.sql.*;

public class Database {
    private String url;
    private String username;
    private String password;
    private Connection connection;

    public Database(String url, String username, String password) {
        this.url = url;
        this.username = username;
        this.password = password;
        this.connection = null;
    }

    public void connect() throws SQLException {
        if (connection == null || connection.isClosed()) {
            connection = DriverManager.getConnection(url, username, password);
            System.out.println("Connected to database.");
        }
    }

    public void disconnect() throws SQLException {
        if (connection != null && !connection.isClosed()) {
            connection.close();
            System.out.println("Disconnected from database.");
        }
    }

    public Connection getConnection() {
        return connection;
    }

    public ResultSet fetchUser(String username) throws SQLException {
        String query = "SELECT USER.*, ROLE.role_name, USERCREDENTIALS.password_hash " +
                "FROM USER " +
                "JOIN ROLE ON USER.role_id = ROLE.role_id " +
                "JOIN USERCREDENTIALS ON USER.user_id = USERCREDENTIALS.user_id " +
                "WHERE USER.user_name = ?";
        PreparedStatement preparedStatement = connection.prepareStatement(query);
        preparedStatement.setString(1, username);
        return preparedStatement.executeQuery();
    }
}
