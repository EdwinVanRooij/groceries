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
import com.orhanobut.hawk.Hawk;
import me.evrooij.groceries.Config;
import me.evrooij.groceries.MainActivity;
import me.evrooij.groceries.ProfileActivity;
import me.evrooij.groceries.R;
import me.evrooij.groceries.adapters.GroceryListAdapter;
import me.evrooij.groceries.data.Account;
import me.evrooij.groceries.data.GroceryList;
import me.evrooij.groceries.data.ListManager;
import me.evrooij.groceries.util.Preferences;
import org.parceler.Parcels;

import java.util.ArrayList;
import java.util.List;

import static android.icu.lang.UCharacter.GraphemeClusterBreak.T;
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
        unbinder.unbind();
        super.onDestroyView();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_my_lists, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        unbinder = ButterKnife.bind(this, view);
        ((MainActivity) getActivity()).setActionBarTitle(getString(R.string.title_lists));
        listManager = new ListManager(getContext());
        thisAccount = Parcels.unwrap(getArguments().getParcelable(KEY_ACCOUNT));

        Hawk.init(getContext()).build();
    }

    @OnItemClick(R.id.lv_my_lists)
    public void onItemClick(int position) {
        GroceryList groceryList = (GroceryList) listView.getAdapter().getItem(position);
        Preferences.saveGroceryListId(getContext(), groceryList.getId());

        MainActivity mainActivity = (MainActivity) getActivity();
        mainActivity.setDefaultListFragment();
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
