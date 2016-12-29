/*
 *    Copyright (C) 2015 Haruki Hasegawa
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */

package me.evrooij.groceries.fragments;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.nfc.Tag;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import com.h6ah4i.android.widget.advrecyclerview.animator.GeneralItemAnimator;
import com.h6ah4i.android.widget.advrecyclerview.animator.SwipeDismissItemAnimator;
import com.h6ah4i.android.widget.advrecyclerview.decoration.SimpleListDividerDecorator;
import com.h6ah4i.android.widget.advrecyclerview.swipeable.RecyclerViewSwipeManager;
import com.h6ah4i.android.widget.advrecyclerview.touchguard.RecyclerViewTouchActionGuardManager;
import com.h6ah4i.android.widget.advrecyclerview.utils.WrapperAdapterUtils;
import com.mikepenz.google_material_typeface_library.GoogleMaterial;
import com.mikepenz.iconics.IconicsDrawable;
import me.evrooij.groceries.MainActivity;
import me.evrooij.groceries.NewProduct;
import me.evrooij.groceries.R;
import me.evrooij.groceries.adapters.ProductAdapter;
import me.evrooij.groceries.adapters.ProductAdapterTwo;
import me.evrooij.groceries.data.Account;
import me.evrooij.groceries.data.GroceryList;
import me.evrooij.groceries.data.Product;
import me.evrooij.groceries.data.ProductDataProvider;
import me.evrooij.groceries.domain.ListManager;
import org.parceler.Parcels;

import java.util.ArrayList;
import java.util.List;

import static com.mikepenz.iconics.Iconics.TAG;
import static me.evrooij.groceries.Constants.KEY_ACCOUNT;
import static me.evrooij.groceries.Constants.KEY_NEW_PRODUCT;

public class DefaultListFragment extends Fragment {

    public static final String TAG = "DefaultListFragment";

    @BindView(R.id.fab)
    FloatingActionButton fab;

    private RecyclerView mRecyclerView;
    private RecyclerView.LayoutManager mLayoutManager;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.Adapter mWrappedAdapter;
    private RecyclerViewSwipeManager mRecyclerViewSwipeManager;
    private RecyclerViewTouchActionGuardManager mRecyclerViewTouchActionGuardManager;

    public DefaultListFragment() {
        super();
    }

    private Unbinder unbinder;
    private Account thisAccount;
    private ListManager listManager;
    private GroceryList thisList;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_default_list, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        unbinder = ButterKnife.bind(this, view);

        thisAccount = Parcels.unwrap(getArguments().getParcelable(KEY_ACCOUNT));
        listManager = new ListManager();


        fab.setImageDrawable(new IconicsDrawable(getActivity(), GoogleMaterial.Icon.gmd_add).color(Color.WHITE).sizeDp(24));

        //noinspection ConstantConditions
        mRecyclerView = (RecyclerView) getView().findViewById(R.id.recycler_view);
        mLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);

        // touch guard manager  (this class is required to suppress scrolling while swipe-dismiss animation is running)
        mRecyclerViewTouchActionGuardManager = new RecyclerViewTouchActionGuardManager();
        mRecyclerViewTouchActionGuardManager.setInterceptVerticalScrollingWhileAnimationRunning(true);
        mRecyclerViewTouchActionGuardManager.setEnabled(true);

        // swipe manager
        mRecyclerViewSwipeManager = new RecyclerViewSwipeManager();

        //adapter
        final ProductAdapter myItemAdapter = new ProductAdapter(getDataProvider());
        myItemAdapter.setEventListener(new ProductAdapter.EventListener() {
            @Override
            public void onItemRemoved(int position) {
                ((MainActivity) getActivity()).onItemRemoved(position);
            }

            @Override
            public void onItemViewClicked(View v, boolean pinned) {
                onItemViewClick(v, pinned);
            }
        });

        mAdapter = myItemAdapter;

        mWrappedAdapter = mRecyclerViewSwipeManager.createWrappedAdapter(myItemAdapter);      // wrap for swiping

        final GeneralItemAnimator animator = new SwipeDismissItemAnimator();

        // Disable the change animation in order to make turning back animation of swiped item works properly.
        animator.setSupportsChangeAnimations(false);

        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mWrappedAdapter);  // requires *wrapped* adapter
        mRecyclerView.setItemAnimator(animator);

        // additional decorations
        //noinspection StatementWithEmptyBody
        mRecyclerView.addItemDecoration(new SimpleListDividerDecorator(ContextCompat.getDrawable(getContext(), R.drawable.list_divider_h), true));

        // NOTE:
        // The initialization order is very important! This order determines the priority of touch event handling.
        //
        // priority: TouchActionGuard > Swipe > DragAndDrop
        mRecyclerViewTouchActionGuardManager.attachRecyclerView(mRecyclerView);
        mRecyclerViewSwipeManager.attachRecyclerView(mRecyclerView);

        setDefaultList();
    }

    @OnClick(R.id.fab)
    public void onFabClick(View view) {
        Intent i = new Intent(getActivity(), NewProduct.class).putExtra(KEY_ACCOUNT, Parcels.wrap(thisAccount));
        startActivityForResult(i, 1);
    }

    @Override
    public void onDestroyView() {
        if (mRecyclerViewSwipeManager != null) {
            mRecyclerViewSwipeManager.release();
            mRecyclerViewSwipeManager = null;
        }

        if (mRecyclerViewTouchActionGuardManager != null) {
            mRecyclerViewTouchActionGuardManager.release();
            mRecyclerViewTouchActionGuardManager = null;
        }

        if (mRecyclerView != null) {
            mRecyclerView.setItemAnimator(null);
            mRecyclerView.setAdapter(null);
            mRecyclerView = null;
        }

        if (mWrappedAdapter != null) {
            WrapperAdapterUtils.releaseAll(mWrappedAdapter);
            mWrappedAdapter = null;
        }
        mAdapter = null;
        mLayoutManager = null;

        unbinder.unbind();

        super.onDestroyView();
    }

    private void onItemViewClick(View v, boolean pinned) {
        int position = mRecyclerView.getChildAdapterPosition(v);
        if (position != RecyclerView.NO_POSITION) {
            ((MainActivity) getActivity()).onItemClicked(position);
        }
    }

    public ProductDataProvider getDataProvider() {
        return ((MainActivity) getActivity()).getDataProvider();
    }

    public void notifyItemChanged(int position) {
        mAdapter.notifyItemChanged(position);
    }

    public void notifyItemInserted(int position) {
        mAdapter.notifyItemInserted(position);
        mRecyclerView.scrollToPosition(position);
    }

    private void setDefaultList() {
        new Thread(() -> {
            List<GroceryList> result = listManager.getMyLists(thisAccount);

            if (result.size() > 0) {
                thisList = result.get(0);
                Log.d(TAG, String.format("setDefaultList: Retrieved %s lists from api, working with list %s which has %s products", result.size(), result.get(0).getName(), result.get(0).getProductList().size()));

                ArrayList<Product> data = new ArrayList<>(thisList.getProductList());

                getActivity().runOnUiThread(() -> {
                            for (Product p : data) {
                                getDataProvider().addItem(p);
                            }
                        }
                );
            } else {
                // No list was found, do nothing
                Log.d(TAG, "setDefaultList: No list found");
            }
        }).start();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 1) {
            if (resultCode == Activity.RESULT_OK) {
                Product newProduct = Parcels.unwrap(data.getParcelableExtra(KEY_NEW_PRODUCT));
                System.out.println(String.format("Received product %s, sending to api now", newProduct.toString()));

                new Thread(() -> {
                    Product p = listManager.newProduct(thisList.getId(), newProduct);
                    System.out.println(String.format("Received product %s", p.toString()));

                    getActivity().runOnUiThread(() -> {
                        getDataProvider().addItem(p);
                        notifyItemInserted(mAdapter.getItemCount());
                        System.out.println(String.format("Added %s to adapter", p));
                    });
                }).start();
            }
            if (resultCode == Activity.RESULT_CANCELED) {
                //Write your code if there's no result
                System.out.println("Canceled");
            }
        }
    }
}
