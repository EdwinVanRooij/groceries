package me.evrooij.groceries.rest;

import me.evrooij.groceries.domain.Account;
import me.evrooij.groceries.domain.GroceryList;
import retrofit2.Call;
import retrofit2.http.*;

import java.util.List;
import java.util.Map;

/**
 * Created by eddy
 * Date: 11-12-16.
 */

public interface ClientInterface {
    // Login interfaces
    @GET("/users/login")
    Call<Account> getAccountByLogin(@Query("username") String username, @Query("password") String password);

    @POST("/users/register")
    Call<Account> registerAccount(@Body Map<String, String> accountMap);

    // Get all lists of a user
    @GET("/user/{id}/lists")
    Call<List<GroceryList>> getListsByAccountId(@Path("id") int accountId);
}
