using System;
using UnityEngine;
using InMobiAds;
using InMobiAds.Api;


// Example script showing how to invoke the InMobi Ads Unity plugin.
public class InMobiAdsDemoScript : MonoBehaviour
{
	private BannerAd bannerAd;
	private InterstitialAd interstitialAd;
	private RewardedVideoAd rewardedVideoAd;
	private float deltaTime = 0.0f;

	public void Start ()
	{
       
	}

	public void Update ()
	{
		// Calculate simple moving average for time to render screen. 0.1 factor used as smoothing
		// value.
		this.deltaTime += (Time.deltaTime - this.deltaTime) * 0.1f;
	}

	public void OnGUI ()
	{
		GUIStyle style = new GUIStyle ();

		Rect rect = new Rect (0, 0, Screen.width, Screen.height);
		style.alignment = TextAnchor.LowerRight;
		style.fontSize = (int)(Screen.height * 0.06);
		style.normal.textColor = new Color (0.0f, 0.0f, 0.5f, 1.0f);
		float fps = 1.0f / this.deltaTime;
		string text = string.Format ("{0:0.} fps", fps);
		GUI.Label (rect, text, style);

		// Puts some basic buttons onto the screen.
		GUI.skin.button.fontSize = (int)(0.035f * Screen.width);
		float buttonWidth = 0.35f * Screen.width;
		float buttonHeight = 0.15f * Screen.height;
		float columnOnePosition = 0.1f * Screen.width;
		float columnTwoPosition = 0.55f * Screen.width;

		Rect initializeInMobiAds = new Rect (
			                           columnOnePosition,
			                           0.05f * Screen.height,
			                           buttonWidth,
			                           buttonHeight);
		if (GUI.Button (initializeInMobiAds, "Init InMobi SDK")) {
			this.initializeInMobiAds ();
		}

		Rect requestBannerRect = new Rect (
			                               columnOnePosition,
			                               0.225f * Screen.height,
			                               buttonWidth,
			                               buttonHeight);
		if (GUI.Button (requestBannerRect, "Request\nBanner")) {
			this.RequestBanner ();
		}

		Rect destroyBannerRect = new Rect (
			                               columnTwoPosition,
			                               0.225f * Screen.height,
			                               buttonWidth,
			                               buttonHeight);
		if (GUI.Button (destroyBannerRect, "Destroy\nBanner")) {
			this.bannerAd.DestroyAd ();
		}

		Rect requestInterstitialRect = new Rect (
			                                     columnOnePosition,
			                                     0.4f * Screen.height,
			                                     buttonWidth,
			                                     buttonHeight);
		if (GUI.Button (requestInterstitialRect, "Request\nInterstitial")) {
			this.RequestInterstitial ();
		}

		Rect showInterstitialRect = new Rect (
			                                  columnOnePosition,
			                                  0.575f * Screen.height,
			                                  buttonWidth,
			                                  buttonHeight);
		if (GUI.Button (showInterstitialRect, "Show\nInterstitial")) {
			this.ShowInterstitial ();
		}

		Rect requestRewardedRect = new Rect (
			                                 columnTwoPosition,
			                                 0.4f * Screen.height,
			                                 buttonWidth,
			                                 buttonHeight);
		if (GUI.Button (requestRewardedRect, "Request\nRewarded Video")) {
			this.RequestRewardBasedVideo ();
		}

		Rect showRewardedRect = new Rect (
			                              columnTwoPosition,
			                              0.575f * Screen.height,
			                              buttonWidth,
			                              buttonHeight);
		if (GUI.Button (showRewardedRect, "Show\nRewarded Video")) {
			this.ShowRewardBasedVideo ();
		}
	}

	private void initializeInMobiAds ()
	{
		InMobiPlugin inmobiPlugin = new InMobiPlugin ("4028cb8b2c3a0b45012c406824e800ba");
		inmobiPlugin.SetLogLevel ("Debug");
		inmobiPlugin.SetAge (23);
	}


	private void RequestBanner ()
	{
		// These ad units are configured to always serve test ads.
		#if UNITY_ANDROID
		string placementId = "1467162141987";
		#elif UNITY_IPHONE
		string placementId = "1464947431995";
		#endif

		// Create a 320x50 banner at the top of the screen.
		this.bannerAd = new BannerAd (placementId, 320, 50, (int)InMobiAdPosition.TopCenter);


		// Register for ad events.
		this.bannerAd.OnAdLoadSucceeded += this.HandleOnAdLoadSucceeded;
		this.bannerAd.OnAdLoadFailed += this.HandleAdLoadFailed;
		this.bannerAd.OnAdDisplayed += this.HandleAdDisplayed;
		this.bannerAd.OnAdDismissed += this.HandleAdDismissed;
		this.bannerAd.OnAdInteraction += this.HandleAdInteraction;
		this.bannerAd.OnUserLeftApplication += this.HandleUserLeftApplication;

		// Load a banner ad.
		this.bannerAd.LoadAd ();
	}

	private void RequestInterstitial ()
	{
		// These ad units are configured to always serve test ads.
		#if UNITY_ANDROID
		string placementId = "1469137441636";
		#elif UNITY_IPHONE
		string placementId = "1467548435003";
		#endif

		// Create an interstitial.
		this.interstitialAd = new InterstitialAd (placementId);

		// Register for ad events.
		this.interstitialAd.OnAdLoadSucceeded += this.HandleOnAdLoadSucceeded;
		this.interstitialAd.OnAdLoadFailed += this.HandleAdLoadFailed;
		this.interstitialAd.OnAdDisplayed += this.HandleAdDisplayed;
		this.interstitialAd.OnAdDismissed += this.HandleAdDismissed;
		this.interstitialAd.OnAdInteraction += this.HandleAdInteraction;
		this.interstitialAd.OnUserLeftApplication += this.HandleUserLeftApplication;

		this.interstitialAd.OnAdReceived += this.HandleAdReceived;
		this.interstitialAd.OnAdDisplayFailed += this.HandleAdDisplayFailed;
		this.interstitialAd.OnAdWillDisplay += this.HandleAdWillDisplay;

		// Load an interstitial ad.
		this.interstitialAd.LoadAd ();
	}

    
	private void RequestRewardBasedVideo ()
	{
		#if UNITY_ANDROID
		string placementId = "1465237901291";
		#elif UNITY_IPHONE
		string placementId = "1465883204802";
		#endif

		this.rewardedVideoAd = new RewardedVideoAd (placementId);

		// Register for ad events.
		this.rewardedVideoAd.OnAdLoadSucceeded += this.HandleOnAdLoadSucceeded;
		this.rewardedVideoAd.OnAdLoadFailed += this.HandleAdLoadFailed;
		this.rewardedVideoAd.OnAdDisplayed += this.HandleAdDisplayed;
		this.rewardedVideoAd.OnAdDismissed += this.HandleAdDismissed;
		this.rewardedVideoAd.OnAdInteraction += this.HandleAdInteraction;
		this.rewardedVideoAd.OnUserLeftApplication += this.HandleUserLeftApplication;

		this.rewardedVideoAd.OnAdReceived += this.HandleAdReceived;
		this.rewardedVideoAd.OnAdDisplayFailed += this.HandleAdDisplayFailed;
		this.rewardedVideoAd.OnAdWillDisplay += this.HandleAdWillDisplay;

		this.rewardedVideoAd.OnAdRewardActionCompleted += this.HandleRewardActionCompleted;

		this.rewardedVideoAd.LoadAd ();
	}

	private void ShowInterstitial ()
	{
		if (this.interstitialAd.isReady ()) {
			this.interstitialAd.Show ();
		} else {
			MonoBehaviour.print ("Interstitial is not ready yet");
		}
	}

	private void ShowRewardBasedVideo ()
	{
		if (this.rewardedVideoAd.isReady ()) {
			this.rewardedVideoAd.Show ();
		} else {
			MonoBehaviour.print ("Rewarded video ad is not ready yet");
		}
	}

	#region callback handlers

	public void HandleOnAdLoadSucceeded (object sender, EventArgs args)
	{
		MonoBehaviour.print ("HandleOnAdLoadSucceded event received");
	}

	public void HandleAdLoadFailed (object sender, AdLoadFailedEventArgs args)
	{
		MonoBehaviour.print ("HandleAdLoadFailed event received with message: " + args.Error);
	}

	public void HandleAdDisplayed (object sender, EventArgs args)
	{
		MonoBehaviour.print ("HandleAdDisplayed event received");
	}

	public void HandleAdDismissed (object sender, EventArgs args)
	{
		MonoBehaviour.print ("HandleAdDismissed event received");
	}

	public void HandleAdInteraction (object sender, AdInteractionEventArgs args)
	{
		MonoBehaviour.print ("HandleAdDismissed event received " + args.Message);
	}

	public void HandleUserLeftApplication (object sender, EventArgs args)
	{
		MonoBehaviour.print ("HandleUserLeftApplication event received");
	}

	#endregion

	#region Interstitial specific callback handlers

	public void HandleAdReceived (object sender, EventArgs args)
	{
		MonoBehaviour.print ("HandleAdReceived event received");
	}

	public void HandleAdWillDisplay (object sender, EventArgs args)
	{
		MonoBehaviour.print (
			"HandleAdWillDisplay event received with message: ");
	}

	public void HandleAdDisplayFailed (object sender, EventArgs args)
	{
		MonoBehaviour.print ("HandleAdDisplayFailed event received");
	}

  

	#endregion


	#region RewardBasedVideo callback handlers


	public void HandleRewardActionCompleted (object sender, AdRewardActionCompletedEventArgs args)
	{
     
		MonoBehaviour.print (
			"HandleRewardActionCompleted event received for " + args.Rewards);
	}

    

	#endregion
}