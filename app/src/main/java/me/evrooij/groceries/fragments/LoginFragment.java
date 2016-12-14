package me.evrooij.groceries.fragments;


import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import me.evrooij.groceries.MainActivity;
import me.evrooij.groceries.R;
import me.evrooij.groceries.domain.Account;
import me.evrooij.groceries.rest.GroceriesClient;
import me.evrooij.groceries.rest.ServiceGenerator;
import retrofit2.Call;
import retrofit2.Response;

import java.io.IOException;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class LoginFragment extends Fragment {

    @BindView(R.id.etUsername)
    EditText etUsername;
    @BindView(R.id.etPassword)
    EditText etPassword;
    @BindView(R.id.etUsernameContainer)
    TextInputLayout containerUsername;
    @BindView(R.id.etPasswordContainer)
    TextInputLayout containerPassword;
    @BindView(R.id.btnLogin)
    Button btnLogin;

    private static final String TAG = "LoginFragment";

    private Unbinder unbinder;

    public LoginFragment() {
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
        View view = inflater.inflate(R.layout.fragment_login, container, false);
        unbinder = ButterKnife.bind(this, view);

        Animation a = AnimationUtils.loadAnimation(getActivity(), R.anim.fade_in);
        a.setStartOffset(getResources().getInteger(R.integer.slide_duration_horizontal));
        containerUsername.startAnimation(a);
        containerPassword.startAnimation(a);

        Animation a2 = AnimationUtils.loadAnimation(getActivity(), R.anim.open_to_sides);
        a2.setStartOffset(getResources().getInteger(R.integer.default_animation_duration) + getResources().getInteger(R.integer.slide_duration_horizontal));
        etUsername.startAnimation(a2);
        etPassword.startAnimation(a2);

        Animation a3 = AnimationUtils.loadAnimation(getActivity(), R.anim.fade_in);
        btnLogin.startAnimation(a3);

        return view;
    }

    @OnClick(R.id.btnLogin)
    public void onLoginClick(View view) {
        try {
//            String username = etUsername.getText().toString().trim();
//            String password = etPassword.getText().toString().trim();
            String username = "usernameee";
            String password = "passwordfromapp";

            getAccountFromRESTApi(username, password);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void getAccountFromRESTApi(String username, String pass) {
        // Create a very simple REST adapter which points the GitHub API endpoint.
        GroceriesClient client = ServiceGenerator.createService(GroceriesClient.class);

        // Fetch and print a list of the contributors to this library.
        Call<Account> call =
                client.getAccountByLogin(username, pass);


        new Thread(() -> {
            try {
                Response<Account> a = call.execute();
                Account acc = a.body();
                System.out.println(String.format("Account: %s", acc));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }).start();

    }
}