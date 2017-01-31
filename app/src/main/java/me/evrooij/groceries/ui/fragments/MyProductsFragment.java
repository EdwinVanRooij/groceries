package me.evrooij.groceries.ui.fragments;


import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;
import butterknife.BindView;
import butterknife.OnItemLongClick;
import me.evrooij.groceries.R;
import me.evrooij.groceries.ui.adapters.ProductAdapter;
import me.evrooij.groceries.models.Product;
import me.evrooij.groceries.network.ClientInterface;
import me.evrooij.groceries.network.FileUploadInterface;
import me.evrooij.groceries.network.ResponseMessage;
import me.evrooij.groceries.network.ApiService;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

import static android.app.Activity.RESULT_OK;

/**
 * A simple {@link Fragment} subclass.
 */
public class MyProductsFragment extends MainFragment {

    @BindView(R.id.lv_my_products)
    ListView listView;

    private ArrayList<Product> data;
    private ProductAdapter adapter;

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
    }

    @Override
    public void onResume() {
        super.onResume();

        mainActivity.executeRunnable(() -> {
            try {
                List<Product> result = ApiService.createService(ClientInterface.class).getMyProducts(mainActivity.getThisAccount().getId()).execute().body();

                refreshListView(result);
            } catch (IOException e) {
                e.printStackTrace();
            }
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
            try {
                Product p = ApiService.createService(ClientInterface.class).addMyProduct(mainActivity.getThisAccount().getId(), newProduct).execute().body();

                mainActivity.runOnUiThread(() -> {
                    adapter.add(p);
                });
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

    private static final int SELECT_PICTURE = 8;


    @OnItemLongClick(R.id.lv_my_products)
    public boolean onItemLongClick(int position) {
        editingProduct = adapter.getItem(position);

        Intent pickIntent = new Intent();
        pickIntent.setType("image/*");
        pickIntent.setAction(Intent.ACTION_GET_CONTENT);

        String pickTitle = "Select or take a new Picture"; // Or get from strings.xml
        Intent chooserIntent = Intent.createChooser(pickIntent, pickTitle);

        mainActivity.startActivityForResult(chooserIntent, SELECT_PICTURE);

        return true;
    }


    public void editProduct(File file) {
        Toast.makeText(mainActivity, getString(R.string.wait), Toast.LENGTH_SHORT).show();
        mainActivity.executeRunnable(() -> {
            try {
                ResponseMessage message = ApiService.createService(
                        FileUploadInterface.class)
                        .upload(
                                mainActivity.getThisAccount().getId(),
                                editingProduct.getId(),
                                MultipartBody.Part.createFormData(
                                        "picture", file.getName(),
                                        RequestBody.create(MediaType.parse("image/*"),
                                                file))).execute().body();

                mainActivity.runOnUiThread(() ->
//                        Toast.makeText(getContext(), message.toString(), Toast.LENGTH_SHORT).show());
                        Toast.makeText(mainActivity, getString(R.string.uploaded_image), Toast.LENGTH_SHORT).show());
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == SELECT_PICTURE && resultCode == RESULT_OK) {
            if (data == null) {
                return;
            }
            try {
                InputStream inputStream = mainActivity.getContentResolver().openInputStream(data.getData());
                Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                editProduct(bitmapToFile(bitmap));
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
