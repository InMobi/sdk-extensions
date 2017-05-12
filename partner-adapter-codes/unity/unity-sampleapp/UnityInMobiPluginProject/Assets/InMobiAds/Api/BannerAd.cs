using System;
using InMobiAds;
using InMobiAds.Common;

namespace InMobiAds.Api
{
	public class BannerAd
	{
		private IBannerAdClient IBannerAdClient;

		public event EventHandler<EventArgs> OnAdLoadSucceeded = delegate{};
		public event EventHandler<AdLoadFailedEventArgs> OnAdLoadFailed = delegate{};
		public event EventHandler<EventArgs> OnAdDisplayed = delegate{};
		public event EventHandler<EventArgs> OnAdDismissed = delegate{};
		public event EventHandler<AdInteractionEventArgs> OnAdInteraction = delegate{};
		public event EventHandler<EventArgs> OnUserLeftApplication = delegate{};
		//public event EventHandler<AdRewardActionCompletedEventArgs> OnAdRewardActionCompleted = delegate{};

		public BannerAd (string placementId, int width, int height, int position)
		{
			IBannerAdClient = InMobiAdsClientFactory.BuildBannerClient ();
			IBannerAdClient.CreateBannerAd (placementId, width, height, position);
			IBannerAdClient.OnAdLoadSucceeded += delegate(object sender, EventArgs e) {
				OnAdLoadSucceeded (this, e);
			};

			IBannerAdClient.OnAdLoadFailed += delegate(object sender, AdLoadFailedEventArgs e) {
				OnAdLoadFailed (this, e);
			};

			IBannerAdClient.OnAdDisplayed += delegate(object sender, EventArgs e) {
				OnAdDisplayed (this, e);
			};

			IBannerAdClient.OnAdDismissed += delegate(object sender, EventArgs e) {
				OnAdDismissed (this, e);
			};

			IBannerAdClient.OnAdInteraction += delegate(object sender, AdInteractionEventArgs e) {
				OnAdInteraction (this, e);
			};

			IBannerAdClient.OnUserLeftApplication += delegate(object sender, EventArgs e) {
				OnUserLeftApplication (this, e);
			};

			IBannerAdClient.OnAdRewardActionCompleted += delegate(object sender, AdRewardActionCompletedEventArgs e) {
				//OnAdRewardActionCompleted (this, e);
				//Do nothing
			};
		}

		public void SetKeywords (string keywords)
		{
			IBannerAdClient.SetKeywords (keywords);
		}

		public void SetEnableAutoRefresh (bool flag)
		{
			IBannerAdClient.SetEnableAutoRefresh (flag);
		}

		public void SetRefreshInterval (int interval)
		{
			IBannerAdClient.SetRefreshInterval (interval);
		}

		public void LoadAd ()
		{
			IBannerAdClient.LoadAd ();
		}

		public void DestroyAd ()
		{
			IBannerAdClient.DestroyBannerAd ();
		}
	}
}

