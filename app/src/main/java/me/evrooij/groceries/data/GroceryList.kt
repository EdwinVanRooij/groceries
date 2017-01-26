package me.evrooij.groceries.data

import java.util.ArrayList

/**
 * Created by eddy
 * Date: 20-11-16.
 */

class GroceryList(
        val name: String,
        val owner: Account,
        // Optional participants parameter
        @Suppress("unused") val participants: List<Account>? = null) {

    val id: Int = 0
    var productList: List<Product> = ArrayList()
}
