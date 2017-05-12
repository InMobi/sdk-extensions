#if UNITY_ANDROID
using System;

namespace InMobiAds.Platforms.Android
{
	internal class Utils
	{
		#region InMobi Ads Unity Plugin class names

		public const string InMobiPluginClassName = "com.unity.inmobi.plugin.InMobiPlugin";
		public const string InMobiPluginDummyListener = "com.unity.inmobi.plugin.InMobiPluginDummyListener";
		public const string BannerAdClassName = "com.unity.inmobi.plugin.Banner";
		public const string BannerAdListenerClassName = "com.unity.inmobi.plugin.UnityBannerAdListener";
		public const string InterstitialAdClassName = "com.unity.inmobi.plugin.Interstitial";
		public const string InterstitialAdListenerClassName = "com.unity.inmobi.plugin.UnityInterstitialAdListener";


		#endregion

		#region Unity class names

		//public const string UnityActivityClassName = "com.unity3d.player.UnityPlayer";

		#endregion
	}
}
#endif


