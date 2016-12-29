package me.evrooij.groceries.rest;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Author: eddy
 * Date: 14-12-16.
 */

public class ServiceGenerator {

    // Development env
//    public static final String API_BASE_URL = "http://192.168.1.126:4567";
    // Production env
    public static final String API_BASE_URL = "http://88.159.34.253:6438";

    private static OkHttpClient.Builder httpClient = new OkHttpClient.Builder();

    private static Retrofit.Builder builder =
            new Retrofit.Builder()
                    .baseUrl(API_BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create());

    public static <S> S createService(Class<S> serviceClass) {
        Retrofit retrofit = builder.client(httpClient.build()).build();
        return retrofit.create(serviceClass);
    }
}
