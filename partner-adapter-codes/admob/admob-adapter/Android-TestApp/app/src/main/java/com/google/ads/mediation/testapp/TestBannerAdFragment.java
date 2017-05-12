// Copyright (C) 2016 Google, Inc.

package com.google.ads.mediation.testapp;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.ads.mediation.testapp.util.Constants;
import com.google.ads.mediation.testapp.util.LoggingAdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;

/**
 * The {@link TestBannerAdFragment} class is used to load standard {@link AdSize} sizes and
 * test their callbacks.
 */
public class TestBannerAdFragment extends Fragment {

    /**
     * Key to get logs for {@link TestBannerAdFragment} from shared preferences.
     */
    public static final String KEY_BANNER_AD_LOGS = "banner_ad_logs";

    /**
     * The ad view to load and show banner ads.
     */
    private AdView mAdView;

    /**
     * Parent view for {@link TestBannerAdFragment} to make the fragment scrollable.
     */
    private ScrollView mScrollView;

    /**
     * Container view for {@link #mAdView}.
     */
    private FrameLayout mFrameLayout;

    /**
     * A button to load ads.
     */
    private Button mLoadAdButton;

    /**
     * Spinner to show different sizes of banner ads.
     */
    private Spinner mAdSizesSpinner;

    /**
     * A text view to display logs.
     */
    private TextView mLogTextView;

    public TestBannerAdFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment.
        View rootView = inflater.inflate(R.layout.fragment_test_banner_ad, container, false);
        mScrollView = (ScrollView) rootView.findViewById(R.id.banner_ad_scroll_view);
        mFrameLayout = (FrameLayout) rootView.findViewById(R.id.ad_content_frame);
        mLogTextView = (TextView) rootView.findViewById(R.id.log_text_view);
        mLoadAdButton = (Button) rootView.findViewById(R.id.load_banner_ad_button);
        mAdSizesSpinner = (Spinner) rootView.findViewById(R.id.banner_sizes_spinner);

        ArrayAdapter<CharSequence> adapter = new ArrayAdapter<CharSequence>(rootView.getContext(),
                android.R.layout.simple_spinner_dropdown_item, getResources().getStringArray(R
                .array.banner_ad_sizes));
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mAdSizesSpinner.setAdapter(adapter);

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
        mLogTextView.setText(preferences.getString(KEY_BANNER_AD_LOGS, ""));

        return rootView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        mLoadAdButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mAdView != null) {
                    mFrameLayout.removeView(mAdView);
                    mAdView.destroy();
                }

                mAdView = new AdView(getActivity());
                mAdView.setAdUnitId(Constants.ADMOB_BANNER_AD_UNIT_ID);
                mFrameLayout.addView(mAdView);

                switch (mAdSizesSpinner.getSelectedItemPosition()) {
                    case 0:
                        mAdView.setAdSize(AdSize.BANNER);
                        break;
                    case 1:
                        mAdView.setAdSize(AdSize.SMART_BANNER);
                        break;
                    case 2:
                        mAdView.setAdSize(AdSize.MEDIUM_RECTANGLE);
                        break;
                    case 3:
                        mAdView.setAdSize(AdSize.LARGE_BANNER);
                        break;
                    case 4:
                        mAdView.setAdSize(AdSize.FULL_BANNER);
                        break;
                    case 5:
                        mAdView.setAdSize(AdSize.LEADERBOARD);
                        break;
                    case 6:
                        mAdView.setAdSize(AdSize.WIDE_SKYSCRAPER);
                        break;
                    default:
                }

                mAdView.setAdListener(new LoggingAdListener(mLogTextView));

                mAdView.loadAd(new AdRequest.Builder().tagForChildDirectedTreatment(true).build());
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
                editor.remove(KEY_BANNER_AD_LOGS).commit();
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
        editor.putString(KEY_BANNER_AD_LOGS, mLogTextView.getText().toString()).commit();
    }
}
