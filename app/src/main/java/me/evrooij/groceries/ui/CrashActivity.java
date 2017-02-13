package me.evrooij.groceries.ui;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;
import android.widget.Toast;
import butterknife.BindView;
import butterknife.ButterKnife;
import me.evrooij.groceries.R;
import me.evrooij.groceries.interfaces.ReturnBoolean;
import me.evrooij.groceries.domain.*;
import me.evrooij.groceries.network.ApiService;
import me.evrooij.groceries.network.ClientInterface;
import me.evrooij.groceries.network.ResponseMessage;
import org.parceler.Parcels;

import static me.evrooij.groceries.util.Config.KEY_ACCOUNT;
import static me.evrooij.groceries.util.Config.KEY_CRASH_REPORT;

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
                                ApiService.createService(ClientInterface.class)
                                        .reportFeedback(new Feedback(report, Feedback.Type.Bug, thisAccount))
                                        .execute()
                                        .body();

                        runOnUiThread(() -> {
                            Toast.makeText(this, responseMessage.toString(), Toast.LENGTH_SHORT).show();
                            new Thread(() -> {
                                try {
                                    Thread.sleep(1000);
                                    System.exit(0);
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }
                            }
                            ).start();
                        });
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }).start();
            } else {
                System.exit(0);
            }
        };
        new ConfirmDialog(this, r).show(getResources().getString(R.string.send_feedback));
    }
}
