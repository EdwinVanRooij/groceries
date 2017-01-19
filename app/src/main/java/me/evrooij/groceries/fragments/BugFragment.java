package me.evrooij.groceries.fragments;


import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import me.evrooij.groceries.MainActivity;
import me.evrooij.groceries.R;
import me.evrooij.groceries.data.Account;
import me.evrooij.groceries.data.Feedback;
import me.evrooij.groceries.data.FeedbackManager;
import me.evrooij.groceries.rest.ResponseMessage;
import org.parceler.Parcels;

import static me.evrooij.groceries.Config.KEY_ACCOUNT;

/**
 * A simple {@link Fragment} subclass.
 */
public class BugFragment extends Fragment {

    @BindView(R.id.bug_edittext_when)
    EditText etWhen;
    @BindView(R.id.bug_edittext_what)
    EditText etWhat;

    private Unbinder unbinder;
    private Account thisAccount;
    private FeedbackManager feedbackManager;

    private MainActivity mainActivity;

    public BugFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_bug, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        unbinder = ButterKnife.bind(this, view);

        mainActivity = ((MainActivity) getActivity());
        mainActivity.setActionBarTitle(getString(R.string.title_bugs));
        thisAccount = Parcels.unwrap(getArguments().getParcelable(KEY_ACCOUNT));
        feedbackManager = new FeedbackManager(getContext());
    }

    @OnClick(R.id.bug_button_send)
    public void onButtonSendClick() {
        mainActivity.executeRunnable(() -> {
            String message = String.format("When: %s\nWhat: %s", etWhen.getText().toString(), etWhat.getText().toString());
            ResponseMessage responseMessage = feedbackManager.reportFeedback(new Feedback(message, Feedback.Type.Bug, thisAccount));

            mainActivity.runOnUiThread(() -> {
                Toast.makeText(getActivity(), responseMessage.toString(), Toast.LENGTH_SHORT).show();
            });
        });
    }

    @Override
    public void onDestroyView() {
        unbinder.unbind();
        super.onDestroyView();
    }
}
