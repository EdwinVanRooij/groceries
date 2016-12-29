package me.evrooij.groceries.fragments;


import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import com.mikepenz.google_material_typeface_library.GoogleMaterial;
import com.mikepenz.iconics.IconicsDrawable;
import me.evrooij.groceries.NewProduct;
import me.evrooij.groceries.R;
import me.evrooij.groceries.adapters.ProductAdapterTwo;
import me.evrooij.groceries.data.Account;
import me.evrooij.groceries.data.GroceryList;
import me.evrooij.groceries.domain.ListManager;
import me.evrooij.groceries.data.Product;
import org.parceler.Parcels;

import java.util.ArrayList;
import java.util.List;

import static me.evrooij.groceries.Constants.KEY_ACCOUNT;
import static me.evrooij.groceries.Constants.KEY_NEW_PRODUCT;
import static me.evrooij.groceries.R.id.fab;


/**
 * A simple {@link Fragment} subclass.
 */
public class DefaultListFragmentTwo extends Fragment {

    public DefaultListFragmentTwo() {
        // Required empty public constructor
    }



}
