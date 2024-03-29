package me.evrooij.groceries.ui.fragments;


import android.os.Bundle;
import android.support.annotation.Nullable;
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
import me.evrooij.groceries.R;
import me.evrooij.groceries.domain.Account;
import me.evrooij.groceries.ui.NewListContainerActivity;
import me.evrooij.groceries.ui.adapters.AccountAdapterJava;
import me.evrooij.groceries.util.Extensions;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class CompleteListFragment extends Fragment {

    @BindView(R.id.lv_users)
    ListView listView;
    @BindView(R.id.etName)
    EditText etName;

    private Unbinder unbinder;

    private NewListContainerActivity containerActivity;

    @Override
    public void onDestroyView() {
        unbinder.unbind();
        super.onDestroyView();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return Extensions.inflate(container, R.layout.fragment_complete_list);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        unbinder = ButterKnife.bind(this, view);
        containerActivity = (NewListContainerActivity) getActivity();

        List<Account> selectedAccounts = containerActivity.getSelectedAccounts();

        // Construct the data source
        ArrayList<Account> data = new ArrayList<>(selectedAccounts);
        // Create the adapter to convert the array to views
        AccountAdapterJava adapter = new AccountAdapterJava(getActivity(), data);
        // Attach the adapter to a ListView
        listView.setAdapter(adapter);

        etName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                containerActivity.setListName(s.toString());
            }
        });
    }
}
