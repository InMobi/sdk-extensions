//
//  InMobiTypes.h
//  Unity-iPhone
//
//  Created by Vineet Srivastava on 11/22/16.
//
//

/// Base type representing a InMobi* pointer.
typedef const void *InMobiRef;

/// Type representing a Unity banner client.
typedef const void *InMobiBannerClientRef;

/// Type representing a Unity Interstitial client.
typedef const void *InMobiInterstitialClientRef;

/// Type representing a InMobiBanner.
typedef const void *InMobiBannerRef;

/// Type representing a InMobiInterstitial.
typedef const void *InMobiInterstitialRef;

/// Callback for when a banner ad request was successfully loaded.
typedef void (*InMobiBannerOnAdLoadSucceeded)(InMobiBannerClientRef *bannerClient);

/// Callback for when a banner ad request failed.
typedef void (*InMobiBannerOnAdLoadFailed)(InMobiBannerClientRef *bannerClient,const char *error);

///Callback for when a banner ad is interacted
typedef void (*InMobiBannerOnAdInteraction)(InMobiBannerClientRef *bannerClient, const char *message);

/// Callback for when a banner ad is dismissed
typedef void (*InMobiBannerOnAdDismissed)(InMobiBannerClientRef *bannerClient);

/// Callback for when a full screen view is presented as a result of a banner click.
typedef void (*InMobiBannerOnAdDisplayed)(InMobiBannerClientRef *bannerClient);

/// Callback for when an application will background or terminate as a result of a banner click.
typedef void (*InMobiBannerOnUserLeftApplication)(InMobiBannerClientRef *bannerClient);

///Callback for when user is rewarded
typedef void (*InMobiBannerOnAdRewardActionCompleted)(InMobiBannerClientRef *bannerClient, const char *rewards);


/// Callback for when a interstitial ad request was successfully received.
typedef void (*InMobiInterstitialOnAdReceived)(InMobiInterstitialClientRef *interstitialClient);

/// Callback for when a interstitial ad request was successfully loaded.
typedef void (*InMobiInterstitialOnAdLoadSucceeded)(InMobiInterstitialClientRef *interstitialClient);

/// Callback for when a interstitial ad request failed.
typedef void (*InMobiInterstitialOnAdLoadFailed)(InMobiInterstitialClientRef *interstitialClient,const char *error);

///Callback for when a interstitial ad is interacted
typedef void (*InMobiInterstitialOnAdInteraction)(InMobiInterstitialClientRef *interstitialClient, const char *message);

/// Callback for when a banner ad is dismissed
typedef void (*InMobiInterstitialOnAdDismissed)(InMobiInterstitialClientRef *interstitialClient);

/// Callback for when a interstitial is displayed
typedef void (*InMobiInterstitialOnAdDisplayed)(InMobiInterstitialClientRef *interstitialClient);

/// Callback for when a interstitial is display failed
typedef void (*InMobiInterstitialOnAdDisplayFailed)(InMobiInterstitialClientRef *interstitialClient);

/// Callback for when a interstitial will Display
typedef void (*InMobiInterstitialWillDisplay)(InMobiInterstitialClientRef *interstitialClient);

/// Callback for when an application will background or terminate as a result of a interstitial click.
typedef void (*InMobiInterstitialOnUserLeftApplication)(InMobiInterstitialClientRef *interstitialClient);

///Callback for when user is rewarded
typedef void (*InMobiInterstitialOnAdRewardActionCompleted)(InMobiInterstitialClientRef *interstitialClient, const char *rewards);
