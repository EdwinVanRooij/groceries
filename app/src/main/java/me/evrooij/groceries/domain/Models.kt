package me.evrooij.groceries.domain

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import java.util.*

/**
 * Author: eddy
 * Date: 13-2-17.
 */
data class Account(var id: Int, var username: String, var email: String) : Parcelable {
    companion object {
        @JvmField val CREATOR: Parcelable.Creator<Account> = object : Parcelable.Creator<Account> {
            override fun createFromParcel(source: Parcel): Account = Account(source)
            override fun newArray(size: Int): Array<Account?> = arrayOfNulls(size)
        }
    }

    constructor(source: Parcel) : this(source.readInt(), source.readString(), source.readString())

    override fun describeContents() = 0

    override fun writeToParcel(dest: Parcel?, flags: Int) {
        dest?.writeInt(id)
        dest?.writeString(username)
        dest?.writeString(email)
    }
}

data class Feedback(val message: String, @SerializedName("type") val type: Type, val sender: Account) {
    enum class Type {
        @SerializedName("0")
        Suggestion,
        @SerializedName("1")
        Bug
    }
}

data class GroceryList(
        val name: String = "EmptyName",
        val owner: Account,
        // Optional participants parameter
        @Suppress("unused") val participants: List<Account>? = null) {

    val id: Int = 0
    var productList: List<Product> = ArrayList()
}
