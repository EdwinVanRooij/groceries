package me.evrooij.groceries.fragments;

import android.content.Intent;
import android.os.Bundle;
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
import me.evrooij.groceries.adapters.GroceryListAdapter;
import me.evrooij.groceries.data.Account;
import me.evrooij.groceries.data.AccountPrefs;
import me.evrooij.groceries.data.GroceryList;
import me.evrooij.groceries.data.ListManager;
import org.parceler.Parcels;

import java.util.ArrayList;
import java.util.List;

import static me.evrooij.groceries.Config.KEY_ACCOUNT;
import static me.evrooij.groceries.Config.KEY_ACCOUNT_PROFILE;


/**
 * A simple {@link Fragment} subclass.
 */
public class MyListsFragment extends Fragment {

    @BindView(R.id.lv_my_lists)
    ListView listView;

    private Account thisAccount;
    private Unbinder unbinder;
    private ArrayList<GroceryList> data;
    private GroceryListAdapter adapter;
    private ListManager listManager;

    public MyListsFragment() {
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
        View view = inflater.inflate(R.layout.fragment_my_lists, container, false);
        unbinder = ButterKnife.bind(this, view);
        ((MainActivity) getActivity()).setActionBarTitle(getString(R.string.title_lists));

        listManager = new ListManager(getActivity().getApplicationContext());

        thisAccount = Parcels.unwrap(getArguments().getParcelable(KEY_ACCOUNT));

        return view;
    }

    @OnItemClick(R.id.lv_my_lists)
    public void onItemClick(int position) {
//        GroceryListPrefs listPrefs = GroceryListPrefs.get(getActivity());
//        //noinspection ConstantConditions
//        accountPrefs.edit().putId(a.getId()).putUsername(a.getUsername()).putEmail(a.getEmail()).putPassword(a.getPassword()).apply();


        Intent intent = new Intent(getActivity(), ProfileActivity.class);
        intent.putExtra(KEY_ACCOUNT, Parcels.wrap(thisAccount));
        intent.putExtra(KEY_ACCOUNT_PROFILE, Parcels.wrap(listView.getAdapter().getItem(position)));
        startActivity(intent);
    }

    @Override
    public void onResume() {
        new Thread(() -> {
            new Thread(() -> {
                List<GroceryList> result = listManager.getMyLists(thisAccount);
                data = new ArrayList<>(result);
                adapter = new GroceryListAdapter(getActivity(), data);

                getActivity().runOnUiThread(() -> listView.setAdapter(adapter));
            }).start();
        }).start();
        super.onResume();
    }
}
