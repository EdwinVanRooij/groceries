package me.evrooij.groceries.models

import com.google.gson.annotations.SerializedName

/**
 * Created by eddy on 27-11-16.
 */

data class Feedback(val message: String, @SerializedName("type") val type: Type, val sender: Account) {
    enum class Type {
        @SerializedName("0")
        Suggestion,
        @SerializedName("1")
        Bug
    }
}
