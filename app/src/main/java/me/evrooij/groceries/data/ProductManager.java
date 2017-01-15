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

public class ProductManager {

    private Context context;

    public ProductManager(Context context) {
        this.context = context;
    }

    public List<Product> getMyProducts(int accountId) {
        // Create a rest adapter
        ClientInterface client = ServiceGenerator.createService(context, ClientInterface.class);

        // Fetch and print a list of the contributors to this library.
        Call<List<Product>> call = client.getMyProducts(accountId);

        // Execute the call
        Response<List<Product>> response;
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
