package me.evrooij.groceries.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import com.bumptech.glide.Glide;
import me.evrooij.groceries.MainActivity;
import me.evrooij.groceries.R;
import me.evrooij.groceries.data.Account;
import me.evrooij.groceries.util.SquareImageView;
import org.parceler.Parcels;

import static me.evrooij.groceries.Config.KEY_ACCOUNT;

/**
 * A simple {@link Fragment} subclass.
 */
public class MyProfileFragment extends Fragment {

    @BindView(R.id.image)
    SquareImageView imageView;

    private Unbinder unbinder;

    private Account thisAccount;

    public MyProfileFragment() {
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
        View view = inflater.inflate(R.layout.fragment_my_profile, container, false);
        unbinder = ButterKnife.bind(this, view);
        ((MainActivity) getActivity()).setActionBarTitle(getString(R.string.title_my_profile));

        thisAccount = Parcels.unwrap(getArguments().getParcelable(KEY_ACCOUNT));

        Glide.with(this)
                .load("http://placekitten.com/300/400")
                .into(imageView);


        return view;
    }

}
