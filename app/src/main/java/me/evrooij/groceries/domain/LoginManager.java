package me.evrooij.groceries.domain;

import me.evrooij.groceries.rest.ClientInterface;
import me.evrooij.groceries.rest.ServiceGenerator;
import retrofit2.Call;
import retrofit2.Response;

import java.io.IOException;

/**
 * Created by eddy
 * Date: 27-11-16.
 */

public class LoginManager {

    /**
     * Retrieves the user's Account on a correct username with password combination.
     *
     * @param username globally unique username
     * @param password password for the user account
     * @return returns the account on correct login credentials, null on incorrect login credentials
     */
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
//            e.printStackTrace();
            return null;
        }

        // Get the account from the body and return
        return response.body();
    }

    /**
     * Creates a new Account entry in the database.
     *
     * @param username identification name, must be:
     *                 unique relative to all of the other usernames
     *                 at least 6 characters long
     *                 at max 30 characters long
     *                 only alphanumeric characters
     * @param email    e-mail address of the user used for communication purposes, must:
     *                 contain something before the @
     *                 contain a @ character
     *                 contain something after the @
     *                 contain a dot after the @
     *                 contain something after the dot
     * @param password used to log into the user account for validation, must be:
     *                 at least 8 characters long
     *                 at max 100 characters long
     * @return the user account if successful, null if unsuccessful
     */
    public Account register(String username, String email, String password) {
        // Create a rest adapter
        ClientInterface client = ServiceGenerator.createService(ClientInterface.class);

        // Fetch and print a list of the contributors to this library.
        Call<Account> call = client.registerAccount(new Account(username, email, password));

        // Execute the call
        Response<Account> response;
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
