//
//  InMobiInterstitial.h
//  Unity-iPhone
//
//  Created by Vineet Srivastava on 11/24/16.
//
//

@import Foundation;
@import InMobiSDK;

#import "InMobiTypes.h"

@interface InMobiInterstitial : NSObject

///Initialize InMobiInterstitial
-(id)initInterstitialAd:(InMobiInterstitialClientRef *)interstitialClient
            placementId:(NSString *)placementId;
@property(nonatomic,strong) IMInterstitial *interstitial;

@property(nonatomic,assign) InMobiInterstitialClientRef *interstitialClient;

///The ad received callback into Unity
@property(nonatomic,assign) InMobiInterstitialOnAdReceived onAdReceived;

///The ad received callback into Unity
@property(nonatomic,assign) InMobiInterstitialOnAdLoadSucceeded onAdLoadSucceeded;

///The ad failed to load callback into Unity
@property(nonatomic,assign) InMobiInterstitialOnAdLoadFailed onAdLoadFailed;

///User interaction callback into Unity
@property(nonatomic,assign) InMobiInterstitialOnAdInteraction onAdInteraction;

///The ad failed to load callback into Unity
@property(nonatomic,assign) InMobiInterstitialWillDisplay onAdWillDisplay;

///The ad failed to load callback into Unity
@property(nonatomic,assign) InMobiInterstitialOnAdDisplayFailed onAdDisplayFailed;

///Full Screen View is dismissed callback into Unity
@property(nonatomic,assign) InMobiInterstitialOnAdDisplayed onAdDisplayed;

///Full Screen View is dismissed callback into Unity
@property(nonatomic,assign) InMobiInterstitialOnAdDismissed onAdDismissed;

///User will leave application callback into Unity
@property(nonatomic,assign) InMobiInterstitialOnUserLeftApplication onUserLeftApplication;

///User is rewarded callback to Unity
@property(nonatomic, assign) InMobiInterstitialOnAdRewardActionCompleted onAdRewardActionCompleted;

-(void)setKeywords:(NSString*) keyWords;

-(void)loadAd;

-(bool)isReady;

-(void)showAd;

@end
