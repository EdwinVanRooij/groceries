package me.evrooij.groceries.domain;

/**
 * Created by eddy on 21-11-16.
 */

public class Friend {
    private String username;
    private String name;
    private String surname;
    private int age;

    public String getUsername() {
        return username;
    }

    public String getName() {
        return name;
    }

    public String getSurname() {
        return surname;
    }

    public int getAge() {
        return age;
    }

    public Friend(String username, String name, String surname, int age) {
        this.username = username;
        this.name = name;
        this.surname = surname;
        this.age = age;
    }

    @Override
    public String toString() {
        return String.format("%s %s", getName(), getSurname());
    }
}
