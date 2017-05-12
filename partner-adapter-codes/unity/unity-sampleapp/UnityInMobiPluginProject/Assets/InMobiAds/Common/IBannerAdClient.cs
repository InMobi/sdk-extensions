using System;
using InMobiAds.Api;

namespace InMobiAds.Common
{
	internal interface IBannerAdClient
	{
		event EventHandler<EventArgs> OnAdLoadSucceeded;
		event EventHandler<AdLoadFailedEventArgs> OnAdLoadFailed;
		event EventHandler<EventArgs> OnAdDisplayed;
		event EventHandler<EventArgs> OnAdDismissed;
		event EventHandler<AdInteractionEventArgs> OnAdInteraction;
		event EventHandler<EventArgs> OnUserLeftApplication;
		event EventHandler<AdRewardActionCompletedEventArgs> OnAdRewardActionCompleted;



		// Creates a banner view and adds it to the view hierarchy.
		void CreateBannerAd (string placementId, int width, int height, int position);

		//Set Keywords for the banner view.
		void SetKeywords (String keywords);

		//Set Enable Auto Refresh for the banner view.
		void SetEnableAutoRefresh (bool flag);

		//Set Refresh Interval for the banner view.
		void SetRefreshInterval (int interval);

		// Requests a new ad for the banner view.
		void LoadAd ();

		// Destroys a banner ad.
		void DestroyBannerAd ();
	}
}