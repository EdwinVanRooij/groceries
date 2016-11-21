package me.evrooij.groceries;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import butterknife.BindView;
import butterknife.ButterKnife;
import me.evrooij.groceries.domain.Friend;
import me.evrooij.groceries.domain.adapters.FriendAdapter;
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
        ArrayList<Friend> data = new ArrayList<>();
        data.add(new Friend("UserName1", "Name1", "Surname1", 23));
        data.add(new Friend("UserName2", "Name2", "Surname2", 24));
        data.add(new Friend("UserName3", "Name3", "Surname3", 18));
        data.add(new Friend("UserName3", "Name3", "Surname3", 18));
        data.add(new Friend("UserName3", "Name3", "Surname3", 18));
        data.add(new Friend("UserName3", "Name3", "Surname3", 18));
        data.add(new Friend("UserName3", "Name3", "Surname3", 18));
        data.add(new Friend("UserName3", "Name3", "Surname3", 18));
        data.add(new Friend("UserName3", "Name3", "Surname3", 18));
        data.add(new Friend("UserName3", "Name3", "Surname3", 18));
        data.add(new Friend("UserName3", "Name3", "Surname3", 18));
        data.add(new Friend("UserName3", "Name3", "Surname3", 18));
        data.add(new Friend("UserName3", "Name3", "Surname3", 18));
        // Create the adapter to convert the array to views
        FriendAdapter adapter = new FriendAdapter(getActivity(), data);
        // Attach the adapter to a ListView
        listView.setAdapter(adapter);

        return view;
    }

}
