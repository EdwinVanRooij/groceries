package me.evrooij.groceries.fragments;


import android.os.Bundle;
import android.os.Process;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import me.evrooij.groceries.R;
import me.evrooij.groceries.adapters.GroceryListAdapter;
import me.evrooij.groceries.adapters.ProductAdapter;
import me.evrooij.groceries.domain.Account;
import me.evrooij.groceries.domain.GroceryList;
import me.evrooij.groceries.domain.ListManager;
import me.evrooij.groceries.domain.Product;
import org.parceler.Parcels;

import java.util.ArrayList;
import java.util.List;

import static android.R.attr.data;
import static android.R.id.list;
import static me.evrooij.groceries.Constants.KEY_GROCERYLIST;
import static me.evrooij.groceries.Constants.KEY_USER;


/**
 * A simple {@link Fragment} subclass.
 */
public class DefaultListFragment extends Fragment {

    @BindView(R.id.lv_my_groceries)
    ListView listView;

    public DefaultListFragment() {
        // Required empty public constructor
    }

    private static final String TAG = "DefaultListFragment";

    private Unbinder unbinder;
    private Account thisAccount;
    private ListManager listManager;
    private GroceryList thisList;

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

        thisAccount = Parcels.unwrap(getArguments().getParcelable(KEY_USER));

        listManager = new ListManager();

        setDefaultList();

        return view;
    }

    private void setDefaultList() {
        new Thread(() -> {
            List<GroceryList> result = listManager.getMyLists(thisAccount);

            if (result.size() > 0) {
                thisList = result.get(0);
                Log.d(TAG, String.format("setDefaultList: Retrieved %s lists from api, working with list %s which has %s products", result.size(), result.get(0).getName(), result.get(0).getProductList().size()));
                refreshListView();
            } else {
                // No list was found, do nothing
                Log.d(TAG, "setDefaultList: No list found");
            }
        }).start();
    }

    private void refreshListView() {
        ArrayList<Product> data = new ArrayList<>(thisList.getProductList());

        ProductAdapter adapter = new ProductAdapter(getActivity(), data);

        getActivity().runOnUiThread(() -> listView.setAdapter(adapter));
    }
}
