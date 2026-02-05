package org.example;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

record User(int id, String name, int wallet){}
record Product(int id, int categoriID, String name, int amount, int price, String category){
    public boolean isLowInStock() {
        return amount < 100;
    }
    public boolean isSoldOut() {
        return amount <1;
    }

}
record Stores(int id, String Address, int postnr){}


public class Storefront {
    Connection connection;

    public Storefront(Connection connection) {
        this.connection = connection;
    }
    public List<User> getUsers() throws SQLException {
        Statement stmt = this.connection.createStatement();
        ResultSet resultSet = stmt.executeQuery("SELECT customerID,name,wallet FROM customer");
        List<User> users= new ArrayList<>();
        while (resultSet.next()) {
            int id = resultSet.getInt("customerID");
            String name = resultSet.getString("name");
            int wallet=resultSet.getInt("wallet");
            users.add(new User(id,name, wallet));
        }
        return users;
    }
    public List<Product> getProducts(int maxPrice) throws SQLException{
        Statement stmt = this.connection.createStatement();

        ResultSet resultSet = stmt.executeQuery("SELECT * FROM product JOIN category ON (product.categoryID=category.categoryID) WHERE price <= " + maxPrice);
        List<Product> products= new ArrayList<>();
        while (resultSet.next()) {
            products.add(getProduct(resultSet));
        }
        return products;
    }

    public Product getProduct(int productID) throws SQLException {
        Statement stmt = this.connection.createStatement();
        ResultSet resultSet = stmt.executeQuery("SELECT * FROM product JOIN category ON (product.categoryID=category.categoryID) WHERE productID = " + productID);

        if (!resultSet.next()) {
            throw new SQLException(String.format("Product %d not found", productID));
        }
        return getProduct(resultSet);
    }

    private static Product getProduct(ResultSet resultSet) throws SQLException {
        int id = resultSet.getInt("productID");
        int catId = resultSet.getInt("categoryID");
        String name = resultSet.getString("name");
        int amount = resultSet.getInt("amount");
        int price = resultSet.getInt("price");
        String category = resultSet.getString("categoryName");
        return new Product(id, catId, name, amount, price, category);
    }
    public List<Stores> getStores() throws SQLException {
        Statement stmt = this.connection.createStatement();

        ResultSet resultSet = stmt.executeQuery("SELECT * FROM shop");
        List<Stores> stores= new ArrayList<>();
        while (resultSet.next()) {
            stores.add(new Stores(resultSet.getInt("shopID"), resultSet.getString("shopAddress"), resultSet.getInt("postnr")));
        }
        return stores;
    }
}
