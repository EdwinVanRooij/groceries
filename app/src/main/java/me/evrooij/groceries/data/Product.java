package me.evrooij.groceries.data;

import org.parceler.Parcel;

import java.util.Date;

/**
 * Author: eddy
 * Date: 22-11-16.
 */

@Parcel
public class Product {
    private int id;
    private String name;
    private int amount;
    private Account owner;
    private String comment;
    private Date deletionDate;
    private String imageUrl;

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getAmount() {
        return amount;
    }

    public String getComment() {
        return comment;
    }

    public Account getOwner() {
        return owner;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public Product() {
    }

    public Product(int id, String name, int amount, String comment, Account owner) {
        this.id = id;
        this.name = name;
        this.amount = amount;
        this.owner = owner;
        this.comment = comment;
    }

    public Product(String name, int amount, String comment, Account owner) {
        this.name = name;
        this.amount = amount;
        this.owner = owner;
        this.comment = comment;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    /**
     * Useful for testing purposes, we'll consider items equal on the same fields
     *
     * @param obj other product
     * @return true if objects are equal, false if not
     */
    @Override
    public boolean equals(Object obj) {
        Product other = (Product) obj;
        // If all fields are the same, return true
//        return other.getId() == getId() // If all fields are the same, return true
        return other.getName().equals(getName())
                && other.getAmount() == getAmount()
                && other.getOwner().equals(getOwner())
                && other.getComment().equals(getComment())
                || super.equals(obj);
    }

    @Override
    public String toString() {
        return String.format("%s, id %s - %s times of %s, %s", getName(), getId(), getAmount(), getOwner(), getComment());
    }
}
