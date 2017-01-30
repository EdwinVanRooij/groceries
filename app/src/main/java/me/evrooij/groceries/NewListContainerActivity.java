package me.evrooij.groceries;

import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import com.mikepenz.google_material_typeface_library.GoogleMaterial;
import com.mikepenz.iconics.IconicsDrawable;
import me.evrooij.groceries.data.Account;
import me.evrooij.groceries.data.GroceryList;
import me.evrooij.groceries.fragments.CompleteListFragment;
import me.evrooij.groceries.fragments.SelectFriendsFragment;
import me.evrooij.groceries.interfaces.ContainerActivity;
import me.evrooij.groceries.rest.ClientInterface;
import me.evrooij.groceries.rest.ServiceGenerator;
import org.parceler.Parcels;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static me.evrooij.groceries.Config.KEY_ACCOUNT;
import static me.evrooij.groceries.Config.THREADPOOL_NEWLIST_SIZE;

public class NewListContainerActivity extends AppCompatActivity implements ContainerActivity {

    @BindView(R.id.fab)
    FloatingActionButton fab;

    private Account thisAccount;
    private List<Account> selectedAccounts;
    private String listName;

    private ExecutorService threadPool;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_container_new_list);
        ButterKnife.bind(this);

        fab.setImageDrawable(new IconicsDrawable(this, GoogleMaterial.Icon.gmd_arrow_forward).color(Color.WHITE).sizeDp(24));

        thisAccount = Parcels.unwrap(getIntent().getParcelableExtra(KEY_ACCOUNT));
        selectedAccounts = new ArrayList<>();

        threadPool = Executors.newFixedThreadPool(THREADPOOL_NEWLIST_SIZE);

        setFragment(SelectFriendsFragment.class, false);
    }

    public void addToSelection(Account account) {
        if (!selectedAccounts.contains(account)) {
            selectedAccounts.add(account);
        }
    }

    public void setListName(String name) {
        this.listName = name;
    }

    @Override
    public void onBackPressed() {
        fab.setImageDrawable(new IconicsDrawable(this, GoogleMaterial.Icon.gmd_arrow_forward).color(Color.WHITE).sizeDp(24));
        super.onBackPressed();
    }

    @OnClick(R.id.fab)
    public void onFabClick(View view) {
        Fragment f = getSupportFragmentManager().findFragmentById(R.id.flContent);

        if (f instanceof SelectFriendsFragment) {
            // Done selecting friends, move on to completion
            setFragment(CompleteListFragment.class, false);
            fab.setImageDrawable(new IconicsDrawable(this, GoogleMaterial.Icon.gmd_done).color(Color.WHITE).sizeDp(24));
        } else if (f instanceof CompleteListFragment) {
            completeList();
        } else {
            Snackbar.make(view, "Could not determine the current fragment", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show();
        }
    }

    private void completeList() {
        executeRunnable(() -> {
            try {
                GroceryList listToAdd = new GroceryList(listName, thisAccount, selectedAccounts);

                GroceryList returnedList = ServiceGenerator.createService(this, ClientInterface.class).newList(listToAdd).execute().body();

                runOnUiThread(() -> {
                    Toast.makeText(this, String.format("Successfully created new list %s", returnedList.getName()), Toast.LENGTH_SHORT).show();
                    finish();
                });
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

    @Override
    public Account getThisAccount() {
        return thisAccount;
    }

    @Override
    public void setFragment(Class fragmentClass, boolean addToStack) {
        try {
            Fragment fragment = (Fragment) fragmentClass.newInstance();
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            // Add this transaction to the back stack
            if (addToStack) {
                transaction.addToBackStack(null);
            }
            transaction.setCustomAnimations(R.anim.slide_in_left, R.anim.slide_out_left, R.anim.slide_in_right, R.anim.slide_out_right);
            transaction.replace(R.id.flContent, fragment).commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void executeRunnable(Runnable runnable) {
        threadPool.execute(runnable);
    }

    @Override
    public void setActionBarTitle(String title) {
        runOnUiThread(() -> setActionBarTitle(title));
    }

    public List<Account> getSelectedAccounts() {
        return selectedAccounts;
    }
}
