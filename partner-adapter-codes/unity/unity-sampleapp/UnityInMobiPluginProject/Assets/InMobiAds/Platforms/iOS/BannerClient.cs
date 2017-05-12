#if UNITY_IOS
using System;
using System.Collections.Generic;
using System.Runtime.InteropServices;

using InMobiAds.Api;
using InMobiAds.Common;

namespace InMobiAds.Platforms.iOS
{
	internal class BannerClient : IBannerAdClient, IDisposable
	{
		private IntPtr bannerAdPtr;

		private IntPtr bannerClientPtr;

		#region Banner callback types

		internal delegate void InMobiBannerOnAdLoadSucceeded(IntPtr bannerClient);

		internal delegate void InMobiBannerOnAdLoadFailed(IntPtr bannerClient, string error);

		internal delegate void InMobiBannerOnAdInteraction(IntPtr bannerClient, string message);

		internal delegate void InMobiBannerOnAdDisplayed(IntPtr bannerClient);

		internal delegate void InMobiBannerOnAdDismissed(IntPtr bannerClient);

		internal delegate void InMobiBannerOnUserLeftApplication(IntPtr bannerClient);

		internal delegate void InMobiBannerOnAdRewardActionCompleted(IntPtr bannerClient, string rewards);

		#endregion

		public event EventHandler<EventArgs> OnAdLoadSucceeded;

		public event EventHandler<AdLoadFailedEventArgs> OnAdLoadFailed;

		public event EventHandler<EventArgs> OnAdDisplayed;

		public event EventHandler<EventArgs> OnAdDismissed;

		public event EventHandler<AdInteractionEventArgs> OnAdInteraction;

		public event EventHandler<EventArgs> OnUserLeftApplication;

		public event EventHandler<AdRewardActionCompletedEventArgs> OnAdRewardActionCompleted;

		// This property should be used when setting the bannerViewPtr.
		private IntPtr BannerAdPtr
		{
			get
			{
				return this.bannerAdPtr;
			}

			set
			{
				InMobiBinding.InMobiRelease(this.bannerAdPtr);
				this.bannerAdPtr = value;
			}
		}

		#region IBannerClient Implementation
		// Creates a banner view and adds it to the view hierarchy.
		public void CreateBannerAd (string placementId, int width, int height, int position){
			this.bannerClientPtr = (IntPtr)GCHandle.Alloc(this);
			this.BannerAdPtr = InMobiBinding.InMobiCreateBannerAd (this.bannerClientPtr, placementId, width, height, position);
			InMobiBinding.SetBannerCallbacks (BannerAdPtr,
				onAdLoadSucceeded,
				onAdLoadFailed,
				onAdDisplayed,
				onAdDismissed,
				onAdInteraction,
				onUserLeftApplication,
				onAdRewardActionCompleted);
		}


		//Set Keywords for the banner view.
		public void SetKeywords (String keywords){
			InMobiBinding.SetBannerKeyWords (this.BannerAdPtr, keywords);
		}

		//Set Enable Auto Refresh for the banner view.
		public void SetEnableAutoRefresh (bool flag){
			InMobiBinding.SetBannerEnableAutoRefresh (this.BannerAdPtr, flag);
		}

		//Set Refresh Interval for the banner view.
		public void SetRefreshInterval (int interval){
			InMobiBinding.SetBannerRefreshInterval (this.BannerAdPtr, interval);
		}

		// Requests a new ad for the banner view.
		public void LoadAd (){
			InMobiBinding.LoadBannerAd (this.BannerAdPtr);
		}

		// Destroys a banner ad.
		public void DestroyBannerAd (){
			InMobiBinding.DestroyBannerAd (this.BannerAdPtr);
		}

		public void Dispose()
		{
			this.DestroyBannerAd();
			((GCHandle)this.bannerClientPtr).Free();
		}

		~BannerClient()
		{
			this.Dispose();
		}
		#endregion

		#region Banner callback methods

		[MonoPInvokeCallback(typeof(InMobiBannerOnAdLoadSucceeded))]
		private static void onAdLoadSucceeded(IntPtr bannerClient){
			BannerClient client = IntPtrToBannerClient (bannerClient);
			client.OnAdLoadSucceeded (client, EventArgs.Empty);
		}

		[MonoPInvokeCallbackAttribute(typeof(InMobiBannerOnAdLoadFailed))]
		private static void onAdLoadFailed(IntPtr bannerClient, string error){
			BannerClient client = IntPtrToBannerClient (bannerClient);
			AdLoadFailedEventArgs args = new AdLoadFailedEventArgs(){
				Error=error
			};
			client.OnAdLoadFailed(client, args);
		}

		[MonoPInvokeCallbackAttribute(typeof(InMobiBannerOnAdInteraction))]
		private static void onAdInteraction(IntPtr bannerClient, string message){
			BannerClient client = IntPtrToBannerClient (bannerClient);
			AdInteractionEventArgs args = new AdInteractionEventArgs () {
				Message=message
			};
			client.OnAdInteraction (client, args);
		}

		[MonoPInvokeCallbackAttribute(typeof(InMobiBannerOnAdDisplayed))]
		private static void onAdDisplayed(IntPtr bannerClient){
			BannerClient client = IntPtrToBannerClient (bannerClient);
			client.OnAdDisplayed (client, EventArgs.Empty);
		}

		[MonoPInvokeCallbackAttribute(typeof(InMobiBannerOnAdDismissed))]
		private static void onAdDismissed(IntPtr bannerClient){
			BannerClient client = IntPtrToBannerClient (bannerClient);
			client.OnAdDismissed (client, EventArgs.Empty);
		}

		[MonoPInvokeCallbackAttribute(typeof(InMobiBannerOnUserLeftApplication))]
		private static void onUserLeftApplication(IntPtr bannerClient){
			BannerClient client = IntPtrToBannerClient (bannerClient);
			client.OnUserLeftApplication (client, EventArgs.Empty);
		}

		[MonoPInvokeCallbackAttribute(typeof(InMobiBannerOnAdRewardActionCompleted))]
		private static void onAdRewardActionCompleted(IntPtr bannerClient, string rewards){
			BannerClient client = IntPtrToBannerClient (bannerClient);
			AdRewardActionCompletedEventArgs args = new AdRewardActionCompletedEventArgs () {
				Rewards = rewards
			};
			client.OnAdRewardActionCompleted (client, args);
		}

		private static BannerClient IntPtrToBannerClient(IntPtr bannerClient)
		{
			GCHandle handle = (GCHandle)bannerClient;
			return handle.Target as BannerClient;
		}

		#endregion
	}
}
#endif

