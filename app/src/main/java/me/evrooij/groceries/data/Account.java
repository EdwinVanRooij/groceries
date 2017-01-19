package me.evrooij.groceries.data;

import org.parceler.Parcel;

/**
 * Author: eddy
 * Date: 27-11-16.
 */

@Parcel
public class Account {
    int id;
    String username;
    String email;
    String password;

    public Account(int id, String username, String email) {
        this.id = id;
        this.username = username;
        this.email = email;
    }

    public Account() {
    }

    public int getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public String getEmail() {
        return email;
    }

    @Override
    public String toString() {
        return String.format("User %s (%s) - Mail %s - Pass %s", username, id, email, password);
    }
}
