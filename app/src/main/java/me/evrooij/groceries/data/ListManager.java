package me.evrooij.groceries.data;

import android.content.Context;
import me.evrooij.groceries.Config;
import me.evrooij.groceries.rest.ClientInterface;
import me.evrooij.groceries.rest.ResponseMessage;
import me.evrooij.groceries.rest.ServiceGenerator;
import retrofit2.Call;
import retrofit2.Response;

import java.io.IOException;
import java.util.List;

import static android.R.id.list;

/**
 * Author: eddy
 * Date: 15-12-16.
 */

public class ListManager {

    private Context context;

    public ListManager(Context context) {
        this.context = context;
    }

    public GroceryList getList(int listId) {
        try {
            return ServiceGenerator.createService(context, ClientInterface.class).getList(listId).execute().body();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public List<GroceryList> getMyLists(Account account) {
        try {
            return ServiceGenerator.createService(context, ClientInterface.class).getLists(account.getId()).execute().body();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public GroceryList newList(GroceryList list) {
        try {
            return ServiceGenerator.createService(context, ClientInterface.class).newList(list).execute().body();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public Product newProduct(int listId, Product newProduct) {
        try {
            return ServiceGenerator.createService(context, ClientInterface.class).newProduct(listId, newProduct).execute().body();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public ResponseMessage deleteProduct(int listId, int productId) {
        try {
            return ServiceGenerator.createService(context, ClientInterface.class).deleteProduct(listId, productId).execute().body();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public ResponseMessage editProduct(int listId, int productId, Product editedProduct) {
        try {
            return ServiceGenerator.createService(context, ClientInterface.class).editProduct(listId, productId, editedProduct).execute().body();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}
