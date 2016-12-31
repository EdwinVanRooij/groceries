package me.evrooij.groceries.data;

import com.google.gson.annotations.SerializedName;

/**
 * Created by eddy on 27-11-16.
 */

public class Feedback {
    private int id;
    private String message;

    // GSON annotation for deserialization
    @SerializedName("type")
    private Type type;

    private Account sender;

    public Feedback(String message, Type type, Account sender) {
        this.message = message;
        this.type = type;
        this.sender = sender;
    }

    public Feedback() {
    }

    public String getMessage() {
        return message;
    }

    public Type getType() {
        return type;
    }

    public Account getSender() {
        return sender;
    }

    @Override
    public String toString() {
        return String.format("Feedback type %s by %s, message %s", type.toString(), sender.toString(), message);
    }

    public enum Type {
        @SerializedName("0")
        Suggestion,
        @SerializedName("1")
        Bug
    }
}
