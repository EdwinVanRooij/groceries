@file:JvmName("Extensions")

package me.evrooij.groceries.util

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import com.bumptech.glide.Glide
import me.evrooij.groceries.R
import java.util.*

/**
 * Author: eddy
 * Date: 1-2-17.
 */
fun ViewGroup.inflate(layoutId: Int, attachToRoot: Boolean = false): View {
    return LayoutInflater.from(context).inflate(layoutId, this, attachToRoot)
}

// This method is required for when calling the method from java, it can't deal with optional parameters
// so we'll set default to false without any options to true here.
fun ViewGroup.inflate(layoutId: Int): View {
    return LayoutInflater.from(context).inflate(layoutId, this, false)
}

fun ImageView.loadImg(imageUrl: String) {
    if (imageUrl.isEmpty()) {
        Glide.with(context).load(R.mipmap.ic_launcher).into(this)
    } else {
        Glide.with(context).load(imageUrl).into(this)
    }
}

fun Context.toast(message: CharSequence) {
    Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
}

fun Long.getFriendlyTime(): String {
    val dateTime = Date(this * 1000)
    val sb = StringBuffer()
    val current = Calendar.getInstance().time
    var diffInSeconds = ((current.time - dateTime.time) / 1000).toInt()

    val sec = if (diffInSeconds >= 60) (diffInSeconds % 60) else diffInSeconds
    diffInSeconds = diffInSeconds / 60
    val min = if (diffInSeconds >= 60) (diffInSeconds % 60) else diffInSeconds
    diffInSeconds = diffInSeconds / 60
    val hrs = if (diffInSeconds >= 24) (diffInSeconds % 24) else diffInSeconds
    diffInSeconds = diffInSeconds / 24
    val days = if (diffInSeconds >= 30) (diffInSeconds % 30) else diffInSeconds
    diffInSeconds = diffInSeconds / 30
    val months = if (diffInSeconds >= 12) (diffInSeconds % 12) else diffInSeconds
    diffInSeconds = diffInSeconds / 12
    val years = diffInSeconds

    if (years > 0) {
        if (years == 1) {
            sb.append("a year")
        } else {
            sb.append("$years years")
        }
        if (years <= 6 && months > 0) {
            if (months == 1) {
                sb.append(" and a month")
            } else {
                sb.append(" and $months months")
            }
        }
    } else if (months > 0) {
        if (months == 1) {
            sb.append("a month")
        } else {
            sb.append("$months months")
        }
        if (months <= 6 && days > 0) {
            if (days == 1) {
                sb.append(" and a day")
            } else {
                sb.append(" and $days days")
            }
        }
    } else if (days > 0) {
        if (days == 1) {
            sb.append("a day")
        } else {
            sb.append("$days days")
        }
        if (days <= 3 && hrs > 0) {
            if (hrs == 1) {
                sb.append(" and an hour")
            } else {
                sb.append(" and $hrs hours")
            }
        }
    } else if (hrs > 0) {
        if (hrs == 1) {
            sb.append("an hour")
        } else {
            sb.append("$hrs hours")
        }
        if (min > 1) {
            sb.append(" and $min minutes")
        }
    } else if (min > 0) {
        if (min == 1) {
            sb.append("a minute")
        } else {
            sb.append("$min minutes")
        }
        if (sec > 1) {
            sb.append(" and $sec seconds")
        }
    } else {
        if (sec <= 1) {
            sb.append("about a second")
        } else {
            sb.append("about $sec seconds")
        }
    }

    sb.append(" ago")

    return sb.toString()
}
