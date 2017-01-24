package me.evrooij.groceries.fragments;


import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import butterknife.BindView;
import butterknife.OnItemClick;
import butterknife.OnItemLongClick;
import me.evrooij.groceries.ConfirmDialog;
import me.evrooij.groceries.NewProductActivity;
import me.evrooij.groceries.R;
import me.evrooij.groceries.adapters.ProductAdapter;
import me.evrooij.groceries.data.Product;
import me.evrooij.groceries.data.ProductManager;
import me.evrooij.groceries.interfaces.ReturnBoolean;
import me.evrooij.groceries.rest.ResponseMessage;
import org.parceler.Parcels;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

import static android.app.Activity.RESULT_OK;
import static me.evrooij.groceries.Config.KEY_ACCOUNT;
import static me.evrooij.groceries.Config.KEY_EDIT_PRODUCT;
import static me.evrooij.groceries.R.id.iv;
import static me.evrooij.groceries.fragments.DefaultListFragment.EDIT_PRODUCT_CODE;

/**
 * A simple {@link Fragment} subclass.
 */
public class MyProductsFragment extends MainFragment {

    @BindView(R.id.testIv)
    ImageView testIv;
    @BindView(R.id.lv_my_products)
    ListView listView;
    @BindView(R.id.tvTipDescription)
    TextView tvTipDescription;

    private ArrayList<Product> data;
    private ProductAdapter adapter;
    private ProductManager productManager;

    private Product editingProduct;

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
        adapter = new ProductAdapter(getActivity(), data);

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

    private static final int SELECT_PICTURE = 8;


    @OnItemLongClick(R.id.lv_my_products)
    public boolean onItemLongClick(int position) {
        Product product = adapter.getItem(position);

        editingProduct = product;

        Intent pickIntent = new Intent();
        pickIntent.setType("image/*");
        pickIntent.setAction(Intent.ACTION_GET_CONTENT);

        Intent takePhotoIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        String pickTitle = "Select or take a new Picture"; // Or get from strings.xml
        Intent chooserIntent = Intent.createChooser(pickIntent, pickTitle);
        chooserIntent.putExtra
                (
                        Intent.EXTRA_INITIAL_INTENTS,
                        new Intent[]{takePhotoIntent}
                );

        mainActivity.startActivityForResult(chooserIntent, SELECT_PICTURE);

        return true;
    }


    public void editProduct(File file) {
        mainActivity.executeRunnable(() -> {
            ResponseMessage message = productManager.editMyProduct(mainActivity.getThisAccount().getId(), editingProduct, file);

            mainActivity.runOnUiThread(() -> {
                Toast.makeText(getContext(), message.toString(), Toast.LENGTH_SHORT).show();
            });
        });
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == SELECT_PICTURE && resultCode == RESULT_OK) {
            Toast.makeText(getContext(), "Starting select picture result", Toast.LENGTH_SHORT).show();
            if (data == null) {
                Toast.makeText(getContext(), "Data was null", Toast.LENGTH_SHORT).show();
                return;
            }
            try {
                InputStream inputStream = mainActivity.getContentResolver().openInputStream(data.getData());
                Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                editProduct(bitmapToFile(bitmap));
                Toast.makeText(getContext(), "May have uploaded image", Toast.LENGTH_SHORT).show();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        } else {
            System.out.println(String.format("Not doing select picture result, request was %s while we need %s. Result was %s while we need %s", requestCode, SELECT_PICTURE, resultCode, RESULT_OK));
        }
    }

    private File bitmapToFile(Bitmap bitmap) {
        try {
            //create a file to write bitmap data
            File f = new File(getContext().getCacheDir(), "fileName");
            f.createNewFile();

//Convert bitmap to byte array
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.PNG, 90 /*ignored for PNG*/, bos);
            byte[] bitmapdata = bos.toByteArray();

//write the bytes in file
            FileOutputStream fos = new FileOutputStream(f);
            fos.write(bitmapdata);
            fos.flush();
            fos.close();

            return f;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}
