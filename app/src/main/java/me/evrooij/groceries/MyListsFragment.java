package me.evrooij.groceries;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import com.google.android.gms.plus.model.people.Person;
import me.evrooij.groceries.androiddomain.GroceryListAdapter;
import me.evrooij.groceries.domain.GroceryList;

import java.security.acl.Group;
import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class MyListsFragment extends Fragment {

    private List<GroceryList> myLists;
    private ListView listView;
    private GroceryListAdapter adapter;

    public MyListsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        listView = (ListView) container.findViewById(R.id.lv_my_lists);
        myLists = new ArrayList<>();
        addDummyData();
        adapter = new GroceryListAdapter(youractivity.this, 0, myLists);
        listView.setAdapter(adapter);

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_my_lists, container, false);
    }

    private void addDummyData() {

        myLists.add(new GroceryList("LijstNaam1"));
        myLists.add(new GroceryList("LijstNaam2"));
    }

}
