using System;
using InMobiAds;
using InMobiAds.Common;

namespace InMobiAds.Api
{
	public class InterstitialAd
	{
		private IInterstitialClient IInterstitialClient;

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

		public InterstitialAd(string placementId)
		{
			IInterstitialClient = InMobiAdsClientFactory.BuildInterstitialClient ();
			IInterstitialClient.CreateInterstitialAd (placementId);
			IInterstitialClient.OnAdReceived += delegate(object sender, EventArgs e) {
				OnAdReceived (this, e);
			};

			IInterstitialClient.OnAdLoadSucceeded += delegate(object sender, EventArgs e) {
				OnAdLoadSucceeded (this, e);
			};

			IInterstitialClient.OnAdLoadFailed += delegate(object sender, AdLoadFailedEventArgs e) {
				OnAdLoadFailed (this, e);
			};

			IInterstitialClient.OnAdDisplayFailed += delegate(object sender, EventArgs e) {
				OnAdDisplayFailed (this, e);
			};

			IInterstitialClient.OnAdWillDisplay += delegate(object sender, EventArgs e) {
				OnAdWillDisplay (this, e);
			};

			IInterstitialClient.OnAdDisplayed += delegate(object sender, EventArgs e) {
				OnAdDisplayed (this, e);
			};

			IInterstitialClient.OnAdDismissed += delegate(object sender, EventArgs e) {
				OnAdDismissed (this, e);
			};

			IInterstitialClient.OnAdInteraction += delegate(object sender, AdInteractionEventArgs e) {
				OnAdInteraction (this, e);
			};

			IInterstitialClient.OnUserLeftApplication += delegate(object sender, EventArgs e) {
				OnUserLeftApplication (this, e);
			};

			IInterstitialClient.OnAdRewardActionCompleted += delegate(object sender, AdRewardActionCompletedEventArgs e) {
				OnAdRewardActionCompleted (this, e);
			};
		}

		public void LoadAd ()
		{
			IInterstitialClient.LoadAd ();
		}

		public void SetKeywords (string keywords)
		{
			IInterstitialClient.SetKeywords (keywords);
		}

		public bool isReady(){
			return IInterstitialClient.IsReady ();
		}

		public void Show ()
		{
			IInterstitialClient.Show ();
		}
	}
}

