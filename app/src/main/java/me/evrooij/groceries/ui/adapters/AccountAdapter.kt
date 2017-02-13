package com.antonioleiva.weatherapp.ui.adapters

import android.support.v7.widget.RecyclerView
import android.util.SparseBooleanArray
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.antonioleiva.weatherapp.extensions.ctx
import kotlinx.android.synthetic.main.item_user.view.*
import me.evrooij.groceries.R
import me.evrooij.groceries.domain.Account
import me.evrooij.groceries.util.loadImg

class AccountAdapter(val accountList: List<Account>, val itemClick: (Account) -> Unit) : RecyclerView.Adapter<AccountAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.ctx).inflate(R.layout.item_user, parent, false)
        return ViewHolder(view, itemClick)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindView(accountList[position])
    }

    override fun getItemCount() = accountList.size

    class ViewHolder(view: View, val itemClick: (Account) -> Unit) : RecyclerView.ViewHolder(view) {
        fun bindView(account: Account) {
            with(account) {
                itemView.ivProfilePicture.loadImg("")
                itemView.tvName.text = username
                itemView.setOnClickListener {
                    itemClick(this)
                }
            }
        }
    }
}