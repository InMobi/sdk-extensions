//
//  InMobiInterstitialCustomEvent.m
//  InMobi
//

#import "InMobiInterstitialCustomEvent.h"
#import "MPInstanceProvider.h"
#import "MPLogging.h"
#import <InMobiSDK/IMSdk.h>

@interface MPInstanceProvider (InMobiInterstitials)

- (IMInterstitial *)buildIMInterstitialWithDelegate:(id)delegate placementId:(long long)placementId;

@end

@implementation MPInstanceProvider (InMobiInterstitials)

- (IMInterstitial *)buildIMInterstitialWithDelegate:(id)delegate placementId:(long long)placementId {
    IMInterstitial *inMobiInterstitial = [[IMInterstitial alloc] initWithPlacementId:placementId];
    inMobiInterstitial.delegate = delegate;
    return inMobiInterstitial;
}

@end


@interface InMobiInterstitialCustomEvent ()

@property (nonatomic, retain) IMInterstitial *inMobiInterstitial;

@end

@implementation InMobiInterstitialCustomEvent

@synthesize inMobiInterstitial = _inMobiInterstitial;

#pragma mark - MPInterstitialCustomEvent Subclass Methods

- (void)requestInterstitialWithCustomEventInfo:(NSDictionary *)info
{
    MPLogInfo(@"Requesting InMobi interstitial");

    //InMobi SDK initialization with the account id setup @Mopub dashboard
    [IMSdk initWithAccountID:[info valueForKey:@"accountid"]];
    
    self.inMobiInterstitial = [[MPInstanceProvider sharedProvider] buildIMInterstitialWithDelegate:self placementId:[[info valueForKey:@"placementid"]longLongValue ]];

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

- (void)showInterstitialFromRootViewController:(UIViewController *)rootViewController
{
    [self.inMobiInterstitial showFromViewController:rootViewController withAnimation:kIMInterstitialAnimationTypeCoverVertical];
}

#pragma mark - IMInterstitialDelegate


-(void)interstitialDidFinishLoading:(IMInterstitial*)interstitial {
    MPLogInfo(@"InMobi interstitial did load");
    [self.delegate interstitialCustomEvent:self didLoadAd:interstitial];
}

-(void)interstitialDidReceiveAd:(IMInterstitial *)interstitial{
    MPLogInfo(@"InMobi Ad Server responded with an Ad");
}

-(void)interstitial:(IMInterstitial*)interstitial didFailToLoadWithError:(IMRequestStatus*)error {
    MPLogInfo(@"InMobi interstitial did fail with error: %@", error);
    [self.delegate interstitialCustomEvent:self didFailToLoadAdWithError:(NSError *)error];
}

-(void)interstitialWillPresent:(IMInterstitial*)interstitial {
    MPLogInfo(@"InMobi interstitial will present");
    [self.delegate interstitialCustomEventWillAppear:self];
}

-(void)interstitialDidPresent:(IMInterstitial *)interstitial {
    MPLogInfo(@"InMobi interstitial did present");
    [self.delegate interstitialCustomEventDidAppear:self];
}

-(void)interstitial:(IMInterstitial*)interstitial didFailToPresentWithError:(IMRequestStatus*)error {
    MPLogInfo(@"InMobi interstitial failed to present with error: %@", error);
    [self.delegate interstitialCustomEvent:self didFailToLoadAdWithError:(NSError *)error];
}

-(void)interstitialWillDismiss:(IMInterstitial*)interstitial {
    MPLogInfo(@"InMobi interstitial will dismiss");
    [self.delegate interstitialCustomEventWillDisappear:self];
}

-(void)interstitialDidDismiss:(IMInterstitial*)interstitial {
    MPLogInfo(@"InMobi interstitial did dismiss");
    [self.delegate interstitialCustomEventDidDisappear:self];
}

-(void)interstitial:(IMInterstitial*)interstitial didInteractWithParams:(NSDictionary*)params {
    MPLogInfo(@"InMobi interstitial was tapped");
    [self.delegate interstitialCustomEventDidReceiveTapEvent:self];
}

-(void)interstitial:(IMInterstitial*)interstitial rewardActionCompletedWithRewards:(NSDictionary*)rewards {
    if(rewards!=nil){
        NSLog(@"InMobi interstitial reward action completed with rewards: %@", [rewards description]);
    }
}

-(void)userWillLeaveApplicationFromInterstitial:(IMInterstitial*)interstitial {
    MPLogInfo(@"InMobi interstitial will leave application");
    [self.delegate interstitialCustomEventWillLeaveApplication:self];
}

@end
