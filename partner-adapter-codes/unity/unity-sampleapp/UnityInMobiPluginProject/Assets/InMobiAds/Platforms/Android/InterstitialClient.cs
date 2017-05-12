#if UNITY_ANDROID
using System;
using UnityEngine;
using System.Collections.Generic;

using InMobiAds.Api;
using InMobiAds.Common;

namespace InMobiAds.Platforms.Android
{
	public class InterstitialClient: AndroidJavaProxy, IInterstitialClient
	{
		private AndroidJavaObject interstitialAd;
		public InterstitialClient (): base(Utils.InterstitialAdListenerClassName)
		{
			this.interstitialAd = new AndroidJavaObject (Utils.InterstitialAdClassName, this);
		
		}

		public event EventHandler<EventArgs> OnAdReceived;
		public event EventHandler<EventArgs> OnAdLoadSucceeded;
		public event EventHandler<AdLoadFailedEventArgs> OnAdLoadFailed;
		public event EventHandler<EventArgs> OnAdDisplayFailed;
		public event EventHandler<EventArgs> OnAdWillDisplay;
		public event EventHandler<EventArgs> OnAdDisplayed;
		public event EventHandler<EventArgs> OnAdDismissed;
		public event EventHandler<AdInteractionEventArgs> OnAdInteraction;
		public event EventHandler<EventArgs> OnUserLeftApplication;
		public event EventHandler<AdRewardActionCompletedEventArgs> OnAdRewardActionCompleted;

		public void CreateInterstitialAd(string placementId){
			this.interstitialAd.Call ("create", new object[1]{ placementId });
		}

		public void LoadAd(){
			this.interstitialAd.Call ("loadAd");
		}

		public bool IsReady(){
			return this.interstitialAd.Call<bool> ("isReady");
		}

		public void Show(){
			this.interstitialAd.Call ("show");
		}

		public void SetKeywords(string keywords){
			this.interstitialAd.Call ("setKeywords", new object[1]{ keywords });
		}

		//UnityInterstitial Ad Listener Callbacks

		public void onAdReceived()
		{
			this.OnAdReceived (this, EventArgs.Empty);
		}

		public void onAdLoadSucceeded()
		{
			this.OnAdLoadSucceeded (this, EventArgs.Empty);
		}

		public void onAdLoadFailed(string errorReason)
		{
			AdLoadFailedEventArgs args = new AdLoadFailedEventArgs () {
				Error = errorReason
			};
			this.OnAdLoadFailed(this, args);
		}

		public void onAdDisplayFailed()
		{
			this.OnAdDisplayFailed (this, EventArgs.Empty);
		}

		public void onAdWillDisplay()
		{
			this.OnAdWillDisplay (this, EventArgs.Empty);
		}

		public void onAdDisplayed()
		{
			this.OnAdDisplayed (this, EventArgs.Empty);
		}

		public void onAdDismissed()
		{
			this.OnAdDismissed (this, EventArgs.Empty);
		}

		public void onAdInteraction(string message)
		{
			AdInteractionEventArgs args = new AdInteractionEventArgs () {
				Message = message
			};
			this.OnAdInteraction (this, args);
		}

		public void onUserLeftApplication()
		{
			this.OnUserLeftApplication (this, EventArgs.Empty);
		}

		public void onAdRewardActionCompleted(string rewards)
		{
			AdRewardActionCompletedEventArgs args = new AdRewardActionCompletedEventArgs () {
				Rewards = rewards
			};
			this.OnAdRewardActionCompleted (this, args);
		}
	}
}
#endif

