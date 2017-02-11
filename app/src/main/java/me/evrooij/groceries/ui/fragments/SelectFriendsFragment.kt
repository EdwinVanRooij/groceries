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
import me.evrooij.groceries.models.AccountManager
import me.evrooij.groceries.network.ApiService
import me.evrooij.groceries.network.ClientInterface
import me.evrooij.groceries.ui.NewListContainerActivity
import me.evrooij.groceries.ui.adapters.AccountAdapter
import me.evrooij.groceries.util.inflate
import me.evrooij.groceries.util.toast
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers
import java.io.IOException
import java.util.*

/**
 * A simple [Fragment] subclass.
 */
class SelectFriendsFragment : RxBaseFragment() {

    private lateinit var containerActivity: NewListContainerActivity

    private val accountManager by lazy { AccountManager() }

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
        super.onViewCreated(view, savedInstanceState)

        containerActivity = activity as NewListContainerActivity

        rv_users.setHasFixedSize(true)
        rv_users.layoutManager = LinearLayoutManager(context)

        initAdapter()

        val subscription = accountManager.getFriends(containerActivity.getThisAccount().id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        { retrievedFriendItems ->
                            (rv_users.adapter as AccountAdapter).addAccounts(retrievedFriendItems)
                        },
                        { e ->
                            context.toast(e.message!!)
                        })
        subscriptions.add(subscription) // add the subscription
    }
}
