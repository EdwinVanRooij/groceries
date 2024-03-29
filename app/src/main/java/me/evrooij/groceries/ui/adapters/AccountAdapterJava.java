package me.evrooij.groceries.ui.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import me.evrooij.groceries.R;
import me.evrooij.groceries.domain.Account;
import me.evrooij.groceries.util.Extensions;

import java.util.ArrayList;

/**
 * Author: eddy
 * Date: 13-2-17.
 */

public class AccountAdapterJava extends ArrayAdapter<Account> {
    public AccountAdapterJava(Context context, ArrayList<Account> users) {
        super(context, 0, users);
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, @NonNull ViewGroup parent) {
        // Get the data item for this position
        Account user = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = Extensions.inflate(parent, R.layout.item_user);
        }
        // Lookup view for data population
        TextView tvName = (TextView) convertView.findViewById(R.id.tvName);
        // Populate the data into the template view using the data object
        if (user != null) {
            tvName.setText(user.getUsername());
        }

        // Return the completed view to render on screen
        return convertView;
    }
}
