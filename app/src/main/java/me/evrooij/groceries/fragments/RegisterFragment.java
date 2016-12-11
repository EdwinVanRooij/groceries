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
import me.evrooij.groceries.rest.GroceriesApiInterface;
import me.evrooij.groceries.rest.RestClient;
import retrofit.Call;
import retrofit.Callback;
import retrofit.Response;


/**
 * A simple {@link Fragment} subclass.
 */
public class RegisterFragment extends Fragment {

    @BindView(R.id.etUsername)
    EditText etUsername;
    @BindView(R.id.etEmail)
    EditText etEmail;
    @BindView(R.id.etPassword)
    EditText etPassword;
    @BindView(R.id.etUsernameContainer)
    TextInputLayout containerUsername;
    @BindView(R.id.etEmailContainer)
    TextInputLayout containerEmail;
    @BindView(R.id.etPasswordContainer)
    TextInputLayout containerPassword;
    @BindView(R.id.btnRegister)
    Button btnRegister;

    private Unbinder unbinder;

    private static final String TAG = "RegisterFragment";

    public RegisterFragment() {
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
        View view = inflater.inflate(R.layout.fragment_register, container, false);
        unbinder = ButterKnife.bind(this, view);

        Animation a = AnimationUtils.loadAnimation(getActivity(), R.anim.fade_in);
        a.setStartOffset(getResources().getInteger(R.integer.slide_duration_horizontal));
        containerUsername.startAnimation(a);
        containerEmail.startAnimation(a);
        containerPassword.startAnimation(a);
        Animation a2 = AnimationUtils.loadAnimation(getActivity(), R.anim.open_to_sides);
        a2.setStartOffset(getResources().getInteger(R.integer.default_animation_duration) + getResources().getInteger(R.integer.slide_duration_horizontal));
        etUsername.startAnimation(a2);
        etEmail.startAnimation(a2);
        etPassword.startAnimation(a2);
        Animation a3 = AnimationUtils.loadAnimation(getActivity(), R.anim.fade_in);
        btnRegister.startAnimation(a3);

        return view;
    }

    @OnClick(R.id.btnRegister)
    public void onLoginClick(View view) {
        try {
            String username = etUsername.getText().toString().trim();
            String email = etEmail.getText().toString().trim();
            String password = etPassword.getText().toString().trim();

            registerAccountFromRESTApi(username, email, password);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void registerAccountFromRESTApi(String username, String email, String pass) {
        // Setup retrofit http request
        GroceriesApiInterface service = RestClient.getClient();
        Call<Account> call = service.registerAccount(new Account(username, email, pass));
        call.enqueue(new Callback<Account>() {
            @Override
            public void onResponse(Response<Account> response) {
                if (response.isSuccess()) {
                    // request successful (status code 200, 201)
                    Account result = response.body();

                    System.out.println(String.format("Received account: %s", result.toString()));
                    startActivity(
                            new Intent(getActivity(), MainActivity.class)
                                    .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK));
                } else {
                    //request not successful (like 400,401,403 etc)
                    //Handle errors
                }
            }

            @Override
            public void onFailure(Throwable t) {

            }
        });
    }
}