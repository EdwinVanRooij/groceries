package me.evrooij.groceries.data;

import me.evrooij.groceries.rest.ClientInterface;
import me.evrooij.groceries.rest.ServiceGenerator;
import retrofit2.Call;
import retrofit2.Response;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by eddy
 * Date: 27-11-16.
 */

public class LoginManager {

    public Account login(String username, String password) {
        // Create a rest adapter
        ClientInterface client = ServiceGenerator.createService(ClientInterface.class);

        // Fetch and print a list of the contributors to this library.
        Call<Account> call = client.getAccountByLogin(username, password);

        // Execute the call
        Response<Account> response;
        try {
            response = call.execute();
        } catch (IOException e) {
            // Call failed, ignore exception. Return null.
            e.printStackTrace();
            return null;
        }

        // Get the account from the body and return
        return response.body();
    }

    public Account register(String username, String email, String password) {
        // Create a rest adapter
        ClientInterface client = ServiceGenerator.createService(ClientInterface.class);

        // Fetch and print a list of the contributors to this library.
        Map<String, String> accountMap = new HashMap<>();
        accountMap.put("username", username);
        accountMap.put("email", email);
        accountMap.put("password", password);
        Call<Account> call = client.registerAccount(accountMap);

        // Execute the call
        Response<Account> response;
        try {
            response = call.execute();
        } catch (IOException e) {
            // Call failed, ignore exception. Return null.
            e.printStackTrace();
            return null;
        }

        // Get the account from the body and return
        return response.body();
    }
}
