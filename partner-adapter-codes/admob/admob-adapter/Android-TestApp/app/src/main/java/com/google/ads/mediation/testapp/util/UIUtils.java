// Copyright (C) 2016 Google, Inc.

package com.google.ads.mediation.testapp.util;

import android.text.TextUtils;
import android.widget.TextView;

import java.text.DateFormat;
import java.util.Date;

/**
 * The {@link UIUtils} class consists of helper methods.
 */
public class UIUtils {

    /**
     * This method appends the log to the text view and also includes a timestamp.
     *
     * @param textView That needs to be updated.
     * @param log      The new log to add to the text view.
     */
    public static void appendLogToTextView(TextView textView, String log) {
        String append = DateFormat.getDateTimeInstance().format(new Date()) + ": " + log;
        textView.append(TextUtils.isEmpty(textView.getText().toString()) ? append
                : "\n" + append);
    }
}
