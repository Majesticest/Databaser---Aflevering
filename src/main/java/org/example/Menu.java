package org.example;

import java.sql.SQLException;
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

        User loggedInUser = null;
        boolean keepRunning = true;
        while (keepRunning) {
            if (loggedInUser==null){
              loggedInUser=this.logInUser();
            }
            System.out.println(String.format("Hello %s what do you want to do?", loggedInUser.name()));
            System.out.println("""
                    1. View all products
                    2. Show available shops and their stock
                    3. Inspect product
                    """);
            int menuID = reader.nextInt();
            switch (menuID){
                case 1:
                    showAllProducts();

            }








    }



}

    private void showAllProducts() throws SQLException {
        for (Product p : storefront.getProducts()){
            System.out.println(String.format("%d %s %d %d", p.id(), p.name(), p.amount(), p.price()));
        }
    }

    private User logInUser() throws SQLException {
        User user = null;

        List<User> users = storefront.getUsers();
        while (user == null) {
            System.out.println("Choose account: ");
            for (User u : users) {
                System.out.println(u.id() + "\t" + u.name());
            }
            int accountChoice = reader.nextInt();
            for (User u : users){
                if (u.id()==accountChoice){
                    user = u;
                }

            }
            if (user == null) {
                System.out.println("Choose again");


            }
        }
        return user;
    }


}