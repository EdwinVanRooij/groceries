package me.evrooij.groceries.rest;

import android.util.Log;
import com.squareup.okhttp.OkHttpClient;
import retrofit.GsonConverterFactory;
import retrofit.Retrofit;

import java.io.IOException;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;

/**
 * Created by eddy
 * Date: 11-12-16.
 */

public class RestClient {

    private static GroceriesApiInterface gitApiInterface;

    public static GroceriesApiInterface getClient() {
        if (gitApiInterface == null) {

            OkHttpClient okClient = new OkHttpClient();
            okClient.interceptors().add(chain -> chain.proceed(chain.request()));

//            http://192.168.1.126:4567/users/login?username=user&password=passs
            String baseUrl = "http://192.168.1.126:4567";
            Retrofit client = new Retrofit.Builder()
                    .baseUrl(baseUrl)
                    .addConverter(String.class, new ToStringConverter())
                    .client(okClient)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
            gitApiInterface = client.create(GroceriesApiInterface.class);
        }
        return gitApiInterface;
    }
}
