package me.evrooij.groceries.data

import java.util.ArrayList

/**
 * Created by eddy
 * Date: 20-11-16.
 */

class GroceryList(
        val name: String,
        val owner: Account) {

    val id: Int = 0
    var participants: List<Account>? = null
    var productList: List<Product>? = null

    init {
        productList = ArrayList<Product>()
        participants = ArrayList<Account>()
    }

    @Suppress("unused")
    constructor(name: String, owner: Account, participants: List<Account>) : this(name, owner) {
        this.participants = participants
        productList = ArrayList<Product>()
    }
}
