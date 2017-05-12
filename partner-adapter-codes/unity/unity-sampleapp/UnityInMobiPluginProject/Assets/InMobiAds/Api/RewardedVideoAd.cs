using System;
using InMobiAds;
using InMobiAds.Common;

namespace InMobiAds.Api
{
	public class RewardedVideoAd
	{
		private IInterstitialClient IRewardedVideoAdClient;

		public event EventHandler<EventArgs> OnAdReceived = delegate{};
		public event EventHandler<EventArgs> OnAdLoadSucceeded = delegate{};
		public event EventHandler<AdLoadFailedEventArgs> OnAdLoadFailed = delegate{};
		public event EventHandler<EventArgs> OnAdDisplayFailed = delegate{};
		public event EventHandler<EventArgs> OnAdWillDisplay = delegate{};
		public event EventHandler<EventArgs> OnAdDisplayed = delegate{};
		public event EventHandler<EventArgs> OnAdDismissed = delegate{};
		public event EventHandler<AdInteractionEventArgs> OnAdInteraction = delegate{};
		public event EventHandler<EventArgs> OnUserLeftApplication = delegate{};
		public event EventHandler<AdRewardActionCompletedEventArgs> OnAdRewardActionCompleted = delegate{};

		public RewardedVideoAd(string placementId)
		{
			IRewardedVideoAdClient = InMobiAdsClientFactory.BuildRewardedVideoClient ();
			IRewardedVideoAdClient.CreateInterstitialAd (placementId);
			IRewardedVideoAdClient.OnAdReceived += delegate(object sender, EventArgs e) {
				OnAdReceived (this, e);
			};

			IRewardedVideoAdClient.OnAdLoadSucceeded += delegate(object sender, EventArgs e) {
				OnAdLoadSucceeded (this, e);
			};

			IRewardedVideoAdClient.OnAdLoadFailed += delegate(object sender, AdLoadFailedEventArgs e) {
				OnAdLoadFailed (this, e);
			};

			IRewardedVideoAdClient.OnAdDisplayFailed += delegate(object sender, EventArgs e) {
				OnAdDisplayFailed (this, e);
			};

			IRewardedVideoAdClient.OnAdWillDisplay += delegate(object sender, EventArgs e) {
				OnAdWillDisplay (this, e);
			};

			IRewardedVideoAdClient.OnAdDisplayed += delegate(object sender, EventArgs e) {
				OnAdDisplayed (this, e);
			};

			IRewardedVideoAdClient.OnAdDismissed += delegate(object sender, EventArgs e) {
				OnAdDismissed (this, e);
			};

			IRewardedVideoAdClient.OnAdInteraction += delegate(object sender, AdInteractionEventArgs e) {
				OnAdInteraction (this, e);
			};

			IRewardedVideoAdClient.OnUserLeftApplication += delegate(object sender, EventArgs e) {
				OnUserLeftApplication (this, e);
			};

			IRewardedVideoAdClient.OnAdRewardActionCompleted += delegate(object sender, AdRewardActionCompletedEventArgs e) {
				OnAdRewardActionCompleted (this, e);
			};
		}

		public void LoadAd ()
		{
			IRewardedVideoAdClient.LoadAd ();
		}

		public void SetKeywords (string keywords)
		{
			IRewardedVideoAdClient.SetKeywords (keywords);
		}

		public bool isReady(){
			return IRewardedVideoAdClient.IsReady ();
		}

		public void Show ()
		{
			IRewardedVideoAdClient.Show ();
		}
	}
}

