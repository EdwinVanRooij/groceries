package me.evrooij.groceries.rest;

import android.content.Context;
import me.evrooij.groceries.R;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


/**
 * Author: eddy
 * Date: 14-12-16.
 */

public class ServiceGenerator {

    private static OkHttpClient.Builder httpClient = new OkHttpClient.Builder();

    private static Retrofit.Builder builder =
            new Retrofit.Builder()
                    .addConverterFactory(GsonConverterFactory.create());

    public static <S> S createService(Context context, Class<S> serviceClass) {
        Retrofit retrofit = builder.baseUrl(context.getString(R.string.root_url)).client(httpClient.build()).build();
        return retrofit.create(serviceClass);
    }
}
