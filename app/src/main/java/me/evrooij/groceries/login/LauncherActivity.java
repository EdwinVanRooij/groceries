package me.evrooij.groceries.login;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import me.evrooij.groceries.MainActivity;

public class LauncherActivity extends AppCompatActivity {

    boolean isLoggedIn = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO: 26-11-16 go to activity based on login status, logged in --> main activity, not logged in --> logincontainer
        if (isLoggedIn) {
            startActivity(
                    new Intent(this, MainActivity.class)
                            .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK));
        } else {
            startActivity(
                    new Intent(this, LoginContainerActivity.class)
                            .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK));
        }
        finish();
        super.onCreate(savedInstanceState);
    }
}
