package org.example;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;


public class Storefront {
    Connection connection;

    public Storefront(Connection connection) {
        this.connection = connection;
    }

    //creates a list of all users in the database using the record class
    public List<User> getUsers() throws SQLException {
        Statement stmt = this.connection.createStatement();
        ResultSet resultSet = stmt.executeQuery("SELECT customerID,name,wallet FROM customer");
        List<User> users = new ArrayList<>();
        while (resultSet.next()) {
            int id = resultSet.getInt("customerID");
            String name = resultSet.getString("name");
            int wallet = resultSet.getInt("wallet");
            users.add(new User(id, name, wallet));
        }
        return users;
    }

    public List<ProductCategory> getCategories() throws SQLException {
        Statement stmt = this.connection.createStatement();
        ResultSet resultSet = stmt.executeQuery("SELECT categoryID,categoryName FROM category");
        List<ProductCategory> categories = new ArrayList<>();
        while (resultSet.next()) {
            int id = resultSet.getInt("categoryID");
            String name = resultSet.getString("categoryName");

            categories.add(new ProductCategory(id, name));
        }
        return categories;
    }

    //does the same as above but for products
    public List<Product> getProducts(int maxPrice) throws SQLException {
        Statement stmt = this.connection.createStatement();

        ResultSet resultSet = stmt.executeQuery("SELECT * FROM product JOIN category ON (product.categoryID=category.categoryID) WHERE price <= " + maxPrice);
        List<Product> products = new ArrayList<>();
        while (resultSet.next()) {
            products.add(getProduct(resultSet));
        }
        return products;
    }

    //fetches all data + category for a single given product from database
    public Product getProduct(int productID) throws SQLException {
        Statement stmt = this.connection.createStatement();
        ResultSet resultSet = stmt.executeQuery("SELECT * FROM product JOIN category ON (product.categoryID=category.categoryID) WHERE productID = " + productID);

        if (!resultSet.next()) {
            throw new SQLException(String.format("Product %d not found", productID));
        }
        return getProduct(resultSet);
    }

    public List<Product> filterProduct(int maxPrice, List<Integer> categories) throws SQLException {
        Statement stmt = this.connection.createStatement();
        String catFilter = "";
        for (int i = 0; i < categories.size(); i++) {
            Integer cat = categories.get(i);
            if (i > 0) {
                catFilter += ", "; //Don't add comma before first entry
            }
            catFilter += cat;
        }

        String sql = "SELECT product.*,categoryName FROM product JOIN category ON (product.categoryID=category.categoryID) WHERE price <=" + maxPrice + " AND product.categoryID IN (" + catFilter + ")";

        ResultSet resultSet = stmt.executeQuery(sql);


        List<Product> products = new ArrayList<>();
        while (resultSet.next()) {
            Product product = getProduct(resultSet);
            products.add(product);

        }
        return products;
    }

    //allows for the two previous functions work by compiling all fetched data for a single product into a Product
    private static Product getProduct(ResultSet resultSet) throws SQLException {
        int id = resultSet.getInt("productID");
        int catId = resultSet.getInt("categoryID");
        String name = resultSet.getString("name");
        int amount = resultSet.getInt("amount");
        int price = resultSet.getInt("price");
        String category = resultSet.getString("categoryName");
        return new Product(id, catId, name, amount, price, category);
    }

    public List<Store> getStores() throws SQLException {
        Statement stmt = this.connection.createStatement();

        ResultSet resultSet = stmt.executeQuery("SELECT * FROM shop");
        List<Store> stores = new ArrayList<>();
        while (resultSet.next()) {
            stores.add(new Store(resultSet.getInt("shopID"), resultSet.getString("shopAddress"), resultSet.getInt("postnr")));
        }
        return stores;
    }

    public void updateStock(int productID, int shopID, int change) throws SQLException {
        // change is the number to increase or decrease the given products stock with
        // i.e. -1 when selling an item
        Statement stmt = this.connection.createStatement();

        stmt.executeUpdate("UPDATE palager SET stock = stock + " + change +
                " WHERE productID = " + productID +
                " AND shopID = " + shopID
        );

    }

    public List<Product> getStoreProducts(int shopID) throws SQLException {
        Statement stmt = this.connection.createStatement();

        ResultSet resultSet = stmt.executeQuery(
                ("SELECT product.*, categoryName FROM palager " +
                        "JOIN product ON palager.productID=product.productID " +
                        "JOIN category ON product.categoryID=category.categoryID " +
                        "WHERE shopID = %d " +
                        "ORDER BY productID").formatted(shopID)
        );
        List<Product> products = new ArrayList<>();
        while (resultSet.next()) {
            products.add(getProduct(resultSet));
        }
        return products;
    }
    public void createUser(String name, int postnr, int wallet, String address) throws SQLException {
        PreparedStatement statement = connection.prepareStatement("INSERT INTO customer (name, postnr, wallet,customerAddress) VALUES (?,?,?,?)");
        statement.setString(1, name);
        statement.setInt(2, postnr);
        statement.setInt(3, wallet);
        statement.setString(4, address);
        statement.execute();
    }
}
