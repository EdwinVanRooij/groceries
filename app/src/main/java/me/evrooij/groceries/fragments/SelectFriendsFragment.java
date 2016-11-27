package me.evrooij.groceries.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import me.evrooij.groceries.R;
import me.evrooij.groceries.domain.User;
import me.evrooij.groceries.adapters.UserAdapter;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 */
public class SelectFriendsFragment extends Fragment {

    @BindView(R.id.lv_users)
    ListView listView;
    private Unbinder unbinder;

    public SelectFriendsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_select_friends, container, false);
        unbinder = ButterKnife.bind(this, view);

        // Construct the data source
        ArrayList<User> data = new ArrayList<>();
        for (int i = 0; i < 15; i++) {
            data.add(new User(String.format("Username%s", i), String.format("Name%s", i), String.format("Surname%s", i), 18));
        }
        // Create the adapter to convert the array to views
        UserAdapter adapter = new UserAdapter(getActivity(), data);
        // Attach the adapter to a ListView
        listView.setAdapter(adapter);

        return view;
        // Inflate the layout for this fragment
    }

}
