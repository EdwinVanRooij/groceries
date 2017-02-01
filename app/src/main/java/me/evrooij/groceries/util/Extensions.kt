@file:JvmName("Extensions")

package me.evrooij.groceries.util

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

/**
 * Author: eddy
 * Date: 1-2-17.
 */
fun ViewGroup.inflate(layoutId: Int, attachToRoot: Boolean = false): View {
    return LayoutInflater.from(context).inflate(layoutId, this, attachToRoot)
}

// This method is required for when calling the method from java, it can't deal with optional parameters
// so we'll set default to false without any options to true here.
fun ViewGroup.inflate(layoutId: Int): View {
    return LayoutInflater.from(context).inflate(layoutId, this, false)
}
