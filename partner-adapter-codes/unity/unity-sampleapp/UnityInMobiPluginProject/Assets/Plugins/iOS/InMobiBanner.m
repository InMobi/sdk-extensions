//
//  InMobiBanner.m
//  Unity-iPhone
//
//  Created by Vineet Srivastava on 11/22/16.
//
//

#import <Foundation/Foundation.h>
#import "InMobiBanner.h"
#import "InMobiPlugin.h"

@interface InMobiBanner()<IMBannerDelegate>

@end

@implementation InMobiBanner

- (id)initBannerAd:(InMobiBannerClientRef*) bannerClient
         placementId:(NSString*) placementId
               width:(int) width
              height:(int) height
            position:(int) position{
    self = [super init];
    if(self){
        _bannerClient = bannerClient;
        _banner = [[IMBanner alloc] initWithFrame:(CGRectMake(0, 0, width, height)) placementId:[placementId longLongValue]];
        _banner.delegate = self;
        _position = position;
        [[InMobiPlugin unityGLViewController].view addSubview:_banner];
    }
    return self;
}

-(void)setKeywords:(NSString*) keyWords{
    if(!_banner){
        NSLog(@"InMobi Banner is nil. Ignoring call to setKeywords");
        return;
    }
    [_banner setKeywords:keyWords];
}

-(void)setEnableAutoRefresh:(bool) flag{
    if(!_banner){
        NSLog(@"InMobi Banner is nil. Ignoring call to setEnableAutoRefresh");
        return;
    }
    [_banner shouldAutoRefresh:flag];
}

-(void)setRefreshInterval:(int) refreshInterval{
    if(!_banner){
        NSLog(@"InMobi Banner is nil. Ignoring call to setRefreshInterval");
        return;
    }
    [_banner setRefreshInterval:refreshInterval];
}

-(void)loadAd{
    if(!_banner){
        NSLog(@"InMobi Banner is nil. Ignoring call to loadAd");
        return;
    }
    _banner.extras=@{@"tp": @"p_unity"};
    [_banner load];
    [self adjustAdViewFrameToShowAdView:_banner withPosition:_position];
}

- (void)adjustAdViewFrameToShowAdView:(IMBanner*)bannerView withPosition:(int)adPosition
{
    // fetch screen dimensions and useful values
    CGRect origFrame = bannerView.frame;
    
    CGFloat screenHeight = [UIScreen mainScreen].bounds.size.height;
    CGFloat screenWidth = [UIScreen mainScreen].bounds.size.width;
    
    if( UIInterfaceOrientationIsLandscape( [InMobiPlugin unityGLViewController].interfaceOrientation ) )
    {
        screenWidth = screenHeight;
        screenHeight = [UIScreen mainScreen].bounds.size.width;
    }
    
    
    switch( (InMobiAdPosition)adPosition )
    {
        case InMobiAdPositionTopLeft:
            origFrame.origin.x = 0;
            origFrame.origin.y = 0;
            _banner.autoresizingMask = ( UIViewAutoresizingFlexibleRightMargin | UIViewAutoresizingFlexibleBottomMargin );
            break;
        case InMobiAdPositionTopCenter:
            origFrame.origin.x = ( screenWidth / 2 ) - ( origFrame.size.width / 2 );
            origFrame.origin.y = 0;
            _banner.autoresizingMask = ( UIViewAutoresizingFlexibleLeftMargin | UIViewAutoresizingFlexibleRightMargin | UIViewAutoresizingFlexibleBottomMargin );
            break;
        case InMobiAdPositionTopRight:
            origFrame.origin.x = screenWidth - origFrame.size.width;
            origFrame.origin.y = 0;
            _banner.autoresizingMask = ( UIViewAutoresizingFlexibleLeftMargin | UIViewAutoresizingFlexibleBottomMargin );
            break;
        case InMobiAdPositionCentered:
            origFrame.origin.x = ( screenWidth / 2 ) - ( origFrame.size.width / 2 );
            origFrame.origin.y = ( screenHeight / 2 ) - ( origFrame.size.height / 2 );
            _banner.autoresizingMask = ( UIViewAutoresizingFlexibleRightMargin | UIViewAutoresizingFlexibleLeftMargin | UIViewAutoresizingFlexibleTopMargin | UIViewAutoresizingFlexibleBottomMargin );
            break;
        case InMobiAdPositionBottomLeft:
            origFrame.origin.x = 0;
            origFrame.origin.y = screenHeight - origFrame.size.height;
            _banner.autoresizingMask = ( UIViewAutoresizingFlexibleRightMargin | UIViewAutoresizingFlexibleTopMargin );
            break;
        case InMobiAdPositionBottomCenter:
            origFrame.origin.x = ( screenWidth / 2 ) - ( origFrame.size.width / 2 );
            origFrame.origin.y = screenHeight - origFrame.size.height;
            _banner.autoresizingMask = ( UIViewAutoresizingFlexibleLeftMargin | UIViewAutoresizingFlexibleRightMargin | UIViewAutoresizingFlexibleTopMargin );
            break;
        case InMobiAdPositionBottomRight:
            origFrame.origin.x = screenWidth - _banner.frame.size.width;
            origFrame.origin.y = screenHeight - origFrame.size.height;
            _banner.autoresizingMask = ( UIViewAutoresizingFlexibleLeftMargin | UIViewAutoresizingFlexibleTopMargin );
            break;
    }
    
    bannerView.frame = origFrame;
}

-(void)destroy{
    if(!_banner){
        NSLog(@"InMobi Banner is nil. Ignoring call to destroy");
        return;
    }
    [_banner removeFromSuperview];
}

- (void)dealloc {
    _banner.delegate = nil;
}

#pragma IMBanner Delegate Implementation

/*Indicates that the banner has received an ad. */
- (void)bannerDidFinishLoading:(IMBanner *)banner {
    NSLog(@"bannerDidFinishLoading");
    self.onAdLoadSucceeded(self.bannerClient);
}
/* Indicates that the banner has failed to receive an ad */
- (void)banner:(IMBanner *)banner didFailToLoadWithError:(IMRequestStatus *)error {
    NSLog(@"banner failed to load ad");
    NSLog(@"Error : %@", error.description);
    self.onAdLoadFailed(self.bannerClient, error.localizedDescription.UTF8String);
}
/* Indicates that the banner is going to present a screen. */
- (void)bannerWillPresentScreen:(IMBanner *)banner {
    NSLog(@"bannerWillPresentScreen");
    //self.onWillPresentScreen(self.bannerClient);
}
/* Indicates that the banner has presented a screen. */
- (void)bannerDidPresentScreen:(IMBanner *)banner {
    NSLog(@"bannerDidPresentScreen");
    self.onAdDisplayed(self.bannerClient);
}
/* Indicates that the banner is going to dismiss the presented screen. */
- (void)bannerWillDismissScreen:(IMBanner *)banner {
    NSLog(@"bannerWillDismissScreen");
    //self.willDismissScreen(self.bannerClient);
}
/* Indicates that the banner has dismissed a screen. */
- (void)bannerDidDismissScreen:(IMBanner *)banner {
    NSLog(@"bannerDidDismissScreen");
    self.onAdDismissed(self.bannerClient);
}
/* Indicates that the user will leave the app. */
- (void)userWillLeaveApplicationFromBanner:(IMBanner *)banner {
    NSLog(@"userWillLeaveApplicationFromBanner");
    self.onUserLeftApplication(self.bannerClient);
}
/*  Indicates that the banner was interacted with. */
-(void)banner:(IMBanner *)banner didInteractWithParams:(NSDictionary *)params{
    NSLog(@"bannerdidInteractWithParams");
    self.onAdInteraction(self.bannerClient, [InMobiPlugin JSONfromObject:params].UTF8String);
}
/*Indicates that the user has completed the action to be incentivised with .*/
-(void)banner:(IMBanner*)banner rewardActionCompletedWithRewards:(NSDictionary*)rewards{
    NSLog(@"rewardActionCompletedWithRewards");
    self.onAdRewardActionCompleted(self.bannerClient, [InMobiPlugin JSONfromObject:rewards].UTF8String);
}

@end

