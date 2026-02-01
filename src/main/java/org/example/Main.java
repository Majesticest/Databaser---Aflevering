package org.example;
import java.sql.*;

public class Main {
    public static void main(String[] args) {
        connectDB db = new connectDB();
        Connection connection = db.getLocalConnection();

        if (connection != null) {
            try {
                Storefront storefront = new Storefront(connection);
                Menu menu = new Menu(storefront);
                menu.run();
                // Select which account the user would like to continue with.


            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
