//  Copyright © 2016 Google. All rights reserved.

@import GoogleMobileAds;
@import AdSupport;

#import "MDINativeAdViewController.h"

#import "MDIConstants.h"
#import "NSString+MDIAppendLoggingAdditions.h"

#import <InMobiSDK/IMSdk.h>
#import "GADInMobiExtras.h"

@interface MDINativeAdViewController ()<GADNativeAppInstallAdLoaderDelegate,
                                        GADNativeContentAdLoaderDelegate, GADNativeAdDelegate> {
  /// Callback log text view.
  __weak IBOutlet UITextView *_textView;

  /// Refresh the native ad.
  __weak IBOutlet UIButton *_refreshButton;

  /// Switch to request app install ads.
  __weak IBOutlet UISwitch *_appInstallAdSwitch;

  /// Switch to request content ads.
  __weak IBOutlet UISwitch *_contentAdSwitch;

  /// Container that holds the native ad.
  __weak IBOutlet UIView *_nativeAdPlaceholder;
}

/// Refreshes the ad.
- (IBAction)refreshAd:(id)sender;

/// You must keep a strong reference to the GADAdLoader during the ad loading process.
@property(nonatomic, strong) GADAdLoader *adLoader;

/// The native ad view that is being presented.
@property(nonatomic, strong) UIView *nativeAdView;

@end

@implementation MDINativeAdViewController

- (void)viewDidLoad {
  [super viewDidLoad];
  self.title = @"Native Ads";
  //[IMSdk setLogLevel:kIMSDKLogLevelDebug];
  NSString *Identifier = [[[UIDevice currentDevice] identifierForVendor] UUIDString];
  NSString *idfaString = [[[ASIdentifierManager sharedManager] advertisingIdentifier] UUIDString];
  NSLog(@"output is : %@ %@", Identifier, idfaString);

  [self refreshAd:nil];
}

- (IBAction)refreshAd:(id)sender {
  NSLog(@"%s", __PRETTY_FUNCTION__);

  // Loads an ad for any of app install, content, or custom native ads.
  NSMutableArray *adTypes = [[NSMutableArray alloc] init];
  if (_appInstallAdSwitch.on) {
    [adTypes addObject:kGADAdLoaderAdTypeNativeAppInstall];
  }
  if (_contentAdSwitch.on) {
    [adTypes addObject:kGADAdLoaderAdTypeNativeContent];
  }

  if (adTypes.count == 0) {
    NSLog(@"At least one ad format must be selected to refresh the ad.");
  } else {
    _refreshButton.enabled = NO;

    self.adLoader = [[GADAdLoader alloc] initWithAdUnitID:MDINativeAdTestUnitID
                                       rootViewController:self
                                                  adTypes:adTypes
                                                  options:nil];
    self.adLoader.delegate = self;

    GADRequest *request = [GADRequest request];
      GADInMobiExtras *ext = [[GADInMobiExtras alloc] init];
      [ext setAge:10];
      [ext setPostalCode:@"ABC123"];
      [ext setAreaCode:@"ABC"];
      [ext setIncome:1000];
      [ext setInterests:@"Hello"];
      [ext setKeywords:@"keyword"];
      [ext setLanguage:@"english, hindi"];
      [ext setLocationWithCity:@"bhilai" state:@"chhattisgarh" country:@"india"];
      [ext setLoginId:@"loginid" ];
      [ext setNationality:@"INDIAN"];
      [ext setSessionId:@"Session"];
      [ext setPostalCode:@""];
      [ext setYearOfBirth:2016];
      NSMutableDictionary *dict = [NSMutableDictionary dictionary];
      [dict setObject:@"refTagValue" forKey:@"refTagKey"];
      [ext setAdditionalParameters:dict];
      [ext setHouseholdIncome:kIMSDKHouseholdIncomeBetween50kAnd75kUSD];
      [ext setEthnicityType:kIMSDKEthnicityHispanic];
      [ext setAgeGroup:kIMSDKAgeGroupBetween25And34];
      [ext setEducationType:kIMSDKEducationCollegeOrGraduate];
       [request registerAdNetworkExtras:ext];
    //    request.testDevices = @[ @"207aca6b75de1723b0162f313f9f2698" ];

    [self.adLoader loadRequest:request];
  }
}

- (void)setAdView:(UIView *)view {
  // Remove previous ad view.
  [self.nativeAdView removeFromSuperview];
  self.nativeAdView = view;

  // Add new ad view and set constraints to fill its container.
  [_nativeAdPlaceholder addSubview:view];
  [self.nativeAdView setTranslatesAutoresizingMaskIntoConstraints:NO];

  NSDictionary *viewDictionary = NSDictionaryOfVariableBindings(_nativeAdView);
  [self.view addConstraints:[NSLayoutConstraint constraintsWithVisualFormat:@"H:|[_nativeAdView]|"
                                                                    options:0
                                                                    metrics:nil
                                                                      views:viewDictionary]];
  [self.view addConstraints:[NSLayoutConstraint constraintsWithVisualFormat:@"V:|[_nativeAdView]|"
                                                                    options:0
                                                                    metrics:nil
                                                                      views:viewDictionary]];
}

#pragma mark GADAdLoaderDelegate implementation

- (void)adLoader:(GADAdLoader *)adLoader didFailToReceiveAdWithError:(GADRequestError *)error {
  _textView.text = [_textView.text mdi_stringByAppendingLogString:@(__PRETTY_FUNCTION__)];
  NSLog(@"%s", __PRETTY_FUNCTION__);
  _refreshButton.enabled = YES;
}

#pragma mark GADNativeAppInstallAdLoaderDelegate implementation

- (void)adLoader:(GADAdLoader *)adLoader
    didReceiveNativeAppInstallAd:(GADNativeAppInstallAd *)nativeAppInstallAd {
  _textView.text = [_textView.text mdi_stringByAppendingLogString:@(__PRETTY_FUNCTION__)];
  NSLog(@"%s", __PRETTY_FUNCTION__);
    nativeAppInstallAd.delegate = self;

  _refreshButton.enabled = YES;

    NSLog(@"Got the native ad from InMobi");
  // Create and place ad in view hierarchy.
  GADNativeAppInstallAdView *appInstallAdView =
      [[[NSBundle mainBundle] loadNibNamed:@"MDINativeAppInstallAdView" owner:nil options:nil]
          firstObject];
  [self setAdView:appInstallAdView];

  // Associate the app install ad view with the app install ad object. This is required to make the
  // ad clickable.
  appInstallAdView.nativeAppInstallAd = nativeAppInstallAd;

  // Populate the app install ad view with the app install ad assets.
  if (nativeAppInstallAd.headline) {
    ((UILabel *)appInstallAdView.headlineView).text = nativeAppInstallAd.headline;
  }
  if (nativeAppInstallAd.icon) {
    ((UIImageView *)appInstallAdView.iconView).image = nativeAppInstallAd.icon.image;
  }
  if (nativeAppInstallAd.body) {
    ((UILabel *)appInstallAdView.bodyView).text = nativeAppInstallAd.body;
  }
  if (nativeAppInstallAd.store) {
    ((UILabel *)appInstallAdView.storeView).text = nativeAppInstallAd.store;
  }
  if (nativeAppInstallAd.price) {
    ((UILabel *)appInstallAdView.priceView).text = nativeAppInstallAd.price;
  }
  if (nativeAppInstallAd.images) {
    ((UIImageView *)appInstallAdView.imageView).image =
        ((GADNativeAdImage *)[nativeAppInstallAd.images firstObject]).image;
  }
  if (nativeAppInstallAd.starRating) {
    ((UIImageView *)appInstallAdView.starRatingView).image =
        [self imageForStars:nativeAppInstallAd.starRating];
  }
  if (nativeAppInstallAd.callToAction) {
      UIButton *btn = (UIButton*)appInstallAdView.callToActionView;
    [btn setTitle:nativeAppInstallAd.callToAction forState:UIControlStateNormal];
      [btn addTarget:self action:@selector(clickBtn) forControlEvents:UIControlEventTouchUpInside];
  }

  // In order for the SDK to process touch events properly, user interaction should be disabled.
  appInstallAdView.callToActionView.userInteractionEnabled = NO;
}

-(void) clickBtn{
    [[UIApplication sharedApplication] openURL:[NSURL URLWithString:@"http://www.google.com/"]];
}

/// Gets an image representing the number of stars. Returns nil if rating is less than 3.5 stars.
- (UIImage *)imageForStars:(NSDecimalNumber *)numberOfStars {
  NSNumberFormatter *numberFormatter = [[NSNumberFormatter alloc] init];
  numberFormatter.numberStyle = NSNumberFormatterDecimalStyle;
  NSString *string = [numberFormatter stringFromNumber:numberOfStars];
  double starRating = [string doubleValue];

  if (starRating >= 5) {
    return [UIImage imageNamed:@"stars_5.png"];
  } else if (starRating >= 4.5) {
    return [UIImage imageNamed:@"stars_4_5.png"];
  } else if (starRating >= 4) {
    return [UIImage imageNamed:@"stars_4.png"];
  } else if (starRating >= 3.5) {
    return [UIImage imageNamed:@"stars_3_5.png"];
  } else {
    return nil;
  }
}

#pragma mark GADNativeContentAdLoaderDelegate implementation

- (void)adLoader:(GADAdLoader *)adLoader
    didReceiveNativeContentAd:(GADNativeContentAd *)nativeContentAd {
  _textView.text = [_textView.text mdi_stringByAppendingLogString:@(__PRETTY_FUNCTION__)];
  NSLog(@"%s", __PRETTY_FUNCTION__);

  _refreshButton.enabled = YES;

  // Create and place ad in view hierarchy.
  GADNativeContentAdView *contentAdView =
      [[[NSBundle mainBundle] loadNibNamed:@"MDINativeContentAdView" owner:nil options:nil]
          firstObject];
  [self setAdView:contentAdView];

  // Associate the content ad view with the content ad object. This is required to make the ad
  // clickable.
  contentAdView.nativeContentAd = nativeContentAd;

  // Populate the content ad view with the content ad assets.
  if (nativeContentAd.headline) {
    ((UILabel *)contentAdView.headlineView).text = nativeContentAd.headline;
  }
  if (nativeContentAd.body) {
    ((UILabel *)contentAdView.bodyView).text = nativeContentAd.body;
  }
  if (nativeContentAd.images) {
    ((UIImageView *)contentAdView.imageView).image =
        ((GADNativeAdImage *)[nativeContentAd.images firstObject]).image;
  }
  if (nativeContentAd.logo) {
    ((UIImageView *)contentAdView.logoView).image = nativeContentAd.logo.image;
  }
  if (nativeContentAd.advertiser) {
    ((UILabel *)contentAdView.advertiserView).text = nativeContentAd.advertiser;
  }
  if (nativeContentAd.callToAction) {
    [((UIButton *)contentAdView.callToActionView) setTitle:nativeContentAd.callToAction
                                                  forState:UIControlStateNormal];
  }

  // In order for the SDK to process touch events properly, user interaction should be disabled.
  contentAdView.callToActionView.userInteractionEnabled = NO;
}

- (void)nativeAdWillPresentScreen:(GADNativeAd *)nativeAd {
    NSLog(@"AdMob native ad will present screen");
  NSLog(@"%s", __PRETTY_FUNCTION__);
}

- (void)nativeAdWillDismissScreen:(GADNativeAd *)nativeAd {
    NSLog(@"AdMob native ad will dismiss screen");
  NSLog(@"%s", __PRETTY_FUNCTION__);
}

- (void)nativeAdDidDismissScreen:(GADNativeAd *)nativeAd {
    NSLog(@"AdMob native ad did dismiss screen");
  NSLog(@"%s", __PRETTY_FUNCTION__);
}

- (void)nativeAdWillLeaveApplication:(GADNativeAd *)nativeAd {
    NSLog(@"AdMob native ad will leave application");
  NSLog(@"%s", __PRETTY_FUNCTION__);
}

@end
