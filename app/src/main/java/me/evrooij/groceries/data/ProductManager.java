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
        try {
            return ServiceGenerator.createService(context, ClientInterface.class).getMyProducts(accountId).execute().body();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public Product addMyProduct(int accountId, Product product) {
        try {
            return ServiceGenerator.createService(context, ClientInterface.class).addMyProduct(accountId, product).execute().body();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}
