package me.evrooij.groceries.ui.fragments


import android.os.Bundle
import kotlinx.android.synthetic.main.fragment_select_friends.*
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ListView
import me.evrooij.groceries.R
import me.evrooij.groceries.models.Account
import me.evrooij.groceries.network.ApiService
import me.evrooij.groceries.network.ClientInterface
import me.evrooij.groceries.ui.NewListContainerActivity
import me.evrooij.groceries.ui.adapters.AccountAdapter
import me.evrooij.groceries.util.inflate
import java.io.IOException
import java.util.*

/**
 * A simple [Fragment] subclass.
 */
class SelectFriendsFragment : Fragment() {

    private var data: ArrayList<Account>? = null
    private var adapter: AccountAdapter? = null

    private var containerActivity: NewListContainerActivity? = null

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return container!!.inflate(R.layout.fragment_select_friends)
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        lv_users.setOnItemClickListener { parent, view, position, id ->
            containerActivity!!.addToSelection(lv_users.adapter.getItem(position) as Account)
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
            lv_users.adapter = adapter
        }
    }
}
