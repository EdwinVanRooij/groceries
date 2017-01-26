package me.evrooij.groceries.data

import com.google.gson.annotations.SerializedName

/**
 * Created by eddy on 27-11-16.
 */

class Feedback(
        var message: String,
        @SerializedName("type") var type: Type,
        var sender: Account) {

    var id: Int = 0

    fun Feedback() {

    }

    override fun toString(): String {
        return String.format("Feedback type %s by %s, message %s", type.toString(), sender.toString(), message)
    }

    enum class Type {
        @SerializedName("0")
        Suggestion,
        @SerializedName("1")
        Bug
    }
}
