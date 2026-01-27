package org.example;
import java.sql.*;
import java.util.Locale;
import java.util.Scanner;

public class productSelect {
    public static int productSelect() throws SQLException {
        connectDB db = new connectDB();
        Connection connection = db.getLocalConnection();
        Scanner reader = new Scanner(System.in);
        //laver et arraylist for de forskellige ID'er som der findes i systemet.
        Statement stmt = connection.createStatement();
        //valg af product
        int productSelection = reader.nextInt();
        ResultSet resultSet= stmt.executeQuery("SELECT * FROM product WHERE productID='"+productSelection+"'");


        if(productSelection >= productBrowser.ProductIDList.getFirst() && productSelection <= productBrowser.ProductIDList.getLast() ){

            //Henter det specifikke data
            int id = resultSet.getInt("productID");
            int price = resultSet.getInt("price");
            String name = resultSet.getString("name");
            int amount = resultSet.getInt("amount");
            int categoryID = resultSet.getInt("categoryID");
            String CatID = EnumTranslator(categoryID);

            //printer dataet
            System.out.println("nr " + id +" | Product: "+ name);
            System.out.println("price: "+ price + "kr | amount: "+ amount + " | "+CatID);

            return productSelection;

        }

        //valgmuligheder, altså vil kunden købe eller noget andet, idfk
        System.out.println("Options: back | buy | quit");
        String options = reader.next();
        switch (options.toLowerCase(Locale.ROOT).trim()) {
            case "back" -> productBrowser.displayProducts();
            case "buy" -> {}
            case "quit" -> {
                resultSet.close();
                stmt.close();
                connection.close();}
        }


        resultSet.close();
        stmt.close();
        connection.close();
        return productSelection;
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

