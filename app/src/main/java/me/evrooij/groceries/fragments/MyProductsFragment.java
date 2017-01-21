package me.evrooij.groceries.fragments;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.ListView;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import com.orhanobut.hawk.Hawk;
import me.evrooij.groceries.MainActivity;
import me.evrooij.groceries.R;
import me.evrooij.groceries.adapters.AccountAdapter;
import me.evrooij.groceries.adapters.GroceryListAdapter;
import me.evrooij.groceries.adapters.MyProductAdapter;
import me.evrooij.groceries.adapters.ProductAdapter;
import me.evrooij.groceries.data.*;
import org.parceler.Parcels;

import java.util.ArrayList;
import java.util.List;

import static me.evrooij.groceries.Config.KEY_ACCOUNT;

/**
 * A simple {@link Fragment} subclass.
 */
public class MyProductsFragment extends MainFragment {

    @BindView(R.id.lv_my_products)
    ListView listView;
    @BindView(R.id.tvTipDescription)
    TextView tvTipDescription;

    private ArrayList<Product> data;
    private MyProductAdapter adapter;
    private ProductManager productManager;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_my_products, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mainActivity.setActionBarTitle(getString(R.string.title_products));
        productManager = new ProductManager(getContext());

        mainActivity.executeRunnable(() -> {
            List<Product> result = productManager.getMyProducts(mainActivity.getThisAccount().getId());

            refreshListView(result);
            getActivity().runOnUiThread(() -> {
                tvTipDescription.setText(getString(R.string.my_products_tip_description, data.size()));
            });
        });
    }

    private void refreshListView(List<Product> products) {
        // Construct the data source
        data = new ArrayList<>(products);
        // Create the adapter to convert the array to views
        adapter = new MyProductAdapter(getActivity(), data);

        mainActivity.runOnUiThread(() -> {
                    // Attach the adapter to a ListView
                    listView.setAdapter(adapter);
                }
        );
    }

    public void createNewProduct(Product newProduct) {
        mainActivity.executeRunnable(() -> {
            Product p = productManager.addMyProduct(mainActivity.getThisAccount().getId(), newProduct);

            mainActivity.runOnUiThread(() -> {
                adapter.add(p);
            });
        });
    }
}
