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
import me.evrooij.groceries.adapters.AccountAdapter;
import me.evrooij.groceries.domain.Account;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 */
public class CompleteListFragment extends Fragment {

    @BindView(R.id.lv_users)
    ListView listView;

    private Unbinder unbinder;

    public CompleteListFragment() {
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
        View view = inflater.inflate(R.layout.fragment_complete_list, container, false);
        unbinder = ButterKnife.bind(this, view);

        // Construct the data source
        ArrayList<Account> data = new ArrayList<>();
        for (int i = 0; i < 4; i++) {
            data.add(new Account(String.format("Accountname%s", i), String.format("Name%s", i), String.format("Surname%s", i)));
        }
        // Create the adapter to convert the array to views
        AccountAdapter adapter = new AccountAdapter(getActivity(), data);
        // Attach the adapter to a ListView
        listView.setAdapter(adapter);

        return view;
        // Inflate the layout for this fragment
    }

}
