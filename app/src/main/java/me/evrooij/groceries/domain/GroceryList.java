package me.evrooij.groceries.domain;

/**
 * Created by eddy on 20-11-16.
 */

public class GroceryList {
    private String name;

    public String getOwner() {
        return owner;
    }

    public GroceryList(String name, String owner) {
        this.name = name;
        this.owner = owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    private String owner;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}

