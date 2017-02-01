package me.evrooij.groceries.ui.fragments


import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.fragment_select_friends.*
import me.evrooij.groceries.R
import me.evrooij.groceries.models.Account
import me.evrooij.groceries.models.AccountItem
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

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        rv_users.setHasFixedSize(true)
        rv_users.layoutManager = LinearLayoutManager(context)

        initAdapter()

    }

    private fun initAdapter() {
        if (rv_users.adapter == null) {
            rv_users.adapter = AccountAdapter()
        }
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return container!!.inflate(R.layout.fragment_select_friends)
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
//        rv_users?.setOnItemClickListener { parent, view, position, id ->
//            containerActivity!!.addToSelection(rv_users!!.adapter.getItem(position) as Account)
//        }

        containerActivity = activity as NewListContainerActivity

        containerActivity!!.executeRunnable(Runnable {
            try {
                val result = ApiService.createService(ClientInterface::class.java).getFriends(containerActivity!!.getThisAccount()!!.id).execute().body()

                addToRecyclerView(result)
            } catch (e: IOException) {
                e.printStackTrace()
            }
        })

        super.onViewCreated(view, savedInstanceState)
    }

    private fun addToRecyclerView(accounts: List<Account>) {
        val news = mutableListOf<AccountItem>()
        for (acc in accounts) {
            news.add(AccountItem(acc))
        }
        (rv_users.adapter as AccountAdapter).addAccounts(news)
    }
}
