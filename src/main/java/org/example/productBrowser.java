package org.example;
import java.sql.*;
import java.util.Scanner;

public class productBrowser {
    //displays all products
    public static void displayProducts() throws SQLException{
        connectDB db = new connectDB();
        Connection connection = db.getLocalConnection();
        // new reader for method
        Scanner reader = new Scanner(System.in);

        Statement stmt = connection.createStatement();
        ResultSet resultSet = stmt.executeQuery("SELECT * FROM product");

        //Display products and stock availability
        while(resultSet.next()){
            int lowSupply = 100;
            int id = resultSet.getInt("productID");
            int price = resultSet.getInt("price");
            String name = resultSet.getString("name");
            int amount = resultSet.getInt("amount");
        // stock availability display
            if(amount > lowSupply){
                System.out.println("nr "+id+" product: "+name+" | "+ price+" kr");
            } else if (amount == 0) {
                System.out.println("nr "+id+" product: "+name+" | "+ price+" kr"+" Sold out");
            } else{
                System.out.println("nr "+id+" product: "+name+" | "+ price+" kr"+" Low Stock");
            }

        }
        resultSet.close();
        stmt.close();
        connection.close();
    }
}

