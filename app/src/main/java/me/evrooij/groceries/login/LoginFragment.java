package me.evrooij.groceries.login;


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
import android.widget.Toast;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import me.evrooij.groceries.MainActivity;
import me.evrooij.groceries.R;
import me.evrooij.groceries.data.Account;
import me.evrooij.groceries.data.AccountPrefs;
import me.evrooij.groceries.data.LoginManager;
import org.parceler.Parcels;

import static me.evrooij.groceries.Constants.KEY_ACCOUNT;


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

    private LoginManager loginManager;

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

        loginManager = new LoginManager();

        return view;
    }

    @OnClick(R.id.btnLogin)
    public void onLoginClick(View view) {
        String username = etUsername.getText().toString().trim();
        String password = etPassword.getText().toString().trim();

        new Thread(() -> {
            Account a = loginManager.login(username, password);
            if (a == null) {
                getActivity().runOnUiThread(() -> Toast.makeText(getActivity(), "Could not login.\nPlease check your credentials.", Toast.LENGTH_SHORT).show());
                return;
            }

            getActivity().runOnUiThread(() -> Toast.makeText(getActivity(), String.format("Login was successful.\rWelcome, %s", a.getUsername()), Toast.LENGTH_SHORT).show());

            AccountPrefs accountPrefs = AccountPrefs.get(getActivity());
            //noinspection ConstantConditions
            accountPrefs.edit().putId(a.getId()).putUsername(a.getUsername()).putEmail(a.getEmail()).putPassword(a.getPassword()).apply();

            Intent i = new Intent(getActivity(), MainActivity.class);
            i.putExtra(KEY_ACCOUNT, Parcels.wrap(a));
            i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(i);
        }).start();
    }
}