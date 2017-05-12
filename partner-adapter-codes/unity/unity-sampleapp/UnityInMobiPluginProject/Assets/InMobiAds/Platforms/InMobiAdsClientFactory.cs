using System;
using UnityEngine;

using InMobiAds.Api;
using InMobiAds.Common;

namespace InMobiAds
{
	internal class InMobiAdsClientFactory
	{
		internal static IBannerAdClient BuildBannerClient ()
		{
			#if UNITY_ANDROID
			return new InMobiAds.Platforms.Android.BannerClient();
			#elif (UNITY_5 && UNITY_IOS) || UNITY_IPHONE
			return new InMobiAds.Platforms.iOS.BannerClient();
			#else
			return null;
			#endif
		}

		internal static IInterstitialClient BuildInterstitialClient ()
		{
			#if UNITY_ANDROID
			return new InMobiAds.Platforms.Android.InterstitialClient();
			#elif (UNITY_5 && UNITY_IOS) || UNITY_IPHONE
			return new InMobiAds.Platforms.iOS.InterstitialClient();
			#else
			return null;
			#endif
		}

		internal static IInterstitialClient BuildRewardedVideoClient ()
		{
			#if UNITY_ANDROID
			return new InMobiAds.Platforms.Android.InterstitialClient();
			#elif (UNITY_5 && UNITY_IOS) || UNITY_IPHONE
			return new InMobiAds.Platforms.iOS.InterstitialClient();
			#else
			return null;
			#endif
		}

		internal static IInMobiPluginClient BuildInMobiPluginClient ()
		{
			#if UNITY_ANDROID
			return new InMobiAds.Platforms.Android.InMobiPluginClient ();
			#elif (UNITY_5 && UNITY_IOS) || UNITY_IPHONE
			return new InMobiAds.Platforms.iOS.InMobiPluginClient();
			#else 
			return null;
			#endif
		}
	}
}

