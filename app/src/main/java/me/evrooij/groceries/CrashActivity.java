package me.evrooij.groceries;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;
import butterknife.BindView;
import butterknife.ButterKnife;
import me.evrooij.groceries.data.Account;
import me.evrooij.groceries.data.Feedback;
import me.evrooij.groceries.interfaces.ReturnBoolean;
import me.evrooij.groceries.rest.ClientInterface;
import me.evrooij.groceries.rest.ResponseMessage;
import me.evrooij.groceries.rest.ServiceGenerator;
import me.evrooij.groceries.util.Preferences;
import org.parceler.Parcels;

import static android.R.id.message;
import static me.evrooij.groceries.Config.KEY_ACCOUNT;
import static me.evrooij.groceries.Config.KEY_CRASH_REPORT;

public class CrashActivity extends AppCompatActivity {

    @BindView(R.id.textView)
    TextView textView;

    private String report;
    private Account thisAccount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crash);
        ButterKnife.bind(this);

        report = Parcels.unwrap(getIntent().getParcelableExtra(KEY_CRASH_REPORT));
        thisAccount = Parcels.unwrap(getIntent().getParcelableExtra(KEY_ACCOUNT));

        textView.setText(report);

        ReturnBoolean r = result -> {
            if (result) {
                new Thread(() -> {
                    try {
                        ResponseMessage responseMessage =
                                ServiceGenerator.createService(getApplicationContext(), ClientInterface.class)
                                        .reportFeedback(new Feedback(report, Feedback.Type.Bug, thisAccount))
                                        .execute()
                                        .body();
                        Preferences.removeAll(this);

                        runOnUiThread(() -> {
                            Toast.makeText(this, responseMessage.toString(), Toast.LENGTH_SHORT).show();
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
