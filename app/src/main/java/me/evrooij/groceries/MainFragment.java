package me.evrooij.groceries;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;
import butterknife.BindView;
import butterknife.ButterKnife;
import me.evrooij.groceries.domain.GroceryList;
import me.evrooij.groceries.domain.Product;
import me.evrooij.groceries.domain.adapters.GroceryListAdapter;
import me.evrooij.groceries.domain.adapters.ProductAdapter;

import java.util.ArrayList;
import java.util.Random;


/**
 * A simple {@link Fragment} subclass.
 */
public class MainFragment extends Fragment {

    @BindView(R.id.lv_my_groceries)
    ListView listView;

    public MainFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main, container, false);
        ButterKnife.bind(this, view);

        // Construct the data source
        ArrayList<Product> data = new ArrayList<>();
        for (int i = 0; i < 15; i++) {
            data.add(new Product(String.format("Appels%s", i), i + 14 / 3, String.format("Henk%s", i), String.format("Comment %s", i)));
        }
        // Create the adapter to convert the array to views
        ProductAdapter adapter = new ProductAdapter(getActivity(), data);
        // Attach the adapter to a ListView
        listView.setAdapter(adapter);

        return view;
    }

}
