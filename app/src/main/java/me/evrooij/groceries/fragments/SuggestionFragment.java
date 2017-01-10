package me.evrooij.groceries.fragments;


import android.os.Bundle;
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
public class SuggestionFragment extends Fragment {

    @BindView(R.id.suggestion_edittext)
    EditText etMessage;

    private Unbinder unbinder;
    private Account thisAccount;
    private FeedbackManager feedbackManager;

    public SuggestionFragment() {
        // Required empty public constructor
    }

    @OnClick(R.id.suggestion_button_send)
    public void onButtonSendClick() {
        new Thread(() -> {
            ResponseMessage message = feedbackManager.reportFeedback(new Feedback(etMessage.getText().toString(), Feedback.Type.Suggestion, thisAccount));

            getActivity().runOnUiThread(() -> Toast.makeText(getActivity(), message.toString(), Toast.LENGTH_SHORT).show());
        }).start();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_suggestion, container, false);
        unbinder = ButterKnife.bind(this, view);
        ((MainActivity) getActivity()).setActionBarTitle(getString(R.string.title_suggestions));

        thisAccount = Parcels.unwrap(getArguments().getParcelable(KEY_ACCOUNT));
        feedbackManager = new FeedbackManager(getActivity().getApplicationContext());

        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

}
