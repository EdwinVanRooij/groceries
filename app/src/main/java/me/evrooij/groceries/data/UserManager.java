package me.evrooij.groceries.data;

import android.content.Context;
import me.evrooij.groceries.rest.ClientInterface;
import me.evrooij.groceries.rest.ResponseMessage;
import me.evrooij.groceries.rest.ServiceGenerator;
import retrofit2.Call;
import retrofit2.Response;

import java.io.IOException;
import java.util.List;

import static android.R.attr.password;

/**
 * Author: eddy
 * Date: 15-12-16.
 */

public class UserManager {
    private Context context;

    public UserManager(Context context) {
        this.context = context;
    }

    public List<Account> findFriends(int accountId, String query) {
        try {
            return ServiceGenerator.createService(context, ClientInterface.class).findFriends(accountId, query).execute().body();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public ResponseMessage addFriend(int accountId, Account friend) {
        try {
            return ServiceGenerator.createService(context, ClientInterface.class).addFriend(accountId, friend).execute().body();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public List<Account> getFriends(int accountId) {
        try {
            return ServiceGenerator.createService(context, ClientInterface.class).getFriends(accountId).execute().body();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}
