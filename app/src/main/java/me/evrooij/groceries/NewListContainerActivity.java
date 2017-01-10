package me.evrooij.groceries;

import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Toast;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import com.mikepenz.google_material_typeface_library.GoogleMaterial;
import com.mikepenz.iconics.IconicsDrawable;
import me.evrooij.groceries.data.Account;
import me.evrooij.groceries.data.GroceryList;
import me.evrooij.groceries.data.ListManager;
import me.evrooij.groceries.fragments.CompleteListFragment;
import me.evrooij.groceries.fragments.SelectFriendsFragment;
import org.parceler.Parcels;

import java.util.ArrayList;
import java.util.List;

import static me.evrooij.groceries.Config.KEY_ACCOUNT;
import static me.evrooij.groceries.Config.KEY_SELECTED_ACCOUNTS;

public class NewListContainerActivity extends AppCompatActivity {
    @BindView(R.id.fab)
    FloatingActionButton fab;

    private Account thisAccount;
    private List<Account> selectedAccounts;
    private String listName;

    private ListManager listManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_container_new_list);
        ButterKnife.bind(this);

        fab.setImageDrawable(new IconicsDrawable(this, GoogleMaterial.Icon.gmd_arrow_forward).color(Color.WHITE).sizeDp(24));

        thisAccount = Parcels.unwrap(getIntent().getParcelableExtra(KEY_ACCOUNT));
        selectedAccounts = new ArrayList<>();
        listManager = new ListManager(getApplicationContext());

        try {
            Fragment fragment = SelectFriendsFragment.class.newInstance();
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

            Bundle bundle = new Bundle();
            bundle.putParcelable(KEY_ACCOUNT, Parcels.wrap(thisAccount));
            fragment.setArguments(bundle);

            transaction.replace(R.id.flContent, fragment);
            transaction.addToBackStack(null);
            transaction.commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void addToSelection(Account account) {
        selectedAccounts.add(account);
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
            try {
                Fragment fragment = CompleteListFragment.class.newInstance();
                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

                Bundle bundle = new Bundle();
                bundle.putParcelable(KEY_ACCOUNT, Parcels.wrap(thisAccount));
                bundle.putParcelable(KEY_SELECTED_ACCOUNTS, Parcels.wrap(selectedAccounts));
                fragment.setArguments(bundle);

                transaction.setCustomAnimations(R.anim.slide_in_left, R.anim.slide_out_left, R.anim.slide_in_right, R.anim.slide_out_right);
                transaction.replace(R.id.flContent, fragment);
                transaction.commit();
            } catch (Exception e) {
                e.printStackTrace();
            }
            fab.setImageDrawable(new IconicsDrawable(this, GoogleMaterial.Icon.gmd_done).color(Color.WHITE).sizeDp(24));
        } else if (f instanceof CompleteListFragment) {
            executeNewList();
        } else {
            Snackbar.make(view, "Could not determine the current fragment", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show();
        }
    }

    private void executeNewList() {
        new Thread(() -> {
            GroceryList listToAdd = new GroceryList(listName, thisAccount, selectedAccounts);

            GroceryList returnedList = listManager.newList(listToAdd);

            runOnUiThread(() -> {
                Toast.makeText(this, String.format("Successfully created new list %s", returnedList.getName()), Toast.LENGTH_SHORT).show();
                finish();
            });

        }).start();
    }
}
