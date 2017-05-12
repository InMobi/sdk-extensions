// Copyright (C) 2016 Google, Inc.

package com.google.ads.mediation.testapp;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.google.ads.mediation.testapp.util.Constants;
import com.google.ads.mediation.testapp.util.LoggingAdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;

/**
 * The {@link TestCustomAdSizeFragment} class is used to load custom sized ads and test their
 * callbacks.
 */
public class TestCustomAdSizeFragment extends Fragment {

    /**
     * Key to get logs for {@link TestCustomAdSizeFragment} from shared preferences.
     */
    public static final String KEY_CUSTOM_BANNER_AD_LOGS = "custom_banner_ad_logs";

    /**
     * The ad view used to load and display banner ads.
     */
    private AdView mAdView;

    /**
     * A button to load ads.
     */
    private Button mLoadAdButton;

    /**
     * Parent view for {@link TestCustomAdSizeFragment} to make the fragment scrollable.
     */
    private ScrollView mScrollView;

    /**
     * Container for {@link #mAdView} to be placed in.
     */
    private FrameLayout mAdFrameLayout;

    /**
     * A text view to display logs.
     */
    private TextView mLogTextView;

    /**
     * Edit text to get width needed to load {@link #mAdView}.
     */
    private EditText mAdWidthEditText;

    /**
     * Edit text to get height needed to load {@link #mAdView}.
     */
    private EditText mAdHeightEditText;

    public TestCustomAdSizeFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_test_custom_ad_size, container, false);
        mLoadAdButton = (Button) rootView.findViewById(R.id.load_custom_size_ad_button);
        mScrollView = (ScrollView) rootView.findViewById(R.id.test_custom_ad_scroll_view);
        mAdFrameLayout = (FrameLayout) rootView.findViewById(R.id.ad_content_frame);
        mLogTextView = (TextView) rootView.findViewById(R.id.log_text_view);
        mAdWidthEditText = (EditText) rootView.findViewById(R.id.custom_size_width_edit_text);
        mAdHeightEditText = (EditText) rootView.findViewById(R.id.custom_size_height_edit_text);

        mLogTextView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence sequence, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence sequence, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
                // Scroll to the bottom of the view when new log is added.
                mScrollView.fullScroll(ScrollView.FOCUS_DOWN);
            }
        });

        SharedPreferences preferences = getActivity()
                .getSharedPreferences(Constants.KEY_LOGS_PREFS_FILE, Context.MODE_PRIVATE);
        mLogTextView.setText(preferences.getString(KEY_CUSTOM_BANNER_AD_LOGS, ""));

        return rootView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        mLoadAdButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (TextUtils.isEmpty(mAdWidthEditText.getText())
                        || TextUtils.isEmpty(mAdHeightEditText.getText())) {
                    Snackbar.make(getView(),
                            "Both width and height are needed.",
                            Snackbar.LENGTH_SHORT).show();
                    return;
                }

                if (mAdView != null) {
                    mAdFrameLayout.removeView(mAdView);
                    mAdView.destroy();
                }

                mAdView = new AdView(getActivity());
                mAdView.setAdUnitId(Constants.ADMOB_BANNER_AD_UNIT_ID);
                mAdFrameLayout.addView(mAdView);
                AdSize adSize = new AdSize(Integer.parseInt(mAdWidthEditText.getText().toString()),
                        Integer.parseInt(mAdHeightEditText.getText().toString()));
                mAdView.setAdSize(adSize);

                mAdView.setAdListener(new LoggingAdListener(mLogTextView));

                mAdView.loadAd(new AdRequest.Builder().build());
            }
        });
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_test_ad_fragment, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_clear_logs:
                mLogTextView.setText("");
                SharedPreferences.Editor editor = getActivity()
                        .getSharedPreferences(Constants.KEY_LOGS_PREFS_FILE, Context.MODE_PRIVATE)
                        .edit();
                editor.remove(KEY_CUSTOM_BANNER_AD_LOGS).commit();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        SharedPreferences.Editor editor = getActivity()
                .getSharedPreferences(Constants.KEY_LOGS_PREFS_FILE, Context.MODE_PRIVATE)
                .edit();
        editor.putString(KEY_CUSTOM_BANNER_AD_LOGS, mLogTextView.getText().toString()).commit();
    }
}
