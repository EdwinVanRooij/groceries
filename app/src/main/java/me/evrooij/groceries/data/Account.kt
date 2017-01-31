package me.evrooij.groceries.data

import android.os.Parcel
import android.os.Parcelable

/**
 * Author: eddy
 * Date: 27-11-16.
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