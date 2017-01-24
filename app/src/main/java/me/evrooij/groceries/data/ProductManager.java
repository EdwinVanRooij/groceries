package me.evrooij.groceries.data;

import android.content.Context;
import android.net.Uri;
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

import java.io.File;
import java.io.IOException;
import java.net.URI;
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

    public ResponseMessage editMyProduct(int accountId, Product product, File file) {
        FileUploadInterface service = ServiceGenerator.createService(context, FileUploadInterface.class);

        // create RequestBody instance from file
        RequestBody requestFile =
                RequestBody.create(
                        MediaType.parse("image/*"),
                        file
                );

        // MultipartBody.Part is used to send also the actual file name
        MultipartBody.Part body =
                MultipartBody.Part.createFormData("picture", file.getName(), requestFile);

        // finally, execute the request
        Call<ResponseMessage> call = service.upload(accountId, product.getId(), body);
        try {
            return call.execute().body();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}
