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
import butterknife.*;
import com.mikepenz.google_material_typeface_library.GoogleMaterial;
import com.mikepenz.iconics.IconicsDrawable;
import me.evrooij.groceries.*;
import me.evrooij.groceries.adapters.ProductAdapter;
import me.evrooij.groceries.data.GroceryList;
import me.evrooij.groceries.data.ListManager;
import me.evrooij.groceries.data.Product;
import me.evrooij.groceries.rest.ResponseMessage;
import me.evrooij.groceries.util.Preferences;
import org.parceler.Parcels;

import java.util.ArrayList;
import java.util.List;

import static me.evrooij.groceries.Config.*;

/**
 * A simple {@link Fragment} subclass.
 */
public class DefaultListFragment extends Fragment {

    @BindView(R.id.fab)
    FloatingActionButton fab;
    @BindView(R.id.lv_my_groceries)
    ListView listView;

    private ProductAdapter adapter;
    private Product editingProduct;
    private MainActivity mainActivity;

    public final static int NEW_PRODUCT_CODE = 1;
    public final static int EDIT_PRODUCT_CODE = 2;
    private static final String TAG = "DefaultListFragment";

    private Unbinder unbinder;
    private ListManager listManager;
    private GroceryList thisList;

    public DefaultListFragment() {
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
        View view = inflater.inflate(R.layout.fragment_default_list, container, false);
        unbinder = ButterKnife.bind(this, view);

        fab.setImageDrawable(new IconicsDrawable(getContext(), GoogleMaterial.Icon.gmd_add).color(Color.WHITE).sizeDp(24));

        listManager = new ListManager(getContext());
        mainActivity = (MainActivity) getActivity();

        setDefaultList();

        return view;
    }

    @OnItemClick(R.id.lv_my_groceries)
    public void onItemClick(int position) {
        Product product = adapter.getItem(position);

        ReturnBoolean r = result -> {
            if (result) {
                mainActivity.executeRunnable(() -> {
                    ResponseMessage message = listManager.deleteProduct(thisList.getId(), product.getId());

                    mainActivity.runOnUiThread(() -> {
                        adapter.remove(product);
                        Toast.makeText(getContext(), (message.toString()), Toast.LENGTH_SHORT).show();
                    });
                });
            }
        };
        new ConfirmDialog(getContext(), r).show(getResources().getString(R.string.product_delete_warning));
    }

    @OnItemLongClick(R.id.lv_my_groceries)
    public boolean onItemLongClick(int position) {
        Product product = adapter.getItem(position);

        editingProduct = product;

        Intent i = new Intent(mainActivity, NewProduct.class).putExtra(KEY_ACCOUNT, Parcels.wrap(mainActivity.getThisAccount())).putExtra(KEY_EDIT_PRODUCT, Parcels.wrap(product));
        startActivityForResult(i, EDIT_PRODUCT_CODE);

        return true;
    }

    private void setDefaultList() {
        mainActivity.executeRunnable(() -> {
                    // Check if user has a preferred list
                    if (Preferences.groceryListIdExists(getContext())) {
                        int id = Preferences.getGroceryListId(getContext());
                        thisList = listManager.getList(id);
                        setListData();
                    } else {
                        List<GroceryList> result = listManager.getMyLists(mainActivity.getThisAccount());

                        if (result.size() > 0) {
                            thisList = result.get(0);
                            setListData();
                        } else {
                            mainActivity.setActionBarTitle(getString(R.string.no_list_found));
                            // No list was found, do nothing
                            Log.d(TAG, "setDefaultList: No list found");
                        }
                    }
                }
        );
    }

    private void setListData() {
        mainActivity.setActionBarTitle(thisList.getName());
        ArrayList<Product> data = new ArrayList<>(thisList.getProductList());

        adapter = new ProductAdapter(getContext(), data);

        mainActivity.runOnUiThread(() -> {
            listView.setAdapter(adapter);
        });
    }

    @OnClick(R.id.fab)
    public void onFabClick(View view) {
        Intent i = new Intent(getContext(), NewProduct.class).putExtra(KEY_ACCOUNT, Parcels.wrap(mainActivity.getThisAccount()));
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
                    Product editProduct = Parcels.unwrap(data.getParcelableExtra(KEY_EDIT_PRODUCT));

                    // Remove product from adapter first
                    adapter.remove(editingProduct);

                    // Update change to backend
                    editProduct(editProduct);
                    break;
                default:
                    Log.d(TAG, "onActivityResult: Could not find product code");
                    break;
            }
        }
        if (resultCode == Activity.RESULT_CANCELED) {
            //Write your code if there's no result
        }
    }

    private void createNewProduct(Product newProduct) {
        new Thread(() -> {
            Product p = listManager.newProduct(thisList.getId(), newProduct);

            mainActivity.runOnUiThread(() -> {
                adapter.add(p);
            });
        }).start();
    }

    private void editProduct(Product editedProduct) {
        mainActivity.executeRunnable(() -> {
            ResponseMessage responseMessage = listManager.editProduct(thisList.getId(), editedProduct.getId(), editedProduct);

            mainActivity.runOnUiThread(() -> {
                adapter.add(editedProduct);
                Toast.makeText(getContext(), responseMessage.toString(), Toast.LENGTH_SHORT).show();
            });
        });
    }
}
