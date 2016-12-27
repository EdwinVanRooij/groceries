package me.evrooij.groceries;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnItemClick;
import me.evrooij.groceries.adapters.AccountAdapter;
import me.evrooij.groceries.adapters.ProductAdapter;
import me.evrooij.groceries.domain.Account;
import me.evrooij.groceries.domain.GroceryList;
import me.evrooij.groceries.domain.Product;
import me.evrooij.groceries.domain.UserManager;
import org.parceler.Parcels;

import java.util.ArrayList;
import java.util.List;

import static me.evrooij.groceries.Constants.KEY_ACCOUNT;

public class SearchUserActivity extends AppCompatActivity {

    @BindView(R.id.lv_users)
    ListView listView;
    @BindView(R.id.etSearchQuery)
    EditText etSearchQuery;

    private Account thisAccount;
    private UserManager userManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_user);
        ButterKnife.bind(this);

        thisAccount = Parcels.unwrap(getIntent().getParcelableExtra(KEY_ACCOUNT));
        userManager = new UserManager();
    }

    @OnItemClick(R.id.lv_users)
    public void onItemClick(int position) {
        startActivity(new Intent(this, ProfileActivity.class));
    }

    @OnClick(R.id.btnSearch)
    public void onBtnSearchClick() {
        System.out.println("clicked button search");
        new Thread(() -> {
            List<Account> result = userManager.findFriends(thisAccount.getId(), etSearchQuery.getText().toString());
            System.out.println(String.format("Found %s results", String.valueOf(result.size())));

            refreshListView(result);
        }).start();
    }

    private void refreshListView(List<Account> accounts) {
        // Construct the data source
        ArrayList<Account> data = new ArrayList<>(accounts);
        // Create the adapter to convert the array to views
        AccountAdapter adapter = new AccountAdapter(this, data);

        runOnUiThread(() ->
                // Attach the adapter to a ListView
                listView.setAdapter(adapter));
    }
}
