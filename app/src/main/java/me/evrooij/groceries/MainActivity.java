package me.evrooij.groceries;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.provider.MediaStore;
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
import android.util.Log;
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
import me.evrooij.groceries.data.Account;
import me.evrooij.groceries.data.Product;
import me.evrooij.groceries.fragments.*;
import me.evrooij.groceries.interfaces.ContainerActivity;
import me.evrooij.groceries.login.LauncherActivity;
import me.evrooij.groceries.rest.FileUploadInterface;
import me.evrooij.groceries.rest.ServiceGenerator;
import me.evrooij.groceries.util.Preferences;
import net.gotev.uploadservice.UploadService;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import org.parceler.Parcels;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static me.evrooij.groceries.Config.*;
import static me.evrooij.groceries.fragments.DefaultListFragment.EDIT_PRODUCT_CODE;
import static me.evrooij.groceries.fragments.DefaultListFragment.NEW_PRODUCT_CODE;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, ContainerActivity {

    public static final int REQUEST_IMAGE_CAPTURE = 100;

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.fab)
    FloatingActionButton fab;
    @BindView(R.id.nav_view)
    NavigationView navigationView;
    @BindView(R.id.drawer_layout)
    DrawerLayout drawer;

    private Account thisAccount;
    private ExecutorService threadPool;

    private static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initAccount();
        Thread.setDefaultUncaughtExceptionHandler(new ExceptionHandler(this, new Account(thisAccount.getId(), thisAccount.getUsername(), thisAccount.getEmail())));
        UploadService.NAMESPACE = BuildConfig.APPLICATION_ID;

        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);

        initNavigationDrawer();

        threadPool = Executors.newFixedThreadPool(THREADPOOL_MAINACTIVITY_SIZE);

        fab.setImageDrawable(new IconicsDrawable(this, GoogleMaterial.Icon.gmd_add).color(Color.WHITE).sizeDp(24));
        setDefaultListFragment();
    }

    public void setDefaultListFragment() {
        setFragment(DefaultListFragment.class, false);
        navigationView.setCheckedItem(R.id.nav_drawer_home);
        fab.show();
    }

    /**
     * Initializes thisAccount
     */
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

    /**
     * Initializes the navigation drawer, including variable usage to set account name and email in the header
     */
    private void initNavigationDrawer() {
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        navigationView.setNavigationItemSelectedListener(this);

        Menu menu = navigationView.getMenu();

        // First group
        menu.findItem(R.id.nav_drawer_home).setIcon(new IconicsDrawable(this, GoogleMaterial.Icon.gmd_home));

        // Communication
        menu.findItem(R.id.nav_drawer_lists).setIcon(new IconicsDrawable(this, GoogleMaterial.Icon.gmd_list));
        menu.findItem(R.id.nav_drawer_products).setIcon(new IconicsDrawable(this, GoogleMaterial.Icon.gmd_content_paste));
        menu.findItem(R.id.nav_drawer_friends).setIcon(new IconicsDrawable(this, GoogleMaterial.Icon.gmd_supervisor_account));
        menu.findItem(R.id.nav_drawer_suggestion).setIcon(new IconicsDrawable(this, GoogleMaterial.Icon.gmd_lightbulb_outline));
        menu.findItem(R.id.nav_drawer_bug).setIcon(new IconicsDrawable(this, GoogleMaterial.Icon.gmd_bug_report));

        // Last group
        menu.findItem(R.id.nav_drawer_settings).setIcon(new IconicsDrawable(this, GoogleMaterial.Icon.gmd_settings));
        menu.findItem(R.id.nav_drawer_logout).setIcon(new IconicsDrawable(this, GoogleMaterial.Icon.gmd_exit_to_app));

        View header = navigationView.getHeaderView(0);

        TextView tvName = (TextView) header.findViewById(R.id.nav_header_name);
        TextView tvEmail = (TextView) header.findViewById(R.id.nav_header_email);
        tvName.setText(thisAccount.getUsername());
        tvEmail.setText(thisAccount.getEmail());

        header.setOnClickListener(v -> {
            drawer.closeDrawer(GravityCompat.START);
            setFragment(MyProfileFragment.class, true);
            fab.hide();
        });
    }


    @OnClick(R.id.fab)
    public void onFabClick(View view) {
        Fragment f = getSupportFragmentManager().findFragmentById(R.id.flContent);

        if (f instanceof MyListsFragment) {
            // Start creating a new list
            startActivity(new Intent(this, NewListContainerActivity.class).putExtra(KEY_ACCOUNT, Parcels.wrap(thisAccount)));
        } else if (f instanceof DefaultListFragment) {
            // Start creating a new product for a list
            Intent i = new Intent(this, NewProductActivity.class).putExtra(KEY_ACCOUNT, Parcels.wrap(getThisAccount()));
            startActivityForResult(i, NEW_PRODUCT_CODE);
        } else if (f instanceof MyProductsFragment) {
            // Start creating a new custom product
            Intent i = new Intent(this, NewProductActivity.class).putExtra(KEY_ACCOUNT, Parcels.wrap(getThisAccount()));
            startActivityForResult(i, NEW_PRODUCT_CODE);
        } else if (f instanceof FriendsFragment) {
            // Start searching for friends
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
            case R.id.nav_drawer_products:
                setFragment(MyProductsFragment.class, false);
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
//        Preferences.removeAccount();
        Preferences.removeAll(this);

        Intent i = new Intent(this, LauncherActivity.class);
        i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(i);
    }

    @Override
    public Account getThisAccount() {
        return thisAccount;
    }

    @Override
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
            Toast.makeText(this, getString(R.string.set_fragment_exception), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void executeRunnable(Runnable runnable) {
        threadPool.execute(runnable);
    }

    @Override
    public void setActionBarTitle(String title) {
        runOnUiThread(() -> toolbar.setTitle(title));
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.flContent);

        // Check what fragment was started an activity for
        if (fragment instanceof DefaultListFragment) {

            // Get specific fragment for calling methods
            DefaultListFragment defaultListFragment = (DefaultListFragment) fragment;

            if (resultCode == Activity.RESULT_OK) {
                switch (requestCode) {
                    case NEW_PRODUCT_CODE:
                        defaultListFragment.createNewProduct(Parcels.unwrap(data.getParcelableExtra(KEY_NEW_PRODUCT)));
                        break;
                    case EDIT_PRODUCT_CODE:
                        Product editProduct = Parcels.unwrap(data.getParcelableExtra(KEY_EDIT_PRODUCT));
                        // Update change to backend
                        defaultListFragment.editProduct(editProduct);
                        break;
                    default:
                        Toast.makeText(this, "onActivityResult: Could not find result code", Toast.LENGTH_SHORT).show();
                        Log.d(TAG, "onActivityResult: Could not find result code");
                        break;
                }
            }
        } else if (fragment instanceof MyProductsFragment) {
            // Get specific fragment for calling methods
            MyProductsFragment myProductsFragment = (MyProductsFragment) fragment;

            if (resultCode == Activity.RESULT_OK) {
                switch (requestCode) {
                    case NEW_PRODUCT_CODE:
                        myProductsFragment.createNewProduct(Parcels.unwrap(data.getParcelableExtra(KEY_NEW_PRODUCT)));
                        break;
                    case REQUEST_IMAGE_CAPTURE:
                        Bitmap imageBitmap = (Bitmap) data.getExtras().get("data");
                        uploadImage(getThisAccount().getId(), 234, imageBitmap);
                        break;
                    default:
                        Toast.makeText(this, "onActivityResult: Could not find result code", Toast.LENGTH_SHORT).show();
                        Log.d(TAG, "onActivityResult: Could not find result code");
                        break;
                }
            }
        }
    }

    public void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
    }

    public void uploadImage(int accountId, int productId, Bitmap imageBitmap) {
        try {
// create upload service client
            FileUploadInterface service =
                    ServiceGenerator.createService(getApplicationContext(), FileUploadInterface.class);

            File f = null;
            //create a file to write bitmap data
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            imageBitmap.compress(Bitmap.CompressFormat.PNG, 0 /*ignored for PNG*/, bos);
            byte[] bitmapdata = bos.toByteArray();

            FileOutputStream fos = new FileOutputStream(f);
            fos.write(bitmapdata);
            fos.flush();
            fos.close();

            // create RequestBody instance from file
            RequestBody requestFile = RequestBody.create(MediaType.parse("image/jpg"), f);

            // MultipartBody.Part is used to send also the actual file name
            MultipartBody.Part body =
                    MultipartBody.Part.createFormData("picture", "file", requestFile);

            // add another part within the multipart request
            String descriptionString = "hello, this is description speaking";
            RequestBody description =
                    RequestBody.create(
                            okhttp3.MultipartBody.FORM, descriptionString);

            // finally, execute the request
            Call<ResponseBody> call = service.upload(accountId, productId, description, body);
            call.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call,
                                       Response<ResponseBody> response) {
                    Log.v("Upload", "success");
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    Log.e("Upload error:", t.getMessage());
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
