package me.evrooij.groceries.network

import me.evrooij.groceries.models.Account
import me.evrooij.groceries.models.Feedback
import me.evrooij.groceries.models.GroceryList
import me.evrooij.groceries.models.Product
import retrofit2.Call
import retrofit2.http.*

/**
 * Created by eddy
 * Date: 11-12-16.
 */
interface ClientInterface {
    // Login
    @GET("/users/login")
    fun getAccountByLogin(@Query("username") username: String, @Query("password") password: String): Call<Account>

    @POST("/users/register")
    fun registerAccount(@Body accountMap: Map<String, String>): Call<Account>


    // Friends
    @GET("/accounts/{ownId}/friends/find")
    fun findFriends(@Path("ownId") ownId: Int, @Query("query") searchQuery: String): Call<List<Account>>

    @POST("/accounts/{ownId}/friends/add")
    fun addFriend(@Path("ownId") ownId: Int, @Body newFriend: Account): Call<ResponseMessage>

    @GET("/accounts/{ownId}/friends")
    fun getFriends(@Path("ownId") ownId: Int): Call<List<Account>>


    // Lists
    @GET("/user/{id}/lists")
    fun getLists(@Path("id") accountId: Int): Call<List<GroceryList>>

    @GET("/lists/{id}")
    fun getList(@Path("id") listId: Int): Call<GroceryList>

    @POST("/lists/new")
    fun newList(@Body list: GroceryList): Call<GroceryList>


    // Products
    @POST("/list/{id}/products/new")
    fun newProduct(@Path("id") listId: Int, @Body newProduct: Product): Call<Product>

    @DELETE("/lists/{listId}/products/{productId}")
    fun deleteProduct(@Path("listId") listId: Int, @Path("productId") productId: Int): Call<ResponseMessage>

    @PUT("/lists/{listId}/products/{productId}/edit")
    fun editProduct(@Path("listId") listId: Int, @Path("productId") productId: Int, @Body editedProduct: Product): Call<ResponseMessage>

    @GET("/{id}/myproducts")
    fun getMyProducts(@Path("id") accountId: Int): Call<List<Product>>

    @POST("/{id}/myproducts")
    fun addMyProduct(@Path("id") accountId: Int, @Body product: Product): Call<Product>


    // Feedback
    @POST("/feedback/new")
    fun reportFeedback(@Body feedback: Feedback): Call<ResponseMessage>

}
