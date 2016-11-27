package me.evrooij.groceries.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import me.evrooij.groceries.R;
import me.evrooij.groceries.domain.GroceryList;

import java.util.ArrayList;

/**
 * Created by eddy on 20-11-16.
 */

public class GroceryListAdapter extends ArrayAdapter<GroceryList> {
    public GroceryListAdapter(Context context, ArrayList<GroceryList> users) {
        super(context, 0, users);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        GroceryList user = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_grocerylist, parent, false);
        }
        // Lookup view for data population
        TextView tvTitle = (TextView) convertView.findViewById(R.id.tvTitle);
        TextView tvDescription = (TextView) convertView.findViewById(R.id.tvDescription);
        // Populate the data into the template view using the data object
        if (user != null) {
            tvTitle.setText(user.getName());
            tvDescription.setText(user.getOwner());
        }
        // Return the completed view to render on screen
        return convertView;
    }
}

