//  Copyright © 2016 Google. All rights reserved.

#import "MDIBannerViewController.h"

#import "MDIConstants.h"
#import "NSString+MDIAppendLoggingAdditions.h"
#import <InMobiSDK/IMSdk.h>

@interface MDIBannerViewController ()<GADBannerViewDelegate> {
  /// Parent view for Bannnerview and logs text view.
  __weak IBOutlet UIScrollView *_scrollView;

  /// Callback log text view.
  __weak IBOutlet UITextView *_textView;

  /// Banner view ad instance.
  GADBannerView *_bannerView;

  /// Banner view ad size.
  GADAdSize _adSize;
}

@end

@implementation MDIBannerViewController

- (instancetype)initWithAdSize:(GADAdSize)size {
  self = [super initWithNibName:@"MDIBannerViewController" bundle:nil];
  if (self) {
    _adSize = size;
  }

  return self;
}

- (void)viewDidLoad {
  [super viewDidLoad];
  self.title = @"End To End Banner Tests";
  //[IMSdk setLogLevel:kIMSDKLogLevelDebug];

  UIDeviceOrientation orientation = [UIDevice currentDevice].orientation;
  _adSize = [self adSizeForOrientation:orientation];
  _bannerView = [[GADBannerView alloc] initWithAdSize:_adSize];
  _bannerView.delegate = self;
  _bannerView.adUnitID = MDIBannerTestAdUnitID;
  _bannerView.rootViewController = self;
  [_scrollView addSubview:_bannerView];
  [_bannerView loadRequest:[GADRequest request]];
}

- (GADAdSize)adSizeForOrientation:(UIDeviceOrientation)orientation {
  if (GADAdSizeEqualToSize(_adSize, kGADAdSizeSmartBannerPortrait) ||
      GADAdSizeEqualToSize(_adSize, kGADAdSizeSmartBannerLandscape)) {
    if (UIDeviceOrientationIsPortrait(orientation)) {
      return kGADAdSizeSmartBannerPortrait;
    } else {
      return kGADAdSizeSmartBannerLandscape;
    }
  }
  return _adSize;
}

#pragma mark - GADBannerViewDelegate Methods

- (void)adViewDidReceiveAd:(GADBannerView *)bannerView {
  NSLog(@"%s", __PRETTY_FUNCTION__);
  _textView.text = [_textView.text mdi_stringByAppendingLogString:@(__PRETTY_FUNCTION__)];
}

- (void)adView:(GADBannerView *)bannerView didFailToReceiveAdWithError:(GADRequestError *)error {
  _textView.text = [_textView.text mdi_stringByAppendingLogString:@(__PRETTY_FUNCTION__)];
  if (error.localizedDescription) {
    NSLog(@"%s %@", __PRETTY_FUNCTION__, error.localizedDescription);
  }
}

- (void)adViewWillPresentScreen:(GADBannerView *)bannerView {
  NSLog(@"%s", __PRETTY_FUNCTION__);
  _textView.text = [_textView.text mdi_stringByAppendingLogString:@(__PRETTY_FUNCTION__)];
}

- (void)adViewWillDismissScreen:(GADBannerView *)bannerView {
  NSLog(@"%s", __PRETTY_FUNCTION__);
  _textView.text = [_textView.text mdi_stringByAppendingLogString:@(__PRETTY_FUNCTION__)];
}

- (void)adViewDidDismissScreen:(GADBannerView *)bannerView {
  NSLog(@"%s", __PRETTY_FUNCTION__);
  _textView.text = [_textView.text mdi_stringByAppendingLogString:@(__PRETTY_FUNCTION__)];
}

- (void)adViewWillLeaveApplication:(GADBannerView *)bannerView {
  NSLog(@"%s", __PRETTY_FUNCTION__);
  _textView.text = [_textView.text mdi_stringByAppendingLogString:@(__PRETTY_FUNCTION__)];
}

@end
