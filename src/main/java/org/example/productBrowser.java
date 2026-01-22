package org.example;
import java.sql.*;
import java.util.ArrayList;
import java.util.Scanner;

public class productBrowser {


    //displays all products
    public static void displayProducts() throws SQLException{
        connectDB db = new connectDB();
        Connection connection = db.getLocalConnection();
        Scanner reader = new Scanner(System.in);
        //laver et arraylist for de forskellige ID'er som der findes i systemet.
        ArrayList<Integer> ProductIDList = new ArrayList<>();


        Statement stmt = connection.createStatement();
        ResultSet resultSet = stmt.executeQuery("SELECT * FROM product");

        //Display products and stock availability
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
            int categoryID = resultSet.getInt("categoryID");

        // stock availability display
            if(amount > lowSupply){
                System.out.println("nr "+ id +" product: "+ name +" | "+ price +" kr");
            } else if (amount == 0) {
                System.out.println("nr "+ id +" product: "+ name +" | "+ price +" kr"+" Sold out");
            } else{
                System.out.println("nr "+ id +" product: "+ name +" | "+ price +" kr"+" Low Stock");
            }

        }

        int productSelection = reader.nextInt();
        if(productSelection >= ProductIDList.getFirst() && productSelection <= ProductIDList.getLast() ){
            ResultSet resultSetLocal = stmt.executeQuery("SELECT * FROM product WHERE productID='"+productSelection+"'");
            //Henter det specifikke data
            id = resultSet.getInt("productID");
            price = resultSet.getInt("price");
            name = resultSet.getString("name");
            amount = resultSet.getInt("amount");
            int categoryID = resultSet.getInt("categoryID");
            String CatID = EnumTranslator(categoryID);

            //printer dataet
            System.out.println("nr " + id +" | Product: "+ name);
            System.out.println("price: "+ price + "kr | amount: "+ amount + " | "+CatID);
        }

        resultSet.close();
        stmt.close();
        connection.close();
    }

    //translates the enums to actual names
    public static String EnumTranslator(int categoryID){
        String CategoryType = null;
        switch(categoryID){
            case 0-> CategoryType = String.valueOf(category.sko);
            case 1-> CategoryType = String.valueOf(category.bukser);
            case 2-> CategoryType = String.valueOf(category.bluser);
            case 3-> CategoryType = String.valueOf(category.hatte);
            case 4-> CategoryType = String.valueOf(category.sokker);
        }
        return CategoryType;
    }
}

