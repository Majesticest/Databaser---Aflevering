package org.example;
import java.sql.*;

// Our main can be described as this meme: https://imgflip.com/i/ajjnw8

public class Main {
    public static void main(String[] args) {
        connectDB db = new connectDB();
        Connection connection = db.getLocalConnection();

        if (connection != null) {
            try {
                Storefront storefront = new Storefront(connection);
                Menu menu = new Menu(storefront);
                menu.run();

            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
