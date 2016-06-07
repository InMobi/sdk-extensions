package com.inmobi.showcase;


import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.inmobi.ads.InMobiAdRequestStatus;
import com.inmobi.ads.InMobiNativeStrand;
import com.inmobi.ads.InMobiNativeStrand.NativeStrandAdListener;
import com.inmobi.sdk.InMobiSdk;
import com.mopub.common.MoPub;
import com.mopub.nativeads.BaseNativeAd;
import com.mopub.nativeads.CustomEventNative;
import com.mopub.nativeads.MoPubAdRenderer;
import com.mopub.nativeads.NativeClickHandler;
import com.mopub.nativeads.NativeErrorCode;

import java.util.HashMap;
import java.util.Map;

public class InMobiNativeStrandCustomEvent extends CustomEventNative {

    private static final String TAG = InMobiNativeStrandCustomEvent.class.getSimpleName();
    private static final String SERVER_EXTRA_ACCOUNT_ID = "accountid";
    private static final String SERVER_EXTRA_PLACEMENT_ID = "placementid";
    private static final String LOCAL_EXTRA_ACTIVITY_CONTEXT = "activityContext";

    private static boolean mIsInMobiSdkInitialized = false;

    @Override
    protected void loadNativeAd(@NonNull Activity activity,
                                @NonNull CustomEventNativeListener customEventNativeListener,
                                @NonNull Map<String, Object> localExtras, @NonNull Map<String, String> serverExtras) {
        if (activity == null) {
            Log.e(TAG, "Could not find Activity Context, Native Strand Mediation failed");
            customEventNativeListener.onNativeAdFailed(NativeErrorCode.NATIVE_ADAPTER_CONFIGURATION_ERROR);
            return;
        }

        String accountId;
        long placementId;

        accountId = serverExtras.get(SERVER_EXTRA_ACCOUNT_ID);
        placementId = Long.parseLong(serverExtras.get(SERVER_EXTRA_PLACEMENT_ID));
        Log.d(TAG, "Server Extras: Account ID:" + accountId + " Placement ID:" + placementId);

        if (!mIsInMobiSdkInitialized) {
            InMobiSdk.init(activity, accountId);
            mIsInMobiSdkInitialized = true;
        }

        /*
            Sample for setting up the InMobi SDK Demographic params.
            Publisher need to set the values of params as they want.

        InMobiSdk.setAreaCode("areacode");
        InMobiSdk.setEducation(Education.HIGH_SCHOOL_OR_LESS);
        InMobiSdk.setGender(Gender.FEMALE);
        InMobiSdk.setIncome(1000);
        InMobiSdk.setAge(32);
        InMobiSdk.setPostalCode("postalcode");
        InMobiSdk.setLogLevel(LogLevel.DEBUG);
        InMobiSdk.setLocationWithCityStateCountry("blore", "kar", "india");
        InMobiSdk.setLanguage("ENG");
        InMobiSdk.setInterests("dance");
        InMobiSdk.setEthnicity(Ethnicity.ASIAN);
        InMobiSdk.setYearOfBirth(1980);
        */



        /*
            Mandatory Params to set in the code by the publisher to identify the supply source type
         */

        Map<String, String> map = new HashMap<>();
        map.put("tp", "c_mopub");
        map.put("tp-ver", MoPub.SDK_VERSION);

        final InMobiNativeStrandAd inMobiNativeStrandAd = new InMobiNativeStrandAd(activity, customEventNativeListener, placementId);
        inMobiNativeStrandAd.setExtras(map);
        inMobiNativeStrandAd.loadAd();
    }

    public static class InMobiNativeStrandRenderer implements MoPubAdRenderer<InMobiNativeStrandAd> {

        @Override
        @NonNull
        public View createAdView(@NonNull Activity activity, @Nullable ViewGroup parent) {
            final LinearLayout linearLayout = new LinearLayout(activity);
            return linearLayout;
        }

        @Override
        public void renderAdView(@NonNull View view,
                                 @NonNull InMobiNativeStrandAd inMobiNativeStrandAd) {
            ViewGroup parent = (ViewGroup) view;
            View strandView;
            if (parent.getChildAt(0) == null) {
                strandView = inMobiNativeStrandAd.mInMobiNativeStrand.getStrandView(null, parent);
                //Add the child view into parent View.
                parent.addView(strandView);
            } else {
                //Child view is already in the parent view. So don't add it again.
                strandView = inMobiNativeStrandAd.mInMobiNativeStrand.getStrandView(parent.getChildAt(0), parent);
            }
        }

        @Override
        public boolean supports(@NonNull BaseNativeAd baseNativeAd) {
            return baseNativeAd instanceof InMobiNativeStrandAd;
        }
    }


    private static class InMobiNativeStrandAd extends BaseNativeAd implements NativeStrandAdListener {

        private static final String TAG = "InMobiNativeStrandAd";
        private final CustomEventNativeListener mCustomEventNativeListener;
        private final NativeClickHandler mNativeClickHandler;
        private final InMobiNativeStrand mInMobiNativeStrand;
        private boolean mIsImpressionRecorded = false;
        private boolean mIsClickRecorded = false;

        InMobiNativeStrandAd(@NonNull final Activity activity,
                             @NonNull final CustomEventNativeListener customEventNativeListener,
                             long placementId) {
            mNativeClickHandler = new NativeClickHandler(activity);
            mCustomEventNativeListener = customEventNativeListener;
            mInMobiNativeStrand = new InMobiNativeStrand(activity, placementId, this);
        }

        void setExtras(Map<String, String> map) {
            mInMobiNativeStrand.setExtras(map);
        }

        void loadAd() {
            mInMobiNativeStrand.load();
        }

        @Override
        public void clear(@NonNull View view) {
            mNativeClickHandler.clearOnClickListener(view);
        }

        @Override
        public void destroy() {
            mInMobiNativeStrand.destroy();
        }

        @Override
        public void prepare(@NonNull View view) {
        }

        @Override
        public void onAdLoadSucceeded(@NonNull InMobiNativeStrand nativeStrand) {
            Log.i(TAG, "InMobi Native Strand loaded successfully");
            mCustomEventNativeListener.onNativeAdLoaded(this);
        }

        @Override
        public void onAdLoadFailed(@NonNull InMobiNativeStrand inMobiNativeStrand,
                                   @NonNull InMobiAdRequestStatus requestStatus) {
            String errorMessage = "Failed to load Native Strand:";
            switch (requestStatus.getStatusCode()) {
                case INTERNAL_ERROR:
                    errorMessage += "INTERNAL_ERROR";
                    mCustomEventNativeListener.onNativeAdFailed(NativeErrorCode.NETWORK_INVALID_STATE);
                    break;

                case REQUEST_INVALID:
                    errorMessage += "INVALID_REQUEST";
                    mCustomEventNativeListener.onNativeAdFailed(NativeErrorCode.NETWORK_INVALID_REQUEST);
                    break;

                case NETWORK_UNREACHABLE:
                    errorMessage += "NETWORK_UNREACHABLE";
                    mCustomEventNativeListener.onNativeAdFailed(NativeErrorCode.CONNECTION_ERROR);
                    break;

                case NO_FILL:
                    errorMessage += "NO_FILL";
                    mCustomEventNativeListener.onNativeAdFailed(NativeErrorCode.NETWORK_NO_FILL);
                    break;

                case REQUEST_PENDING:
                    errorMessage += "REQUEST_PENDING";
                    mCustomEventNativeListener.onNativeAdFailed(NativeErrorCode.UNSPECIFIED);
                    break;

                case REQUEST_TIMED_OUT:
                    errorMessage += "REQUEST_TIMED_OUT";
                    mCustomEventNativeListener.onNativeAdFailed(NativeErrorCode.NETWORK_TIMEOUT);
                    break;

                case SERVER_ERROR:
                    errorMessage += "SERVER_ERROR";
                    mCustomEventNativeListener.onNativeAdFailed(NativeErrorCode.SERVER_ERROR_RESPONSE_CODE);
                    break;

                case AD_ACTIVE:
                    errorMessage += "AD_ACTIVE";
                    mCustomEventNativeListener.onNativeAdFailed(NativeErrorCode.UNSPECIFIED);
                    break;

                case EARLY_REFRESH_REQUEST:
                    errorMessage += "EARLY_REFRESH_REQUEST";
                    mCustomEventNativeListener.onNativeAdFailed(NativeErrorCode.UNSPECIFIED);
                    break;

                default:
                    errorMessage = "UNKNOWN_ERROR" + requestStatus.getStatusCode();
                    mCustomEventNativeListener.onNativeAdFailed(NativeErrorCode.UNSPECIFIED);
                    break;
            }
            Log.w(TAG, errorMessage);
            destroy();
        }

        @Override
        public void onAdImpressed(@NonNull InMobiNativeStrand inMobiNativeStrand) {
            mIsImpressionRecorded = true;
            notifyAdImpressed();
        }

        @Override
        public void onAdClicked(@NonNull InMobiNativeStrand inMobiNativeStrand) {
            if (!mIsClickRecorded) {
                notifyAdClicked();
                mIsClickRecorded = true;
            }
        }
    }
}
