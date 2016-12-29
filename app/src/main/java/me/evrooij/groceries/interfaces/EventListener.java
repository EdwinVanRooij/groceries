package me.evrooij.groceries.interfaces;

import android.view.View;

/**
 * Author: eddy
 * Date: 29-12-16.
 */

public interface EventListener {
    void onItemRemoved(int position);

    void onItemViewClicked(View v);
}
