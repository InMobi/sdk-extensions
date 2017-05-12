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
import android.widget.CheckBox;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;

import com.google.ads.mediation.inmobi.InMobiAdapter;
import com.google.ads.mediation.inmobi.InMobiNetworkKeys;
import com.google.ads.mediation.inmobi.InMobiNetworkValues;
import com.google.ads.mediation.testapp.util.Constants;
import com.google.ads.mediation.testapp.util.LoggingAdListener;
import com.google.android.gms.ads.AdLoader;
import com.google.android.gms.ads.doubleclick.PublisherAdRequest;
import com.google.android.gms.ads.formats.NativeAd;
import com.google.android.gms.ads.formats.NativeAdOptions;
import com.google.android.gms.ads.formats.NativeAppInstallAd;
import com.google.android.gms.ads.formats.NativeAppInstallAdView;
import com.google.android.gms.ads.formats.NativeContentAd;
import com.google.android.gms.ads.formats.NativeContentAdView;

import java.text.SimpleDateFormat;
import java.util.List;

/**
 * The {@link TestNativeAdFragment} class class is used to load native ads and test their
 * callbacks.
 */
public class TestNativeAdFragment extends Fragment {

    /**
     * Key to obtain logs for {@link TestNativeAdFragment} from shared preferences.
     */
    private static final String KEY_NATIVE_AD_LOGS = "native_ad_logs";

    /**
     * Key to retain and restore the save state of {@link #mContentAdCheckBox}.
     */
    private static final String KEY_CONTENT_AD_CHECKED = "content_ad_checked";

    /**
     * Key to retain and restore the save state of {@link #mAppInstallAdCheckBox}.
     */
    private static final String KEY_APP_INSTALL_AD_CHECKED = "app_install_ad_checked";

    /**
     * Checkbox to determine whether or not to load content ads.
     */
    private CheckBox mContentAdCheckBox;

    /**
     * Checkbox to determine whether or not to load app install ads.
     */
    private CheckBox mAppInstallAdCheckBox;

    /**
     * Button used to load native ads.
     */
    private Button mLoadAdButton;

    /**
     * Container to hold native ad.
     */
    private FrameLayout mNativeAdContainerView;

    /**
     * Text view to display logs to the user.
     */
    private TextView mLogTextView;

    public TestNativeAdFragment() {
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
        View rootView = inflater.inflate(R.layout.fragment_test_native_ad, container, false);
        final ScrollView scrollView =
                (ScrollView) rootView.findViewById(R.id.native_ad_scroll_view);
        mContentAdCheckBox = (CheckBox) rootView.findViewById(R.id.content_ad_check_box);
        mAppInstallAdCheckBox = (CheckBox) rootView.findViewById(R.id.app_install_ad_check_box);
        if (savedInstanceState != null) {
            mContentAdCheckBox.setChecked(
                    savedInstanceState.getBoolean(KEY_CONTENT_AD_CHECKED, false));
            mAppInstallAdCheckBox.setChecked(
                    savedInstanceState.getBoolean(KEY_APP_INSTALL_AD_CHECKED, false));
        }

        mLoadAdButton = (Button) rootView.findViewById(R.id.load_native_ad_button);
        mNativeAdContainerView = (FrameLayout) rootView.findViewById(R.id.native_ad_content_frame);
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
                // Scroll to the bottom when new logs are available.
                scrollView.fullScroll(ScrollView.FOCUS_DOWN);
            }
        });

        return rootView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        SharedPreferences preferences = getActivity()
                .getSharedPreferences(Constants.KEY_LOGS_PREFS_FILE, Context.MODE_PRIVATE);
        mLogTextView.setText(preferences.getString(KEY_NATIVE_AD_LOGS, ""));

        mLoadAdButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loadNativeAd();
            }
        });
    }

    /**
     * This method will load native ads.
     */
    private void loadNativeAd() {
        if (!mContentAdCheckBox.isChecked() && !mAppInstallAdCheckBox.isChecked()) {
            Snackbar.make(getView(),
                    "At least one ad type must be checked to load an ad.",
                    Snackbar.LENGTH_SHORT).show();
            return;
        }
        mLoadAdButton.setEnabled(false);

        AdLoader.Builder builder =
                new AdLoader.Builder(getActivity(), Constants.ADMOB_NATIVE_AD_UNIT_ID);
        NativeAdOptions nativeAdOptions = new NativeAdOptions.Builder().setReturnUrlsForImageAssets(false).build();
        builder.withNativeAdOptions(nativeAdOptions);

        if (mContentAdCheckBox.isChecked()) {
            builder.forContentAd(new NativeContentAd.OnContentAdLoadedListener() {
                @Override
                public void onContentAdLoaded(NativeContentAd contentAd) {
                    showContentAd(mNativeAdContainerView, contentAd);
                }
            });
        }

        if (mAppInstallAdCheckBox.isChecked()) {
            builder.forAppInstallAd(new NativeAppInstallAd.OnAppInstallAdLoadedListener() {
                @Override
                public void onAppInstallAdLoaded(NativeAppInstallAd appInstallAd) {
                    showAppInstallAd(mNativeAdContainerView, appInstallAd);
                }
            });
        }


        AdLoader adLoader = builder.withAdListener(new LoggingAdListener(mLogTextView) {
            @Override
            public void onAdFailedToLoad(int errorCode) {
                mLoadAdButton.setEnabled(true);
                super.onAdFailedToLoad(errorCode);
            }

            @Override
            public void onAdLoaded() {
                mLoadAdButton.setEnabled(true);
                super.onAdLoaded();
            }
        }).build();

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

        adLoader.loadAd(new PublisherAdRequest.Builder().addNetworkExtrasBundle(InMobiAdapter.class, extraParams).build());
    }

    /**
     * This method will inflate a content ad view, set the content from contentAd and add it to
     * the parent to display the ad.
     *
     * @param parent    View group the content ad needs to be placed in.
     * @param contentAd The content ad obtained from the ad network to be shown to the user.
     */
    private void showContentAd(ViewGroup parent, NativeContentAd contentAd) {
        // Inflate the native content ad layout.
        LayoutInflater layoutInflater = (LayoutInflater) parent.getContext()
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        NativeContentAdView contentAdView = (NativeContentAdView) layoutInflater
                .inflate(R.layout.native_content_ad, parent, false);

        // Set the headline text.
        TextView headlineTextView =
                (TextView) contentAdView.findViewById(R.id.content_ad_headline_text_view);
        headlineTextView.setText(contentAd.getHeadline());
        contentAdView.setHeadlineView(headlineTextView);

        // Get the images.
        List<NativeAd.Image> images = contentAd.getImages();
        if (images != null && images.size() > 0) {
            // Display the first image.
            NativeAd.Image image = images.get(0);
            if (image != null && image.getDrawable() != null) {
                ImageView contentAdImageView =
                        (ImageView) contentAdView.findViewById(R.id.content_ad_image);
                contentAdImageView.setImageDrawable(image.getDrawable());
                contentAdView.setImageView(contentAdImageView);
            }
            // Display the number of images obtained from the ad network.
            ((TextView) contentAdView.findViewById(R.id.content_ad_images_number_text_view))
                    .setText(String.format(getString(R.string.showing_images), images.size()));
        }

        // Set the body text.
        TextView bodyTextView = (TextView) contentAdView.findViewById(R.id.ad_body_text_view);
        bodyTextView.setText(contentAd.getBody());
        contentAdView.setBodyView(bodyTextView);

        // Get the logo.
        NativeAd.Image logoImage = contentAd.getLogo();
        if (logoImage != null && logoImage.getDrawable() != null) {
            // Display the logo.
            ImageView logoImageView =
                    (ImageView) contentAdView.findViewById(R.id.content_ad_logo_image_view);
            logoImageView.setImageDrawable(logoImage.getDrawable());
            contentAdView.setLogoView(logoImageView);
        }

        // Set the call to action text.
        TextView callToActionTextView =
                (TextView) contentAdView.findViewById(R.id.content_ad_call_to_action_text_view);
        callToActionTextView.setText(contentAd.getCallToAction());
        contentAdView.setCallToActionView(callToActionTextView);

        // Set the advertiser text.
        TextView advertiserTextView =
                (TextView) contentAdView.findViewById(R.id.content_ad_advertiser_text_view);
        advertiserTextView.setText(contentAd.getAdvertiser());
        contentAdView.setAdvertiserView(advertiserTextView);

        // Set the native ad to the ad view.
        contentAdView.setNativeAd(contentAd);

        // Remove previously loaded ads and add contentAdView to the parent.
        parent.removeAllViews();
        parent.addView(contentAdView);
    }

    /**
     * This method will inflate a app install ad, set the content from appInstallAd and add it to
     * the parent to display the ad.
     *
     * @param parent       View group the app install ad needs to be added to.
     * @param appInstallAd The app install ad obtained from the ad network to be displayed to the
     *                     user.
     */
    private void showAppInstallAd(ViewGroup parent, NativeAppInstallAd appInstallAd) {
        // Inflate the app install ad layout.
        LayoutInflater layoutInflater = (LayoutInflater) parent.getContext()
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        NativeAppInstallAdView appInstallAdView = (NativeAppInstallAdView) layoutInflater
                .inflate(R.layout.native_app_install_ad, parent, false);

        // Set the headline text.
        TextView headlineTextView =
                (TextView) appInstallAdView.findViewById(R.id.ai_ad_headline_text_view);
        headlineTextView.setText(appInstallAd.getHeadline());
        appInstallAdView.setHeadlineView(headlineTextView);

        // Get the images.
        List<NativeAd.Image> images = appInstallAd.getImages();
        if (images != null && images.size() > 0) {
            NativeAd.Image image = images.get(0);
            if (image != null && image.getDrawable() != null) {
                // Display the first image.
                ImageView appInstallAdImageView =
                        (ImageView) appInstallAdView.findViewById(R.id.ai_ad_image);
                appInstallAdImageView.setImageDrawable(image.getDrawable());
                appInstallAdView.setImageView(appInstallAdImageView);
            }
            // Display the number of images obtained from the ad network.
            ((TextView) appInstallAdView.findViewById(R.id.ai_ad_images_number_text_view))
                    .setText(String.format(getString(R.string.showing_images), images.size()));
        }

        // Set the body text.
        TextView bodyTextView = (TextView) appInstallAdView.findViewById(R.id.ai_ad_body_text_view);
        bodyTextView.setText(appInstallAd.getBody());
        appInstallAdView.setBodyView(bodyTextView);

        // Get the icon.
        NativeAd.Image iconImage = appInstallAd.getIcon();
        if (iconImage != null && iconImage.getDrawable() != null) {
            // Display the icon.
            ImageView iconImageView =
                    (ImageView) appInstallAdView.findViewById(R.id.ai_ad_icon_image_view);
            iconImageView.setImageDrawable(iconImage.getDrawable());
            appInstallAdView.setIconView(iconImageView);
        }

        // Set the call to action text.
        TextView callToActionTextView =
                (TextView) appInstallAdView.findViewById(R.id.ai_ad_call_to_action_text_view);
        callToActionTextView.setText(appInstallAd.getCallToAction());
        appInstallAdView.setCallToActionView(callToActionTextView);

        // Set the star rating text.
        TextView starRatingTextView =
                (TextView) appInstallAdView.findViewById(R.id.ai_ad_star_rating_text_view);
        starRatingTextView.setText(
                String.format(getString(R.string.star_rating_value), appInstallAd.getStarRating()));
        appInstallAdView.setStarRatingView(starRatingTextView);

        // Set the store text.
        TextView storeTextView =
                (TextView) appInstallAdView.findViewById(R.id.ai_ad_store_text_view);
        storeTextView.setText(appInstallAd.getStore());
        appInstallAdView.setStoreView(storeTextView);

        // Set the price text.
        TextView priceTextView =
                (TextView) appInstallAdView.findViewById(R.id.ai_ad_price_text_view);
        priceTextView.setText(appInstallAd.getPrice());
        appInstallAdView.setPriceView(priceTextView);

        // Set the native ad to the ad view.
        appInstallAdView.setNativeAd(appInstallAd);

        // Remove any previously loaded ad and add the appInstallAdView to the parent.
        parent.removeAllViews();
        parent.addView(appInstallAdView);
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
                editor.remove(KEY_NATIVE_AD_LOGS).commit();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (mContentAdCheckBox != null) {
            outState.putBoolean(KEY_CONTENT_AD_CHECKED, mContentAdCheckBox.isChecked());
        }
        if (mAppInstallAdCheckBox != null) {
            outState.putBoolean(KEY_APP_INSTALL_AD_CHECKED, mAppInstallAdCheckBox.isChecked());
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        SharedPreferences.Editor editor = getActivity()
                .getSharedPreferences(Constants.KEY_LOGS_PREFS_FILE, Context.MODE_PRIVATE)
                .edit();
        editor.putString(KEY_NATIVE_AD_LOGS, mLogTextView.getText().toString()).commit();
    }
}
