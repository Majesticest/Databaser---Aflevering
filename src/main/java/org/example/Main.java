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
                //
                productBrowser();

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
    //displays all products
    private static void productBrowser() throws SQLException {
        // Method setup to connectDB and database connection
        connectDB db = new connectDB();
        Connection connection = db.getLocalConnection();
        // new reader for method
        Scanner reader = new Scanner(System.in);

        Statement stmt = connection.createStatement();
        ResultSet resultSet = stmt.executeQuery("SELECT productID,price,name,amount FROM product");

        //Display products and stock availability
        while(resultSet.next()){
            int id = resultSet.getInt("productID");
            int price = resultSet.getInt("price");
            String name = resultSet.getString("name");
            int amount = resultSet.getInt("amount");

            if(amount > 100){
                System.out.println("nr "+id+" product: "+name+" | "+ price+" kr");
            } else if (amount == 0) {
                System.out.println("nr "+id+" product: "+name+" | "+ price+" kr"+" Sold out");
            } else{
                System.out.println("nr "+id+" product: "+name+" | "+ price+" kr"+" Low Stock");
            }

        }
    }
}