package me.evrooij.groceries.ui

import android.app.FragmentManager
import android.graphics.Color
import android.os.Bundle
import android.os.Parcelable
import android.support.design.widget.FloatingActionButton
import android.support.design.widget.Snackbar
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.widget.Toast
import butterknife.BindView
import butterknife.ButterKnife
import butterknife.OnClick
import com.mikepenz.google_material_typeface_library.GoogleMaterial
import com.mikepenz.iconics.IconicsDrawable
import me.evrooij.groceries.models.Account
import me.evrooij.groceries.models.GroceryList
import me.evrooij.groceries.ui.fragments.CompleteListFragment
import me.evrooij.groceries.ui.fragments.SelectFriendsFragment
import me.evrooij.groceries.interfaces.ContainerActivity
import me.evrooij.groceries.network.ClientInterface
import me.evrooij.groceries.network.ApiService
import org.parceler.Parcels

import java.io.IOException
import java.util.ArrayList
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

import me.evrooij.groceries.util.Config.KEY_ACCOUNT
import me.evrooij.groceries.util.Config.THREADPOOL_NEWLIST_SIZE
import me.evrooij.groceries.R

class NewListContainerActivity : AppCompatActivity(), ContainerActivity {

    //        @BindView(R.id.fab)
    lateinit var fab: FloatingActionButton

    private var thisAccount: Account? = null
    private var selectedAccounts: MutableList<Account>? = null
    private var listName: String? = null

    private var threadPool: ExecutorService? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_container_new_list)
        ButterKnife.bind(this)

        fab = findViewById(R.id.fab) as FloatingActionButton
        fab.setImageDrawable(IconicsDrawable(this, GoogleMaterial.Icon.gmd_arrow_forward).color(Color.WHITE).sizeDp(24))
        fab.setOnClickListener({
            val f = supportFragmentManager.findFragmentById(R.id.flContent)

            if (f is SelectFriendsFragment) {
                // Done selecting friends, move on to completion
                setFragment(CompleteListFragment::class.java, false)
                fab.setImageDrawable(IconicsDrawable(this, GoogleMaterial.Icon.gmd_done).color(Color.WHITE).sizeDp(24))
            } else if (f is CompleteListFragment) {
                completeList()
            } else {
                Snackbar.make(fab, "Could not determine the current fragment", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show()
            }
        })

        thisAccount = Parcels.unwrap<Account>(intent.getParcelableExtra<Parcelable>(KEY_ACCOUNT))
        selectedAccounts = ArrayList<Account>()

        threadPool = Executors.newFixedThreadPool(THREADPOOL_NEWLIST_SIZE)

        setFragment(SelectFriendsFragment::class.java, false)
    }

    fun addToSelection(account: Account) {
        if (!selectedAccounts!!.contains(account)) {
            selectedAccounts!!.add(account)
        }
    }

    fun setListName(name: String) {
        this.listName = name
    }

    override fun onBackPressed() {
        fab.setImageDrawable(IconicsDrawable(this, GoogleMaterial.Icon.gmd_arrow_forward).color(Color.WHITE).sizeDp(24))
        super.onBackPressed()
    }


    private fun completeList() {
        executeRunnable {
            try {
                val listToAdd = GroceryList(listName!!, thisAccount!!, selectedAccounts)

                val (name) = ApiService.createService(ClientInterface::class.java).newList(listToAdd).execute().body()

                runOnUiThread {
                    Toast.makeText(this, String.format("Successfully created new list %s", name), Toast.LENGTH_SHORT).show()
                    finish()
                }
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
    }

    override fun getThisAccount(): Account? {
        return thisAccount
    }

    fun changeFragment(f: Fragment, cleanStack: Boolean = false) {
        val ft = supportFragmentManager.beginTransaction()
        ft.setCustomAnimations(R.anim.abc_fade_in, R.anim.abc_fade_out, R.anim.abc_popup_enter, R.anim.abc_popup_exit)
        ft.replace(R.id.flContent, f)
        ft.addToBackStack(null)
        ft.commit()
    }

    override fun setFragment(fragmentClass: Class<*>, addToStack: Boolean = false) {
        try {
            val fragment = fragmentClass.newInstance() as Fragment
            val transaction = supportFragmentManager.beginTransaction()
            // Add this transaction to the back stack
            if (addToStack) {
                transaction.addToBackStack(null)
            }
            transaction.setCustomAnimations(R.anim.slide_in_left, R.anim.slide_out_left, R.anim.slide_in_right, R.anim.slide_out_right)
            transaction.replace(R.id.flContent, fragment).commit()
        } catch (e: Exception) {
            e.printStackTrace()
        }

    }

    override fun executeRunnable(runnable: Runnable) {
        threadPool!!.execute(runnable)
    }

    override fun setActionBarTitle(title: String) {
        runOnUiThread { setActionBarTitle(title) }
    }

    fun getSelectedAccounts(): List<Account> {
        return ArrayList(selectedAccounts)
    }
}
