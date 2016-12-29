package me.evrooij.groceries;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.EditText;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import me.evrooij.groceries.domain.Account;
import me.evrooij.groceries.domain.Product;
import org.parceler.Parcels;

import static me.evrooij.groceries.Constants.KEY_ACCOUNT;
import static me.evrooij.groceries.Constants.KEY_EDIT_PRODUCT;
import static me.evrooij.groceries.Constants.KEY_NEW_PRODUCT;

public class NewProduct extends AppCompatActivity {
    //region Fields
    @BindView(R.id.etName)
    EditText etName;
    @BindView(R.id.etAmount)
    EditText etAmount;
    @BindView(R.id.etRemark)
    EditText etRemark;

    @BindView(R.id.btnAdd)
    Button btnAdd;

    private Account thisAccount;

    private boolean isUpdate;
    private Product thisProduct;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_product);
        ButterKnife.bind(this);

        thisAccount = Parcels.unwrap(getIntent().getParcelableExtra(KEY_ACCOUNT));
        thisProduct = Parcels.unwrap(getIntent().getParcelableExtra(KEY_EDIT_PRODUCT));
        System.out.println(String.format("Received %s in new product", thisProduct.toString()));
        if (thisProduct != null) {
            isUpdate = true;

            etName.setText(thisProduct.getName());
            etAmount.setText(String.valueOf(thisProduct.getAmount()));
            etRemark.setText(thisProduct.getComment());

            btnAdd.setText(R.string.button_update_product);
        }
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

        Product product = new Product(thisProduct.getId(), name, amount, comment, thisAccount.getUsername());

        if (isUpdate) {
            // Product already exists, do an update
            Intent returnIntent = new Intent();
            returnIntent.putExtra(KEY_EDIT_PRODUCT, Parcels.wrap(product));
            setResult(Activity.RESULT_OK, returnIntent);
            finish();
        } else {
            // Product doesn't exist yet, make new product
            Intent returnIntent = new Intent();
            returnIntent.putExtra(KEY_NEW_PRODUCT, Parcels.wrap(product));
            setResult(Activity.RESULT_OK, returnIntent);
            finish();
        }
    }
}
