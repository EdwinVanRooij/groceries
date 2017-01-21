package me.evrooij.groceries.data;

import android.content.Context;
import me.evrooij.groceries.rest.ClientInterface;
import me.evrooij.groceries.rest.ServiceGenerator;
import retrofit2.Call;
import retrofit2.Response;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by eddy
 * Date: 27-11-16.
 */

public class LoginManager {

    private Context context;

    public LoginManager(Context context) {
        this.context = context;
    }

    public Account login(String username, String password) {
        try {
            return ServiceGenerator.createService(context, ClientInterface.class).getAccountByLogin(username, password).execute().body();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public Account register(String username, String email, String password) {
        try {
            Map<String, String> accountMap = new HashMap<>();
            accountMap.put("username", username);
            accountMap.put("email", email);
            accountMap.put("password", password);
            return ServiceGenerator.createService(context, ClientInterface.class).registerAccount(accountMap).execute().body();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}
