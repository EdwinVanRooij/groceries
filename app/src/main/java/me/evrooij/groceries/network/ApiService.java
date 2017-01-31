package me.evrooij.groceries.network;

import android.content.Context;
import me.evrooij.groceries.BuildConfig;
import me.evrooij.groceries.R;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


/**
 * Author: eddy
 * Date: 14-12-16.
 */

public class ApiService {

    private static final String BASE_URL = BuildConfig.GROCERIES_API_ENDPOINT;

    private static OkHttpClient.Builder httpClient = new OkHttpClient.Builder();

    private static Retrofit.Builder builder =
            new Retrofit.Builder()
                    .addConverterFactory(GsonConverterFactory.create());

    public static <S> S createService(Class<S> serviceClass) {
        Retrofit retrofit = builder.baseUrl(BASE_URL).client(httpClient.build()).build();
        return retrofit.create(serviceClass);
    }
}
