package me.evrooij.groceries.data;

import android.content.Context;
import me.evrooij.groceries.rest.ClientInterface;
import me.evrooij.groceries.rest.ResponseMessage;
import me.evrooij.groceries.rest.ServiceGenerator;
import retrofit2.Call;
import retrofit2.Response;

import java.io.IOException;
import java.util.List;

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
        System.out.println(String.format("Starting search query with account id %s, query %s", String.valueOf(accountId), query));
        // Create a rest adapter
        ClientInterface client = ServiceGenerator.createService(context, ClientInterface.class);

        // Fetch and print a list of the contributors to this library.
        Call<List<Account>> call = client.findFriends(accountId, query);

        // Execute the call
        Response<List<Account>> response;
        try {
            response = call.execute();
        } catch (IOException e) {
            // Call failed, ignore exception. Return null.
//            e.printStackTrace();
            return null;
        }

        // Get the account from the body and return
        return response.body();
    }

    public ResponseMessage addFriend(int accountId, Account friend) {
        // Create a rest adapter
        ClientInterface client = ServiceGenerator.createService(context, ClientInterface.class);

        // Fetch and print a list of the contributors to this library.
        Call<ResponseMessage> call = client.addFriend(accountId, friend);

        // Execute the call
        Response<ResponseMessage> response;
        try {
            response = call.execute();
        } catch (IOException e) {
            // Call failed, ignore exception. Return null.
//            e.printStackTrace();
            return null;
        }

        // Get the account from the body and return
        return response.body();
    }

    public List<Account> getFriends(int accountId) {
        // Create a rest adapter
        ClientInterface client = ServiceGenerator.createService(context, ClientInterface.class);

        // Fetch and print a list of the contributors to this library.
        Call<List<Account>> call = client.getFriends(accountId);

        // Execute the call
        Response<List<Account>> response;
        try {
            response = call.execute();
        } catch (IOException e) {
            // Call failed, ignore exception. Return null.
//            e.printStackTrace();
            return null;
        }

        // Get the account from the body and return
        return response.body();
    }
}
