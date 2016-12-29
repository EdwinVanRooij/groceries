package me.evrooij.groceries.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import me.evrooij.groceries.R;
import me.evrooij.groceries.domain.Product;

import java.util.ArrayList;

/**
 * Author: eddy
 * Date: 20-11-16.
 */

public class ProductAdapter extends ArrayAdapter<Product> {
    public ProductAdapter(Context context, ArrayList<Product> products) {
        super(context, 0, products);
    }

    @Override
    public View getView(int position, View convertView, @NonNull ViewGroup parent) {
        // Get the data item for this position
        Product product = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_product, parent, false);
        }
        // Lookup view for data population
        TextView tvName = (TextView) convertView.findViewById(R.id.tvName);
        TextView tvAmount = (TextView) convertView.findViewById(R.id.tvAmount);
        TextView tvComment = (TextView) convertView.findViewById(R.id.tvComment);
        TextView tvOwner = (TextView) convertView.findViewById(R.id.tvOwner);
        // Populate the data into the template view using the data object
        if (product != null) {
            tvName.setText(product.getName());
            tvAmount.setText(String.valueOf(product.getAmount()));
            tvComment.setText(product.getComment());
            tvOwner.setText(product.getOwner());
        }

        // Return the completed view to render on screen
        return convertView;
    }
}

