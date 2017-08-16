//
//  InMobiNativeStrandCustomEvent.m
//  InMobiMopubSampleApp
//
//  Created by Niranjan Agrawal on 1/4/16.
//
//


#import <Foundation/Foundation.h>
#import "InMobiNativeStrandCustomEvent.h"
#import "InMobiNativeStrandAdAdapter.h"
#import "MPNativeAd.h"
#import "MPLogging.h"
#import "MPNativeAdError.h"
#import "MPNativeAdConstants.h"
#import "MPNativeAdUtils.h"
#import <InMobiSDK/IMSdk.h>

static NSString *gAppId = nil;

@interface InMobiNativeStrandCustomEvent ()

@property (nonatomic, strong) IMNativeStrands *inmobiStrands;
@property (nonatomic, strong) InMobiNativeStrandAdAdapter *adAdapter;

@end

@implementation InMobiNativeStrandCustomEvent
- (void)requestAdWithCustomEventInfo:(NSDictionary *)info
{
    NSLog(@"In InMobiStrandCustomEvent");
    NSString *appId = [[info objectForKey:@"placementid"]description];
    
    [IMSdk initWithAccountID:[info valueForKey:@"accountid"]];

    self.inmobiStrands = [[IMNativeStrands alloc] initWithPlacementId:[appId longLongValue]];
    
    /*
     Mandatory params to be set by the publisher to identify the supply source type
     */

    NSMutableDictionary *paramsDict = [[NSMutableDictionary alloc] init];
    [paramsDict setObject:@"c_mopub" forKey:@"tp"];
    [paramsDict setObject:MP_SDK_VERSION forKey:@"tp-ver"];

    //Make sure to have the below line commented before the app is released.
    //[paramsDict setObject:@"17.0.0.1" forKey:@"mk-carrier"];

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

    self.inmobiStrands.extras = paramsDict; // For supply source identification
    self.inmobiStrands.delegate = self;
    [self.inmobiStrands load];
}

#pragma mark - IMNativeDelegate

-(void)nativeStrandsDidFinishLoading:(IMNativeStrands *)imnativestrands{
    
    NSLog(@"Native strands did finish loading");
    self.adAdapter = [[InMobiNativeStrandAdAdapter alloc] initWithInMobiNativeStrandsAd:imnativestrands];
    MPNativeAd *interfaceAd = [[MPNativeAd alloc] initWithAdAdapter:_adAdapter];

    [self.delegate nativeCustomEvent:self didLoadAd:interfaceAd];
}

-(void)nativeStrands:(IMNativeStrands*)nativeStrands didFailToLoadWithError:(IMRequestStatus*)error{
    NSLog(@"Native strands did fail to load");
    [self.delegate nativeCustomEvent:self didFailToLoadAdWithError:MPNativeAdNSErrorForInvalidAdServerResponse(@"InMobi ad load error")];
}

-(void)nativeStrandsWillPresentScreen:(IMNativeStrands*)native{
    NSLog(@"Native will present screen");
}

-(void)nativeStrandsDidPresentScreen:(IMNativeStrands*)native{
    NSLog(@"Native did present screen");
}

-(void)nativeStrandsWillDismissScreen:(IMNativeStrands*)native{
    NSLog(@"Native will dismiss screen");
}

-(void)nativeStrandsDidDismissScreen:(IMNativeStrands*)native{
    NSLog(@"Native did dismiss screen");
}

-(void)userWillLeaveApplicationFromNativeStrands:(IMNativeStrands*)native{
    NSLog(@"User will leave application from native");
}

-(void)nativeStrandsAdImpressed:(IMNativeStrands*)nativeStrands{
    NSLog(@"InMobi impression tracked successfully");
    [_adAdapter.delegate nativeAdWillLogImpression:_adAdapter];
}

-(void)nativeStrandsAdClicked:(IMNativeStrands*)nativeStrands{
    NSLog(@"InMobi click tracked successfully");
    [_adAdapter.delegate nativeAdDidClick:_adAdapter];
}
- (void)dealloc {
    NSLog(@"InMobi Native Strand Custom Event");
}

@end
