// Copyright (C) 2016 Google, Inc.

package com.google.ads.mediation.testapp;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ScrollView;
import android.widget.TextView;

import com.google.ads.mediation.inmobi.InMobiAdapter;
import com.google.ads.mediation.inmobi.InMobiNetworkKeys;
import com.google.ads.mediation.inmobi.InMobiNetworkValues;
import com.google.ads.mediation.testapp.util.Constants;
import com.google.ads.mediation.testapp.util.UIUtils;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.reward.RewardItem;
import com.google.android.gms.ads.reward.RewardedVideoAd;
import com.google.android.gms.ads.reward.RewardedVideoAdListener;

import java.text.SimpleDateFormat;

/**
 * The {@link TestRewardedVideoAdFragment} class is used to load rewarded video ads and test their
 * callbacks.
 */
public class TestRewardedVideoAdFragment extends Fragment implements RewardedVideoAdListener {
    private static final String TAG = TestRewardedVideoAdFragment.class.getSimpleName();

    /**
     * Key to get logs for {@link TestRewardedVideoAdFragment} from shared preferences.
     */
    public static final String KEY_REWARDED_VIDEO_AD_LOGS = "rewarded_video_ad_logs";

    /**
     * The rewarded video ad instance used to load and show Rewarded Video ads.
     */
    private RewardedVideoAd mRewardedVideoAd;

    /**
     * Container view for {@link #mLogTextView}.
     */
    private ScrollView mScrollView;

    /**
     * A text view to display logs.
     */
    private TextView mLogTextView;

    /**
     * Button to load rewarded video ads.
     */
    private Button mLoadVideoAdButton;

    /**
     * Button to display rewarded video ads.
     */
    private Button mShowVideoAdButton;

    /**
     * Flag to keep track of weather or not {@link #mRewardedVideoAd} is loading.
     */
    private boolean mIsRewardedVideoLoading;

    public TestRewardedVideoAdFragment() {
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
        ((TextView) rootView.findViewById(R.id.title_interstitial_ad))
                .setText(getString(R.string.title_rewarded_video_ad_fragment));
        mScrollView = (ScrollView) rootView.findViewById(R.id.interstitial_ad_scroll_view);
        mLoadVideoAdButton = (Button) rootView.findViewById(R.id.load_ad_button);
        mShowVideoAdButton = (Button) rootView.findViewById(R.id.show_ad_button);
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
        mLogTextView.setText(preferences.getString(KEY_REWARDED_VIDEO_AD_LOGS, ""));

        return rootView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        mRewardedVideoAd = MobileAds.getRewardedVideoAdInstance(getActivity());
        mRewardedVideoAd.setUserId(Constants.ADMOB_REWARDED_VIDEO_USER_ID);
        mRewardedVideoAd.setRewardedVideoAdListener(this);

        mLoadVideoAdButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                view.setEnabled(false);
                loadRewardedVideoAd();
            }
        });

        mShowVideoAdButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showRewardedVideo();
            }
        });

        if (mRewardedVideoAd.isLoaded() || mIsRewardedVideoLoading) {
            mLoadVideoAdButton.setEnabled(false);
        }
    }

    private void loadRewardedVideoAd() {
        if (!mIsRewardedVideoLoading && !mRewardedVideoAd.isLoaded()) {
            mIsRewardedVideoLoading = true;
            AdRequest.Builder b = new AdRequest.Builder();
            b.addKeyword("abdef");
            Bundle extraParams = new Bundle();
            extraParams.putString(InMobiNetworkKeys.AGE, "");
            extraParams.putString(InMobiNetworkKeys.AGE_GROUP, InMobiNetworkValues.BETWEEN_21_AND_24);
            extraParams.putString(InMobiNetworkKeys.AREA_CODE, "");
            extraParams.putString(InMobiNetworkKeys.POSTAL_CODE, "");
            extraParams.putString(InMobiNetworkKeys.INCOME, "");
            extraParams.putString(InMobiNetworkKeys.EDUCATION, InMobiNetworkValues.EDUCATION_COLLEGEORGRADUATE);
            extraParams.putString(InMobiNetworkKeys.ETHNICITY, InMobiNetworkValues.ETHNICITY_ASIAN);
            extraParams.putString(InMobiNetworkKeys.HOUSEHOLD_INCOME, InMobiNetworkValues.BETWEEN_USD_75K_AND_100K);
            extraParams.putString(InMobiNetworkKeys.INTERESTS, "Cycling");
            extraParams.putString(InMobiNetworkKeys.CITY, "bengaluru");
            extraParams.putString(InMobiNetworkKeys.COUNTRY, "idia");
            extraParams.putString(InMobiNetworkKeys.STATE, "kar");
            extraParams.putString(InMobiNetworkKeys.ImIdType_LOGIN, "login");
            extraParams.putString(InMobiNetworkKeys.ImIdType_SESSION, "session");
            SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy");
            b.addNetworkExtrasBundle(InMobiAdapter.class,extraParams);
            AdRequest adRequest = b.build();
            mRewardedVideoAd.loadAd(Constants.ADMOB_REWARDED_VIDEO_AD_UNIT_ID, adRequest);
        }
    }

    private void showRewardedVideo() {
        if (mRewardedVideoAd.isLoaded()) {
            mRewardedVideoAd.show();
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
                editor.remove(KEY_REWARDED_VIDEO_AD_LOGS).commit();
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
        editor.putString(KEY_REWARDED_VIDEO_AD_LOGS, mLogTextView.getText().toString()).apply();
    }

    @Override
    public void onRewardedVideoAdLoaded() {
        mIsRewardedVideoLoading = false;
        String log = "Ad loaded";
        Log.d(TAG, log);
        UIUtils.appendLogToTextView(mLogTextView, log);
    }

    @Override
    public void onRewardedVideoAdOpened() {
        String log = "Ad opened";
        Log.d(TAG, log);
        UIUtils.appendLogToTextView(mLogTextView, log);
    }

    @Override
    public void onRewardedVideoStarted() {
        String log = "Video started";
        Log.d(TAG, log);
        UIUtils.appendLogToTextView(mLogTextView, log);
    }

    @Override
    public void onRewardedVideoAdClosed() {
        mLoadVideoAdButton.setEnabled(true);
        String log = "Ad closed";
        Log.d(TAG, log);
        UIUtils.appendLogToTextView(mLogTextView, log);
    }

    @Override
    public void onRewarded(RewardItem rewardItem) {
        String log = String.format("Rewarded with reward of type: %s & with amount: %d",
                rewardItem.getType(),
                rewardItem.getAmount());
        Log.d(TAG, log);
        UIUtils.appendLogToTextView(mLogTextView, log);
    }

    @Override
    public void onRewardedVideoAdLeftApplication() {
        String log = "Ad left application";
        Log.d(TAG, log);
        UIUtils.appendLogToTextView(mLogTextView, log);
    }

    @Override
    public void onRewardedVideoAdFailedToLoad(int errorCode) {
        mIsRewardedVideoLoading = false;
        mLoadVideoAdButton.setEnabled(true);
        String log = "Ad failed to load with error code: " + errorCode;
        Log.d(TAG, log);
        UIUtils.appendLogToTextView(mLogTextView, log);
    }
}
