//
//  InMobiRewardedCustomEvent.m
//  InMobiMopubSampleApp
//
//  Created by Niranjan Agrawal on 28/10/15.
//
//

#import <Foundation/Foundation.h>
#import "InMobiRewardedCustomEvent.h"
#import "MPLogging.h"
#import "MPRewardedVideoReward.h"
#import "MPRewardedVideoError.h"
#import "MPInstanceProvider.h"
#import <InMobiSDK/IMSdk.h>


@interface MPInstanceProvider (InMobiInterstitials)

- (IMInterstitial *)buildIMRewardedInterstitialWithDelegate:(id)delegate placementId:(long long)placementId;

@end

@implementation MPInstanceProvider (InMobiInterstitials)

- (IMInterstitial *)buildIMRewardedInterstitialWithDelegate:(id)delegate placementId:(long long)placementId {
    IMInterstitial *inMobiInterstitial = [[IMInterstitial alloc] initWithPlacementId:placementId];
    inMobiInterstitial.delegate = delegate;
    return inMobiInterstitial;
}

@end


@implementation InMobiRewardedCustomEvent
@synthesize inMobiInterstitial = _inMobiInterstitial;

- (void)requestRewardedVideoWithCustomEventInfo:(NSDictionary *)info
{
    [IMSdk initWithAccountID:[info valueForKey:@"accountid"]];
//    self.inMobiInterstitial = [[MPInstanceProvider sharedProvider] buildIMRewardedInterstitialWithDelegate:self placementId:[[info valueForKey:@"placementid"] longLongValue]];
    self.inMobiInterstitial = [[MPInstanceProvider sharedProvider] buildIMRewardedInterstitialWithDelegate:self placementId:[@"1451381172077" longLongValue]];

    /*
     Mandatory params to be set by the publisher to identify the supply source type
     */

    NSMutableDictionary *paramsDict = [[NSMutableDictionary alloc] init];
    [paramsDict setObject:@"c_mopub" forKey:@"tp"];
    [paramsDict setObject:MP_SDK_VERSION forKey:@"tp-ver"];

    /*
     Sample for setting up the InMobi SDK Demographic params.
     Publisher need to set the values of params as they want.
     
     [IMSdk setAreaCode:@"1223"];
     [IMSdk setEducation:kIMSDKEducationHighSchoolOrLess];
     [IMSdk setGender:kIMSDKGenderMale];
     [IMSdk setAge:12];
     [IMSdk setPostalCode:@"234"];
     [IMSdk setLogLevel:kIMSDKLogLevelDebug];
     [IMSdk setLocationWithCity:@"BAN" state:@"KAN" country:@"IND"];
     [IMSdk setLanguage:@"ENG"];
     [IMSdk setIncome:1000];
     [IMSdk setEthnicity:kIMSDKEthnicityHispanic];
     */

    self.inMobiInterstitial.extras = paramsDict; // For supply source identification
    [self.inMobiInterstitial load];
}

- (BOOL)hasAdAvailable
{
    if (self.inMobiInterstitial!=NULL && [self.inMobiInterstitial isReady]) {
        return true;
    }
    return false;
}

- (void)presentRewardedVideoFromViewController:(UIViewController *)viewController
{
    if([self hasAdAvailable]){
    [self.inMobiInterstitial showFromViewController:viewController withAnimation:kIMInterstitialAnimationTypeCoverVertical];
    }
    else{
         NSError *error = [NSError errorWithDomain:MoPubRewardedVideoAdsSDKDomain code:MPRewardedVideoAdErrorNoAdsAvailable userInfo:nil];
         [self.delegate rewardedVideoDidFailToPlayForCustomEvent:self error:error];
    }
}

- (BOOL)enableAutomaticImpressionAndClickTracking{
    return YES;
}

-(void)interstitialDidFinishLoading:(IMInterstitial*)interstitial {
    MPLogInfo(@"InMobi interstitial did load");
    [self.delegate rewardedVideoDidLoadAdForCustomEvent:self];
    //[self.delegate interstitialCustomEvent:self didLoadAd:interstitial];
}

-(void)interstitialDidReceiveAd:(IMInterstitial *)interstitial{
    MPLogInfo(@"InMobi Adserver responded with an Ad");
}

- (void)handleCustomEventInvalidated
{
    //Do nothing
}

-(void)interstitial:(IMInterstitial*)interstitial didFailToLoadWithError:(IMRequestStatus*)error {
    MPLogInfo(@"InMobi interstitial did fail with error: %@", error);
    [self.delegate rewardedVideoDidFailToLoadAdForCustomEvent:self error:(NSError*)error];
}

-(void)interstitialWillPresent:(IMInterstitial*)interstitial {
    MPLogInfo(@"InMobi interstitial will present");
    [self.delegate rewardedVideoWillAppearForCustomEvent:self];\
}

-(void)interstitialDidPresent:(IMInterstitial *)interstitial {
    MPLogInfo(@"InMobi interstitial did present");
    [self.delegate rewardedVideoDidAppearForCustomEvent:self];
}

-(void)interstitial:(IMInterstitial*)interstitial didFailToPresentWithError:(IMRequestStatus*)error {
    MPLogInfo(@"InMobi interstitial failed to present with error: %@", error);
    [self.delegate rewardedVideoDidFailToPlayForCustomEvent:self error:(NSError *)error];
}

-(void)interstitialWillDismiss:(IMInterstitial*)interstitial {
    MPLogInfo(@"InMobi interstitial will dismiss");
    [self.delegate rewardedVideoWillDisappearForCustomEvent:self];
}

-(void)interstitialDidDismiss:(IMInterstitial*)interstitial {
    MPLogInfo(@"InMobi interstitial did dismiss");
    [self.delegate rewardedVideoDidDisappearForCustomEvent:self];
}

-(void)interstitial:(IMInterstitial*)interstitial didInteractWithParams:(NSDictionary*)params {
    MPLogInfo(@"InMobi interstitial was tapped");
    [self.delegate rewardedVideoDidReceiveTapEventForCustomEvent:self];
}

-(void)interstitial:(IMInterstitial*)interstitial rewardActionCompletedWithRewards:(NSDictionary*)rewards {
    NSLog(@"InMobi Rewarded Video Rewards!");
    if(rewards!=nil){
        NSLog(@"InMobi interstitial reward action completed with rewards: %@", [rewards description]);
        MPRewardedVideoReward *reward = [[MPRewardedVideoReward alloc]initWithCurrencyType:kMPRewardedVideoRewardCurrencyTypeUnspecified amount:[rewards allValues][0]];
        [self.delegate rewardedVideoShouldRewardUserForCustomEvent:self reward:(MPRewardedVideoReward*)reward];
    }
}

-(void)userWillLeaveApplicationFromInterstitial:(IMInterstitial*)interstitial {
    MPLogInfo(@"InMobi interstitial will leave application");
    [self.delegate rewardedVideoWillLeaveApplicationForCustomEvent:self];
}


@end
