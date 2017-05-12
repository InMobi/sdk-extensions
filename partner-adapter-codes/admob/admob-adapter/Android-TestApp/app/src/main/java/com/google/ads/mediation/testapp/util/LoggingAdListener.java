package com.google.ads.mediation.testapp.util;

import android.util.Log;
import android.widget.TextView;

import com.google.android.gms.ads.AdListener;

/**
 * The {@link LoggingAdListener} class is used log the callbacks from ads and display them to the
 * user.
 */
public class LoggingAdListener extends AdListener {

    /**
     * Text view used to display the logs.
     */
    private TextView mLogTextView;

    public LoggingAdListener(TextView logTextView) {
        this.mLogTextView = logTextView;
    }

    @Override
    public void onAdClosed() {
        super.onAdClosed();
        String log = "Ad closed.";
        Log.d(Constants.TAG, log);
        UIUtils.appendLogToTextView(mLogTextView, log);
    }

    @Override
    public void onAdFailedToLoad(int errorCode) {
        super.onAdFailedToLoad(errorCode);
        String log = "Ad failed to load with error: " + errorCode;
        Log.d(Constants.TAG, log);
        UIUtils.appendLogToTextView(mLogTextView, log);
    }

    @Override
    public void onAdLeftApplication() {
        super.onAdLeftApplication();
        String log = "Ad left application.";
        Log.d(Constants.TAG, log);
        UIUtils.appendLogToTextView(mLogTextView, log);
    }

    @Override
    public void onAdOpened() {
        super.onAdOpened();
        String log = "Ad opened.";
        Log.d(Constants.TAG, log);
        UIUtils.appendLogToTextView(mLogTextView, log);
    }

    @Override
    public void onAdLoaded() {
        super.onAdLoaded();
        String log = "Ad loaded.";
        Log.d(Constants.TAG, log);
        UIUtils.appendLogToTextView(mLogTextView, log);
    }
}
