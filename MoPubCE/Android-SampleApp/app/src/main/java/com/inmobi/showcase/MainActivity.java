package com.inmobi.showcase;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.mopub.common.MoPub;
import com.mopub.common.MoPubReward;
import com.mopub.mobileads.MoPubErrorCode;
import com.mopub.mobileads.MoPubInterstitial;
import com.mopub.mobileads.MoPubRewardedVideoListener;
import com.mopub.mobileads.MoPubView;

import java.util.Set;


public class MainActivity extends Activity implements OnClickListener, MoPubInterstitial.InterstitialAdListener, MoPubView.BannerAdListener, MoPubRewardedVideoListener {

	private MoPubView mMopubBannerView;
	private MoPubInterstitial mMopubInterstitialView;
	private Button BtnRefreshAd;
	private Button BtnLoadInterstitial;
	private Button BtnLoadNative;
	private Button BtnLoadNativeStrands;
	private Button BtnLoadRewarded;
	private static boolean rewardedVideoInitialized;
	private static final String TAG = MainActivity.class.getSimpleName();


	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		MoPub.onCreate(this);
		BtnRefreshAd = (Button) findViewById(R.id.btnRefreshAd);
		BtnLoadInterstitial = (Button) findViewById(R.id.btnLoadInterstitial);

		BtnLoadRewarded = (Button) findViewById(R.id.btnLoadRewarded);
		BtnLoadNative = (Button) findViewById(R.id.btnLoadNative);
		BtnLoadNativeStrands = (Button) findViewById(R.id.btnLoadNativeStrands);
		BtnRefreshAd.setOnClickListener(this);
		BtnLoadInterstitial.setOnClickListener(this);
		BtnLoadNative.setOnClickListener(this);
		BtnLoadRewarded.setOnClickListener(this);
		BtnLoadNativeStrands.setOnClickListener(this);
		// Initialize Ad components
		mMopubBannerView = (MoPubView) findViewById(R.id.bannerview);
		mMopubBannerView.setAdUnitId("bf140ef38903425da67953cbeb1c7631");
		mMopubBannerView.setBannerAdListener(this);
		mMopubBannerView.setAutorefreshEnabled(false);
		mMopubBannerView.loadAd();
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		if (mMopubBannerView != null)
			mMopubBannerView.destroy();
		if (mMopubInterstitialView != null)
			mMopubInterstitialView.destroy();
	}

	public void getInterstitialAd() {
		mMopubInterstitialView = new MoPubInterstitial(this,
				"15b0e16394174ee3b837635b73298135");
		mMopubInterstitialView.setInterstitialAdListener(this);
		mMopubInterstitialView.load();
	}

	public void refreshAd() {
		mMopubBannerView.loadAd();
	}

	@Override
	public void onClick(View v) {
		int id = v.getId();
		switch (id) {
			case R.id.btnRefreshAd:
				refreshAd();
				break;
			case R.id.btnLoadInterstitial:
				getInterstitialAd();
				break;
			case R.id.btnLoadNative:
				getNativeAd();
				break;
			case R.id.btnLoadNativeStrands:
				getNativeStrands();
				break;
			case R.id.btnLoadRewarded:
				getRewardedAd();
		}
	}

	public void getRewardedAd(){
		if (!rewardedVideoInitialized) {
			MoPub.initializeRewardedVideo(this);
			rewardedVideoInitialized = true;
		}
		MoPub.setRewardedVideoListener(this);
		MoPub.loadRewardedVideo("a20bf45862464a3c825be02334f3ab40");
	}

	public void getNativeStrands() {

		Intent i = new Intent(this,NativeAdActivity.class);
		startActivity(i);
	}

	public void getNativeAd() {

		Intent i = new Intent(this,NativeView.class);
		startActivity(i);
	}

	@Override
	public void onBannerLoaded(MoPubView banner) {
		Log.v(TAG, "Banner ad loaded successfully.");
	}

	@Override
	public void onBannerFailed(MoPubView banner, MoPubErrorCode errorCode) {
		Log.v(TAG,
				"Banner ad failed to load with ErrorCode" + errorCode.toString());
	}

	@Override
	public void onBannerClicked(MoPubView banner) {
		Log.v(TAG, "Banner ad Clicked");
	}

	@Override
	public void onBannerExpanded(MoPubView banner) {
		Log.v(TAG, "Banner ad Expanded");
	}

	@Override
	public void onBannerCollapsed(MoPubView banner) {
		Log.v(TAG, "Banner ad Collapsed");
	}

	@Override
	public void onInterstitialLoaded(MoPubInterstitial interstitial) {
		Log.v(TAG, "Interstitial ad loaded successfully.");
		if (mMopubInterstitialView.isReady()) {
			mMopubInterstitialView.show();
		}
	}

	@Override
	public void onInterstitialFailed(MoPubInterstitial interstitial, MoPubErrorCode errorCode) {
		Log.v(TAG,
				"Interstitial ad failed with ErrorCode " + errorCode.toString());
	}

	@Override
	public void onInterstitialShown(MoPubInterstitial interstitial) {
		Log.v(TAG, "Interstitial ad Shown");
	}

	@Override
	public void onInterstitialClicked(MoPubInterstitial interstitial) {
		Log.v(TAG, "Interstitial ad Clicked.");
	}

	@Override
	public void onInterstitialDismissed(MoPubInterstitial interstitial) {
		Log.v(TAG, "Interstitial ad Dismissed.");
	}

	@Override
	public void onRewardedVideoLoadSuccess(String adUnitId) {
		Log.v(TAG,"Rewarded video loaded successfully");
		MoPub.showRewardedVideo("a20bf45862464a3c825be02334f3ab40");
	}

	@Override
	public void onRewardedVideoLoadFailure(String adUnitId, MoPubErrorCode errorCode) {
		Log.v(TAG,"Rewarded video failure:"+errorCode.toString());
	}

	@Override
	public void onRewardedVideoStarted(String adUnitId) {
		Log.v(TAG,"Rewarded video started");
	}

	@Override
	public void onRewardedVideoPlaybackError(String adUnitId, MoPubErrorCode errorCode) {
		Log.v(TAG,"Playback error:"+errorCode.toString());
	}

	@Override
	public void onRewardedVideoClosed(String adUnitId) {
		Log.v(TAG,"Rewarded video closed");
	}

	@Override
	public void onRewardedVideoCompleted(Set<String> adUnitIds, MoPubReward reward) {
		Log.v(TAG,"Rewarded video completed:"+reward.getAmount());
	}
}
