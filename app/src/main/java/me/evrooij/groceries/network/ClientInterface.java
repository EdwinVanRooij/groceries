package me.evrooij.groceries.network;

import me.evrooij.groceries.models.Account;
import me.evrooij.groceries.models.Feedback;
import me.evrooij.groceries.models.GroceryList;
import me.evrooij.groceries.models.Product;
import retrofit2.Call;
import retrofit2.http.*;

import java.util.List;
import java.util.Map;

/**
 * Created by eddy
 * Date: 11-12-16.
 */

public interface ClientInterface {

    // Login
    @GET("/users/login")
    Call<Account> getAccountByLogin(@Query("username") String username, @Query("password") String password);

    @POST("/users/register")
    Call<Account> registerAccount(@Body Map<String, String> accountMap);


    // Friends
    @GET("/accounts/{ownId}/friends/find")
    Call<List<Account>> findFriends(@Path("ownId") int ownId, @Query("query") String searchQuery);

    @POST("/accounts/{ownId}/friends/add")
    Call<ResponseMessage> addFriend(@Path("ownId") int ownId, @Body Account newFriend);

    @GET("/accounts/{ownId}/friends")
    Call<List<Account>> getFriends(@Path("ownId") int ownId);


    // Lists
    @GET("/user/{id}/lists")
    Call<List<GroceryList>> getLists(@Path("id") int accountId);

    @GET("/lists/{id}")
    Call<GroceryList> getList(@Path("id") int listId);

    @POST("/lists/new")
    Call<GroceryList> newList(@Body GroceryList list);


    // Products
    @POST("/list/{id}/products/new")
    Call<Product> newProduct(@Path("id") int listId, @Body Product newProduct);

    @DELETE("/lists/{listId}/products/{productId}")
    Call<ResponseMessage> deleteProduct(@Path("listId") int listId, @Path("productId") int productId);

    @PUT("/lists/{listId}/products/{productId}/edit")
    Call<ResponseMessage> editProduct(@Path("listId") int listId, @Path("productId") int productId, @Body Product editedProduct);

    @GET("/{id}/myproducts")
    Call<List<Product>> getMyProducts(@Path("id") int accountId);

    @POST("/{id}/myproducts")
    Call<Product> addMyProduct(@Path("id") int accountId, @Body Product product);


    // Feedback
    @POST("/feedback/new")
    Call<ResponseMessage> reportFeedback(@Body Feedback feedback);

}
