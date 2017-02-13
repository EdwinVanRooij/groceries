package me.evrooij.groceries.ui.fragments;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import butterknife.BindView;
import butterknife.OnItemClick;
import me.evrooij.groceries.R;
import me.evrooij.groceries.domain.Account;
import me.evrooij.groceries.network.ApiService;
import me.evrooij.groceries.network.ClientInterface;
import me.evrooij.groceries.ui.ProfileActivity;
import me.evrooij.groceries.ui.adapters.AccountAdapterJava;
import me.evrooij.groceries.util.Extensions;
import org.parceler.Parcels;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static me.evrooij.groceries.util.Config.KEY_ACCOUNT;
import static me.evrooij.groceries.util.Config.KEY_ACCOUNT_PROFILE;


/**
 * A simple {@link Fragment} subclass.
 */
public class FriendsFragment extends MainFragment {

    @BindView(R.id.lv_users)
    ListView listView;

    private ArrayList<Account> data;
    private AccountAdapterJava adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return Extensions.inflate(container, R.layout.fragment_friends);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mainActivity.setActionBarTitle(getString(R.string.title_friends));
    }

    @Override
    public void onResume() {
        super.onResume();

        mainActivity.executeRunnable(() -> {
            try {
                List<Account> result = ApiService.createService(ClientInterface.class).getFriends(mainActivity.getThisAccount().getId()).execute().body();

                refreshListView(result);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

    @OnItemClick(R.id.lv_users)
    public void onItemClick(int position) {
        Intent intent = new Intent(getContext(), ProfileActivity.class);
        intent.putExtra(KEY_ACCOUNT, Parcels.wrap(mainActivity.getThisAccount()));
        intent.putExtra(KEY_ACCOUNT_PROFILE, Parcels.wrap(listView.getAdapter().getItem(position)));
        startActivity(intent);
    }

    private void refreshListView(List<Account> accounts) {
        // Construct the data source
        data = new ArrayList<>(accounts);
        // Create the adapter to convert the array to views
        adapter = new AccountAdapterJava(getContext(), data);

        mainActivity.runOnUiThread(() -> {
            // Attach the adapter to a ListView
            listView.setAdapter(adapter);
        });
    }
}
