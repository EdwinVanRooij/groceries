package me.evrooij.groceries;

import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import com.mikepenz.google_material_typeface_library.GoogleMaterial;
import com.mikepenz.iconics.IconicsDrawable;
import me.evrooij.groceries.fragments.CompleteListFragment;
import me.evrooij.groceries.fragments.SelectFriendsFragment;

public class NewListContainerActivity extends AppCompatActivity {
    @BindView(R.id.fab)
    FloatingActionButton fab;
    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_container_new_list);
        ButterKnife.bind(this);

        setSupportActionBar(toolbar);

        fab.setImageDrawable(new IconicsDrawable(this, GoogleMaterial.Icon.gmd_arrow_forward).color(Color.WHITE).sizeDp(24));

        setFragment(SelectFriendsFragment.class, false, true);
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
            setFragment(CompleteListFragment.class, true, false);
            fab.setImageDrawable(new IconicsDrawable(this, GoogleMaterial.Icon.gmd_done).color(Color.WHITE).sizeDp(24));
        } else if (f instanceof CompleteListFragment) {
            finish();
        } else {
            Snackbar.make(view, "Could not determine the current fragment", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show();
        }
    }

    private void setFragment(Class fragmentClass, boolean animated, boolean isFirst) {
        try {
            FragmentManager fragmentManager = getSupportFragmentManager();
            Fragment fragment = (Fragment) fragmentClass.newInstance();
            FragmentTransaction transaction = fragmentManager.beginTransaction();

            if (animated) {
                transaction.setCustomAnimations(R.anim.slide_in_left, R.anim.slide_out_left, R.anim.slide_in_right, R.anim.slide_out_right);
            }
            transaction.replace(R.id.flContent, fragment);
            if (!isFirst) {
                transaction.addToBackStack(null);
            }
            transaction.commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
