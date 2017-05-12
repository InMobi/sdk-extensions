using System;
using InMobiAds.Api;

namespace InMobiAds.Common
{
	internal interface IInterstitialClient
	{
		event EventHandler<EventArgs> OnAdReceived;
		event EventHandler<EventArgs> OnAdLoadSucceeded;
		event EventHandler<AdLoadFailedEventArgs> OnAdLoadFailed;
		event EventHandler<EventArgs> OnAdDisplayFailed;
		event EventHandler<EventArgs> OnAdWillDisplay;
		event EventHandler<EventArgs> OnAdDisplayed;
		event EventHandler<EventArgs> OnAdDismissed;
		event EventHandler<AdInteractionEventArgs> OnAdInteraction;
		event EventHandler<EventArgs> OnUserLeftApplication;
		event EventHandler<AdRewardActionCompletedEventArgs> OnAdRewardActionCompleted;

		//Create a Intersttitial Ad
		void CreateInterstitialAd(string placementId);

		//Load Interstitial Ad
		void LoadAd();

		//Check if Interstitial is Ready
		bool IsReady();

		//Show Interstitial Ad
		void Show();

		//Set Keywords
		void SetKeywords(string keywords);
	}
}

