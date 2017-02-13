package me.evrooij.groceries.ui.fragments


import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.antonioleiva.weatherapp.ui.adapters.AccountAdapter
import kotlinx.android.synthetic.main.fragment_select_friends.*
import me.evrooij.groceries.R
import me.evrooij.groceries.domain.AccountManager
import me.evrooij.groceries.ui.NewListContainerActivity
import me.evrooij.groceries.util.inflate
import me.evrooij.groceries.util.toast
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers

/**
 * A simple [Fragment] subclass.
 */
class SelectFriendsFragment : RxBaseFragment() {

    private lateinit var containerActivity: NewListContainerActivity

    private val accountManager by lazy { AccountManager() }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return container!!.inflate(R.layout.fragment_select_friends)
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        containerActivity = activity as NewListContainerActivity

        rv_users.apply {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(context)
        }


        requestFriends()
    }

    fun requestFriends() {
        val subscription = accountManager.getFriends(containerActivity.getThisAccount().id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        { retrievedFriendItems ->
                            val adapter = AccountAdapter(retrievedFriendItems) {
                                context.toast("Added ${it.username}.")
                                containerActivity.addToSelection(it)
                            }
                            rv_users.adapter = adapter
                        },
                        { e ->
                            context.toast(e.message!!)
                        })
        subscriptions.add(subscription) // add the subscription
    }
}
