package me.evrooij.groceries.ui.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import butterknife.BindView;
import butterknife.OnItemClick;
import com.orhanobut.hawk.Hawk;
import me.evrooij.groceries.R;
import me.evrooij.groceries.domain.GroceryList;
import me.evrooij.groceries.network.ApiService;
import me.evrooij.groceries.network.ClientInterface;
import me.evrooij.groceries.ui.MainActivity;
import me.evrooij.groceries.ui.adapters.GroceryListAdapter;
import me.evrooij.groceries.util.Extensions;
import me.evrooij.groceries.util.Preferences;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class MyListsFragment extends MainFragment {

    @BindView(R.id.lv_my_lists)
    ListView listView;

    private ArrayList<GroceryList> data;
    private GroceryListAdapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return Extensions.inflate(container, R.layout.fragment_my_lists);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mainActivity.setActionBarTitle(getString(R.string.title_lists));

        Hawk.init(getContext()).build();
    }

    @OnItemClick(R.id.lv_my_lists)
    public void onItemClick(int position) {
        GroceryList groceryList = (GroceryList) listView.getAdapter().getItem(position);
        Preferences.saveGroceryListId(getContext(), groceryList.getId());

        MainActivity mainActivity = (MainActivity) getActivity();
        mainActivity.setDefaultListFragment();
    }

    @Override
    public void onResume() {
        mainActivity.executeRunnable(() -> {
            try {
                List<GroceryList> result = ApiService.createService(ClientInterface.class).getLists(mainActivity.getThisAccount().getId()).execute().body();
                data = new ArrayList<>(result);
                adapter = new GroceryListAdapter(getActivity(), data);

                getActivity().runOnUiThread(() -> listView.setAdapter(adapter));
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        super.onResume();
    }
}
