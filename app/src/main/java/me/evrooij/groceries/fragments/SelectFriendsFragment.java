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
import android.widget.Toast;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import com.mikepenz.fastadapter.FastAdapter;
import com.mikepenz.fastadapter.adapters.HeaderAdapter;
import com.mikepenz.fastadapter.adapters.ItemAdapter;
import com.mikepenz.fastadapter_extensions.ActionModeHelper;
import com.mikepenz.fastadapter_extensions.UndoHelper;
import me.evrooij.groceries.R;
import me.evrooij.groceries.adapters.AccountAdapter;
import me.evrooij.groceries.domain.Account;
import me.evrooij.groceries.domain.SimpleItem;
import me.evrooij.groceries.domain.UserManager;
import org.parceler.Parcels;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import static me.evrooij.groceries.Constants.KEY_USER;

/**
 * A simple {@link Fragment} subclass.
 */
public class SelectFriendsFragment extends Fragment {

    @BindView(R.id.rv)
    RecyclerView recyclerView;
    //    @BindView(R.id.lv_users)
//    ListView listView;
    private Unbinder unbinder;

    private Account thisAccount;
    private UserManager userManager;

    ArrayList<Account> data;
    AccountAdapter adapter;

    //save our FastAdapter
    private FastAdapter<SimpleItem> mFastAdapter;
    private UndoHelper mUndoHelper;
    private ActionModeHelper mActionModeHelper;

    private List<Account> selectedAccounts;

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

        thisAccount = Parcels.unwrap(getArguments().getParcelable(KEY_USER));
        userManager = new UserManager();

        selectedAccounts = new ArrayList<>();

//        fillListView();

        return view;
        // Inflate the layout for this fragment
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        //create our FastAdapter
        mFastAdapter = new FastAdapter<>();

        mUndoHelper = new UndoHelper(mFastAdapter, new UndoHelper.UndoListener<SimpleItem>() {
            @Override
            public void commitRemove(Set<Integer> positions, ArrayList<FastAdapter.RelativeInfo<SimpleItem>> removed) {
                Log.e("UndoHelper", "Positions: " + positions.toString() + " Removed: " + removed.size());
            }
        });

        //we init our ActionModeHelper
        mActionModeHelper = new ActionModeHelper(mFastAdapter, R.menu.cab, new ActionBarCallBack());

        //create our adapters
        ItemAdapter<SimpleItem> itemAdapter = new ItemAdapter<>();
        final HeaderAdapter<SimpleItem> headerAdapter = new HeaderAdapter<>();

        //configure our mFastAdapter
        //as we provide id's for the items we want the hasStableIds enabled to speed up things
        mFastAdapter.setHasStableIds(true);
        mFastAdapter.withSelectable(true);
        mFastAdapter.withMultiSelect(true);
        mFastAdapter.withOnClickListener((v, adapter, item, position) -> {
//            if (v.isSelected()) {
//                selectedAccounts.add(item);
//            }
            Boolean res = mActionModeHelper.onClick(item);
            Toast.makeText(v.getContext(), "SelectedCount: " + mFastAdapter.getSelections().size() + " ItemsCount: " + mFastAdapter.getSelectedItems().size(), Toast.LENGTH_SHORT).show();
            return res != null ? res : false;
        });

        //get our recyclerView and do basic setup
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(itemAdapter.wrap(headerAdapter.wrap(mFastAdapter)));

        //fill with some sample data
        SimpleItem SimpleItem = new SimpleItem();
        SimpleItem
                .withName("Header")
                .withIdentifier(1)
                .withSelectable(false);
        headerAdapter.add(SimpleItem);
        List<SimpleItem> items = new ArrayList<>();
        for (int i = 1; i <= 100; i++) {
            SimpleItem item = new SimpleItem();
            item
                    .withName("Test " + i).
                    withIdentifier(100 + i);
            items.add(item);
        }
        itemAdapter.add(items);

        //restore selections (this has to be done after the items were added
        mFastAdapter.withSavedInstanceState(savedInstanceState);

        //inform that longClick is required
        Toast.makeText(getActivity(), "LongClick to enable Multi-Selection", Toast.LENGTH_LONG).show();

        super.onViewCreated(view, savedInstanceState);
    }

//    private void fillListView() {
//        new Thread(() -> {
//            List<Account> result = userManager.getFriends(thisAccount.getId());
//            System.out.println(String.format("Found %s friends of %s", String.valueOf(result.size()), thisAccount.getUsername()));
//
//            refreshListView(result);
//        }).start();
//    }
//
//    private void refreshListView(List<Account> accounts) {
//        // Construct the data source
//        data = new ArrayList<>(accounts);
//        // Create the adapter to convert the array to views
//        adapter = new AccountAdapter(getActivity(), data);
//
//        getActivity().runOnUiThread(() ->
//                // Attach the adapter to a ListView
//                listView.setAdapter(adapter));
//    }

    /**
     * Our ActionBarCallBack to showcase the CAB
     */
    class ActionBarCallBack implements ActionMode.Callback {

        @Override
        public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
            mUndoHelper.remove(getActivity().findViewById(android.R.id.content), "Item removed", "Undo", Snackbar.LENGTH_LONG, mFastAdapter.getSelections());
            //as we no longer have a selection so the actionMode can be finished
            mode.finish();
            //we consume the event
            return true;
        }

        @Override
        public boolean onCreateActionMode(ActionMode mode, Menu menu) {
            return true;
        }

        @Override
        public void onDestroyActionMode(ActionMode mode) {
        }

        @Override
        public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
            return false;
        }
    }
}
