package com.inmobi.showcase;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

import com.inmobi.ads.InMobiAdRequestStatus;
import com.inmobi.ads.InMobiNative;
import com.inmobi.ads.InMobiNative.NativeAdListener;
import com.inmobi.sdk.InMobiSdk;
import com.mopub.common.MoPub;
import com.mopub.common.logging.MoPubLog;
import com.mopub.mobileads.MoPubErrorCode;
import com.mopub.nativeads.CustomEventNative;
import com.mopub.nativeads.ImpressionTracker;
import com.mopub.nativeads.NativeClickHandler;
import com.mopub.nativeads.NativeErrorCode;
import com.mopub.nativeads.NativeImageHelper;
import com.mopub.nativeads.StaticNativeAd;

import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.mopub.common.util.Json.getJsonValue;
import static com.mopub.common.util.Numbers.parseDouble;
import static com.mopub.nativeads.NativeImageHelper.preCacheImages;

/*
 * Tested with InMobi SDK  6.0.4
 */

public class InMobiNativeCustomEvent extends CustomEventNative {

	private static final String TAG = InMobiNativeCustomEvent.class.getSimpleName();

	private static final String SERVER_EXTRA_ACCOUNT_ID = "accountid";

	private static final String SERVER_EXTRA_PLACEMENT_ID = "placementid";

	private static boolean mIsInMobiSdkInitialized = false;


	@Override
	protected void loadNativeAd(@NonNull Context context,
			@NonNull CustomEventNativeListener customEventNativeListener,
			@NonNull Map<String, Object> localExtras, @NonNull Map<String, String> serverExtras) {

		Log.d(TAG, "Reached native adapter");

		if (context == null) {
			Log.e(TAG, "Could not find Activity Context, Native Mediation failed");
			customEventNativeListener.onNativeAdFailed(NativeErrorCode.NETWORK_NO_FILL);
			return;
		}

		Activity activity;

		if (context!=null && context instanceof Activity) {
			activity = (Activity) context;
		} else {
			Log.w(TAG, "Context not an Activity. Returning error!");
			customEventNativeListener.onNativeAdFailed(NativeErrorCode.NETWORK_NO_FILL);
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
         */

		/*InMobiSdk.setAreaCode("areacode");
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

		/*
            Mandatory Params to set in the code by the publisher to identify the supply source type
         */

		Map<String, String> map = new HashMap<>();
		map.put("tp", "c_mopub");
		map.put("tp-ver", MoPub.SDK_VERSION);

		final InMobiStaticNativeAd inMobiStaticNativeAd =
				new InMobiStaticNativeAd(context,
						new ImpressionTracker(context),
						new NativeClickHandler(context),
						customEventNativeListener);
		inMobiStaticNativeAd.setIMNative(new InMobiNative(activity,placementId, inMobiStaticNativeAd));
		inMobiStaticNativeAd.setExtras(map);
		inMobiStaticNativeAd.loadAd();
	}

	static class InMobiStaticNativeAd extends StaticNativeAd implements NativeAdListener,InMobiNative.NativeAdEventsListener {
		static final int IMPRESSION_MIN_TIME_VIEWED = 1000;

		// Modifiable keys
		static final String TITLE = "title";
		static final String DESCRIPTION = "description";
		static final String SCREENSHOTS = "screenshots";
		static final String ICON = "icon";
		static final String LANDING_URL = "landingURL";
		static final String CTA = "cta";
		static final String RATING = "rating";

		// Constant keys
		static final String URL = "url";

		private final Context mContext;
		private final CustomEventNativeListener mCustomEventNativeListener;
		private final ImpressionTracker mImpressionTracker;
		private final NativeClickHandler mNativeClickHandler;
		private InMobiNative mImNative;

		InMobiStaticNativeAd(final Context context,
				final ImpressionTracker impressionTracker,
				final NativeClickHandler nativeClickHandler,
				final CustomEventNativeListener customEventNativeListener) {
			mContext = context.getApplicationContext();
			mImpressionTracker = impressionTracker;
			mNativeClickHandler = nativeClickHandler;
			mCustomEventNativeListener = customEventNativeListener;
		}

		void setIMNative(final InMobiNative imNative) {
			mImNative = imNative;
			mImNative.setNativeAdEventListener(this);
		}

        void setExtras(Map<String,String> map){
        	mImNative.setExtras(map);
        }
        
		void loadAd() {
			mImNative.load();
		}


		// Lifecycle Handlers
		@Override
		public void prepare(final View view) {
			if (view != null && view instanceof ViewGroup) {
				InMobiNative.bind(view, mImNative);
			} else if (view != null && view.getParent() instanceof ViewGroup) {
				InMobiNative.bind((ViewGroup)(view.getParent()), mImNative);
			} else {
				Log.e("MoPub", "InMobi did not receive ViewGroup to attachToView, unable to record impressions");
			}
			mImpressionTracker.addView(view, this);
			mNativeClickHandler.setOnClickListener(view, this);
		}

		@Override
		public void clear(final View view) {
			InMobiNative.unbind(view);
			mImpressionTracker.removeView(view);
			mNativeClickHandler.clearOnClickListener(view);
		}

		@Override
		public void destroy() {
			mImpressionTracker.destroy();
		}

		// Event Handlers
		@Override
		public void recordImpression(final View view) {
//			notifyAdImpressed();
		}

		@Override
		public void handleClick(final View view) {
			notifyAdClicked();
			mNativeClickHandler.openClickDestinationUrl(getClickDestinationUrl(), view);
			mImNative.reportAdClick(null);
		}

		void parseJson(final InMobiNative imNative) throws JSONException  {
			final JSONTokener jsonTokener = new JSONTokener((String) imNative.getAdContent());
			final JSONObject jsonObject = new JSONObject(jsonTokener);

			setTitle(getJsonValue(jsonObject, TITLE, String.class));
			String text = getJsonValue(jsonObject, DESCRIPTION, String.class);
			if(text!=null)
				setText(text);
			final JSONObject screenShotJsonObject = getJsonValue(jsonObject, SCREENSHOTS, JSONObject.class);
			if (screenShotJsonObject != null) {
				setMainImageUrl(getJsonValue(screenShotJsonObject, URL, String.class));
			}

			final JSONObject iconJsonObject = getJsonValue(jsonObject, ICON, JSONObject.class);
			if (iconJsonObject != null) {
				setIconImageUrl(getJsonValue(iconJsonObject, URL, String.class));
			}

			final String clickDestinationUrl = getJsonValue(jsonObject, LANDING_URL, String.class);
			if (clickDestinationUrl == null) {
				final String errorMessage = "InMobi JSON response missing required key: "
						+ LANDING_URL + ". Failing over.";
				MoPubLog.d(errorMessage);
				throw new JSONException(errorMessage);
			}

			setClickDestinationUrl(clickDestinationUrl);
			String cta = getJsonValue(jsonObject, CTA, String.class);
			if(cta!=null)
				setCallToAction(cta);
			try {
				if(jsonObject.opt(RATING)!=null){
					setStarRating(parseDouble(jsonObject.opt(RATING)));	
				}
			} catch (ClassCastException e) {
				Log.d("MoPub", "Unable to set invalid star rating for InMobi Native.");
			}            setImpressionMinTimeViewed(IMPRESSION_MIN_TIME_VIEWED);
		}

		@Override
		public void onAdDismissed(InMobiNative arg0) {
			Log.d(TAG,"Native Ad is dismissed");
		}

		@Override
		public void onAdDisplayed(InMobiNative arg0) {
			Log.d(TAG,"Native Ad is displayed");
		}

		@Override
		public void onAdLoadFailed(InMobiNative arg0, InMobiAdRequestStatus arg1) {
			Log.d(TAG,"Native ad failed to load");
			String errorMsg="";
			switch (arg1.getStatusCode()) {
			case INTERNAL_ERROR:
				errorMsg="INTERNAL_ERROR";
				break;
			case REQUEST_INVALID:
				errorMsg="INVALID_REQUEST";
				break;
			case NETWORK_UNREACHABLE:
				errorMsg="NETWORK_UNREACHABLE";
				break;
			case NO_FILL:
				errorMsg="NO_FILL";
				break;
			case REQUEST_PENDING:
				errorMsg="REQUEST_PENDING";
				break;
			case REQUEST_TIMED_OUT:
				errorMsg="REQUEST_TIMED_OUT";
				break;
			case SERVER_ERROR:
				errorMsg="SERVER_ERROR";
				break;
			case AD_ACTIVE:
				errorMsg="AD_ACTIVE";
				break;
			case EARLY_REFRESH_REQUEST:
				errorMsg="EARLY_REFRESH_REQUEST";
				break;
			default:
				errorMsg="NETWORK_ERROR";
				break;
			}
			if (errorMsg == "INVALID_REQUEST") {
				mCustomEventNativeListener.onNativeAdFailed(NativeErrorCode.NETWORK_INVALID_REQUEST);
			} else if (errorMsg == "INTERNAL_ERROR" || errorMsg == "NETWORK_ERROR") {
				mCustomEventNativeListener.onNativeAdFailed(NativeErrorCode.NETWORK_INVALID_STATE);
			} else if (errorMsg == "NO_FILL") {
				mCustomEventNativeListener.onNativeAdFailed(NativeErrorCode.NETWORK_NO_FILL);
			} else if (errorMsg == "REQUEST_TIMED_OUT"){
				mCustomEventNativeListener.onNativeAdFailed(NativeErrorCode.NETWORK_TIMEOUT);
			}else if(errorMsg == "NETWORK_UNREACHABLE"){
				mCustomEventNativeListener.onNativeAdFailed(NativeErrorCode.CONNECTION_ERROR);
			}
			else {
				mCustomEventNativeListener.onNativeAdFailed(NativeErrorCode.UNSPECIFIED);
			}
		}

		@Override
		public void onAdLoadSucceeded(InMobiNative imNative) {
			Log.v(TAG, "Ad loaded:"+imNative.getAdContent().toString());
			try {
				parseJson(imNative);
			} catch (JSONException e) {
				mCustomEventNativeListener.onNativeAdFailed(NativeErrorCode.INVALID_RESPONSE);
				return;
			}

			final List<String> imageUrls = new ArrayList<String>();
			/*final String mainImageUrl = getMainImageUrl();
            if (mainImageUrl != null) {
                imageUrls.add(mainImageUrl);
            }*/

			final String iconUrl = getIconImageUrl();
			if (iconUrl != null) {
				imageUrls.add(iconUrl);
			}

			preCacheImages(mContext, imageUrls, new NativeImageHelper.ImageListener() {
				@Override
				public void onImagesCached() {
					Log.v(TAG, "image cached");
					mCustomEventNativeListener.onNativeAdLoaded(InMobiStaticNativeAd.this);
				}

				@Override
				public void onImagesFailedToCache(NativeErrorCode errorCode) {
					Log.v(TAG, "image failed to cache");
					mCustomEventNativeListener.onNativeAdFailed(errorCode);
				}
			});            
		}

		@Override
		public void onUserLeftApplication(InMobiNative arg0) {
			Log.d(TAG,"User left application");
		}

		@Override
		public void onAdImpressed(InMobiNative inMobiNative) {
			Log.d(TAG,"InMobi impression recorded successfully");
			notifyAdImpressed();
		}
	}
}
