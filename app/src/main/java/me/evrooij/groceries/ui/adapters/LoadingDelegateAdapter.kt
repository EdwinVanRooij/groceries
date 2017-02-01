package me.evrooij.groceries.ui.adapters

import android.support.v7.widget.RecyclerView
import android.view.ViewGroup
import me.evrooij.groceries.R
import me.evrooij.groceries.util.inflate

/**
 * Author: eddy
 * Date: 1-2-17.
 */

class LoadingDelegateAdapter : ViewTypeDelegateAdapter {

    override fun onCreateViewHolder(parent: ViewGroup) = TurnsViewHolder(parent)

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, item: ViewType) {
    }

    class TurnsViewHolder(parent: ViewGroup) : RecyclerView.ViewHolder(
            parent.inflate(R.layout.item_loading)) {
    }
}
