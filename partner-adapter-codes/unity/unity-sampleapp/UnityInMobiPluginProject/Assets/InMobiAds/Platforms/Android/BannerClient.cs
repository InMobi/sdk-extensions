#if UNITY_ANDROID
using System;
using UnityEngine;
using System.Collections.Generic;

using InMobiAds.Api;
using InMobiAds.Common;

namespace InMobiAds.Platforms.Android{

	internal class BannerClient : AndroidJavaProxy, IBannerAdClient
	{
		private AndroidJavaObject bannerAd;

		public BannerClient() : base(Utils.BannerAdListenerClassName)
		{
			this.bannerAd = new AndroidJavaObject (Utils.BannerAdClassName, this);
		}

		public event EventHandler<EventArgs> OnAdLoadSucceeded;

		public event EventHandler<AdLoadFailedEventArgs> OnAdLoadFailed;

		public event EventHandler<EventArgs> OnAdDisplayed;

		public event EventHandler<EventArgs> OnAdDismissed;

		public event EventHandler<AdInteractionEventArgs> OnAdInteraction;

		public event EventHandler<EventArgs> OnUserLeftApplication;

		public event EventHandler<AdRewardActionCompletedEventArgs> OnAdRewardActionCompleted;

		// Creates a banner view and adds it to the view hierarchy.
		public void CreateBannerAd (string placementId, int width, int height, int position){
			this.bannerAd.Call ("create", new object[4]{ placementId, width, height, position });
		}

		//Set Keywords for the banner view.
		public void SetKeywords (String keywords){
			this.bannerAd.Call ("setKeywords", new object[1]{ keywords });
		}

		//Set Enable Auto Refresh for the banner view.
		public void SetEnableAutoRefresh (bool flag){
			this.bannerAd.Call ("setEnableAutoRefresh", new object[1]{ flag });
		}

		//Set Refresh Interval for the banner view.
		public void SetRefreshInterval (int interval){
			this.bannerAd.Call ("setRefreshInterval", new object[1]{ interval });
		}

		// Requests a new ad for the banner view.
		public void LoadAd (){
			this.bannerAd.Call ("load");
		}

		// Destroys a banner view.
		public void DestroyBannerAd (){
			if (this.bannerAd != null) {
				this.bannerAd.Call ("destroy");
			} else {
				MonoBehaviour.print ("Banner Ad object is already destroyed");
			}
		}

		#region Callbacks from UnityBannerAdListener

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
		#endregion
	}
}
#endif