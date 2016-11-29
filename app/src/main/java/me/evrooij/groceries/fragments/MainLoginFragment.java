package me.evrooij.groceries.fragments;


import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import com.bumptech.glide.Glide;
import me.evrooij.groceries.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class MainLoginFragment extends Fragment {

    @BindView(R.id.iv)
    ImageView iv;
    @BindView(R.id.tvAppName)
    TextView tvAppName;
    @BindView(R.id.btnRegister)
    Button btnRegister;
    @BindView(R.id.btnLogin)
    Button btnLogin;

    private Unbinder unbinder;

    public MainLoginFragment() {
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
        View view = inflater.inflate(R.layout.fragment_main_login, container, false);
        unbinder = ButterKnife.bind(this, view);

        Glide.with(this)
                .load("http://placekitten.com/600/400")
                .into(iv);

        Typeface tf = Typeface.createFromAsset(getActivity().getAssets(), "fonts/ADAM.otf");
        tvAppName.setTypeface(tf);

        Animation aLogo = AnimationUtils.loadAnimation(getActivity(), R.anim.move_mid_to_top);
        aLogo.setStartOffset(getResources().getInteger(R.integer.default_animation_duration) * 4 / 2);
        iv.startAnimation(aLogo);

        Animation a = AnimationUtils.loadAnimation(getActivity(), R.anim.fade_in_from_bottom);
        a.setStartOffset(getResources().getInteger(R.integer.default_animation_duration) * 4);
        tvAppName.startAnimation(a);
        btnRegister.startAnimation(a);
        btnLogin.startAnimation(a);

        return view;
    }

    @OnClick(R.id.btnLogin)
    public void onLoginClick() {
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        Fragment loginFragment = new LoginFragment();
        ft.add(R.id.flContent, loginFragment);
        ft.setCustomAnimations(R.anim.slide_in_left, 0, 0, R.anim.slide_out_right);
        ft.addToBackStack(null);
        ft.show(loginFragment);
        ft.commit();
    }

    @OnClick(R.id.btnRegister)
    public void onRegisterClick() {
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        Fragment registerFragment = new RegisterFragment();
        ft.add(R.id.flContent, registerFragment);
        ft.setCustomAnimations(R.anim.slide_in_left, 0, 0, R.anim.slide_out_right);
        ft.addToBackStack(null);
        ft.show(registerFragment);
        ft.commit();
    }
}
