//  Copyright © 2016 Google. All rights reserved.

@import GoogleMobileAds;

#import "MDIViewController.h"

#import "MDIBannerViewController.h"
#import "MDIInterstitialViewController.h"
#import "MDINativeAdViewController.h"
#import "MDIRewardBasedVideoAdViewController.h"

static NSString *const MDICellReuseIdentifier = @"Cell";
static NSInteger MDIiPhoneRowCount = 6;
static NSInteger MDIiPadRowCount = 9;

typedef NS_ENUM(NSInteger, MDIEndToEndTestType) {
  MDIEndToEndTestTypeInterstitial,
  MDIEndToEndTestTypeBanner,
  MDIEndToEndTestTypeSmartBanner,
  MDIEndToEndTestTypeMediumRectangle,
  MDIEndToEndTestTypeRewardBasedVideo,
  MDIEndToEndTestTypeNativeAd,
  MDIEndToEndTestTypeFullBanner,
  MDIEndToEndTestTypeLeaderboard,
  MDIEndToEndTestTypeSkyscraper
};

@interface MDIViewController ()<UITableViewDataSource, UITableViewDelegate> {
  /// Table view which shows list of end to end test types.
  __weak IBOutlet UITableView *_tableView;
}

@end

@implementation MDIViewController

- (void)viewDidLoad {
  [super viewDidLoad];
  self.title = @"End To End Tests";
  [_tableView registerClass:[UITableViewCell class] forCellReuseIdentifier:MDICellReuseIdentifier];
}

#pragma mark - UITableView Data Source Methods

- (NSInteger)tableView:(UITableView *)tableView numberOfRowsInSection:(NSInteger)section {
  if (UI_USER_INTERFACE_IDIOM() == UIUserInterfaceIdiomPad) {
    return MDIiPadRowCount;
  } else {
    return MDIiPhoneRowCount;
  }
}

- (UITableViewCell *)tableView:(UITableView *)tableView
         cellForRowAtIndexPath:(NSIndexPath *)indexPath {
  UITableViewCell *cell =
      [tableView dequeueReusableCellWithIdentifier:MDICellReuseIdentifier forIndexPath:indexPath];
  NSString *labelText = @"";
  switch (indexPath.row) {
    case MDIEndToEndTestTypeInterstitial:
      labelText = @"Interstitial";
      break;
    case MDIEndToEndTestTypeBanner:
      labelText = @"Banner";
      break;
    case MDIEndToEndTestTypeSmartBanner:
      labelText = @"Smart Banner";
      break;
    case MDIEndToEndTestTypeMediumRectangle:
      labelText = @"Medium Rectangle";
      break;
    case MDIEndToEndTestTypeRewardBasedVideo:
      labelText = @"RewardBasedVideo";
      break;
    case MDIEndToEndTestTypeNativeAd:
      labelText = @"Native Ad";
      break;

    case MDIEndToEndTestTypeFullBanner:
      labelText = @"Full Banner";
      break;
    case MDIEndToEndTestTypeLeaderboard:
      labelText = @"Leaderboard";
      break;
    case MDIEndToEndTestTypeSkyscraper:
      labelText = @"Skyscraper";
      break;
  }

  cell.textLabel.text = labelText;
  cell.accessoryType = UITableViewCellAccessoryDisclosureIndicator;

  return cell;
}

#pragma mark - UITableView Delegate Methods

- (void)tableView:(UITableView *)tableView didSelectRowAtIndexPath:(NSIndexPath *)indexPath {
  if (indexPath.row == MDIEndToEndTestTypeInterstitial) {
    MDIInterstitialViewController *interstitialViewController =
        (MDIInterstitialViewController *)[self.storyboard
            instantiateViewControllerWithIdentifier:@"EndToEndInterstitial"];
    [self.navigationController pushViewController:interstitialViewController animated:YES];
  } else if (indexPath.row == MDIEndToEndTestTypeRewardBasedVideo) {
    MDIRewardBasedVideoAdViewController *rewardBasedVideoAdViewController =
        (MDIRewardBasedVideoAdViewController *)[self.storyboard
            instantiateViewControllerWithIdentifier:@"RewardBasedVideo"];
    [self.navigationController pushViewController:rewardBasedVideoAdViewController animated:YES];
  } else if (indexPath.row == MDIEndToEndTestTypeNativeAd) {
    MDINativeAdViewController *nativeAdViewController =
        (MDINativeAdViewController *)[self.storyboard
            instantiateViewControllerWithIdentifier:@"NativeAdVC"];
    [self.navigationController pushViewController:nativeAdViewController animated:YES];
  } else {
    MDIBannerViewController *bannerViewController;
    switch (indexPath.row) {
      case MDIEndToEndTestTypeBanner: {
        bannerViewController = [[MDIBannerViewController alloc] initWithAdSize:kGADAdSizeBanner];
      } break;
      case MDIEndToEndTestTypeSmartBanner: {
        bannerViewController =
            [[MDIBannerViewController alloc] initWithAdSize:kGADAdSizeSmartBannerPortrait];
      } break;
      case MDIEndToEndTestTypeMediumRectangle: {
        bannerViewController =
            [[MDIBannerViewController alloc] initWithAdSize:kGADAdSizeMediumRectangle];
      } break;
      case MDIEndToEndTestTypeFullBanner: {
        bannerViewController =
            [[MDIBannerViewController alloc] initWithAdSize:kGADAdSizeFullBanner];
      } break;
      case MDIEndToEndTestTypeLeaderboard: {
        bannerViewController =
            [[MDIBannerViewController alloc] initWithAdSize:kGADAdSizeLeaderboard];
      } break;
      case MDIEndToEndTestTypeSkyscraper: {
        bannerViewController =
            [[MDIBannerViewController alloc] initWithAdSize:kGADAdSizeSkyscraper];
      } break;
    }
    [self.navigationController pushViewController:bannerViewController animated:YES];
  }
}

@end
