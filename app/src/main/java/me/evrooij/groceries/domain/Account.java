package me.evrooij.groceries.domain;

/**
 * Created by eddy on 27-11-16.
 */

public class Account {
    private String username;
    private String email;
    private String password;

    public Account(String username, String email, String password) {
        this.username = username;
        this.email = email;
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }
}