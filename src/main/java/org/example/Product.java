package org.example;



public record Product(int id, int categoryID, String name, int amount, int price, String category) {

    public boolean isLowInStock() {
        return amount < 100;
    }

    public boolean isSoldOut() {
        return amount < 1;
    }




}
