package me.evrooij.groceries.fragments;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import butterknife.BindView;
import com.bumptech.glide.Glide;
import com.github.ksoichiro.android.observablescrollview.ObservableScrollView;
import com.github.ksoichiro.android.observablescrollview.ObservableScrollViewCallbacks;
import com.github.ksoichiro.android.observablescrollview.ScrollState;
import com.github.ksoichiro.android.observablescrollview.ScrollUtils;
import com.nineoldandroids.view.ViewHelper;
import me.evrooij.groceries.R;

import static me.evrooij.groceries.R.id.toolbar;

/**
 * A simple {@link Fragment} subclass.
 */
public class MyProfileFragment extends MainFragment implements ObservableScrollViewCallbacks, NavigationView.OnNavigationItemSelectedListener {

    @BindView(R.id.image)
    ImageView mImageView;
    @BindView(toolbar)
    Toolbar mToolbarView;

    private int mParallaxImageHeight;

    @Override
    public void onDestroyView() {
        mainActivity.getSupportActionBar().show();
        super.onDestroyView();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_my_profile, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Disable MainActivity toolbar
        mainActivity.getSupportActionBar().hide();

        mToolbarView.setBackgroundColor(ScrollUtils.getColorWithAlpha(0, ContextCompat.getColor(getContext(), R.color.primary)));
        mToolbarView.setTitle(getString(R.string.title_my_profile));

        // Basic init
        ObservableScrollView mScrollView = (ObservableScrollView) view.findViewById(R.id.scroll);
        mScrollView.setScrollViewCallbacks(this);

        mParallaxImageHeight = getResources().getDimensionPixelSize(R.dimen.parallax_image_height);

        Glide.with(this)
                .load("http://placekitten.com/600/300")
                .into(mImageView);
    }

    @Override
    public void onScrollChanged(int scrollY, boolean firstScroll, boolean dragging) {
        int baseColor = ContextCompat.getColor(getContext(), R.color.primary_dark);
        float alpha = Math.min(1, (float) scrollY / mParallaxImageHeight);
        mToolbarView.setBackgroundColor(ScrollUtils.getColorWithAlpha(alpha, baseColor));
        ViewHelper.setTranslationY(mImageView, scrollY / 2);
    }

    @Override
    public void onDownMotionEvent() {

    }

    @Override
    public void onUpOrCancelMotionEvent(ScrollState scrollState) {

    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        return false;
    }
}
