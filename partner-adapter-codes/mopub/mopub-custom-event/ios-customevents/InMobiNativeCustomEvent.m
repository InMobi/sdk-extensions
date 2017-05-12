//
//  InMobiNativeCustomEvent.m
//  InMobiMopubSampleApp
//
//  Created by Niranjan Agrawal on 28/10/15.
//
//

#import <Foundation/Foundation.h>
#import "InMobiNativeCustomEvent.h"
#import "InMobiNativeAdAdapter.h"
#import "MPNativeAd.h"
#import "MPLogging.h"
#import "MPNativeAdError.h"
#import "MPNativeAdConstants.h"
#import "MPNativeAdUtils.h"
#import <InMobiSDK/IMSdk.h>

static NSString *gAppId = nil;

@interface InMobiNativeCustomEvent ()

@property (nonatomic, strong) IMNative *inMobiAd;
@property (nonatomic, strong) InMobiNativeAdAdapter *adAdapter;

@end

@implementation InMobiNativeCustomEvent
- (void)requestAdWithCustomEventInfo:(NSDictionary *)info
{
    NSString *appId = [[info objectForKey:@"placementid"]description];

    //InMobi SDK initialization with the account id setup @Mopub dashboard
    [IMSdk initWithAccountID:[info valueForKey:@"accountid"]];

    self.inMobiAd = [[IMNative alloc] initWithPlacementId:[appId longLongValue]];
    
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

    self.inMobiAd.extras = paramsDict; // For supply source identification
    self.inMobiAd.delegate = self;
    [self.inMobiAd load];
}

#pragma mark - IMNativeDelegate

-(void)nativeDidFinishLoading:(IMNative *)imnative{
    
    NSLog(@"%@",[imnative adContent]);
    
    _adAdapter = [[InMobiNativeAdAdapter alloc] initWithInMobiNativeAd:imnative];
    MPNativeAd *interfaceAd = [[MPNativeAd alloc] initWithAdAdapter:_adAdapter];
    
    NSMutableArray *imageURLs = [NSMutableArray array];
    
    if ([[interfaceAd.properties objectForKey:kAdIconImageKey] length]) {
        if (![MPNativeAdUtils addURLString:[interfaceAd.properties objectForKey:kAdIconImageKey] toURLArray:imageURLs]) {
            [self.delegate nativeCustomEvent:self didFailToLoadAdWithError:MPNativeAdNSErrorForInvalidImageURL()];
        }
    }
    
    if ([[interfaceAd.properties objectForKey:kAdMainImageKey] length]) {
        if (![MPNativeAdUtils addURLString:[interfaceAd.properties objectForKey:kAdMainImageKey] toURLArray:imageURLs]) {
            [self.delegate nativeCustomEvent:self didFailToLoadAdWithError:MPNativeAdNSErrorForInvalidImageURL()];
        }
    }
    
    [super precacheImagesWithURLs:imageURLs completionBlock:^(NSArray *errors) {
        if (errors) {
            MPLogDebug(@"%@", errors);
            [self.delegate nativeCustomEvent:self didFailToLoadAdWithError:MPNativeAdNSErrorForImageDownloadFailure()];
        } else {
            [self.delegate nativeCustomEvent:self didLoadAd:interfaceAd];
        }
    }];
}

-(void)native:(IMNative*)native didFailToLoadWithError:(IMRequestStatus*)error{
    [self.delegate nativeCustomEvent:self didFailToLoadAdWithError:MPNativeAdNSErrorForInvalidAdServerResponse(@"InMobi ad load error")];
}

-(void)nativeAdImpressed:(IMNative *)native{
    NSLog(@"InMobi impression tracked successfully");
    [_adAdapter.delegate nativeAdWillLogImpression:_adAdapter];
}

-(void)nativeWillPresentScreen:(IMNative*)native{
    NSLog(@"Native will present screen");
}

-(void)nativeDidPresentScreen:(IMNative*)native{
    NSLog(@"Native did present screen");
}

-(void)nativeWillDismissScreen:(IMNative*)native{
    NSLog(@"Native will dismiss screen");
}

-(void)nativeDidDismissScreen:(IMNative*)native{
    NSLog(@"Native did dismiss screen");
}

-(void)userWillLeaveApplicationFromNative:(IMNative*)native{
    NSLog(@"User will leave application from native");
}

-(void) dealloc{
    NSLog(@"InMobi Native custom event class destroyed");
}

@end
