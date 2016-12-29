package me.evrooij.groceries;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.EditText;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import me.evrooij.groceries.domain.Account;
import me.evrooij.groceries.domain.ListManager;
import me.evrooij.groceries.domain.Product;
import org.parceler.Parcels;

import static me.evrooij.groceries.Constants.KEY_ACCOUNT;
import static me.evrooij.groceries.Constants.KEY_NEW_PRODUCT;

public class NewProduct extends AppCompatActivity {
    //region Fields
    @BindView(R.id.etName)
    EditText etName;
    @BindView(R.id.etAmount)
    EditText etAmount;
    @BindView(R.id.etRemark)
    EditText etRemark;

    private Account thisAccount;
    private ListManager listManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_product);
        ButterKnife.bind(this);

        thisAccount = Parcels.unwrap(getIntent().getParcelableExtra(KEY_ACCOUNT));
        listManager = new ListManager();
    }

    @OnClick(R.id.btnAdd)
    void btnAddClicked() {
        String name = etName.getText().toString();

        int amount = 0;
        if (etAmount.getText().toString().length() == 0) {
            amount = 1;
        } else {
            amount = Integer.valueOf(etAmount.getText().toString());
        }
        String comment = etRemark.getText().toString();

        Product product = new Product(name, amount, comment, thisAccount.getUsername());

        Intent returnIntent = new Intent();
        returnIntent.putExtra(KEY_NEW_PRODUCT, Parcels.wrap(product));
        setResult(Activity.RESULT_OK, returnIntent);
        finish();
    }
}
