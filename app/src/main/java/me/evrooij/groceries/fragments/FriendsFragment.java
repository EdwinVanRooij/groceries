package me.evrooij.groceries.fragments;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnItemClick;
import butterknife.Unbinder;
import me.evrooij.groceries.MainActivity;
import me.evrooij.groceries.ProfileActivity;
import me.evrooij.groceries.R;
import me.evrooij.groceries.adapters.AccountAdapter;
import me.evrooij.groceries.data.Account;
import me.evrooij.groceries.data.UserManager;
import org.parceler.Parcels;

import java.util.ArrayList;
import java.util.List;

import static me.evrooij.groceries.Config.KEY_ACCOUNT;
import static me.evrooij.groceries.Config.KEY_ACCOUNT_PROFILE;


/**
 * A simple {@link Fragment} subclass.
 */
public class FriendsFragment extends MainFragment {

    @BindView(R.id.lv_users)
    ListView listView;

    private UserManager userManager;

    private ArrayList<Account> data;
    private AccountAdapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_friends, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mainActivity.setActionBarTitle(getString(R.string.title_friends));
        userManager = new UserManager(getContext());
    }

    @Override
    public void onResume() {
        super.onResume();

        mainActivity.executeRunnable(() -> {
            List<Account> result = userManager.getFriends(mainActivity.getThisAccount().getId());

            refreshListView(result);
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
        adapter = new AccountAdapter(getContext(), data);

        mainActivity.runOnUiThread(() -> {
            // Attach the adapter to a ListView
            listView.setAdapter(adapter);
        });
    }
}
