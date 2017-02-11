package me.evrooij.groceries.ui.fragments

import rx.subscriptions.CompositeSubscription
import android.support.v4.app.Fragment

/**
 * Author: eddy
 * Date: 11-2-17.
 */

open class RxBaseFragment() : Fragment() {

    protected var subscriptions = CompositeSubscription()

    override fun onResume() {
        super.onResume()
        subscriptions = CompositeSubscription()
    }

    override fun onPause() {
        super.onPause()
        if (!subscriptions.isUnsubscribed) {
            subscriptions.unsubscribe()
        }
        subscriptions.clear()
    }
}
