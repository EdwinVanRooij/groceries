package me.evrooij.groceries.ui;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import me.evrooij.groceries.models.Account;
import org.parceler.Parcels;

import java.io.PrintWriter;
import java.io.StringWriter;

import static me.evrooij.groceries.util.Config.KEY_ACCOUNT;
import static me.evrooij.groceries.util.Config.KEY_CRASH_REPORT;

/**
 * Author: eddy
 * Date: 20-1-17.
 */

public class ExceptionHandler implements Thread.UncaughtExceptionHandler {

    private final Context context;
    private final Account account;

    public ExceptionHandler(Activity context, Account account) {
        this.context = context;
        this.account = account;
    }

    public void uncaughtException(Thread thread, Throwable exception) {
        StringWriter stackTrace = new StringWriter();
        exception.printStackTrace(new PrintWriter(stackTrace));

        String lineSeparator = "\n";
        String errorReport = String.format("************ CAUSE OF ERROR ************%s%s", lineSeparator, lineSeparator) +
                stackTrace.toString() +
                String.format("%s************ DEVICE INFORMATION ***********%s", lineSeparator, lineSeparator) +
                String.format("Brand: %s%s", Build.BRAND, lineSeparator) +
                String.format("Device: %s%s", Build.DEVICE, lineSeparator) +
                String.format("Model: %s%s", Build.MODEL, lineSeparator) +
                String.format("Id: %s%s", Build.ID, lineSeparator) +
                String.format("Product: %s%s", Build.PRODUCT, lineSeparator) +
                String.format("%s************ FIRMWARE ************%s", lineSeparator, lineSeparator) +
                String.format("SDK: %s%s", Build.VERSION.SDK, lineSeparator) +
                String.format("Release: %s%s", Build.VERSION.RELEASE, lineSeparator) +
                String.format("Incremental: %s%s", Build.VERSION.INCREMENTAL, lineSeparator);

        Intent intent = new Intent(context, CrashActivity.class);
        intent.putExtra(KEY_CRASH_REPORT, Parcels.wrap(errorReport));
        intent.putExtra(KEY_ACCOUNT, Parcels.wrap(account));
        context.startActivity(intent);

        android.os.Process.killProcess(android.os.Process.myPid());
        System.exit(10);
    }
}
