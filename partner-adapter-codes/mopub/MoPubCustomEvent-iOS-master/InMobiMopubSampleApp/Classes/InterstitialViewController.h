//
//  InterstitialViewController.h
//
//  Copyright (c) 2013 InMobi. All rights reserved.
//

#import <UIKit/UIKit.h>
#import "MPAdView.h"
#import "MPInterstitialAdController.h"

#define PUB_ID_INTERSTITIAL @"6e20400477fa47f9aed563b4d8bc12b9"

@class MPInterstitialAdController;

@interface InterstitialViewController : UIViewController <UITextFieldDelegate, MPInterstitialAdControllerDelegate> {
	IBOutlet UIButton* showInterstitialButton;

	MPInterstitialAdController *interstitialAdController;
}
@property(nonatomic,retain) IBOutlet UIButton* showInterstitialButton;
@property(nonatomic,retain) MPInterstitialAdController* interstitialAdController;

-(IBAction) showModalInterstitial;
-(IBAction) getModalInterstitial;

@end
