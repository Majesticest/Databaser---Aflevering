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
                selectUser();
                //

            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
    }


    private static int selectUser() throws SQLException {
        connectDB db = new connectDB();
        Connection connection = db.getLocalConnection();
        // new reader
        Scanner reader = new Scanner(System.in);
        //accountChoice
        int accountChoice;

        Statement stmt = connection.createStatement();
        ResultSet resultSet = stmt.executeQuery("SELECT kundeID,name FROM kunde");
        while(resultSet.next()){
            int id = resultSet.getInt("kundeID");
            String name = resultSet.getString("name");
            System.out.println("ID "+(id+1)+" name "+ name);
        }
        System.out.println("choose account (1-4): ");
        accountChoice = reader.nextInt() - 1;

        return accountChoice;
    }

    static void showDB() throws SQLException {
        try {
            //
            connectDB db = new connectDB();
            Connection connection = db.getLocalConnection();

            Statement stmt = connection.createStatement();
            ResultSet resultSet = stmt.executeQuery("SELECT * FROM kunde");

            //fetches metadata from tables, amount of columns, column names.
            ResultSetMetaData meta = resultSet.getMetaData();
            int columnCount = meta.getColumnCount();

            //prints column data for how many columns there are.
            while (resultSet.next()) {
                for (int i = 1; i <= columnCount; i++) {
                    System.out.print(resultSet.getString(i) + "\t");
                }
                System.out.println();

                // Print column headers
                for (int i = 1; i <= columnCount; i++) {
                    System.out.print(meta.getColumnName(i) + "\t");
                }
                System.out.println();

                // Print rows
                while (resultSet.next()) {
                    for (int i = 1; i <= columnCount; i++) {
                        System.out.print(resultSet.getString(i) + "\t");
                    }
                    System.out.println();
                }
            }
        }catch(RuntimeException e){
            throw new RuntimeException(e);
        }
    }
}