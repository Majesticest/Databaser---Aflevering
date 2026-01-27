package org.example;
import java.sql.*;
import java.util.ArrayList;
import java.util.Scanner;

public class productBrowser {
    //laver et arraylist for de forskellige ID'er som der findes i systemet.
    public static ArrayList<Integer> ProductIDList = new ArrayList<>();

    //displays all products
    public static void displayProducts() throws SQLException{
        connectDB db = new connectDB();
        Connection connection = db.getLocalConnection();
        Statement stmt = connection.createStatement();

        ResultSet resultSet = stmt.executeQuery("SELECT * FROM product");

        //Display products
        int id;
        int price;
        String name;
        int amount;
        while(resultSet.next()){
            int lowSupply = 100;
            id = resultSet.getInt("productID");
            ProductIDList.add(id);
            price = resultSet.getInt("price");
            name = resultSet.getString("name");
            amount = resultSet.getInt("amount");

        // stock availability display
            if(amount > lowSupply){
                System.out.println("nr "+ id +" product: "+ name +" | "+ price +" kr");
            } else if (amount == 0) {
                System.out.println("nr "+ id +" product: "+ name +" | "+ price +" kr"+" Sold out");
            } else{
                System.out.println("nr "+ id +" product: "+ name +" | "+ price +" kr"+" Low Stock");
            }

        }
        //giver brugeren lov til at vælge produkt
        productSelect.productSelect();

    }

}

