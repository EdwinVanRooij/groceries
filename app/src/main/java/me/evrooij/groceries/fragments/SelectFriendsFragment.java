package me.evrooij.groceries.fragments;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import me.evrooij.groceries.MultiselectSampleActivity;
import me.evrooij.groceries.NewListContainerActivity;
import me.evrooij.groceries.R;
import me.evrooij.groceries.adapters.AccountAdapter;
import me.evrooij.groceries.domain.Account;
import me.evrooij.groceries.domain.UserManager;
import org.parceler.Parcels;

import java.util.ArrayList;
import java.util.List;

import static me.evrooij.groceries.Constants.KEY_ACCOUNT;
import static me.evrooij.groceries.Constants.KEY_USER;


/**
 * A simple {@link Fragment} subclass.
 */
public class SelectFriendsFragment extends Fragment {

//    @BindView(R.id.lv_users)
//    ListView listView;
    private Unbinder unbinder;

    private Account thisAccount;
    private UserManager userManager;

    ArrayList<Account> data;
    AccountAdapter adapter;

    public SelectFriendsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_select_friends, container, false);
        unbinder = ButterKnife.bind(this, view);

        fillListView();

        thisAccount = Parcels.unwrap(getArguments().getParcelable(KEY_USER));
        userManager = new UserManager();


        return view;
        // Inflate the layout for this fragment
    }

    private void fillListView() {
        new Thread(() -> {
            List<Account> result = userManager.getFriends(thisAccount.getId());
            System.out.println(String.format("Found %s friends of %s", String.valueOf(result.size()), thisAccount.getUsername()));

            refreshListView(result);
        }).start();
    }

    private void refreshListView(List<Account> accounts) {
        // Construct the data source
        data = new ArrayList<>(accounts);
        // Create the adapter to convert the array to views
        adapter = new AccountAdapter(getActivity(), data);

//        getActivity().runOnUiThread(() ->
//                // Attach the adapter to a ListView
//                listView.setAdapter(adapter));
    }
}
