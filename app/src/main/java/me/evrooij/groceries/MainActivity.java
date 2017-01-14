package me.evrooij.groceries;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import com.mikepenz.google_material_typeface_library.GoogleMaterial;
import com.mikepenz.iconics.IconicsDrawable;
import com.orhanobut.hawk.Hawk;
import me.evrooij.groceries.data.Account;
import me.evrooij.groceries.fragments.*;
import me.evrooij.groceries.login.LauncherActivity;
import me.evrooij.groceries.util.Preferences;
import org.parceler.Parcels;

import static android.R.attr.id;
import static me.evrooij.groceries.Config.KEY_ACCOUNT;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.fab)
    FloatingActionButton fab;
    @BindView(R.id.nav_view)
    NavigationView navigationView;
    @BindView(R.id.drawer_layout)
    DrawerLayout drawer;

    TextView tvName;
    TextView tvEmail;

    private Account thisAccount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        setSupportActionBar(toolbar);

        fab.setImageDrawable(new IconicsDrawable(this, GoogleMaterial.Icon.gmd_add).color(Color.WHITE).sizeDp(24));

        initializeNavigationView();

        initAccount();

        View header = navigationView.getHeaderView(0);
        tvName = (TextView) header.findViewById(R.id.nav_header_name);
        tvEmail = (TextView) header.findViewById(R.id.nav_header_email);
        tvName.setText(thisAccount.getUsername());
        tvEmail.setText(thisAccount.getEmail());

        header.setOnClickListener(v -> {
            drawer.closeDrawer(GravityCompat.START);
            setFragment(MyProfileFragment.class, true);
            fab.hide();
        });

        setDefaultListFragment();
    }

    public void setDefaultListFragment() {
        setFragment(DefaultListFragment.class, false);
        navigationView.setCheckedItem(R.id.nav_drawer_home);
        fab.hide();
    }

    private void initAccount() {
        // Attempt to get account from launcher activity
        thisAccount = Parcels.unwrap(getIntent().getParcelableExtra(KEY_ACCOUNT));
        if (thisAccount == null) {
            // If activity was not created from launcher activity, get it from shared pref
            if (Preferences.accountExists(this)) {
                // Account exists, set account
                thisAccount = Preferences.getAccount(this);
            } else {
                // Account does not exist, redirect to login
                Toast.makeText(this, getString(R.string.account_not_found), Toast.LENGTH_SHORT).show();
                logOut();
            }
        }
    }

    private void initializeNavigationView() {
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        navigationView.setNavigationItemSelectedListener(this);

        Menu menu = navigationView.getMenu();

        // First group
        menu.findItem(R.id.nav_drawer_home).setIcon(new IconicsDrawable(this, GoogleMaterial.Icon.gmd_home));

        // Communication
        menu.findItem(R.id.nav_drawer_lists).setIcon(new IconicsDrawable(this, GoogleMaterial.Icon.gmd_list));
        menu.findItem(R.id.nav_drawer_friends).setIcon(new IconicsDrawable(this, GoogleMaterial.Icon.gmd_supervisor_account));
        menu.findItem(R.id.nav_drawer_suggestion).setIcon(new IconicsDrawable(this, GoogleMaterial.Icon.gmd_lightbulb_outline));
        menu.findItem(R.id.nav_drawer_bug).setIcon(new IconicsDrawable(this, GoogleMaterial.Icon.gmd_bug_report));

        // Last group
        menu.findItem(R.id.nav_drawer_settings).setIcon(new IconicsDrawable(this, GoogleMaterial.Icon.gmd_settings));
        menu.findItem(R.id.nav_drawer_logout).setIcon(new IconicsDrawable(this, GoogleMaterial.Icon.gmd_exit_to_app));
    }

    public void setActionBarTitle(String title) {
        runOnUiThread(() -> toolbar.setTitle(title));
    }

    @OnClick(R.id.fab)
    public void onFabClick(View view) {
        Fragment f = getSupportFragmentManager().findFragmentById(R.id.flContent);

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
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        switch (id) {
            case R.id.nav_drawer_home:
                setDefaultListFragment();
                break;
            case R.id.nav_drawer_lists:
                setFragment(MyListsFragment.class, false);
                fab.show();
                break;
            case R.id.nav_drawer_friends:
                setFragment(FriendsFragment.class, false);
                fab.show();
                break;
            case R.id.nav_drawer_suggestion:
                setFragment(SuggestionFragment.class, false);
                fab.hide();
                break;
            case R.id.nav_drawer_bug:
                setFragment(BugFragment.class, false);
                fab.hide();
                break;
            case R.id.nav_drawer_settings:
//                We don't need a fab in settings
                setFragment(SettingsFragment.class, false);
                fab.hide();
                break;
            case R.id.nav_drawer_logout:
                logOut();
                break;
            default:
                Toast.makeText(this, "Could not determine which drawer item was clicked", Toast.LENGTH_SHORT).show();
        }

        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void logOut() {
        Preferences.removeAccount();

        Intent i = new Intent(this, LauncherActivity.class);
        i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(i);
    }

    public void setFragment(Class fragmentClass, boolean addToStack) {
        try {
            Fragment fragment = (Fragment) fragmentClass.newInstance();
            Bundle bundle = new Bundle();
            bundle.putParcelable(KEY_ACCOUNT, Parcels.wrap(thisAccount));
            fragment.setArguments(bundle);

            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction ft = fragmentManager.beginTransaction();
            // Add this transaction to the back stack
            if (addToStack) {
                ft.addToBackStack(null);
            }
            ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
            ft.replace(R.id.flContent, fragment).commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
