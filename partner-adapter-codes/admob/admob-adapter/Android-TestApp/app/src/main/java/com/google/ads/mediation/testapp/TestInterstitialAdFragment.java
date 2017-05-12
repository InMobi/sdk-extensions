// Copyright (C) 2016 Google, Inc.

package com.google.ads.mediation.testapp;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ScrollView;
import android.widget.TextView;

import com.google.ads.mediation.testapp.util.Constants;
import com.google.ads.mediation.testapp.util.LoggingAdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;

/**
 * The {@link TestInterstitialAdFragment} class is used to load interstitial ads and test their
 * callbacks.
 */
public class TestInterstitialAdFragment extends Fragment {

    /**
     * Ket to get logs for {@link TestInterstitialAdFragment} from shared preferences.
     */
    public static final String KEY_INTERSTITIAL_AD_LOGS = "interstitial_ad_logs";

    /**
     * The interstitial ad used to load and show ads.
     */
    private InterstitialAd mInterstitialAd;

    /**
     * A button used to load {@link #mInterstitialAd}.
     */
    private Button mLoadAdButton;

    /**
     * A button used to show {@link #mInterstitialAd}.
     */
    private Button mShowAdButton;

    /**
     * The container view for {@link #mLogTextView}.
     */
    private ScrollView mScrollView;

    /**
     * A text view to display logs.
     */
    private TextView mLogTextView;


    public TestInterstitialAdFragment() {
        // Required empty public constructor.
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_test_interstitial_ad, container, false);
        mLoadAdButton = (Button) rootView.findViewById(R.id.load_ad_button);
        mShowAdButton = (Button) rootView.findViewById(R.id.show_ad_button);
        mScrollView = (ScrollView) rootView.findViewById(R.id.interstitial_ad_scroll_view);
        mLogTextView = (TextView) rootView.findViewById(R.id.log_text_view);
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
        mLogTextView.setText(preferences.getString(KEY_INTERSTITIAL_AD_LOGS, ""));

        return rootView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        // Create the InterstitialAd and set the adUnitId.
        mInterstitialAd = new InterstitialAd(getActivity());
        // Defined in res/values/strings.xml
        mInterstitialAd.setAdUnitId(Constants.ADMOB_INTERSTITIAL_AD_UNIT_ID);

        mInterstitialAd.setAdListener(new LoggingAdListener(mLogTextView) {
            @Override
            public void onAdClosed() {
                mLoadAdButton.setEnabled(true);
                super.onAdClosed();
            }

            @Override
            public void onAdFailedToLoad(int errorCode) {
                mLoadAdButton.setEnabled(true);
                super.onAdFailedToLoad(errorCode);
            }
        });

        mLoadAdButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!mInterstitialAd.isLoading() && !mInterstitialAd.isLoaded()) {
                    mInterstitialAd.loadAd(new AdRequest.Builder().build());
                    view.setEnabled(false);
                }
            }
        });

        mShowAdButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showInterstitial();
            }
        });
    }

    /**
     * This method will show the {@link #mInterstitialAd} if it is loaded else snack bar is shown
     * with the error.
     */
    private void showInterstitial() {
        // Show the ad if it's ready. Otherwise snack.
        if (mInterstitialAd != null && mInterstitialAd.isLoaded()) {
            mInterstitialAd.show();
        } else {
            Snackbar.make(getView(), "Ad did not load", Snackbar.LENGTH_SHORT).show();
        }
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
                editor.remove(KEY_INTERSTITIAL_AD_LOGS).commit();
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
        editor.putString(KEY_INTERSTITIAL_AD_LOGS, mLogTextView.getText().toString()).commit();
    }
}
