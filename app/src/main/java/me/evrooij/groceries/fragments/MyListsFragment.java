package me.evrooij.groceries.fragments;


import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import me.evrooij.groceries.MainActivity;
import me.evrooij.groceries.R;
import me.evrooij.groceries.adapters.GroceryListAdapter;
import me.evrooij.groceries.domain.Account;
import me.evrooij.groceries.domain.GroceryList;
import me.evrooij.groceries.domain.ListManager;
import org.parceler.Parcels;

import java.util.ArrayList;
import java.util.List;

import static android.R.attr.password;
import static android.R.attr.recognitionService;


/**
 * A simple {@link Fragment} subclass.
 */
public class MyListsFragment extends Fragment {

    @BindView(R.id.lv_my_lists)
    ListView listView;

    public MyListsFragment() {
        // Required empty public constructor
    }

    private Unbinder unbinder;
    private ArrayList<GroceryList> data;
    private GroceryListAdapter adapter;
    private ListManager listManager;

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

        listManager = new ListManager();

        addLists();

        return view;
    }

    private void addLists() {
        new Thread(() -> {
            MainActivity activity = (MainActivity) getActivity();
            System.out.println(String.format("Account: %s", activity.getAccount()));

            List<GroceryList> result = listManager.getMyLists(activity.getAccount());
            data = new ArrayList<>(result);
            adapter = new GroceryListAdapter(getActivity(), data);

            executeOnMainThread();
        }).start();
    }

    private void executeOnMainThread() {
        getActivity().runOnUiThread(() -> listView.setAdapter(adapter));
    }
}
