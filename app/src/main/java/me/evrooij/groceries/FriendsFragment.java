package me.evrooij.groceries;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import butterknife.BindView;
import butterknife.ButterKnife;
import me.evrooij.groceries.domain.User;
import me.evrooij.groceries.domain.adapters.FriendAdapter;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 */
public class FriendsFragment extends Fragment {

    @BindView(R.id.lv_my_friends)
    ListView listView;

    public FriendsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_friends, container, false);
        ButterKnife.bind(this, view);

        // Construct the data source
        ArrayList<User> data = new ArrayList<>();
        for (int i = 0; i < 15; i++) {
            data.add(new User(String.format("Username%s", i), String.format("Name%s", i), String.format("Surname%s", i), 18));
        }
        // Create the adapter to convert the array to views
        FriendAdapter adapter = new FriendAdapter(getActivity(), data);
        // Attach the adapter to a ListView
        listView.setAdapter(adapter);

        return view;
    }

}
