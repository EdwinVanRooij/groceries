package me.evrooij.groceries.models

import java.util.ArrayList

/**
 * Created by eddy
 * Date: 20-11-16.
 */

data class GroceryList(
        val name: String = "EmptyName",
        val owner: Account,
        // Optional participants parameter
        @Suppress("unused") val participants: List<Account>? = null) {

    val id: Int = 0
    var productList: List<Product> = ArrayList()
}
