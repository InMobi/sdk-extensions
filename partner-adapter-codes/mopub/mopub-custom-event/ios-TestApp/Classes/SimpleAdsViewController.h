//
//  SimpleAdsViewController.h
//
//  Copyright (c) 2013 InMobi. All rights reserved.
//

#import <UIKit/UIKit.h>
#import "MPAdView.h"
#import "MPInterstitialAdController.h"

#define PUB_ID_320x50 @"ef72bbe00114449f99ffc8ec952e9795"

@class InterstitialAdController;

@interface SimpleAdsViewController : UIViewController <MPAdViewDelegate> {
	
	MPAdView* mpAdView;
}
@property(nonatomic,retain) MPAdView* mpAdView;

-(IBAction) refreshAd;

@end