package org.example;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Menu {
    private final Scanner reader;
    Storefront storefront;

    public Menu(Storefront storefront) {
        this.storefront = storefront;
        reader = new Scanner(System.in);
    }

    public void run() throws SQLException {


        boolean validInput = false;
        int menuReturn;
        User loggedInUser = null;
        boolean keepRunning = true;
        while (keepRunning) {
            if (loggedInUser == null) {
                loggedInUser = this.logInUser();
            }
            System.out.println(String.format("Hello %s what do you want to do?", loggedInUser.name()));
            System.out.println("""
                    1. View all products
                    2. Filter products
                    3. Show available shops
                    4. Exit program
                    """);
            int menuID = reader.nextInt();
            switch (menuID) {
                case 1:
                    showAllProducts(loggedInUser);
                    System.out.println("Inspect product? (yes: 1, no: 0)");
                    int selctProd = reader.nextInt();
                    if (selctProd == 1){
                        while (true) {

                            System.out.println("Please select product to view details for");
                            int productID = reader.nextInt();
                            try {
                                showProductDetails(productID);
                                break;
                            } catch (SQLException e) {
                                System.out.println(e.getMessage());
                            }
                        }
                    } else if (selctProd == 0) {
                        break;
                    } else {
                        System.out.println("Please enter a valid ID");
                    }
                    break;

                case 2:
                    List<Integer> catfilter = getCatfilter();
                    System.out.println("Enter max price you are willing to spend:");
                    int maxPrice = reader.nextInt();
                    filterProducts(maxPrice, catfilter);
                    break;


                case 3:
                    showAllStores();
                    break;
                case 4:
                    System.exit(0);

            }


        }


    }

    private List<Integer> getCatfilter() throws SQLException {
        List<ProductCategory> categories = storefront.getCategories();
        System.out.println("Select categories to filter by. Single enter to use filter:");
        for (ProductCategory cat:categories){
            System.out.println(String.format("%d: %s", cat.id(), cat.categoryName()));

        }
        List<Integer> catfilter = new ArrayList<>();
        reader.nextLine(); // makes the code work properly
        while (true){
            String productFilter = reader.nextLine();
            if (productFilter.isEmpty()){
                //no more filters to add
                break;
            }
            try {
                int catID = Integer.parseInt(productFilter);
                boolean validCatID = false;
                for (ProductCategory cat:categories){
                    if (cat.id()==catID){
                        catfilter.add(catID);
                        validCatID = true;
                        break;
                    }
                }
                if (!validCatID){
                    System.out.println("Illegal input");
                }
            } catch (NumberFormatException e) {
                System.out.println("Illegal input");
            }
        }
        return catfilter;
    }

    private void showAllProducts(User loggedInUser) throws SQLException {

        List<Product> products = storefront.getProducts(loggedInUser.wallet());
        if (products.isEmpty()){
            System.out.println("Sorry, you don't have enough money to buy anything. You only have: " + loggedInUser.wallet());
        }
        for (Product p : products) {

            System.out.println(String.format("%d, %s, %d kr", p.id(), p.name(), p.price()));
        }
    }

    private void showProductDetails(int productID) throws SQLException {
        Product p = storefront.getProduct(productID);
        String extra = "";
        if (p.isSoldOut()) {
            extra = "out of stock";
        } else if (p.isLowInStock()) {
            extra = "Low stock";
        }
        System.out.println(String.format("%d, %s, %d kr, %d in stock, %s", p.id(), p.name(), p.price(), p.amount(), p.category(), extra));

    }

    private void filterProducts(int maxPrice, List<Integer> categories) throws SQLException {
        List<Product> products = storefront.filterProduct(maxPrice, categories);
        if (products.isEmpty()){
            System.out.println("No products matches your filter");
        }
        for (Product p : products){
            System.out.println(String.format("%d, %s, %d kr, %d in stock, %s", p.id(), p.name(), p.price(), p.amount(), p.category()));
        }


    }

    public User logInUser() throws SQLException {
        User user = null;


        List<User> users = storefront.getUsers();
        while (user == null) {
            System.out.println("Choose account: ");
            for (User u : users) {
                System.out.println(u.id() + "\t" + u.name());
            }
            int accountChoice = reader.nextInt();
            for (User u : users) {
                if (u.id() == accountChoice) {
                    user = u;
                }

            }
            if (user == null) {
                System.out.println("Choose again");


            }
        }
        return user;
    }
    private void showAllStores() throws SQLException {
        List<Stores> stores = storefront.getStores();
        for (Stores s : stores) {
            System.out.println(String.format("%d %s %d", s.id(),s.Address(),s.postnr()));
        }
    }


}