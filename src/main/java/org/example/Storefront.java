package org.example;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

record User(int id, String name){}
record Product(int id, String name, int amount, int price){}



public class Storefront {
    Connection connection;

    public Storefront(Connection connection) {
        this.connection = connection;
    }
    public List<User> getUsers() throws SQLException {
        Statement stmt = this.connection.createStatement();
        ResultSet resultSet = stmt.executeQuery("SELECT kundeID,name FROM kunde");
        List<User> users= new ArrayList<>();
        while (resultSet.next()) {
            int id = resultSet.getInt("kundeID");
            String name = resultSet.getString("name");
            users.add(new User(id,name));
        }
        return users;
    }
        public List<Product> getProducts() throws SQLException{
            Statement stmt = this.connection.createStatement();
            ResultSet resultSet = stmt.executeQuery("SELECT * FROM product");
            List<Product> products= new ArrayList<>();
            while (resultSet.next()) {
                int id = resultSet.getInt("productID");
                String name = resultSet.getString("name");
                int amount = resultSet.getInt("amount");
                int price = resultSet.getInt("price");

                products.add(new Product(id,name,amount,price));
            }
            return products;
        }

}
