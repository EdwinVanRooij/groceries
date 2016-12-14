package me.evrooij.groceries.rest;

import me.evrooij.groceries.domain.Account;
import retrofit2.Call;
import retrofit2.http.*;

/**
 * Created by eddy
 * Date: 11-12-16.
 */

public interface GroceriesClient {
    //    http://localhost:4567/users/login?username=user&password=passs
    @GET("/users/login")
    Call<Account> getAccountByLogin(@Query("username") String username, @Query("password") String password);

    @POST("/users/register")
    Call<Account> registerAccount(@Body Account account);

//    @GET("/repos/{owner}/{repo}/contributors")
//    Call<List<Contributor>> contributors(
//            @Path("owner") String owner,
//            @Path("repo") String repo
//    );
}
