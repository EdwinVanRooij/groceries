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
import butterknife.BindView;
import butterknife.ButterKnife;
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
import org.parceler.Parcels;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import static me.evrooij.groceries.Config.KEY_ACCOUNT;

/**
 * A simple {@link Fragment} subclass.
 */
public class SelectFriendsFragment extends Fragment {

    @BindView(R.id.rv)
    RecyclerView recyclerView;
    //    @BindView(R.id.lv_users)
//    ListView listView;
    private Unbinder unbinder;

    private UserManager userManager;

    //save our FastAdapter
    private FastAdapter<AccountItem> mFastAdapter;
    private UndoHelper mUndoHelper;
    private ActionModeHelper mActionModeHelper;

    private ItemAdapter<AccountItem> itemAdapter;

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

        //create our FastAdapter
        mFastAdapter = new FastAdapter<>();

        mUndoHelper = new UndoHelper(mFastAdapter, new UndoHelper.UndoListener<AccountItem>() {
            @Override
            public void commitRemove(Set<Integer> positions, ArrayList<FastAdapter.RelativeInfo<AccountItem>> removed) {
                Log.e("UndoHelper", "Positions: " + positions.toString() + " Removed: " + removed.size());
            }
        });

        //we init our ActionModeHelper
        mActionModeHelper = new ActionModeHelper(mFastAdapter, R.menu.cab, new ActionBarCallBack());

        //create our adapters
        itemAdapter = new ItemAdapter<>();

        //configure our mFastAdapter
        //as we provide id's for the items we want the hasStableIds enabled to speed up things
        mFastAdapter.setHasStableIds(true);
        mFastAdapter.withSelectable(true);
        mFastAdapter.withMultiSelect(true);
        mFastAdapter.withOnClickListener((v, adapter, item, position) -> {
            if (v.isSelected()) {
                containerActivity.addToSelection(item.getAccount());
            }
            Boolean res = mActionModeHelper.onClick(item);
            return res != null ? res : false;
        });

        //get our recyclerView and do basic setup
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(itemAdapter.wrap(mFastAdapter));

        //restore selections (this has to be done after the items were added
        mFastAdapter.withSavedInstanceState(savedInstanceState);

        fillListView();

        super.onViewCreated(view, savedInstanceState);
    }

    private void fillListView() {
        containerActivity.executeRunnable(() -> {
            List<Account> result = userManager.getFriends(containerActivity.getThisAccount().getId());

            getActivity().runOnUiThread(() -> {
                for (Account a : result) {
                    itemAdapter.add(new AccountItem().withAccount(a));
                }
            });
        });
    }

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
