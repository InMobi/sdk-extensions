package com.inmobi.showcase;

import android.app.Activity;
import android.content.Context;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.WindowManager;
import android.widget.LinearLayout;

import com.inmobi.ads.InMobiAdRequestStatus;
import com.inmobi.ads.InMobiBanner;
import com.inmobi.ads.InMobiBanner.AnimationType;
import com.inmobi.ads.InMobiBanner.BannerAdListener;
import com.inmobi.sdk.InMobiSdk;
import com.mopub.common.MoPub;
import com.mopub.mobileads.CustomEventBanner;
import com.mopub.mobileads.MoPubErrorCode;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import com.inmobi.ads.InMobiAdRequestStatus.StatusCode;

/*
 * Tested with InMobi SDK  6.0.4
 */
public class InMobiBannerCustomEvent extends CustomEventBanner implements BannerAdListener  {


    private CustomEventBannerListener mBannerListener;
    private InMobiBanner imbanner;
    private static boolean isAppIntialize = false;
    private JSONObject serverParams;
    private JSONObject localExtras;
    private String accountId="";
    private long placementId=-1;
    private static final String TAG = InMobiBannerCustomEvent.class.getSimpleName();
    private int adWidth=0;
    private int adHeight=0;

    private final AdSize BANNER = new AdSize(320, 50);
    private final AdSize MEDIUM_RECTANGLE = new AdSize(300, 250);

    @Override
    public void onAdDismissed(InMobiBanner arg0) {
        Log.v(TAG,"Ad Dismissed");
    }

    @Override
    public void onAdDisplayed(InMobiBanner arg0) {
        Log.v(TAG,"Ad displayed");
    }

    @Override
    public void onAdInteraction(InMobiBanner arg0, Map<Object, Object> arg1) {
        Log.v(TAG,"Ad interaction");
        mBannerListener.onBannerClicked();
    }

    @Override
    public void onAdLoadFailed(InMobiBanner arg0, InMobiAdRequestStatus arg1) {
        Log.v(TAG,"Ad failed to load");

        if (mBannerListener != null) {

            if (arg1.getStatusCode() == StatusCode.INTERNAL_ERROR) {
                mBannerListener
                        .onBannerFailed(MoPubErrorCode.INTERNAL_ERROR);
            } else if (arg1.getStatusCode() == StatusCode.REQUEST_INVALID) {
                mBannerListener
                        .onBannerFailed(MoPubErrorCode.ADAPTER_CONFIGURATION_ERROR);
            } else if (arg1.getStatusCode() == StatusCode.NETWORK_UNREACHABLE) {
                mBannerListener
                        .onBannerFailed(MoPubErrorCode.NETWORK_INVALID_STATE);
            } else if (arg1.getStatusCode() == StatusCode.NO_FILL) {
                mBannerListener
                        .onBannerFailed(MoPubErrorCode.NO_FILL);
            } else if (arg1.getStatusCode() == StatusCode.REQUEST_TIMED_OUT) {
                mBannerListener
                        .onBannerFailed(MoPubErrorCode.NETWORK_TIMEOUT);
            } else if (arg1.getStatusCode() == StatusCode.SERVER_ERROR) {
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
        if(mBannerListener!=null){
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
        Log.v(TAG,"Ad rewarded");
    }

    @Override
    public void onUserLeftApplication(InMobiBanner arg0) {
        Log.v(TAG,"User left applicaton");
        mBannerListener.onLeaveApplication();
    }

    @Override
    protected void loadBanner(Context context, CustomEventBannerListener arg1, Map<String, Object> arg2,
                              Map<String, String> arg3) {
        mBannerListener = arg1;
        Activity activity;

        if (context!=null && context instanceof Activity) {
            activity = (Activity) context;
        } else {
            Log.w(TAG, "Context not an Activity. Returning error!");
            mBannerListener.onBannerFailed(MoPubErrorCode.NO_FILL);
            return;
        }

        try {
            serverParams = new JSONObject(arg3);
            accountId = serverParams.getString("accountid");
            placementId = serverParams.getLong("placementid");

            localExtras = new JSONObject(arg2);
            adWidth = localExtras.getInt("com_mopub_ad_width");
            adHeight = localExtras.getInt("com_mopub_ad_height");
            Log.d(TAG,String.valueOf(placementId));
            Log.d(TAG, accountId);

        } catch (JSONException e1) {
            e1.printStackTrace();
        }

        if (!isAppIntialize) {
            try {
                InMobiSdk.init(activity,accountId);
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
        imbanner = new InMobiBanner(activity, placementId);
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
        final AdSize adSize = calculateAdSize(adWidth, adHeight);

        if (adSize == null) {
            mBannerListener.onBannerFailed(MoPubErrorCode.ADAPTER_CONFIGURATION_ERROR);
            return;
        }

        imbanner.setLayoutParams(new LinearLayout.LayoutParams(Math.round( adSize.getWidth() * dm.density ),
                Math.round( adSize.getHeight() * dm.density )));
        imbanner.load();
    }

    private AdSize calculateAdSize(int width, int height) {
        // Use the smallest AdSize that will properly contain the adView
        if (width <= 320 && height <= 50) {
            return BANNER;
        } else if (width <= 300 && height <= 250) {
            return MEDIUM_RECTANGLE;
        } else {
            return null;
        }
    }

    @Override
    protected void onInvalidate() {
        // TODO Auto-generated method stub

    }

    private class AdSize{
        private int mWidth;
        private int mHeight;

        public  AdSize(int var1, int var2){
            mWidth = var1;
            mHeight = var2;
        }

        public int getHeight(){
            return mHeight;
        }

        public int getWidth(){
            return mWidth;
        }
    }
}

