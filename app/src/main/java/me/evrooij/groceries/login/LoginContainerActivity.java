package me.evrooij.groceries.login;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import butterknife.ButterKnife;
import me.evrooij.groceries.R;

public class LoginContainerActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_container);
        ButterKnife.bind(this);

        setFragment(MainLoginFragment.class);
    }

    private void setFragment(Class fragmentClass) {
        try {
            FragmentManager fragmentManager = getSupportFragmentManager();
            Fragment fragment = (Fragment) fragmentClass.newInstance();
            FragmentTransaction transaction = fragmentManager.beginTransaction();
            transaction.replace(R.id.flContent, fragment);
            transaction.commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
