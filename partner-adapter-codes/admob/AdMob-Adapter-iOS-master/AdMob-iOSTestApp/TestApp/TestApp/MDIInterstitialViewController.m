//  Copyright © 2016 Google. All rights reserved.

@import GoogleMobileAds;

#import "MDIInterstitialViewController.h"

#import "MDIConstants.h"
#import "NSString+MDIAppendLoggingAdditions.h"
#import <InMobiSDK/IMSdk.h>

@interface MDIInterstitialViewController ()<GADInterstitialDelegate> {
  /// Callback log text view.
  __weak IBOutlet UITextView *_textView;

  /// Interstitial ad instance.
  GADInterstitial *_interstitial;
}

@end

@implementation MDIInterstitialViewController

- (void)viewDidLoad {
  [super viewDidLoad];
  self.title = @"End To End Interstitial Tests";
  //[IMSdk setLogLevel:kIMSDKLogLevelDebug];
  [self loadInterstitial];
}

- (void)loadInterstitial {
  _interstitial = [[GADInterstitial alloc] initWithAdUnitID:MDIInterstitialTestAdUnitID];
  _interstitial.delegate = self;
  [_interstitial loadRequest:[GADRequest request]];
}

#pragma mark - GADInterstitialDelegate Methods

- (void)interstitialDidReceiveAd:(GADInterstitial *)interstitial {
  NSLog(@"%s", __PRETTY_FUNCTION__);
  _textView.text = [_textView.text mdi_stringByAppendingLogString:@(__PRETTY_FUNCTION__)];
}

- (void)interstitial:(GADInterstitial *)interstitial
    didFailToReceiveAdWithError:(GADRequestError *)error {
  _textView.text = [_textView.text mdi_stringByAppendingLogString:@(__PRETTY_FUNCTION__)];
  if (error.localizedDescription) {
    NSLog(@"%s %@", __PRETTY_FUNCTION__, error.localizedDescription);
  }
}

- (void)interstitialWillPresentScreen:(GADInterstitial *)interstitial {
  NSLog(@"%s", __PRETTY_FUNCTION__);
  _textView.text = [_textView.text mdi_stringByAppendingLogString:@(__PRETTY_FUNCTION__)];
}

- (void)interstitialWillDismissScreen:(GADInterstitial *)interstitial {
  NSLog(@"%s", __PRETTY_FUNCTION__);
  _textView.text = [_textView.text mdi_stringByAppendingLogString:@(__PRETTY_FUNCTION__)];
}

- (void)interstitialDidDismissScreen:(GADInterstitial *)interstitial {
  NSLog(@"%s", __PRETTY_FUNCTION__);
  _textView.text = [_textView.text mdi_stringByAppendingLogString:@(__PRETTY_FUNCTION__)];
  [self loadInterstitial];
}

- (void)interstitialWillLeaveApplication:(GADInterstitial *)interstitial {
  NSLog(@"%s", __PRETTY_FUNCTION__);
  _textView.text = [_textView.text mdi_stringByAppendingLogString:@(__PRETTY_FUNCTION__)];
}

#pragma mark - Actions

- (IBAction)showInterstitial:(id)sender {
  [_interstitial presentFromRootViewController:self];
}

@end
