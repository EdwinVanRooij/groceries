package me.evrooij.groceries.domain;


import org.jraf.android.prefs.Name;
import org.jraf.android.prefs.Prefs;
import org.parceler.Parcel;

/**
 * Created by eddy on 27-11-16.
 */

@Prefs
@Parcel
public class Account {
    @Name("PREF_ID")
    Integer id;
    @Name("PREF_USERNAME")
    String username;
    @Name("PREF_EMAIL")
    String email;
    String password;

    public Account(int id, String username, String email, String password) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.password = password;
    }

    public Account(String username, String email, String password) {
        this.username = username;
        this.email = email;
        this.password = password;
    }

    public Account() {
    }

    public int getId() {
        return id;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPassword(String password) {
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

    @Override
    public String toString() {
        return String.format("User %s (%s) - Mail %s - Pass %s", username, id, email, password);
    }
}
