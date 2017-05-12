#if UNITY_IOS
using System;
using System.Collections.Generic;
using System.Runtime.InteropServices;

using InMobiAds.Api;
using InMobiAds.Common;

namespace InMobiAds.Platforms.iOS
{
	public class InterstitialClient : IInterstitialClient, IDisposable
	{
		private IntPtr interstitialAdPtr;

		private IntPtr interstitialClientPtr;

		#region Interstitial callback types

		internal delegate void InMobiInterstitialOnAdReceived(IntPtr interstitialClient);

		internal delegate void InMobiInterstitialOnAdLoadSucceeded(IntPtr interstitialClient);

		internal delegate void InMobiInterstitialOnAdLoadFailed(IntPtr interstitialClient, string error);

		internal delegate void InMobiInterstitialOnAdInteraction(IntPtr interstitialClient, string message);

		internal delegate void InMobiInterstitialOnAdDisplayFailed(IntPtr interstitialClient);

		internal delegate void InMobiInterstitialOnAdWillDisplay(IntPtr interstitialClient);

		internal delegate void InMobiInterstitialOnAdDisplayed(IntPtr interstitialClient);

		internal delegate void InMobiInterstitialOnAdDismissed(IntPtr interstitialClient);

		internal delegate void InMobiInterstitialOnUserLeftApplication(IntPtr interstitialClient);

		internal delegate void InMobiInterstitialOnAdRewardActionCompleted(IntPtr interstitialClient, string rewards);

		#endregion

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

		// This property should be used when setting the bannerViewPtr.
		private IntPtr InterstitialAdPtr
		{
			get
			{
				return this.interstitialAdPtr;
			}

			set
			{
				InMobiBinding.InMobiRelease(this.interstitialAdPtr);
				this.interstitialAdPtr = value;
			}
		}


		//Create a Intersttitial Ad
		public void CreateInterstitialAd(string placementId){
			this.interstitialClientPtr = (IntPtr)GCHandle.Alloc (this);
			this.InterstitialAdPtr = InMobiBinding.InMobiCreateInterstitialAd (interstitialClientPtr, placementId);
			InMobiBinding.SetInterstitialCallbacks(interstitialAdPtr,
				onAdReceived,
				onAdLoadSucceeded,
				onAdLoadFailed,
				onAdDisplayFailed,
				onAdWillDisplay,
				onAdDisplayed,
				onAdDismissed,
				onAdInteraction,
				onUserLeftApplication,
				onAdRewardActionCompleted);
		}

		//Load Interstitial Ad
		public void LoadAd(){
			InMobiBinding.LoadInterstitialAd (this.interstitialAdPtr);
		}

		//Check if Interstitial is Ready
		public bool IsReady(){
			return InMobiBinding.IsInterstitialAdReady (this.interstitialAdPtr);
		}

		//Show Interstitial Ad
		public void Show(){
			InMobiBinding.ShowInterstitialAd (this.interstitialAdPtr);
		}

		//Set Keywords
		public void SetKeywords(string keywords){
			InMobiBinding.SetInterstitialKeyWords (this.interstitialAdPtr, keywords);
		}

		public void Dispose()
		{
			((GCHandle)this.interstitialClientPtr).Free();
		}


		#region Interstitial callback methods

		[MonoPInvokeCallback(typeof(InMobiInterstitialOnAdReceived))]
		private static void onAdReceived(IntPtr interstitialClient){
			InterstitialClient client = IntPtrToInterstitialClient (interstitialClient);
			client.OnAdReceived (client, EventArgs.Empty);
		}

		[MonoPInvokeCallback(typeof(InMobiInterstitialOnAdLoadSucceeded))]
		private static void onAdLoadSucceeded(IntPtr interstitialClient){
			InterstitialClient client = IntPtrToInterstitialClient (interstitialClient);
			client.OnAdLoadSucceeded (client, EventArgs.Empty);
		}

		[MonoPInvokeCallbackAttribute(typeof(InMobiInterstitialOnAdLoadFailed))]
		private static void onAdLoadFailed(IntPtr interstitialClient, string error){
			InterstitialClient client = IntPtrToInterstitialClient (interstitialClient);
			AdLoadFailedEventArgs args = new AdLoadFailedEventArgs(){
				Error=error
			};
			client.OnAdLoadFailed(client, args);
		}

		[MonoPInvokeCallback(typeof(InMobiInterstitialOnAdDisplayFailed))]
		private static void onAdDisplayFailed(IntPtr interstitialClient){
			InterstitialClient client = IntPtrToInterstitialClient (interstitialClient);
			client.OnAdDisplayFailed (client, EventArgs.Empty);
		}

		[MonoPInvokeCallback(typeof(InMobiInterstitialOnAdWillDisplay))]
		private static void onAdWillDisplay(IntPtr interstitialClient){
			InterstitialClient client = IntPtrToInterstitialClient (interstitialClient);
			client.OnAdWillDisplay (client, EventArgs.Empty);
		}

		[MonoPInvokeCallbackAttribute(typeof(InMobiInterstitialOnAdInteraction))]
		private static void onAdInteraction(IntPtr interstitialClient, string message){
			InterstitialClient client = IntPtrToInterstitialClient (interstitialClient);
			AdInteractionEventArgs args = new AdInteractionEventArgs () {
				Message=message
			};
			client.OnAdInteraction (client, args);
		}

		[MonoPInvokeCallbackAttribute(typeof(InMobiInterstitialOnAdDisplayed))]
		private static void onAdDisplayed(IntPtr interstitialClient){
			InterstitialClient client = IntPtrToInterstitialClient (interstitialClient);
			client.OnAdDisplayed (client, EventArgs.Empty);
		}

		[MonoPInvokeCallbackAttribute(typeof(InMobiInterstitialOnAdDismissed))]
		private static void onAdDismissed(IntPtr interstitialClient){
			InterstitialClient client = IntPtrToInterstitialClient (interstitialClient);
			client.OnAdDismissed (client, EventArgs.Empty);
		}

		[MonoPInvokeCallbackAttribute(typeof(InMobiInterstitialOnUserLeftApplication))]
		private static void onUserLeftApplication(IntPtr interstitialClient){
			InterstitialClient client = IntPtrToInterstitialClient (interstitialClient);
			client.OnUserLeftApplication (client, EventArgs.Empty);
		}

		[MonoPInvokeCallbackAttribute(typeof(InMobiInterstitialOnAdRewardActionCompleted))]
		private static void onAdRewardActionCompleted(IntPtr interstitialClient, string rewards){
			InterstitialClient client = IntPtrToInterstitialClient (interstitialClient);
			AdRewardActionCompletedEventArgs args = new AdRewardActionCompletedEventArgs () {
				Rewards = rewards
			};
			client.OnAdRewardActionCompleted (client, args);
		}

		private static InterstitialClient IntPtrToInterstitialClient(IntPtr interstitialClient)
		{
			GCHandle handle = (GCHandle)interstitialClient;
			return handle.Target as InterstitialClient;
		}
		#endregion
	}
}

#endif