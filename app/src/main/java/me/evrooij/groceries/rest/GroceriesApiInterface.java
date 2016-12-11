package me.evrooij.groceries.rest;

import me.evrooij.groceries.domain.Account;
import retrofit.Call;
import retrofit.http.Body;
import retrofit.http.GET;
import retrofit.http.POST;
import retrofit.http.Query;

/**
 * Created by eddy
 * Date: 11-12-16.
 */

public interface GroceriesApiInterface {
    //    http://localhost:4567/users/login?username=user&password=passs
    @GET("/users/login")
    Call<Account> getAccountByLogin(@Query("username") String username, @Query("password") String password);

    @POST("/users/register")
    Call<Account> registerAccount(@Body Account account);
}
