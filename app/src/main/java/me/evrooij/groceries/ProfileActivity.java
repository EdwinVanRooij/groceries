package me.evrooij.groceries;

import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.view.ViewCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.transition.Slide;
import android.view.MotionEvent;
import android.widget.TextView;
import android.widget.Toast;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import com.bumptech.glide.Glide;
import me.evrooij.groceries.data.Account;
import me.evrooij.groceries.data.UserManager;
import me.evrooij.groceries.rest.ResponseMessage;
import me.evrooij.groceries.util.SquareImageView;
import org.parceler.Parcels;

import static me.evrooij.groceries.Config.KEY_ACCOUNT;
import static me.evrooij.groceries.Config.KEY_SEARCHED_USER;

public class ProfileActivity extends AppCompatActivity {

    @BindView(R.id.image)
    SquareImageView imageView;
    @BindView(R.id.tvTitle)
    TextView tvTitle;
    @BindView(R.id.tvDescription)
    TextView tvDescription;

    private static final String EXTRA_IMAGE = "com.antonioleiva.materializeyourapp.extraImage";
    private CollapsingToolbarLayout collapsingToolbarLayout;

    private UserManager userManager;

    private Account thisAccount;
    private Account searchedUser;

    @SuppressWarnings("ConstantConditions")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initActivityTransitions();
        setContentView(R.layout.activity_profile);
        ButterKnife.bind(this);

        thisAccount = Parcels.unwrap(getIntent().getParcelableExtra(KEY_ACCOUNT));
        searchedUser = Parcels.unwrap(getIntent().getParcelableExtra(KEY_SEARCHED_USER));

        userManager = new UserManager(getApplicationContext());

        ViewCompat.setTransitionName(findViewById(R.id.app_bar_layout), EXTRA_IMAGE);
        supportPostponeEnterTransition();

        setSupportActionBar((Toolbar) findViewById(R.id.toolbar));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        collapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
        collapsingToolbarLayout.setExpandedTitleColor(getResources().getColor(android.R.color.transparent));
        collapsingToolbarLayout.setTitle(searchedUser.getUsername());

        tvTitle.setText(searchedUser.getUsername());
        tvDescription.setText(String.format("%s likes to take a shit while being naked...", searchedUser.getUsername()));

        Glide.with(this)
                .load("http://placekitten.com/300/400")
                .into(imageView);
    }

    @OnClick(R.id.btnAdd)
    public void onBtnAddClick() {
        new Thread(() -> {
            ResponseMessage result = userManager.addFriend(thisAccount.getId(), searchedUser);

            runOnUiThread(() -> Toast.makeText(this, String.format("Result: %s", result.toString()), Toast.LENGTH_SHORT).show());
        }).start();
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent motionEvent) {
        try {
            return super.dispatchTouchEvent(motionEvent);
        } catch (NullPointerException e) {
            return false;
        }
    }

    private void initActivityTransitions() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Slide transition = new Slide();
            transition.excludeTarget(android.R.id.statusBarBackground, true);
            getWindow().setEnterTransition(transition);
            getWindow().setReturnTransition(transition);
        }
    }
}
