package me.evrooij.groceries;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ListView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnItemClick;
import me.evrooij.groceries.ProfileActivity;
import me.evrooij.groceries.R;
import me.evrooij.groceries.adapters.AccountAdapter;
import me.evrooij.groceries.domain.Account;

import java.util.ArrayList;

public class SearchUserActivity extends AppCompatActivity {

    @BindView(R.id.lv_users)
    ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_user);
        ButterKnife.bind(this);

        // Construct the data source
        ArrayList<Account> data = new ArrayList<>();
        for (int i = 0; i < 15; i++) {
            data.add(new Account(String.format("Accountname%s", i), String.format("Name%s", i), String.format("Surname%s", i)));
        }
        // Create the adapter to convert the array to views
        AccountAdapter adapter = new AccountAdapter(this, data);
        // Attach the adapter to a ListView
        listView.setAdapter(adapter);
    }

    @OnItemClick(R.id.lv_users)
    public void onItemClick(int position) {
        startActivity(new Intent(this, ProfileActivity.class));
    }

}
