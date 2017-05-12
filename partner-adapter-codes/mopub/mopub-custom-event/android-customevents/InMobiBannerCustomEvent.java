package com.inmobi.showcase;

import android.content.Context;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.WindowManager;
import android.widget.LinearLayout;

import com.inmobi.ads.InMobiAdRequestStatus;
import com.inmobi.ads.InMobiAdRequestStatus.StatusCode;
import com.inmobi.ads.InMobiBanner;
import com.inmobi.ads.InMobiBanner.AnimationType;
import com.inmobi.ads.InMobiBanner.BannerAdListener;
import com.mopub.common.MoPub;
import com.mopub.mobileads.CustomEventBanner;
import com.mopub.mobileads.MoPubErrorCode;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/*
 * Tested with InMobi SDK  6.2.0
 */
public class InMobiBannerCustomEvent extends CustomEventBanner implements BannerAdListener {


    private CustomEventBannerListener mBannerListener;
    private InMobiBanner imbanner;
    private static boolean isAppIntialize = false;
    private JSONObject serverParams;
    private String accountId = "";
    private long placementId = -1;
    private static final String TAG = InMobiBannerCustomEvent.class.getSimpleName();

    @Override
    public void onAdDismissed(InMobiBanner arg0) {
        Log.v(TAG, "Ad Dismissed");
    }

    @Override
    public void onAdDisplayed(InMobiBanner arg0) {
        Log.v(TAG, "Ad displayed");
    }

    @Override
    public void onAdInteraction(InMobiBanner arg0, Map<Object, Object> arg1) {
        Log.v(TAG, "Ad interaction");
        mBannerListener.onBannerClicked();
    }

    @Override
    public void onAdLoadFailed(InMobiBanner arg0, InMobiAdRequestStatus inMobiAdRequestStatus) {
        Log.v(TAG, "Ad failed to load");

        if (mBannerListener != null) {

            if (inMobiAdRequestStatus.getStatusCode() == StatusCode.INTERNAL_ERROR) {
                mBannerListener
                        .onBannerFailed(MoPubErrorCode.INTERNAL_ERROR);
            } else if (inMobiAdRequestStatus.getStatusCode() == StatusCode.REQUEST_INVALID) {
                mBannerListener
                        .onBannerFailed(MoPubErrorCode.ADAPTER_CONFIGURATION_ERROR);
            } else if (inMobiAdRequestStatus.getStatusCode() == StatusCode.NETWORK_UNREACHABLE) {
                mBannerListener
                        .onBannerFailed(MoPubErrorCode.NETWORK_INVALID_STATE);
            } else if (inMobiAdRequestStatus.getStatusCode() == StatusCode.NO_FILL) {
                mBannerListener
                        .onBannerFailed(MoPubErrorCode.NO_FILL);
            } else if (inMobiAdRequestStatus.getStatusCode() == StatusCode.REQUEST_TIMED_OUT) {
                mBannerListener
                        .onBannerFailed(MoPubErrorCode.NETWORK_TIMEOUT);
            } else if (inMobiAdRequestStatus.getStatusCode() == StatusCode.SERVER_ERROR) {
                mBannerListener
                        .onBannerFailed(MoPubErrorCode.SERVER_ERROR);
            } else {
                mBannerListener
                        .onBannerFailed(MoPubErrorCode.UNSPECIFIED);
            }
        }

    }

    @Override
    public void onAdLoadSucceeded(InMobiBanner arg0) {
        Log.d(TAG, "InMobi banner ad loaded successfully.");
        if (mBannerListener != null) {
            if (arg0 != null) {
                mBannerListener.onBannerLoaded(arg0);
            } else {
                mBannerListener
                        .onBannerFailed(MoPubErrorCode.NETWORK_INVALID_STATE);
            }
        }
    }

    @Override
    public void onAdRewardActionCompleted(InMobiBanner arg0, Map<Object, Object> arg1) {
        Log.v(TAG, "Ad rewarded");
    }

    @Override
    public void onUserLeftApplication(InMobiBanner arg0) {
        Log.v(TAG, "User left applicaton");
        mBannerListener.onLeaveApplication();
    }

    @Override
    protected void loadBanner(Context context, CustomEventBannerListener customEventBannerListener, Map<String,
            Object> localExtras, Map<String, String> serverParameters) {
        mBannerListener = customEventBannerListener;

        try {
            serverParams = new JSONObject(serverParameters);
            accountId = serverParams.getString("accountid");
            placementId = serverParams.getLong("placementid");
            Log.d(TAG, String.valueOf(placementId));
            Log.d(TAG, accountId);

        } catch (JSONException e1) {
            e1.printStackTrace();
        }

        if (!isAppIntialize) {
            try {
                InMobiSdk.init(context, accountId);
            } catch (Exception e) {
                e.printStackTrace();
            }
            isAppIntialize = true;
        }
        /*
        Sample for setting up the InMobi SDK Demographic params.
        Publisher need to set the values of params as they want.

		InMobiSdk.setAreaCode("areacode");
		InMobiSdk.setEducation(Education.HIGH_SCHOOL_OR_LESS);
		InMobiSdk.setGender(Gender.MALE);
		InMobiSdk.setIncome(1000);
		InMobiSdk.setAge(23);
		InMobiSdk.setPostalCode("postalcode");
		InMobiSdk.setLogLevel(LogLevel.DEBUG);
		InMobiSdk.setLocationWithCityStateCountry("blore", "kar", "india");
		InMobiSdk.setLanguage("ENG");
		InMobiSdk.setInterests("dance");
		InMobiSdk.setEthnicity(Ethnicity.ASIAN);
		InMobiSdk.setYearOfBirth(1980);*/
        imbanner = new InMobiBanner(context, placementId);
        imbanner.setListener(this);
        imbanner.setEnableAutoRefresh(false);
        imbanner.setAnimationType(AnimationType.ANIMATION_OFF);

        DisplayMetrics dm = new DisplayMetrics();
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        display.getMetrics(dm);
        Map<String, String> map = new HashMap<String, String>();
        map.put("tp", "c_mopub");
        map.put("tp-ver", MoPub.SDK_VERSION);
        imbanner.setExtras(map);

        imbanner.setLayoutParams(new LinearLayout.LayoutParams(Math.round(320 * dm.density), Math
                .round(50 * dm.density)));
        imbanner.load();
    }

    @Override
    protected void onInvalidate() {
        // TODO Auto-generated method stub

    }
}

