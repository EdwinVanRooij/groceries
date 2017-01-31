package me.evrooij.groceries.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.EditText;
import android.widget.ListView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnItemClick;
import me.evrooij.groceries.R;
import me.evrooij.groceries.network.ApiService;
import me.evrooij.groceries.network.ClientInterface;
import me.evrooij.groceries.ui.adapters.AccountAdapter;
import me.evrooij.groceries.models.Account;
import org.parceler.Parcels;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static me.evrooij.groceries.util.Config.KEY_ACCOUNT;
import static me.evrooij.groceries.util.Config.KEY_ACCOUNT_PROFILE;

public class SearchUserActivity extends AppCompatActivity {

    @BindView(R.id.lv_users)
    ListView listView;
    @BindView(R.id.etSearchQuery)
    EditText etSearchQuery;

    ArrayList<Account> data;
    AccountAdapter adapter;

    private Account thisAccount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_user);
        ButterKnife.bind(this);

        thisAccount = Parcels.unwrap(getIntent().getParcelableExtra(KEY_ACCOUNT));
    }

    @OnItemClick(R.id.lv_users)
    public void onItemClick(int position) {
        Intent intent = new Intent(this, ProfileActivity.class);
        intent.putExtra(KEY_ACCOUNT, Parcels.wrap(thisAccount));
        intent.putExtra(KEY_ACCOUNT_PROFILE, Parcels.wrap(listView.getAdapter().getItem(position)));
        startActivity(intent);
    }

    @OnClick(R.id.btnSearch)
    public void onBtnSearchClick() {
        new Thread(() -> {
            try {
                List<Account> result = ApiService.createService(ClientInterface.class).findFriends(thisAccount.getId(), etSearchQuery.getText().toString()).execute().body();

                refreshListView(result);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }).start();
    }

    private void refreshListView(List<Account> accounts) {
        // Construct the data source
        data = new ArrayList<>(accounts);
        // Create the adapter to convert the array to views
        adapter = new AccountAdapter(this, data);

        runOnUiThread(() ->
                // Attach the adapter to a ListView
                listView.setAdapter(adapter));
    }
}
