package me.evrooij.groceries.fragments;


import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import butterknife.BindView;
import butterknife.OnItemClick;
import me.evrooij.groceries.ConfirmDialog;
import me.evrooij.groceries.R;
import me.evrooij.groceries.adapters.MyProductAdapter;
import me.evrooij.groceries.data.Product;
import me.evrooij.groceries.data.ProductManager;
import me.evrooij.groceries.interfaces.ReturnBoolean;
import me.evrooij.groceries.rest.FileUploadInterface;
import me.evrooij.groceries.rest.ResponseMessage;
import me.evrooij.groceries.rest.ServiceGenerator;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import org.parceler.Parcels;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static android.media.MediaRecorder.VideoSource.CAMERA;
import static com.mikepenz.iconics.Iconics.TAG;
import static me.evrooij.groceries.Config.KEY_ACCOUNT;
import static me.evrooij.groceries.Config.KEY_NEW_PRODUCT;
import static me.evrooij.groceries.Config.KEY_PRODUCT_ID;
import static me.evrooij.groceries.MainActivity.REQUEST_IMAGE_CAPTURE;
import static me.evrooij.groceries.fragments.DefaultListFragment.NEW_PRODUCT_CODE;

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
