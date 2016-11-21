package me.evrooij.groceries;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;
import butterknife.BindView;
import butterknife.ButterKnife;
import me.evrooij.groceries.domain.User;
import me.evrooij.groceries.domain.adapters.UserAdapter;

import java.util.ArrayList;

public class SearchUserActivity extends AppCompatActivity {

    @BindView(R.id.lv_users)
    ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_user);
        ButterKnife.bind(this);

        // Construct the data source
        ArrayList<User> data = new ArrayList<>();
        for (int i = 0; i < 15; i++) {
            data.add(new User(String.format("Username%s", i), String.format("Name%s", i), String.format("Surname%s", i), 18));
        }
        // Create the adapter to convert the array to views
        UserAdapter adapter = new UserAdapter(this, data);
        // Attach the adapter to a ListView
        listView.setAdapter(adapter);
    }
}
