package me.evrooij.groceries.domain;

/**
 * Created by eddy on 22-11-16.
 */

public class Product {
    private String name;
    private int amount;
    private String owner;
    private String comment;

    public String getName() {
        return name;
    }

    public int getAmount() {
        return amount;
    }

    public String getComment() {
        return comment;
    }

    public String getOwner() {
        return owner;
    }

    public Product(String name, int amount, String owner, String comment) {
        this.name = name;
        this.amount = amount;
        this.owner = owner;
        this.comment = comment;
    }
}