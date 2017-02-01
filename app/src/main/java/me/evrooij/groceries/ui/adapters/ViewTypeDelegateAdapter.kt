package me.evrooij.groceries.ui.adapters

import android.support.v7.widget.RecyclerView
import android.view.ViewGroup

/**
 * Author: eddy
 * Date: 1-2-17.
 */

interface ViewTypeDelegateAdapter {
    fun onCreateViewHolder(parent: ViewGroup): RecyclerView.ViewHolder

    fun onBindViewHolder(holder: RecyclerView.ViewHolder, item: ViewType)
}
