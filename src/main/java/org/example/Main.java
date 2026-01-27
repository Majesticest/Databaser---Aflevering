package org.example;
import java.sql.*;

public class Main {
    public static void main(String[] args) {
        connectDB db = new connectDB();
        Connection connection = db.getLocalConnection();

        if (connection != null) {
            try {
                // Select which account the user would like to continue with.
                int accountChoice = selectUser.selectUser();

                //Displays all products regardless of availability
                productBrowser.displayProducts();

                System.out.println(accountChoice); //test of accountchoice variable DEBUG

            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
