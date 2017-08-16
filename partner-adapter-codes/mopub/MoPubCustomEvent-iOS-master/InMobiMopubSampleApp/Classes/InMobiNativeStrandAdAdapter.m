//
//  InMobiNativeStrandAdAdapter.m
//  InMobiMopubSampleApp
//
//  Created by Niranjan Agrawal on 1/5/16.
//
//

#import <Foundation/Foundation.h>
#import "InMobiNativeStrandAdAdapter.h"
#import "MPNativeAdError.h"
#import "MPNativeAdConstants.h"
#import "MPAdDestinationDisplayAgent.h"
#import "MPCoreInstanceProvider.h"
#import "MPLogging.h"
#import "MPStaticNativeAdImpressionTimer.h"


//@interface InMobiNativeStrandAdAdapter() <MPAdDestinationDisplayAgentDelegate, MPStaticNativeAdImpressionTimerDelegate>
@interface InMobiNativeStrandAdAdapter()

//@property (nonatomic) MPStaticNativeAdImpressionTimer *impressionTimer;
//@property (nonatomic, readonly) MPAdDestinationDisplayAgent *destinationDisplayAgent;


@end


@implementation InMobiNativeStrandAdAdapter
@synthesize properties = _properties;
@synthesize defaultActionURL = _defaultActionURL;


- (instancetype)initWithInMobiNativeStrandsAd:(IMNativeStrands *)nativeStrandsAd
{
    self = [super init];
    if (self) {
        self.nativeStrands = nativeStrandsAd;
        self.nativeStrands.delegate=self;
        
    }
    return self;
}

- (void)dealloc
{
//    [_destinationDisplayAgent cancel];
//    [_destinationDisplayAgent setDelegate:nil];
}


#pragma mark - <MPStaticNativeAdImpressionTimerDelegate>

//- (void)trackImpression
//{
//    //[self.delegate nativeAdWillLogImpression:self];
//}

#pragma mark - <MPNativeAdAdapter>

//- (void)willAttachToView:(UIView *)view
//{
//    //[self.impressionTimer startTrackingView:view];
//    //[IMNativeStrands bindNative:self.nativeStrands toView:view];
//}
//
//- (void)trackClick
//{
//    
//}

- (void)displayContentForURL:(NSURL *)URL rootViewController:(UIViewController *)controller
{
    NSLog(@"Ad is interacted by user!");
//    if (!controller) {
//        return;
//    }
//    
//    if (!URL || ![URL isKindOfClass:[NSURL class]] || ![URL.absoluteString length]) {
//        return;
//    }
//    
//    [self.destinationDisplayAgent displayDestinationForURL:URL];
}

#pragma mark - <MPAdDestinationDisplayAgentDelegate>

//- (UIViewController *)viewControllerForPresentingModalView
//{
//    return [self.delegate viewControllerForPresentingModalView];
//}
//
//- (void)displayAgentWillPresentModal
//{
//    [self.delegate nativeAdWillPresentModalForAdapter:self];
//}
//
//- (void)displayAgentWillLeaveApplication
//{
//    [self.delegate nativeAdWillLeaveApplicationFromAdapter:self];
//}
//
//- (void)displayAgentDidDismissModal
//{
//    [self.delegate nativeAdDidDismissModalForAdapter:self];
//}


-(void)nativeStrandsDidFinishLoading:(IMNativeStrands *)imnativestrands{
    NSLog(@"In adapter, ad finished loading");
}

-(void)nativeStrands:(IMNativeStrands*)nativeStrands didFailToLoadWithError:(IMRequestStatus*)error{
    NSLog(@"In adapter, ad failed to load");
}

-(void)nativeStrandsWillPresentScreen:(IMNativeStrands*)native{
    NSLog(@"Native will present screen");
    [self.delegate nativeAdWillPresentModalForAdapter:self];
}

-(void)nativeStrandsDidPresentScreen:(IMNativeStrands*)native{
    NSLog(@"Native did present screen");
}

-(void)nativeStrandsWillDismissScreen:(IMNativeStrands*)native{
    NSLog(@"Native will dismiss screen");
}

-(void)nativeStrandsDidDismissScreen:(IMNativeStrands*)native{
    NSLog(@"Native did dismiss screen");
    [self.delegate nativeAdDidDismissModalForAdapter:self];
}

-(void)userWillLeaveApplicationFromNativeStrands:(IMNativeStrands*)native{
    NSLog(@"User will leave application from native");
    [self.delegate nativeAdWillLeaveApplicationFromAdapter:self];
}

-(void)nativeStrandsAdImpressed:(IMNativeStrands*)nativeStrands{
    NSLog(@"InMobi impression tracked successfully");
    [self.delegate nativeAdWillLogImpression:self];
}

-(void)nativeStrandsAdClicked:(IMNativeStrands*)nativeStrands{
    NSLog(@"InMobi click tracked successfully");
    [self.delegate nativeAdDidClick:self];
}

@end
