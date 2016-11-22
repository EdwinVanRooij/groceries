package me.evrooij.groceries;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.widget.ListView;
import butterknife.BindView;
import butterknife.ButterKnife;
import me.evrooij.groceries.domain.GroceryList;
import me.evrooij.groceries.domain.adapters.GroceryListAdapter;
import me.evrooij.groceries.domain.adapters.UserAdapter;

import java.util.ArrayList;

public class ProfileActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        ButterKnife.bind(this);
    }
}
