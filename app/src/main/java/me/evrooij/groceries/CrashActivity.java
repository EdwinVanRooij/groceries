package me.evrooij.groceries;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import butterknife.BindView;
import butterknife.ButterKnife;
import me.evrooij.groceries.data.Account;
import me.evrooij.groceries.data.Feedback;
import me.evrooij.groceries.data.FeedbackManager;
import me.evrooij.groceries.rest.ResponseMessage;
import me.evrooij.groceries.util.Preferences;
import org.parceler.Parcels;

import static java.security.AccessController.getContext;
import static me.evrooij.groceries.Config.KEY_ACCOUNT;
import static me.evrooij.groceries.Config.KEY_CRASH_REPORT;
import static me.evrooij.groceries.R.id.textView;

public class CrashActivity extends AppCompatActivity {

    @BindView(R.id.textView)
    TextView textView;

    private String report;
    private Account thisAccount;
    private FeedbackManager feedbackManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crash);
        ButterKnife.bind(this);

        report = Parcels.unwrap(getIntent().getParcelableExtra(KEY_CRASH_REPORT));
        thisAccount = Parcels.unwrap(getIntent().getParcelableExtra(KEY_ACCOUNT));
        feedbackManager = new FeedbackManager(this);

        textView.setText(report);

        ReturnBoolean r = result -> {
            if (result) {
                new Thread(() -> {
                    try {
                        Feedback feedback = new Feedback(report, Feedback.Type.Bug, thisAccount);
                        Preferences.removeAll(this);
                        ResponseMessage message = feedbackManager.reportFeedback(feedback);

                        runOnUiThread(() -> {
                            Toast.makeText(this, message.toString(), Toast.LENGTH_SHORT).show();
                            Preferences.removeAll(this);
                            System.exit(0);
                        });
                    } catch (Exception e) {
                        Preferences.removeAll(this);
                        e.printStackTrace();
                    }
                }).start();
            } else {
                Preferences.removeAll(this);
                System.exit(0);
            }
        };
        new ConfirmDialog(this, r).show(getResources().getString(R.string.send_feedback));
    }
}
