package me.evrooij.groceries.domain;

import me.evrooij.groceries.rest.ClientInterface;
import me.evrooij.groceries.rest.ServiceGenerator;
import retrofit2.Call;
import retrofit2.Response;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static android.R.attr.password;

/**
 * Author: eddy
 * Date: 15-12-16.
 */

public class ListManager {

    public List<GroceryList> getMyLists(Account account) {
        // Create a rest adapter
        ClientInterface client = ServiceGenerator.createService(ClientInterface.class);

        // Fetch and print a list of the contributors to this library.
        Call<List<GroceryList>> call = client.getListsByAccountId(account.getId());

        // Execute the call
        Response<List<GroceryList>> response;
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

    public GroceryList newList(GroceryList list) {
        // Create a rest adapter
        ClientInterface client = ServiceGenerator.createService(ClientInterface.class);

        // Fetch and print a list of the contributors to this library.
        Call<GroceryList> call = client.newList(list);

        // Execute the call
        Response<GroceryList> response;
        try {
            response = call.execute();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }

        return response.body();
    }
}
