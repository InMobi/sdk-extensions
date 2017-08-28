//
//  InMobiBanner.h
//  Unity-iPhone
//
//  Created by Vineet Srivastava on 11/22/16.
//
//

@import Foundation;
@import InMobiSDK;

#import "InMobiTypes.h"

typedef enum
{
    InMobiAdPositionTopLeft,
    InMobiAdPositionTopCenter,
    InMobiAdPositionTopRight,
    InMobiAdPositionCentered,
    InMobiAdPositionBottomLeft,
    InMobiAdPositionBottomCenter,
    InMobiAdPositionBottomRight
} InMobiAdPosition;

@interface InMobiBanner : NSObject

-(id)initBannerAd:(InMobiBannerClientRef*) bannerClient
                    placementId:(NSString*) placementId
                    width:(int) width
                    height:(int) height
                    position:(int) position;

//// A reference to the Unity banner client.
@property(nonatomic, assign) InMobiBannerClientRef *bannerClient;

@property (nonatomic, strong) IMBanner *banner;

@property(nonatomic,assign) int position;

///The ad received callback into Unity
@property(nonatomic,assign) InMobiBannerOnAdLoadSucceeded onAdLoadSucceeded;

///The ad failed to load callback into Unity
@property(nonatomic,assign) InMobiBannerOnAdLoadFailed onAdLoadFailed;

///User interaction callback into Unity
@property(nonatomic,assign) InMobiBannerOnAdInteraction onAdInteraction;

///Full Screen View is dismissed callback into Unity
@property(nonatomic,assign) InMobiBannerOnAdDisplayed onAdDisplayed;

///Full Screen View is dismissed callback into Unity
@property(nonatomic,assign) InMobiBannerOnAdDismissed onAdDismissed;

///User will leave application callback into Unity
@property(nonatomic,assign) InMobiBannerOnUserLeftApplication onUserLeftApplication;

///User is rewarded callback to Unity
@property(nonatomic, assign) InMobiBannerOnAdRewardActionCompleted onAdRewardActionCompleted;

-(void)setKeywords:(NSString*) keyWords;

-(void)setEnableAutoRefresh:(bool) flag;

-(void)setRefreshInterval:(int) refreshInterval;

-(void)loadAd;

-(void)destroy;


@end
