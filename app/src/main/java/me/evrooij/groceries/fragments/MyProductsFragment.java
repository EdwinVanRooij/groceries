package me.evrooij.groceries.fragments;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.ListView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import com.orhanobut.hawk.Hawk;
import me.evrooij.groceries.MainActivity;
import me.evrooij.groceries.R;
import me.evrooij.groceries.adapters.AccountAdapter;
import me.evrooij.groceries.adapters.GroceryListAdapter;
import me.evrooij.groceries.adapters.ProductAdapter;
import me.evrooij.groceries.data.*;
import org.parceler.Parcels;

import java.util.ArrayList;
import java.util.List;

import static me.evrooij.groceries.Config.KEY_ACCOUNT;

/**
 * A simple {@link Fragment} subclass.
 */
public class MyProductsFragment extends Fragment {

    @BindView(R.id.lv_my_products)
    ListView listView;

    private Account thisAccount;
    private Unbinder unbinder;

    private ArrayList<Product> data;
    private ProductAdapter adapter;
    private ProductManager productManager;

    public MyProductsFragment() {
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
        View view = inflater.inflate(R.layout.fragment_my_products, container, false);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ((MainActivity) getActivity()).setActionBarTitle(getString(R.string.title_products));
        productManager = new ProductManager(getContext());
        thisAccount = Parcels.unwrap(getArguments().getParcelable(KEY_ACCOUNT));
    }

    @Override
    public void onResume() {
//        new Thread(() -> {
//            List<Product> result = productManager.getMyProducts(thisAccount.getId());
//
//            refreshListView(result);
//        }).start();
        super.onResume();
    }

    private void refreshListView(List<Product> products) {
        // Construct the data source
        data = new ArrayList<>(products);
        // Create the adapter to convert the array to views
        adapter = new ProductAdapter(getActivity(), data);

        getActivity().runOnUiThread(() ->
                // Attach the adapter to a ListView
                listView.setAdapter(adapter));
    }
}
