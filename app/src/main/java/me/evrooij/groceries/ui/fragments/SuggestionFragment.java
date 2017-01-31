package me.evrooij.groceries.ui.fragments;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;
import butterknife.BindView;
import butterknife.OnClick;
import me.evrooij.groceries.R;
import me.evrooij.groceries.models.Feedback;
import me.evrooij.groceries.network.ClientInterface;
import me.evrooij.groceries.network.ResponseMessage;
import me.evrooij.groceries.network.ApiService;

import java.io.IOException;

/**
 * A simple {@link Fragment} subclass.
 */
public class SuggestionFragment extends MainFragment {

    @BindView(R.id.suggestion_edittext)
    EditText etMessage;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_suggestion, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mainActivity.setActionBarTitle(getString(R.string.title_suggestions));
    }

    @OnClick(R.id.suggestion_button_send)
    public void onButtonSendClick() {
        mainActivity.executeRunnable(() -> {
                    try {
                        ResponseMessage responseMessage =
                                ApiService.createService( ClientInterface.class)
                                        .reportFeedback(new Feedback(etMessage.getText().toString(), Feedback.Type.Suggestion, mainActivity.getThisAccount()))
                                        .execute()
                                        .body();
                        getActivity().runOnUiThread(() -> Toast.makeText(getActivity(), responseMessage.toString(), Toast.LENGTH_SHORT).show());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
        );
    }
}
