package me.evrooij.groceries;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.*;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnTextChanged;
import me.evrooij.groceries.adapters.MyProductAdapter;
import me.evrooij.groceries.adapters.ProductAdapter;
import me.evrooij.groceries.data.Account;
import me.evrooij.groceries.data.Product;
import me.evrooij.groceries.data.ProductManager;
import org.parceler.Parcels;

import java.util.ArrayList;
import java.util.List;

import static android.R.attr.data;
import static me.evrooij.groceries.Config.*;
import static me.evrooij.groceries.R.id.etEmailContainer;
import static me.evrooij.groceries.R.id.textView;
import static me.evrooij.groceries.R.id.tvTipDescription;

public class NewProductActivity extends AppCompatActivity {
    //region Fields
    @BindView(R.id.etName)
    AutoCompleteTextView etName;
    @BindView(R.id.etAmount)
    EditText etAmount;
    @BindView(R.id.etRemark)
    EditText etRemark;

    @BindView(R.id.btnAdd)
    Button btnAdd;

    private Account thisAccount;

    private ProductManager productManager;
    private ArrayList<Product> data;
    private MyProductAdapter adapter;

    private Product selectedMyProduct;

    private boolean isUpdate;
    private Product thisProduct;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_product);
        ButterKnife.bind(this);

        thisAccount = Parcels.unwrap(getIntent().getParcelableExtra(KEY_ACCOUNT));
        thisProduct = Parcels.unwrap(getIntent().getParcelableExtra(KEY_EDIT_PRODUCT));
        if (thisProduct != null) {
            isUpdate = true;

            etName.setText(thisProduct.getName());
            etAmount.setText(String.valueOf(thisProduct.getAmount()));
            etRemark.setText(thisProduct.getComment());

            btnAdd.setText(R.string.button_update_product);
        }
        if (!isUpdate) {
            productManager = new ProductManager(this);
            new Thread(() -> {
                ArrayList<Product> result = (ArrayList<Product>) productManager.getMyProducts(thisAccount.getId());

                ProductAdapter adapter = new ProductAdapter(this, result);

                runOnUiThread(() -> {
                    etName.setAdapter(adapter);
                    etName.setOnItemClickListener((parent, view, position, id) -> {
                        selectedMyProduct = (Product) parent.getItemAtPosition(position);
                        etName.setText(selectedMyProduct.getName());
                        etAmount.setText(String.valueOf(selectedMyProduct.getAmount()));
                        etRemark.setText(selectedMyProduct.getComment());
                    });
                });
            }).start();

        }
    }

    private static final String TAG = "NewProductActivity";

    @OnTextChanged(R.id.etName)
    protected void onTextChanged(CharSequence text) {
        String featureName = text.toString();
        Log.i(TAG, String.format("feature name: %s", featureName));
    }

    @OnClick(R.id.btnAdd)
    void btnAddClicked() {
        String name = etName.getText().toString();

        int amount;
        if (etAmount.getText().toString().length() == 0) {
            amount = 1;
        } else {
            amount = Integer.valueOf(etAmount.getText().toString());
        }
        String comment = etRemark.getText().toString();

        Product product;

        if (isUpdate) {
            product = new Product(thisProduct.getId(), name, amount, comment, thisAccount);
            // Product already exists, do an update
            Intent returnIntent = new Intent();
            returnIntent.putExtra(KEY_EDIT_PRODUCT, Parcels.wrap(product));
            setResult(Activity.RESULT_OK, returnIntent);
            finish();
        } else {
            if (selectedMyProduct == null) {
                product = new Product(name, amount, comment, thisAccount);
            } else {
                selectedMyProduct.setName(name);
                selectedMyProduct.setAmount(amount);
                selectedMyProduct.setComment(comment);
                product = selectedMyProduct;
            }
            // Product doesn't exist yet, make new product
            Intent returnIntent = new Intent();
            returnIntent.putExtra(KEY_NEW_PRODUCT, Parcels.wrap(product));
            setResult(Activity.RESULT_OK, returnIntent);
            finish();
        }
    }
}
