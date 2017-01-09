package me.evrooij.groceries.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ListView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import me.evrooij.groceries.NewListContainerActivity;
import me.evrooij.groceries.R;
import me.evrooij.groceries.adapters.AccountAdapter;
import me.evrooij.groceries.data.Account;
import org.parceler.Parcels;

import java.util.ArrayList;
import java.util.List;

import static me.evrooij.groceries.Config.KEY_SELECTED_ACCOUNTS;


/**
 * A simple {@link Fragment} subclass.
 */
public class CompleteListFragment extends Fragment {

    @BindView(R.id.lv_users)
    ListView listView;
    @BindView(R.id.etName)
    EditText etName;

    private Unbinder unbinder;

    private NewListContainerActivity activity;

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

        List<Account> selectedAccounts = Parcels.unwrap(getArguments().getParcelable(KEY_SELECTED_ACCOUNTS));

        // Construct the data source
        ArrayList<Account> data = new ArrayList<>(selectedAccounts);
        // Create the adapter to convert the array to views
        AccountAdapter adapter = new AccountAdapter(getActivity(), data);
        // Attach the adapter to a ListView
        listView.setAdapter(adapter);

        activity = (NewListContainerActivity) getActivity();
        etName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                activity.setListName(s.toString());
            }
        });

        return view;
    }

}
