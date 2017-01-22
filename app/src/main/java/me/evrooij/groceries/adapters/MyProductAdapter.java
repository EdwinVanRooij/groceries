package me.evrooij.groceries.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.bumptech.glide.Glide;
import com.mikepenz.google_material_typeface_library.GoogleMaterial;
import com.mikepenz.iconics.IconicsDrawable;
import me.evrooij.groceries.R;
import me.evrooij.groceries.data.Product;

import java.util.ArrayList;

/**
 * Author: eddy
 * Date: 20-11-16.
 */

public class MyProductAdapter extends ArrayAdapter<Product> {
    private View.OnClickListener listener;
    private boolean plusIfEmpty;

    public MyProductAdapter(Context context, ArrayList<Product> products, View.OnClickListener listener, boolean plusIfEmpty) {
        super(context, 0, products);
        this.listener = listener;
        this.plusIfEmpty = plusIfEmpty;
    }

    @Override
    public View getView(int position, View convertView, @NonNull ViewGroup parent) {
        // Get the data item for this position
        Product product = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_my_product, parent, false);
        }
        // Lookup view for data population
        ImageView ivPhoto = (ImageView) convertView.findViewById(R.id.ivPhoto);
        TextView tvName = (TextView) convertView.findViewById(R.id.tvName);
        TextView tvAmount = (TextView) convertView.findViewById(R.id.tvAmount);
        TextView tvComment = (TextView) convertView.findViewById(R.id.tvComment);
        // Populate the data into the template view using the data object
        if (product != null) {
            if (product.getImageUrl() != null) {
                Glide.with(getContext())
                        .load(product.getImageUrl())
                        .into(ivPhoto);
            } else {
                if (plusIfEmpty) {
                    ivPhoto.setImageDrawable(new IconicsDrawable(getContext(), GoogleMaterial.Icon.gmd_add).color(getContext().getResources().getColor(R.color.primary_dark)).sizeDp(24));
                }
            }
            tvName.setText(product.getName());
            tvAmount.setText(String.valueOf(product.getAmount()));
            tvComment.setText(product.getComment());
        }

        ivPhoto.setOnClickListener(v -> {
            if (listener != null) {
                listener.onClick(v);
            }
        });

        // Return the completed view to render on screen
        return convertView;
    }
}

