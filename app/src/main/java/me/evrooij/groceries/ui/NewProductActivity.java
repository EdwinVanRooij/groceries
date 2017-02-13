package me.evrooij.groceries.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnTextChanged;
import me.evrooij.groceries.R;
import me.evrooij.groceries.domain.Account;
import me.evrooij.groceries.domain.Product;
import me.evrooij.groceries.network.ApiService;
import me.evrooij.groceries.network.ClientInterface;
import me.evrooij.groceries.ui.adapters.ProductAdapter;
import org.parceler.Parcels;

import java.io.IOException;
import java.util.ArrayList;

import static me.evrooij.groceries.util.Config.*;

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
            new Thread(() -> {
                try {
                    ArrayList<Product> result = (ArrayList<Product>) ApiService.createService(ClientInterface.class).getMyProducts(thisAccount.getId()).execute().body();

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
                } catch (IOException e) {
                    e.printStackTrace();
                }
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
