package me.evrooij.groceries.login;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import me.evrooij.groceries.MainActivity;
import me.evrooij.groceries.data.Account;
import me.evrooij.groceries.data.AccountPrefs;
import org.parceler.Parcels;

import static me.evrooij.groceries.Config.KEY_ACCOUNT;

public class LauncherActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        AccountPrefs accountPrefs = AccountPrefs.get(this);

        if (accountPrefs.getId() != null) {
            Intent i = new Intent(this, MainActivity.class);
            Account a = new Account(accountPrefs.getId(), accountPrefs.getUsername(), accountPrefs.getEmail(), accountPrefs.getPassword());
            i.putExtra(KEY_ACCOUNT, Parcels.wrap(a));
            i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(i);
        } else {
            Intent i = new Intent(this, LoginContainerActivity.class);
            i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(i);
        }
        finish();
        super.onCreate(savedInstanceState);
    }


}
