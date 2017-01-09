package me.evrooij.groceries.data;

import android.content.Context;
import me.evrooij.groceries.rest.ClientInterface;
import me.evrooij.groceries.rest.ResponseMessage;
import me.evrooij.groceries.rest.ServiceGenerator;
import retrofit2.Call;
import retrofit2.Response;

import java.io.IOException;

/**
 * Author: eddy
 * Date: 15-12-16.
 */

public class FeedbackManager {

    private Context context;

    public FeedbackManager(Context context) {
        this.context = context;

    }

    public ResponseMessage reportFeedback(Feedback feedback) {
        // Create a rest adapter
        ClientInterface client = ServiceGenerator.createService(context, ClientInterface.class);

        // Fetch and print a list of the contributors to this library.
        Call<ResponseMessage> call = client.reportFeedback(feedback);

        // Execute the call
        Response<ResponseMessage> response;
        try {
            response = call.execute();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }

        return response.body();
    }
}
