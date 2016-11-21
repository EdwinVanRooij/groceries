package me.evrooij.groceries.domain;

/**
 * Created by eddy on 20-11-16.
 */

public class GroceryList {
    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public GroceryList(String name) {
        this.name = name;
    }
}

