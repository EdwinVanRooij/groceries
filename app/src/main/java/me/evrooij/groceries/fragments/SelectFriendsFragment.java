package me.evrooij.groceries.fragments;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.view.ActionMode;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.*;
import android.widget.ListView;
import android.widget.Toast;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnItemClick;
import butterknife.Unbinder;
import com.mikepenz.fastadapter.FastAdapter;
import com.mikepenz.fastadapter.adapters.ItemAdapter;
import com.mikepenz.fastadapter_extensions.ActionModeHelper;
import com.mikepenz.fastadapter_extensions.UndoHelper;
import me.evrooij.groceries.NewListContainerActivity;
import me.evrooij.groceries.R;
import me.evrooij.groceries.adapters.AccountAdapter;
import me.evrooij.groceries.data.Account;
import me.evrooij.groceries.data.AccountItem;
import me.evrooij.groceries.data.UserManager;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import static android.R.attr.data;

/**
 * A simple {@link Fragment} subclass.
 */
public class SelectFriendsFragment extends Fragment {

    @BindView(R.id.lv_users)
    ListView listView;

    private Unbinder unbinder;
    private UserManager userManager;

    private ArrayList<Account> data;
    private AccountAdapter adapter;

    private NewListContainerActivity containerActivity;

    @Override
    public void onDestroyView() {
        unbinder.unbind();
        super.onDestroyView();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_select_friends, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        unbinder = ButterKnife.bind(this, view);
        containerActivity = (NewListContainerActivity) getActivity();

        userManager = new UserManager(getContext());

        containerActivity.executeRunnable(() -> {
            List<Account> result = userManager.getFriends(containerActivity.getThisAccount().getId());

            refreshListView(result);
        });

        super.onViewCreated(view, savedInstanceState);
    }

    private void refreshListView(List<Account> accounts) {
        // Construct the data source
        data = new ArrayList<>(accounts);
        // Create the adapter to convert the array to views
        adapter = new AccountAdapter(getContext(), data);

        containerActivity.runOnUiThread(() -> {
            // Attach the adapter to a ListView
            listView.setAdapter(adapter);
        });
    }

    @OnItemClick(R.id.lv_users)
    public void onItemClick(int position) {
        containerActivity.addToSelection((Account) listView.getAdapter().getItem(position));
    }
}
