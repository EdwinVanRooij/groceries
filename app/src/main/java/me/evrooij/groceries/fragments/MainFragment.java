package me.evrooij.groceries.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.View;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import me.evrooij.groceries.MainActivity;

/**
 * Author: eddy
 * Date: 21-1-17.
 */

public abstract class MainFragment extends Fragment {

    private Unbinder unbinder;
    protected MainActivity mainActivity;

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        unbinder = ButterKnife.bind(this, view);
        mainActivity = ((MainActivity) getActivity());
    }

    @Override
    public void onDestroyView() {
        unbinder.unbind();
        super.onDestroyView();
    }
}
