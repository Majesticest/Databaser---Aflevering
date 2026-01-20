package org.example;
import java.sql.*;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        connectDB db = new connectDB();
        Connection connection = db.getLocalConnection();
        if (connection != null) {
            try {
                // Select which account the user would like to continue with.
                int accountChoice = selectUser();
                //Displays all products regardless of availability
                productBrowser.displayProducts();

            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
    }

    private static int selectUser() throws SQLException {
        connectDB db = new connectDB();
        Connection connection = db.getLocalConnection();
        // new reader for method
        Scanner reader = new Scanner(System.in);
        //accountChoice
        int accountChoice = 0;
        boolean validInput = false;
        Statement stmt = connection.createStatement();
        ResultSet resultSet = stmt.executeQuery("SELECT kundeID,name FROM kunde");
        while (resultSet.next()) {
            int id = resultSet.getInt("kundeID");
            String name = resultSet.getString("name");
            System.out.println("ID " + (id + 1) + " name " + name);
        }

        while (validInput == false) {
            System.out.println("choose account (1-4): ");
            accountChoice = reader.nextInt();
            if (1 <= accountChoice && accountChoice <= 4) {
                accountChoice = accountChoice - 1;
                validInput = true;
            } else {
                System.out.println("Please enter a correct ID");
            }
        }
        return accountChoice;
    }
}
