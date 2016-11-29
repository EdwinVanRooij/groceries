package me.evrooij.groceries.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import me.evrooij.groceries.R;
import me.evrooij.groceries.domain.Product;
import me.evrooij.groceries.adapters.ProductAdapter;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 */
public class MainFragment extends Fragment {

    @BindView(R.id.lv_my_groceries)
    ListView listView;

    public MainFragment() {
        // Required empty public constructor
    }

    private Unbinder unbinder;

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main, container, false);
        unbinder = ButterKnife.bind(this, view);

        // Construct the data source
        ArrayList<Product> data = new ArrayList<>();
        for (int i = 0; i < 15; i++) {
            data.add(new Product(id, String.format("Appels%s", i), ((i * 3 ) % 5) +1, String.format("Comment %s", i), String.format("Hankinson %s", i)));
        }
        // Create the adapter to convert the array to views
        ProductAdapter adapter = new ProductAdapter(getActivity(), data);
        // Attach the adapter to a ListView
        listView.setAdapter(adapter);

        return view;
    }

}
