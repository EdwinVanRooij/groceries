package me.evrooij.groceries.ui.fragments


import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.*
import android.widget.ListView
import butterknife.BindView
import butterknife.ButterKnife
import butterknife.OnItemClick
import butterknife.Unbinder
import me.evrooij.groceries.network.ApiService
import me.evrooij.groceries.network.ClientInterface
import me.evrooij.groceries.ui.NewListContainerActivity
import me.evrooij.groceries.R
import me.evrooij.groceries.ui.adapters.AccountAdapter
import me.evrooij.groceries.models.Account

import java.io.IOException
import java.util.ArrayList

/**
 * A simple [Fragment] subclass.
 */
class SelectFriendsFragment : Fragment() {

    private var listView: ListView? = null

    private var data: ArrayList<Account>? = null
    private var adapter: AccountAdapter? = null

    private var containerActivity: NewListContainerActivity? = null

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater!!.inflate(R.layout.fragment_select_friends, container, false)
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        listView = view?.findViewById(R.id.lv_users) as ListView?
        listView?.setOnItemClickListener { parent, view, position, id ->
            containerActivity!!.addToSelection(listView!!.adapter.getItem(position) as Account)
        }

        containerActivity = activity as NewListContainerActivity

        containerActivity!!.executeRunnable(Runnable {
            try {
                val result = ApiService.createService(ClientInterface::class.java).getFriends(containerActivity!!.getThisAccount()!!.id).execute().body()

                refreshListView(result)
            } catch (e: IOException) {
                e.printStackTrace()
            }
        })

        super.onViewCreated(view, savedInstanceState)
    }

    private fun refreshListView(accounts: List<Account>) {
        // Construct the data source
        data = ArrayList(accounts)
        // Create the adapter to convert the array to views
        adapter = AccountAdapter(context, data)

        containerActivity!!.runOnUiThread {
            // Attach the adapter to a ListView
            listView!!.adapter = adapter
        }
    }
}
