package me.evrooij.groceries.rest;

import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Path;

/**
 * Author: eddy
 * Date: 22-1-17.
 */

public interface FileUploadInterface {
    @Multipart
    @POST("/{accountId}/myproducts/{productId}")
    Call<ResponseMessage> upload(
            @Path("accountId") int accountId,
            @Path("productId") int productId,
            @Part MultipartBody.Part file
    );
}
