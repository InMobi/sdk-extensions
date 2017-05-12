//
//  InMobiInterstitial.m
//  Unity-iPhone
//
//  Created by Vineet Srivastava on 11/24/16.
//
//

#import <Foundation/Foundation.h>
#import "InMobiInterstitial.h"
#import "InMobiPlugin.h"

@import UIKit;

@interface InMobiInterstitial()<IMInterstitialDelegate>

@end

@implementation InMobiInterstitial

-(id)initInterstitialAd:(InMobiInterstitialClientRef *)interstitialClient placementId:(NSString *)placementId
{
    self = [super init];
    if(self){
        _interstitialClient = interstitialClient;
        _interstitial = [[IMInterstitial alloc] initWithPlacementId:[placementId longLongValue]];
        _interstitial.delegate = self;
    }
    return self;
}

-(void)setKeywords:(NSString*) keyWords{
    if(!_interstitial){
        NSLog(@"InMobi Interstitial is nil. Ignoring call to setKeywords");
        return;
    }
    [_interstitial setKeywords:keyWords];
}

-(void)loadAd
{
    if(!_interstitial){
        NSLog(@"InMobi Interstitial is nil. Please initialize Interstitial Object before calling load");
        return;
    }
    _interstitial.extras=@{@"tp": @"p_unity"};
    [_interstitial load];
}

-(bool)isReady
{
    if(!_interstitial){
        NSLog(@"InMobi Interstitial is nil.");
        return false;
    }
    return _interstitial.isReady;
}

-(void)showAd
{
    if(!_interstitial){
        NSLog(@"InMobi Interstitial is nil. Please initialize Interstitial Object before calling show");
        return;
    }
    [_interstitial showFromViewController:[InMobiPlugin unityGLViewController]];
}

#pragma IMInterstitialDelegate Callbacks

/*Indicates that the interstitial is ready to be shown */
- (void)interstitialDidFinishLoading:(IMInterstitial *)interstitial {
    NSLog(@"interstitialDidFinishLoading");
    self.onAdLoadSucceeded(_interstitialClient);
}

/* Indicates that the interstitial has failed to receive an ad. */
- (void)interstitial:(IMInterstitial *)interstitial didFailToLoadWithError:(IMRequestStatus *)error {
    NSLog(@"Interstitial failed to load ad");
    NSLog(@"Error : %@",error.description);
    self.onAdLoadFailed(_interstitialClient, [error.description UTF8String]);
}

/* Indicates that the interstitial has failed to present itself. */
- (void)interstitial:(IMInterstitial *)interstitial didFailToPresentWithError:(IMRequestStatus *)error {
    NSLog(@"Interstitial didFailToPresentWithError");
    NSLog(@"Error : %@",error.description);
    self.onAdDisplayFailed(_interstitialClient);
}

/* indicates that the interstitial is going to present itself. */
- (void)interstitialWillPresent:(IMInterstitial *)interstitial {
    NSLog(@"interstitialWillPresent");
    self.onAdWillDisplay(_interstitialClient);
}

/* Indicates that the interstitial has presented itself */
- (void)interstitialDidPresent:(IMInterstitial *)interstitial {
    NSLog(@"interstitialDidPresent");
    self.onAdDisplayed(_interstitialClient);
}

/* Indicates that the interstitial is going to dismiss itself. */
- (void)interstitialWillDismiss:(IMInterstitial *)interstitial {
    NSLog(@"interstitialWillDismiss");
    //self.onAdDismissed(_interstitialClient);
}

/* Indicates that the interstitial has dismissed itself. */
- (void)interstitialDidDismiss:(IMInterstitial *)interstitial {
    NSLog(@"interstitialDidDismiss");
    self.onAdDismissed(_interstitialClient);
}

/* Indicates that the user will leave the app. */
- (void)userWillLeaveApplicationFromInterstitial:(IMInterstitial *)interstitial {
    NSLog(@"userWillLeaveApplicationFromInterstitial");
    self.onUserLeftApplication(_interstitialClient);
}

/* Indicates that a reward action is completed */
- (void)interstitial:(IMInterstitial *)interstitial rewardActionCompletedWithRewards:(NSDictionary *)rewards {
    NSLog(@"rewardActionCompletedWithRewards");
    self.onAdRewardActionCompleted(_interstitialClient, [InMobiPlugin JSONfromObject:rewards].UTF8String);
}

/* interstitial:didInteractWithParams: Indicates that the interstitial was interacted with. */
- (void)interstitial:(IMInterstitial *)interstitial didInteractWithParams:(NSDictionary *)params {
    NSLog(@"InterstitialDidInteractWithParams");
    self.onAdInteraction(_interstitialClient, [InMobiPlugin JSONfromObject:params].UTF8String);
}

/* Not used for direct integration. Notifies the delegate that the ad server has returned an ad but assets are not yet available. */
- (void)interstitialDidReceiveAd:(IMInterstitial *)interstitial {
    NSLog(@"interstitialDidReceiveAd");
    self.onAdReceived(_interstitialClient);
}

@end
