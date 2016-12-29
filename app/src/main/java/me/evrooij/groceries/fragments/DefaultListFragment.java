package me.evrooij.groceries.fragments;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import com.mikepenz.google_material_typeface_library.GoogleMaterial;
import com.mikepenz.iconics.IconicsDrawable;
import me.evrooij.groceries.ConfirmDialog;
import me.evrooij.groceries.NewProduct;
import me.evrooij.groceries.R;
import me.evrooij.groceries.ReturnBoolean;
import me.evrooij.groceries.adapters.ProductAdapter;
import me.evrooij.groceries.domain.Account;
import me.evrooij.groceries.domain.GroceryList;
import me.evrooij.groceries.domain.ListManager;
import me.evrooij.groceries.domain.Product;
import me.evrooij.groceries.rest.ResponseMessage;
import org.parceler.Parcels;

import java.util.ArrayList;
import java.util.List;

import static android.R.attr.data;
import static android.R.attr.defaultHeight;
import static me.evrooij.groceries.Constants.*;

/**
 * A simple {@link Fragment} subclass.
 */
public class DefaultListFragment extends Fragment {

    public final static int NEW_PRODUCT_CODE = 1;
    public final static int EDIT_PRODUCT_CODE = 2;

    @BindView(R.id.fab)
    FloatingActionButton fab;
    @BindView(R.id.lv_my_groceries)
    ListView listView;

    ProductAdapter adapter;

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
        View view = inflater.inflate(R.layout.fragment_default_list, container, false);
        unbinder = ButterKnife.bind(this, view);

        fab.setImageDrawable(new IconicsDrawable(getActivity(), GoogleMaterial.Icon.gmd_add).color(Color.WHITE).sizeDp(24));
        thisAccount = Parcels.unwrap(getArguments().getParcelable(KEY_ACCOUNT));

        listManager = new ListManager();

        setDefaultList();

        listView.setOnItemClickListener((adapter1, v, position, id) -> {
            Product product = (Product) adapter1.getItemAtPosition(position);

            ReturnBoolean r = result -> {
                if (result) {
                    new Thread(() -> {
                        ResponseMessage message = listManager.deleteProduct(thisList.getId(), product.getId());

                        getActivity().runOnUiThread(() -> {
                            adapter.remove(product);
                            Toast.makeText(getActivity(), String.format("Result: %s", message.toString()), Toast.LENGTH_SHORT).show();
                        });
                    }).start();
                }
            };
            new ConfirmDialog(getActivity(), r).show(getResources().getString(R.string.product_delete_warning));
        });

        listView.setOnItemLongClickListener((adapterView, view1, pos, id) -> {
            Product product = (Product) adapterView.getItemAtPosition(pos);

            Intent i = new Intent(getActivity(), NewProduct.class).putExtra(KEY_ACCOUNT, Parcels.wrap(thisAccount)).putExtra(KEY_EDIT_PRODUCT, Parcels.wrap(product));
            startActivityForResult(i, NEW_PRODUCT_CODE);

            return true;
        });

        return view;
    }

    private void setDefaultList() {
        new Thread(() -> {
            List<GroceryList> result = listManager.getMyLists(thisAccount);

            if (result.size() > 0) {
                thisList = result.get(0);
                Log.d(TAG, String.format("setDefaultList: Retrieved %s lists from api, working with list %s which has %s products", result.size(), result.get(0).getName(), result.get(0).getProductList().size()));

                ArrayList<Product> data = new ArrayList<>(thisList.getProductList());

                adapter = new ProductAdapter(getActivity(), data);

                getActivity().runOnUiThread(() -> {
                    listView.setAdapter(adapter);
                    System.out.println(String.format("Finished setting new adapter with %s items", adapter.getCount()));
                });
            } else {
                // No list was found, do nothing
                Log.d(TAG, "setDefaultList: No list found");
            }
        }).start();
    }

    @OnClick(R.id.fab)
    public void onFabClick(View view) {
        Intent i = new Intent(getActivity(), NewProduct.class).putExtra(KEY_ACCOUNT, Parcels.wrap(thisAccount));
        startActivityForResult(i, NEW_PRODUCT_CODE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK) {
            switch (requestCode) {
                case NEW_PRODUCT_CODE:
                    createNewProduct(Parcels.unwrap(data.getParcelableExtra(KEY_NEW_PRODUCT)));
                    break;
                case EDIT_PRODUCT_CODE:

                    // edit product to back end
                    break;
                default:
                    Log.d(TAG, "onActivityResult: Could not find product code");
                    break;
            }
        }
        if (resultCode == Activity.RESULT_CANCELED) {
            //Write your code if there's no result
            System.out.println("Canceled");
        }
    }

    private void createNewProduct(Product newProduct) {
        System.out.println(String.format("Received product %s, sending to api now", newProduct.toString()));

        new Thread(() -> {
            Product p = listManager.newProduct(thisList.getId(), newProduct);

            getActivity().runOnUiThread(() -> {
                adapter.add(p);
                System.out.println(String.format("Added %s to adapter", p));
            });
        }).start();
    }

    private void editProduct(Product editedProduct) {
        new Thread(() -> {
            Product productFromApi = listManager.editProduct(thisList.getId(), editedProduct);

            getActivity().runOnUiThread(() -> {
                adapter.remove(editedProduct);
                adapter.add(editedProduct);
                System.out.println(String.format("Added %s to adapter", p));
            });
        }).start();
    }
}
