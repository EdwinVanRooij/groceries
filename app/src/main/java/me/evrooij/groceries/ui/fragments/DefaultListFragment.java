package me.evrooij.groceries.ui.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;
import butterknife.BindView;
import butterknife.OnItemClick;
import butterknife.OnItemLongClick;
import me.evrooij.groceries.R;
import me.evrooij.groceries.domain.GroceryList;
import me.evrooij.groceries.interfaces.ReturnBoolean;
import me.evrooij.groceries.domain.Product;
import me.evrooij.groceries.network.ApiService;
import me.evrooij.groceries.network.ClientInterface;
import me.evrooij.groceries.network.ResponseMessage;
import me.evrooij.groceries.ui.ConfirmDialog;
import me.evrooij.groceries.ui.NewProductActivity;
import me.evrooij.groceries.ui.adapters.ProductAdapter;
import me.evrooij.groceries.util.Extensions;
import me.evrooij.groceries.util.Preferences;
import org.parceler.Parcels;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static me.evrooij.groceries.util.Config.KEY_ACCOUNT;
import static me.evrooij.groceries.util.Config.KEY_EDIT_PRODUCT;

/**
 * A simple {@link Fragment} subclass.
 */
public class DefaultListFragment extends MainFragment {

    @BindView(R.id.lv_my_groceries)
    ListView listView;

    private ProductAdapter adapter;
    private Product editingProduct;

    public final static int NEW_PRODUCT_CODE = 1;
    public final static int EDIT_PRODUCT_CODE = 2;
    private static final String TAG = "DefaultListFragment";

    private GroceryList thisList;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return Extensions.inflate(container, R.layout.fragment_default_list);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        setDefaultList();
    }

    @OnItemClick(R.id.lv_my_groceries)
    public void onItemClick(int position) {
        Product product = adapter.getItem(position);

        ReturnBoolean r = result -> {
            if (result) {
                mainActivity.executeRunnable(() -> {
                    try {
                        ResponseMessage message = ApiService.createService(ClientInterface.class).deleteProduct(thisList.getId(), product.getId()).execute().body();

                        mainActivity.runOnUiThread(() -> {
                            adapter.remove(product);
                            Toast.makeText(getContext(), (message.toString()), Toast.LENGTH_SHORT).show();
                        });
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                });
            }
        };
        new ConfirmDialog(getContext(), r).show(getResources().getString(R.string.product_delete_warning));
    }

    @OnItemLongClick(R.id.lv_my_groceries)
    public boolean onItemLongClick(int position) {
        Product product = adapter.getItem(position);

        editingProduct = product;

        Intent i = new Intent(mainActivity, NewProductActivity.class).putExtra(KEY_ACCOUNT, Parcels.wrap(mainActivity.getThisAccount())).putExtra(KEY_EDIT_PRODUCT, Parcels.wrap(product));
        mainActivity.startActivityForResult(i, EDIT_PRODUCT_CODE);

        return true;
    }

    private void setDefaultList() {
        mainActivity.executeRunnable(() -> {
                    try {
                        // Check if user has a preferred list
                        if (Preferences.groceryListIdExists(getContext())) {
                            int id = Preferences.getGroceryListId(getContext());
                            thisList = ApiService.createService(ClientInterface.class).getList(id).execute().body();
                            setListData();
                        } else {
                            List<GroceryList> result = ApiService.createService(ClientInterface.class).getLists(mainActivity.getThisAccount().getId()).execute().body();

                            if (result != null && result.size() > 0) {
                                thisList = result.get(0);
                                setListData();
                            } else {
                                mainActivity.setActionBarTitle(getString(R.string.no_list_found));
                                // No list was found, do nothing
                                Log.d(TAG, "setDefaultList: No list found");
                            }
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
        );
    }

    private void setListData() {
        if (thisList == null) {
            return;
        }
        mainActivity.setActionBarTitle(thisList.getName());
        ArrayList<Product> data = new ArrayList<>(thisList.getProductList());

        adapter = new ProductAdapter(getContext(), data);

        mainActivity.runOnUiThread(() -> {
            if (listView != null) {
                listView.setAdapter(adapter);
            }
        });
    }

    public void createNewProduct(Product newProduct) {
        mainActivity.executeRunnable(() -> {
            try {
                Product p = ApiService.createService(ClientInterface.class).newProduct(thisList.getId(), newProduct).execute().body();

                mainActivity.runOnUiThread(() -> {
                    adapter.add(p);
                });
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

    public void editProduct(Product editedProduct) {
        // Remove product from adapter first
        adapter.remove(editingProduct);

        mainActivity.executeRunnable(() -> {
            try {
                ResponseMessage responseMessage = ApiService.createService(ClientInterface.class).editProduct(thisList.getId(), editedProduct.getId(), editedProduct).execute().body();

                mainActivity.runOnUiThread(() -> {
                    adapter.add(editedProduct);
                    Toast.makeText(getContext(), responseMessage.toString(), Toast.LENGTH_SHORT).show();
                });
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }
}
