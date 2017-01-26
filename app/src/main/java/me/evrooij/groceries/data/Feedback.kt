package me.evrooij.groceries.data

import com.google.gson.annotations.SerializedName

/**
 * Created by eddy on 27-11-16.
 */

class Feedback(val message: String, @SerializedName("type") val type: Type, val sender: Account) {
    enum class Type {
        @SerializedName("0")
        Suggestion,
        @SerializedName("1")
        Bug
    }
}

