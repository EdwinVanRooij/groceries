package me.evrooij.groceries.domain;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by eddy
 * Date: 20-11-16.
 */

public class GroceryList {
    private String name;
    private Account owner;
    private List<Product> productList;

    /**
     * Gets the name of this list
     *
     * @return String name
     */
    public String getName() {
        return name;
    }

    /**
     * Gets the owner of this list, it's the user who created this list originally
     *
     * @return Account object of the owner
     */
    public Account getOwner() {
        return owner;
    }

    /**
     * A list of product objects
     *
     * @param name  name of the list
     * @param owner account of the user who created this list
     */
    public GroceryList(String name, Account owner) {
        this.name = name;
        this.owner = owner;
        productList = new ArrayList<>();
    }

    /**
     * Adds a new item to the list
     *
     * @param id      unique identifier of the product
     * @param name    name of the product
     * @param amount  amount of times you want the product
     * @param comment comment about the product
     * @param owner   username of the user who added this product
     */
    public void addItem(int id, String name, int amount, String comment, String owner) {
        productList.add(new Product(id, name, amount, comment, owner));
    }

    /**
     * Edits an existing item of this list
     *
     * @param id      unique identifier of the product
     * @param name    new name of the product
     * @param amount  new amount of the product
     * @param comment new comment of the product
     * @param owner   must equal the owner of this product
     *                you're not allowed to edit the product of someone else
     * @return boolean indicating the exit status of the method
     */
    public boolean editItem(int id, String name, int amount, String comment, String owner) {
        Product product = getProduct(id);
        if (product.getOwner().equals(owner)) {
            product.setName(name);
            product.setAmount(amount);
            product.setComment(comment);
            return true;
        }
        return false;
    }

    /**
     * Removes a product
     *
     * @param id unique identifier of the product to remove
     */
    public void removeItem(int id) {
        productList.remove(getProduct(id));
    }

    /**
     * Returns the amount of products currently in the list
     *
     * @return integer value indicating the size of the product list
     */
    public int getAmountOfProducts() {
        return productList.size();
    }

    /**
     * Searches the GroceryList for a product
     *
     * @param id id of the product to look for
     * @return the product if it's in here, null if it's not
     */
    public Product getProduct(int id) {
        for (Product p :
                productList) {
            if (p.getId() == id) {
                // Product found
                return p;
            }
        }
        // No product found
        return null;
    }

    public List<Product> getProductList() {
        return productList;
    }
}

