package me.evrooij.groceries.fragments;


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
import me.evrooij.groceries.interfaces.ContainerActivity;
import me.evrooij.groceries.rest.ResponseMessage;
import org.parceler.Parcels;

import static me.evrooij.groceries.Config.KEY_ACCOUNT;

/**
 * A simple {@link Fragment} subclass.
 */
public class SuggestionFragment extends MainFragment {

    @BindView(R.id.suggestion_edittext)
    EditText etMessage;

    private FeedbackManager feedbackManager;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_suggestion, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mainActivity.setActionBarTitle(getString(R.string.title_suggestions));

        feedbackManager = new FeedbackManager(getContext());
    }

    @OnClick(R.id.suggestion_button_send)
    public void onButtonSendClick() {
        mainActivity.executeRunnable(() -> {
                    ResponseMessage message = feedbackManager.reportFeedback(new Feedback(etMessage.getText().toString(), Feedback.Type.Suggestion, mainActivity.getThisAccount()));
                    getActivity().runOnUiThread(() -> Toast.makeText(getActivity(), message.toString(), Toast.LENGTH_SHORT).show());
                }
        );
    }
}
