package me.evrooij.groceries.ui.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.text.Html;
import android.text.Spanned;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.bumptech.glide.Glide;
import me.evrooij.groceries.R;
import me.evrooij.groceries.models.Product;

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
        ImageView ivPhoto = (ImageView) convertView.findViewById(R.id.ivPhoto);
        TextView tvName = (TextView) convertView.findViewById(R.id.tvName);
        TextView tvComment = (TextView) convertView.findViewById(R.id.tvComment);
        TextView tvOwner = (TextView) convertView.findViewById(R.id.tvOwner);
        // Populate the data into the template view using the data object
        if (product != null) {
            if (product.getImageUrl() != null) {
                Glide.with(getContext())
                        .load(product.getImageUrl())
                        .into(ivPhoto);
            }
            if (product.getAmount() > 1) {
                tvName.setText(fromHtml(String.format("<b>(%s)</b> %s", product.getAmount(), product.getName())));
            } else {
                tvName.setText(product.getName());
            }
            tvComment.setText(product.getComment());
            tvOwner.setText(product.getOwner().getUsername());
        }

        // Return the completed view to render on screen
        return convertView;
    }

    @SuppressWarnings("deprecation")
    public static Spanned fromHtml(String html) {
        Spanned result;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
            result = Html.fromHtml(html, Html.FROM_HTML_MODE_LEGACY);
        } else {
            result = Html.fromHtml(html);
        }
        return result;
    }
}

