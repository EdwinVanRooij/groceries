package me.evrooij.groceries.rest;

import me.evrooij.groceries.domain.Account;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

/**
 * Created by eddy
 * Date: 11-12-16.
 */

public interface LoginClientInterface {
    //    http://localhost:4567/users/login?username=user&password=passs
    @GET("/users/login")
    Call<Account> getAccountByLogin(@Query("username") String username, @Query("password") String password);

    @POST("/users/register")
    Call<Account> registerAccount(@Body Account account);
}
