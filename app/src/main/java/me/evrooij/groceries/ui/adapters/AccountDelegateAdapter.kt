package me.evrooij.groceries.ui.adapters

import android.support.v7.widget.RecyclerView
import android.view.ViewGroup
import me.evrooij.groceries.util.inflate
import me.evrooij.groceries.R
import me.evrooij.groceries.models.AccountItem

import kotlinx.android.synthetic.main.item_user.view.*
import me.evrooij.groceries.util.loadImg

/**
 * Author: eddy
 * Date: 1-2-17.
 */

class AccountDelegateAdapter : ViewTypeDelegateAdapter {

    override fun onCreateViewHolder(parent: ViewGroup): RecyclerView.ViewHolder {
        return TurnsViewHolder(parent)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, item: ViewType) {
        holder as TurnsViewHolder
        holder.bind(item as AccountItem)
    }

    class TurnsViewHolder(parent: ViewGroup) : RecyclerView.ViewHolder(
            parent.inflate(R.layout.item_user)) {

        fun bind(item: AccountItem) = with(itemView) {
            ivProfilePicture.loadImg("")
            tvName.text = item.account.username
        }
    }
}