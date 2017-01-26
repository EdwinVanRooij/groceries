package me.evrooij.groceries.data

import org.parceler.Parcel
import paperparcel.PaperParcel

import java.util.Date

/**
 * Author: eddy
 * Date: 22-11-16.
 */

@PaperParcel
class Product(
        var name: String,
        var amount: Int = 1,
        var comment: String,
        var owner: Account) {

    var id: Int = 0
    var deletionDate: Date? = null
    var imageUrl: String? = null

    fun Product() {
    }

    constructor(id: Int = 0,
                name: String,
                amount: Int = 1,
                comment: String,
                owner: Account) : this(name, amount, comment, owner) {
        this.id = id
    }

    override fun toString(): String {
        return String.format("%s, id %s - %s times of %s, %s", name, id, amount, owner, comment)
    }
}
