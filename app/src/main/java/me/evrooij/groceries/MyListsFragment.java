package me.evrooij.groceries;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import butterknife.BindView;
import butterknife.ButterKnife;
import me.evrooij.groceries.domain.adapters.GroceryListAdapter;
import me.evrooij.groceries.domain.GroceryList;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 */
public class MyListsFragment extends Fragment {

    @BindView(R.id.lv_my_lists) ListView listView;


    public MyListsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_my_lists, container, false);
        ButterKnife.bind(this, view);

        // Construct the data source
        ArrayList<GroceryList> data = new ArrayList<>();
        data.add(new GroceryList("ListOne", "Owner"));
        data.add(new GroceryList("ListTwo", "Owner2"));
        data.add(new GroceryList("ListThree", "Owner3"));
        // Create the adapter to convert the array to views
        GroceryListAdapter adapter = new GroceryListAdapter(getActivity(), data);
        // Attach the adapter to a ListView
        listView.setAdapter(adapter);

        return view;
    }
}
