package me.evrooij.groceries.data

import org.parceler.Parcel

/**
 * Author: eddy
 * Date: 27-11-16.
 */

@Parcel
class Account(var id: Int, var username: String, var email: String) {

    fun Account() {
    }

    override fun toString(): String = String.format("User %s (%s) - Mail %s", username, id, email)
}
