//
//  InMobiNativeAdapter.m
//  InMobiMopubSampleApp
//
//  Created by Niranjan Agrawal on 28/10/15.
//
//
#import "InMobiNativeAdAdapter.h"
#import <InMobiSDK/IMNative.h>
#import "MPNativeAdError.h"
#import "MPNativeAdConstants.h"
#import "MPAdDestinationDisplayAgent.h"
#import "MPCoreInstanceProvider.h"
#import "MPLogging.h"
#import "MPStaticNativeAdImpressionTimer.h"

/*
 * Default keys for InMobi Native Ads
 *
 * These values must correspond to the strings configured with InMobi.
 */
static NSString *gInMobiTitleKey = @"title";
static NSString *gInMobiDescriptionKey = @"description";
static NSString *gInMobiCallToActionKey = @"cta";
static NSString *gInMobiPrimaryAdViewKey = @"primaryAdView";
static NSString *gInMobiRatingKey = @"rating";
static NSString *gInMobiScreenshotKey = @"screenshots";
static NSString *gInMobiIconKey = @"icon";

static NSString *gInMobiLandingURLKey = @"landingURL";

/*
 * InMobi Key - Do Not Change.
 */
static NSString *const kInMobiImageURL = @"url";

@interface InMobiNativeAdAdapter() <MPAdDestinationDisplayAgentDelegate, MPStaticNativeAdImpressionTimerDelegate>
@property (nonatomic, readonly) IMNative *inMobiNativeAd;
//@property (nonatomic) MPStaticNativeAdImpressionTimer *impressionTimer;
@property (nonatomic, readonly) MPAdDestinationDisplayAgent *destinationDisplayAgent;

@end

@implementation InMobiNativeAdAdapter

@synthesize properties = _properties;
@synthesize defaultActionURL = _defaultActionURL;

+ (void)setCustomKeyForTitle:(NSString *)key
{
    gInMobiTitleKey = [key copy];
}

+ (void)setCustomKeyForDescription:(NSString *)key
{
    gInMobiDescriptionKey = [key copy];
}

+ (void)setCustomKeyForCallToAction:(NSString *)key
{
    gInMobiCallToActionKey = [key copy];
}

+ (void)setCustomKeyForPrimaryAdView:(NSString *)key
{
    gInMobiPrimaryAdViewKey = [key copy];
}

+ (void)setCustomKeyForRating:(NSString *)key
{
    gInMobiRatingKey = [key copy];
}

+ (void)setCustomKeyForScreenshot:(NSString *)key
{
    gInMobiScreenshotKey = [key copy];
}

+ (void)setCustomKeyForIcon:(NSString *)key
{
    gInMobiIconKey = [key copy];
}

+ (void)setCustomKeyForLandingURL:(NSString *)key
{
    gInMobiLandingURLKey = [key copy];
}

- (instancetype)initWithInMobiNativeAd:(IMNative *)nativeAd
{
    self = [super init];
    if (self) {
        _inMobiNativeAd = nativeAd;
        self.inMobiNativeAd.delegate = self;
        
        NSDictionary *inMobiProperties = [self inMobiProperties];
        NSMutableDictionary *properties = [NSMutableDictionary dictionary];
        
        if ([_inMobiNativeAd adRating]) {
            [properties setObject:[_inMobiNativeAd adRating] forKey:kAdStarRatingKey];
        }
        
        if ([[_inMobiNativeAd adTitle] length]) {
            [properties setObject:[_inMobiNativeAd adTitle] forKey:kAdTitleKey];
        }
        
        if ([[_inMobiNativeAd adDescription] length]) {
            [properties setObject:[_inMobiNativeAd adDescription] forKey:kAdTextKey];
        }
        
        if ([[_inMobiNativeAd adCtaText] length]) {
            [properties setObject:[_inMobiNativeAd adCtaText] forKey:kAdCTATextKey];
        }
        
        //[properties setObject:[_inMobiNativeAd adIcon] forKey:kAdIconImageKey];
        NSDictionary *iconDictionary = [inMobiProperties objectForKey:gInMobiIconKey];
        
        if ([[iconDictionary objectForKey:kInMobiImageURL] length]) {
            [properties setObject:[iconDictionary objectForKey:kInMobiImageURL] forKey:kAdIconImageKey];
        }
        
        _properties = properties;
        
        if ([_inMobiNativeAd adLandingPageUrl]) {
            _defaultActionURL = [_inMobiNativeAd adLandingPageUrl];
        } else {
            // Log a warning if we can't find the landing URL since the key can either be "landing_url", "landingURL", or a custom key depending on the date the property was created.
            MPLogWarn(@"WARNING: Couldn't find landing url with key: %@ for InMobi network.  Double check your ad property and call setCustomKeyForLandingURL: with the correct key if necessary.", gInMobiLandingURLKey);
        }
        
        _destinationDisplayAgent = [[MPCoreInstanceProvider sharedProvider] buildMPAdDestinationDisplayAgentWithDelegate:self];
        
    }
    return self;
}

- (void)dealloc
{
    [_destinationDisplayAgent cancel];
    [_destinationDisplayAgent setDelegate:nil];
}

- (NSDictionary *)inMobiProperties
{
    NSData *data = [self.inMobiNativeAd.customAdContent dataUsingEncoding:NSUTF8StringEncoding];
    NSError* error = nil;
    NSDictionary *propertyDictionary = nil;
    if (data) {
        propertyDictionary = [NSJSONSerialization JSONObjectWithData:data options:kNilOptions error:&error];
    }
    if (propertyDictionary && !error) {
        return propertyDictionary;
    }
    else {
        return nil;
    }
}

#pragma mark - <MPStaticNativeAdImpressionTimerDelegate>

- (void)trackImpression
{
    [self.delegate nativeAdWillLogImpression:self];
}

#pragma mark - <MPNativeAdAdapter>

- (void)willAttachToView:(UIView *)view
{
//    [self.impressionTimer startTrackingView:view];
//    [IMNative bindNative:self.inMobiNativeAd toView:view];
}

- (void)trackClick
{
    [self.inMobiNativeAd reportAdClick];
}

- (void)displayContentForURL:(NSURL *)URL rootViewController:(UIViewController *)controller
{
    if (!controller) {
        return;
    }
    
    if (!URL || ![URL isKindOfClass:[NSURL class]] || ![URL.absoluteString length]) {
        return;
    }
    
    [self.destinationDisplayAgent displayDestinationForURL:URL];
}

- (UIView *)mainMediaView
{
    return [_inMobiNativeAd primaryViewOfWidth:[self viewControllerForPresentingModalView].view.frame.size.width];
}


#pragma mark - <MPAdDestinationDisplayAgentDelegate>

- (UIViewController *)viewControllerForPresentingModalView
{
    return [self.delegate viewControllerForPresentingModalView];
}

- (void)displayAgentWillPresentModal
{
    [self.delegate nativeAdWillPresentModalForAdapter:self];
}

- (void)displayAgentWillLeaveApplication
{
    [self.delegate nativeAdWillLeaveApplicationFromAdapter:self];
}

- (void)displayAgentDidDismissModal
{
    [self.delegate nativeAdDidDismissModalForAdapter:self];
}

#pragma mark -<IMNativeDelegate>

-(void)nativeDidFinishLoading:(IMNative*)native{
    NSLog(@"Native Ad load Successful"); // Ad is ready to be displayed
}
-(void)native:(IMNative*)native didFailToLoadWithError:(IMRequestStatus*)error{
    NSLog(@"Native Ad load Failed"); // No Fill or error
}
-(void)nativeWillPresentScreen:(IMNative*)native{
    NSLog(@"Native Ad will present screen"); //Full Screen experience is about to be presented
    [self.delegate nativeAdWillPresentModalForAdapter:self];
}
-(void)nativeDidPresentScreen:(IMNative*)native{
    NSLog(@"Native Ad did present screen"); //Full Screen experience has been presented
}
-(void)nativeWillDismissScreen:(IMNative*)native{
    NSLog(@"Native Ad will dismiss screen"); //Full Screen experience is going to be dismissed
}
-(void)nativeDidDismissScreen:(IMNative*)native{
    NSLog(@"Native Ad did dismiss screen"); //Full Screen experience has been dismissed
    [self.delegate nativeAdDidDismissModalForAdapter:self];
}
-(void)userWillLeaveApplicationFromNative:(IMNative*)native{
    NSLog(@"User leave"); //User is about to leave the app on clicking the ad
    [self.delegate nativeAdWillLeaveApplicationFromAdapter:self];
}
-(void)native:(IMNative *)native didInteractWithParams:(NSDictionary *)params{
    NSLog(@"User clicked"); // Called when the user clicks on the ad.
}
-(void)nativeAdImpressed:(IMNative *)native{
    NSLog(@"User viewed the ad"); // Called when impression event is fired.
    [self.delegate nativeAdWillLogImpression:self];
}
-(void)nativeDidFinishPlayingMedia:(IMNative*)native{
    NSLog(@"The Video has finished playing"); // Called when the video has finished playing. Used for preroll use-case
}
-(void)native:(IMNative *)native rewardActionCompletedWithRewards:(NSDictionary *)rewards{
    NSLog(@"Rewarded"); // Called when the user is rewarded to watch the ad.
}




@end
