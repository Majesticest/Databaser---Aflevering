package org.example;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

// Frontend but ugly class, UI and stuff

public class Menu {
    private final Scanner reader;
    Storefront storefront;

    public Menu(Storefront storefront) {
        this.storefront = storefront;
        reader = new Scanner(System.in);
    }

    // Fader metoden til alle metoder, basically Main.java lite
    public void run() throws SQLException {

        User loggedInUser = null;
        boolean keepRunning = true;
        while (keepRunning) {
        if (loggedInUser == null) {
            loggedInUser = this.logInUser();
        }
        //System choice prompt
        System.out.printf("\nHello %s what do you want to do?%n", loggedInUser.name());
        System.out.println("""
                1. View all products
                2. Show available shops
                3. Exit program
                """);
        int menuID = reader.nextInt();
        switch (menuID) {
            case 1:
                //displays all products before selecting
                showAllProducts(loggedInUser);
                System.out.println("Inspect: 1, Filter: 2, Buy: 3");
                int optionProduct = reader.nextInt();
                switch (optionProduct) {
                    case 1:
                        //product inspector
                        inspectProducts();
                        break;
                    case 2:
                        //filters
                        filterInput();
                        break;
                    case 3:
                        //creates orders
                        List<Integer> orderItems = getOrderItems();
                        if (orderItems.isEmpty()){
                            System.out.println("Order aborted");
                            break;
                        }
                        int productCount = orderItems.size();
                        int orderID = storefront.createOrder(loggedInUser.id(), orderItems);
                        System.out.println("Your order has been submitted with the following id: " + orderID);
                        break;
                    default:
                        // go back or return
                        break;
                }
                break;

            case 2:
                showAllStores();
                System.out.println("Check specific store: 1, go back: 2");
                int optionStore = reader.nextInt();
                if (optionStore == 1) {
                    storeSelection();
                }
                break;

            case 3:
                System.exit(0);

            // Denne case står gemt for en sikkerheds skyld.
            // Selvom den er ikke særligt beskyttet
            case 4:
                System.out.println("create or delete account? (create: 1, delete: 0, back: other keys)");
                int choice = reader.nextInt();
                if (choice == 1){createUser();}
                //loggedInUser bliver brugt for at stoppe brugeren i at slette den nuværende konto.
                else if (choice==0) {deleteUser(loggedInUser);}
                else {break;}
            }
        }
    }

    //Product viewing methods: --------------------------------------------------

    //displays all available products
    private void showAllProducts(User loggedInUser) throws SQLException {

        List<Product> products = storefront.getProducts(loggedInUser.wallet());
        if (products.isEmpty()){
            System.out.println("Sorry, you don't have enough money to buy anything. You only have: " + loggedInUser.wallet());
        }
        for (Product p : products) {

            System.out.printf("%d, %s, %d kr%n", p.id(), p.name(), p.price());
        }
    }

    //fremviser alt info om product, viser også dens stock info.
    private void showProductDetails(int productID) throws SQLException {
        Product p = storefront.getProduct(productID);
        String extra = "";
        if (p.isSoldOut()) {
            extra = "out of stock";
        } else if (p.isLowInStock()) {
            extra = "Low stock";
        }
        System.out.printf("%d, %s, %d kr, %d in stock, %s%n", p.id(), p.name(), p.price(), p.amount(), p.category(), extra);

    }

    //User can inspect all details of products via this method
    private void inspectProducts() {
        System.out.println("Please select product to view details for");
        int productID = reader.nextInt();
        try {
            showProductDetails(productID);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    //Filter methods:------------------------------------------------------------

    //input method for the filter
    private void filterInput() throws SQLException {
        List<Integer> catfilter = getCatfilter();
        System.out.println("Enter max price you are willing to spend:");
        int maxPrice = reader.nextInt();
        filterProducts(maxPrice, catfilter);
    }

    //filterer produkter.
    private void filterProducts(int maxPrice, List<Integer> categories) throws SQLException {
        List<Product> products = storefront.filterProduct(maxPrice, categories);
        if (products.isEmpty()){
            System.out.println("No products matches your filter");
        }
        for (Product p : products){
            System.out.printf("%d, %s, %d kr, %d in stock, %s%n", p.id(), p.name(), p.price(), p.amount(), p.category());
        }


    }

    //User picks the specific categories they want to see
    private List<Integer> getCatfilter() throws SQLException {
        List<ProductCategory> categories = storefront.getCategories();
        System.out.println("Select categories to filter by. Single enter to use filter:");
        for (ProductCategory cat:categories){
            System.out.printf("%d: %s%n", cat.id(), cat.categoryName());

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

    //User methods: -------------------------------------------------------------

    // methode til at vælge en account til brugeren, der er 4 kontoer i alt til at vælge mellem.
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

    //giver brugeren lov til at lave nye kontoer
    private void createUser () throws SQLException {
        //input block, ser grim ud
        System.out.println("Create a new User, Name:");
        String name = reader.next();

        System.out.println("Postnummer:");
        int postnr = reader.nextInt();

        System.out.println("Money:");
        int wallet = reader.nextInt();

        System.out.println("address");
        String Address = reader.next();

        storefront.createUser(name, postnr, wallet, Address);
        storefront.getUsers();

    }

    //giver brugeren lov til at slette kontoer
    private void deleteUser(User user) throws SQLException {
        List<User> users = storefront.getUsers();

        System.out.println("Choose account: ");
        for (User u : users) {
            System.out.println(u.id() + "\t" + u.name());}

        int id = reader.nextInt();

        //account deletion confirmation/failsafe
        if (id == user.id()) {
            System.out.println("You cannot delete this user");
        } else {
            System.out.println("Are you sure you want to delete this user? (1: yes, 0: no)");
            int deleteUser = reader.nextInt();
            if (deleteUser == 1) {
                storefront.deleteUser(id);
            }
        }

    }

    // Store methods: ------------------------------------------------------------

    //printer alle butikkerne som er en del af kæden.
    private List<Store> showAllStores() throws SQLException {
        List<Store> stores = storefront.getStores();
        for (Store s : stores) {
            System.out.printf("%d %s %d%n", s.id(),s.Address(),s.postnr());
        }
        return stores;
    }

    //method to view all products in a specific store
    private void storeSelection() throws SQLException {
        System.out.println("Select a store:");
        List<Store> stores = showAllStores();
        boolean found=false;
        int shopID=-1;
        while (!found){
            shopID = reader.nextInt();
            for (Store s : stores){
                if (shopID == s.id()) {
                    found = true;
                    break;
                }
            }
        }
        List<Product> products = storefront.getStoreProducts(shopID);
        for (Product p : products){
            System.out.printf("%d, %s, %d kr%n", p.id(), p.name(), p.price());
        }
    }

    // Order methods: ------------------------------------------------------------

    //Sends request to storefront to create order
    private List<Integer> getOrderItems() throws SQLException {
        List<Product> products = storefront.getProducts(100000000);
        System.out.println("Which products would you like to buy: ");
        for (Product p:products){
            System.out.printf("%d: %s, %d kr%n", p.id(), p.name(), p.price());

        }
        List<Integer> productIDs = new ArrayList<>();
        reader.nextLine(); // makes the code work properly
        while (true){
            String productFilter = reader.nextLine();
            if (productFilter.isEmpty()){
                //no more filters to add
                break;
            }
            try {
                int prodID = Integer.parseInt(productFilter);
                boolean validCatID = false;
                for (Product product:products){
                    if (product.id()== prodID){
                        productIDs.add(prodID);
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
        return productIDs;
    }
}