package me.evrooij.groceries.ui.fragments;


import android.os.Bundle;
import android.os.UserManager;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.*;
import android.widget.ListView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnItemClick;
import butterknife.Unbinder;
import me.evrooij.groceries.network.ApiService;
import me.evrooij.groceries.network.ClientInterface;
import me.evrooij.groceries.ui.NewListContainerActivity;
import me.evrooij.groceries.R;
import me.evrooij.groceries.ui.adapters.AccountAdapter;
import me.evrooij.groceries.models.Account;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class SelectFriendsFragment extends Fragment {

    @BindView(R.id.lv_users)
    ListView listView;

    private Unbinder unbinder;

    private ArrayList<Account> data;
    private AccountAdapter adapter;

    private NewListContainerActivity containerActivity;

    @Override
    public void onDestroyView() {
        unbinder.unbind();
        super.onDestroyView();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_select_friends, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        unbinder = ButterKnife.bind(this, view);
        containerActivity = (NewListContainerActivity) getActivity();


        containerActivity.executeRunnable(() -> {
            try {
                List<Account> result = ApiService.createService(ClientInterface.class).getFriends(containerActivity.getThisAccount().getId()).execute().body();

                refreshListView(result);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        super.onViewCreated(view, savedInstanceState);
    }

    private void refreshListView(List<Account> accounts) {
        // Construct the data source
        data = new ArrayList<>(accounts);
        // Create the adapter to convert the array to views
        adapter = new AccountAdapter(getContext(), data);

        containerActivity.runOnUiThread(() -> {
            // Attach the adapter to a ListView
            listView.setAdapter(adapter);
        });
    }

    @OnItemClick(R.id.lv_users)
    public void onItemClick(int position) {
        containerActivity.addToSelection((Account) listView.getAdapter().getItem(position));
    }
}
