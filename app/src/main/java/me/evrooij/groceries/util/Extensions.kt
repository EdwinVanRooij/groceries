package me.evrooij.groceries.util

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

/**
 * Author: eddy
 * Date: 1-2-17.
 */
fun ViewGroup.inflate(layoutId: Int): View {
    return LayoutInflater.from(context).inflate(layoutId, this, false)
}