package me.evrooij.groceries;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import com.mikepenz.google_material_typeface_library.GoogleMaterial;
import com.mikepenz.iconics.IconicsDrawable;
import me.evrooij.groceries.data.Account;
import me.evrooij.groceries.data.Product;
import me.evrooij.groceries.data.ProductDataProvider;
import me.evrooij.groceries.fragments.*;
import org.parceler.Parcels;

import static me.evrooij.groceries.Constants.KEY_ACCOUNT;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private ProductDataProvider mDataProvider;

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.fab)
    FloatingActionButton fab;
    @BindView(R.id.nav_view)
    NavigationView navigationView;
    @BindView(R.id.drawer_layout)
    DrawerLayout drawer;

    private Account thisAccount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        setSupportActionBar(toolbar);

        fab.setImageDrawable(new IconicsDrawable(this, GoogleMaterial.Icon.gmd_add).color(Color.WHITE).sizeDp(24));

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        navigationView.setNavigationItemSelectedListener(this);

        thisAccount = Parcels.unwrap(getIntent().getParcelableExtra(KEY_ACCOUNT));

        mDataProvider = new ProductDataProvider();

        setFragment(DefaultListFragment.class, DefaultListFragment.TAG);

        fab.hide();
    }

    @OnClick(R.id.fab)
    public void onFabClick(View view) {
        Fragment f = getSupportFragmentManager().findFragmentById(R.id.container);

        if (f instanceof MyListsFragment) {
//            Start creating a new list
            startActivity(new Intent(this, NewListContainerActivity.class).putExtra(KEY_ACCOUNT, Parcels.wrap(thisAccount)));
        } else if (f instanceof FriendsFragment) {
//            Start searching for friends
            startActivity(new Intent(this, SearchUserActivity.class).putExtra(KEY_ACCOUNT, Parcels.wrap(thisAccount)));
        } else {
            Snackbar.make(view, "Could not determine the current fragment", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show();
        }
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        switch (id) {
            case R.id.nav_drawer_home:
                setFragment(DefaultListFragmentTwo.class, null);
                fab.hide();
                break;
            case R.id.nav_drawer_lists:
                setFragment(MyListsFragment.class, null);
                fab.show();
                break;
            case R.id.nav_drawer_friends:
                setFragment(FriendsFragment.class, null);
                fab.show();
                break;
            case R.id.nav_drawer_settings:
//                We don't need a fab in settings
                setFragment(SettingsFragment.class, null);
                fab.hide();
                break;
            case R.id.nav_drawer_logout:
                // TODO: 20-11-16 execute logout
                break;
            default:
                System.out.println("Could not determine which drawer item was clicked");
        }

        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void setFragment(Class fragmentClass, @Nullable String tag) {
        try {
            Fragment fragment = (Fragment) fragmentClass.newInstance();

            Bundle bundle = new Bundle();
            bundle.putParcelable(KEY_ACCOUNT, Parcels.wrap(thisAccount));
            fragment.setArguments(bundle);

            FragmentManager fragmentManager = getSupportFragmentManager();
            if (tag != null) {
                fragmentManager.beginTransaction().replace(R.id.container, fragment, tag).commit();
            } else {
                fragmentManager.beginTransaction().replace(R.id.container, fragment).commit();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * This method will be called when a list item is removed
     *
     * @param position The position of the item within data set
     */
    public void onItemRemoved(int position) {
        Snackbar snackbar = Snackbar.make(
                findViewById(R.id.container),
                R.string.snack_bar_text_item_removed,
                Snackbar.LENGTH_LONG);

        snackbar.setAction(R.string.snack_bar_action_undo, v -> onItemUndoActionClicked());
        snackbar.setActionTextColor(ContextCompat.getColor(this, R.color.snackbar_action_color_done));
        snackbar.show();
    }

    /**
     * This method will be called when a list item is clicked
     *
     * @param position The position of the item within data set
     */
    public void onItemClicked(int position) {
        ProductDataProvider.ProductData data = getDataProvider().getItem(position);
        Product product = data.getItem();

        System.out.println(String.format("Clicked item %s", product.getName()));
    }

    private void onItemUndoActionClicked() {
        int position = getDataProvider().undoLastRemoval();
        if (position >= 0) {
            final Fragment fragment = getSupportFragmentManager().findFragmentByTag(DefaultListFragment.TAG);
            ((DefaultListFragment) fragment).notifyItemInserted(position);
        }
    }

    public ProductDataProvider getDataProvider() {
        return mDataProvider;
    }
}
