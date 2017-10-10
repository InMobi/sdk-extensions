#if UNITY_IOS
using System;
using System.Runtime.InteropServices;

namespace InMobiAds.Platforms.iOS
{
	internal class InMobiBinding
	{
		[DllImport("__Internal")]
		internal static extern void InMobiRelease(IntPtr obj);

		#region InMobi SDK API externs
		[DllImport("__Internal")]
		internal static extern void Init (string accountId);

		//Set log level
		[DllImport("__Internal")]
		internal static extern void SetLogLevel(string logLevel);

		//Set Age
		[DllImport("__Internal")]
		internal static extern void SetAge(int age);

		//Set AgeGroup
		[DllImport("__Internal")]
		internal static extern void SetAgeGroup(string ageGroup);

		//Set AreaCode
		[DllImport("__Internal")]
		internal static extern void SetAreaCode(string areaCode);

		//Set PostalCode
		[DllImport("__Internal")]
		internal static extern void SetPostalCode(string postalCode);

		//Set Location With City State Country
		[DllImport("__Internal")]
		internal static extern void SetLocationWithCityStateCountry(string city, string state, string country);

		//Set Year of Birth
		[DllImport("__Internal")]
		internal static extern void SetYearOfBirth(int yearOfBirth);

		//Set Gender GENDER_MALE or GENDER_FEMALE
		[DllImport("__Internal")]
		internal static extern void SetGender(string gender);

		//set Education EDUCATION_HIGHSCHOOLORLESS, EDUCATION_COLLEGEORGRADUATE, EDUCATION_POSTGRADUATEORABOVE
		[DllImport("__Internal")]
		internal static extern void SetEducation(string education);

		//set Ethnicity ASIAN AFRICAN_AMERICAN CAUCASIAN HISPANIC OTHER
		[DllImport("__Internal")]
		internal static extern void SetEthnicity(string ethnicity);

		//set Language
		[DllImport("__Internal")]
		internal static extern void SetLanguage(string language);

		//set Income
		[DllImport("__Internal")]
		internal static extern void SetIncome(int income);

		//set HouseHold Income
		//BELOW_USD_5K
		//BETWEEN_USD_5K_AND_10K
		//BETWEEN_USD_10K_AND_15K
		//BETWEEN_USD_15K_AND_20K
		//BETWEEN_USD_20K_AND_25K
		//BETWEEN_USD_25K_AND_50K
		//BETWEEN_USD_50K_AND_75K
		//BETWEEN_USD_75K_AND_100K
		//BETWEEN_USD_100K_AND_150K
		//ABOVE_USD_150K
		[DllImport("__Internal")]
		internal static extern void SetHouseHoldIncome(string incomeLevel);

		//set Interests
		[DllImport("__Internal")]
		internal static extern void SetInterests(string interests);

		//set Nationality
		[DllImport("__Internal")]
		internal static extern void SetNationality(string nationality);
		#endregion

		#region Banner Externs
		[DllImport("__Internal")]
		internal static extern IntPtr InMobiCreateBannerAd(IntPtr bannerClient, String placementId, int width, int height, int position);

		[DllImport("__Internal")]
		internal static extern void SetBannerKeyWords(IntPtr BannerAd, String keywords);

		[DllImport("__Internal")]
		internal static extern void SetBannerEnableAutoRefresh(IntPtr BannerAd, bool flag);

		[DllImport("__Internal")]
		internal static extern void SetBannerRefreshInterval(IntPtr BannerAd, int refreshInterval);

		[DllImport("__Internal")]
		internal static extern void LoadBannerAd(IntPtr BannerAd);

		[DllImport("__Internal")]
		internal static extern void DestroyBannerAd(IntPtr BannerAd);

		[DllImport("__Internal")]
		internal static extern void SetBannerCallbacks(IntPtr BannerAd,
			BannerClient.InMobiBannerOnAdLoadSucceeded onAdLoadSucceeded,
			BannerClient.InMobiBannerOnAdLoadFailed onAdLoadFailed,
			BannerClient.InMobiBannerOnAdDisplayed onAdDisplayed,
			BannerClient.InMobiBannerOnAdDismissed onAdDismissed,
			BannerClient.InMobiBannerOnAdInteraction onAdInteraction,
			BannerClient.InMobiBannerOnUserLeftApplication onUserLeftApplication,
			BannerClient.InMobiBannerOnAdRewardActionCompleted onAdRewardActionCompleted);
		#endregion

		#region Interstitial Externs
		[DllImport("__Internal")]
		internal static extern IntPtr InMobiCreateInterstitialAd (IntPtr interstitialClient, String placementId);

		[DllImport("__Internal")]
		internal static extern void SetInterstitialKeyWords(IntPtr InterstitialAd, String keywords);

		[DllImport("__Internal")]
		internal static extern void LoadInterstitialAd (IntPtr InterstitialAd);

		[DllImport("__Internal")]
		internal static extern bool IsInterstitialAdReady(IntPtr InterstitialAd);

		[DllImport("__Internal")]
		internal static extern void ShowInterstitialAd (IntPtr InterstitialAd);

		[DllImport("__Internal")]
		internal static extern void SetInterstitialCallbacks(IntPtr InterstitialAd,
			InterstitialClient.InMobiInterstitialOnAdReceived onAdReceived,
			InterstitialClient.InMobiInterstitialOnAdLoadSucceeded onAdLoadSucceeded,
			InterstitialClient.InMobiInterstitialOnAdLoadFailed onAdLoadFailed,
			InterstitialClient.InMobiInterstitialOnAdDisplayFailed onAdDisplayFailed,
			InterstitialClient.InMobiInterstitialOnAdWillDisplay onAdWilDisplayFailed,
			InterstitialClient.InMobiInterstitialOnAdDisplayed onAdDisplayed,
			InterstitialClient.InMobiInterstitialOnAdDismissed onAdDismissed,
			InterstitialClient.InMobiInterstitialOnAdInteraction onAdInteraction,
			InterstitialClient.InMobiInterstitialOnUserLeftApplication onUserLeftApplication,
			InterstitialClient.InMobiInterstitialOnAdRewardActionCompleted onAdRewardActionCompleted);
		#endregion
	}
}
#endif

