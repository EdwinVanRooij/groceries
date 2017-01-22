package me.evrooij.groceries.data;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;
import me.evrooij.groceries.rest.ClientInterface;
import me.evrooij.groceries.rest.FileUploadInterface;
import me.evrooij.groceries.rest.ResponseMessage;
import me.evrooij.groceries.rest.ServiceGenerator;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.http.Part;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

import static android.R.attr.bitmap;

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
