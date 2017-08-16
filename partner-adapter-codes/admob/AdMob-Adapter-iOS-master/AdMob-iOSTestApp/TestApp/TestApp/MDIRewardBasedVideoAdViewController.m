//  Copyright © 2016 Google. All rights reserved.

@import GoogleMobileAds;

#import "MDIRewardBasedVideoAdViewController.h"

#import "MDIConstants.h"
#import "NSString+MDIAppendLoggingAdditions.h"

@interface MDIRewardBasedVideoAdViewController ()<GADRewardBasedVideoAdDelegate> {
  /// Callback log text view.
  __weak IBOutlet UITextView *_textView;

  /// Load reward-based video ad button.
  __weak IBOutlet UIButton *_loadRewardVideoButton;

  /// Show reward-based video ad button.
  __weak IBOutlet UIButton *_showRewardVideoButton;
}

@end

@implementation MDIRewardBasedVideoAdViewController

- (void)viewDidLoad {
  [super viewDidLoad];

  self.title = @"Reward-based Video Ads";
  [GADRewardBasedVideoAd sharedInstance].delegate = self;
  _showRewardVideoButton.enabled = NO;
}

#pragma mark - Button Actions

- (IBAction)loadRewardbasedVideoAd:(id)sender {
  GADRequest *request = [GADRequest request];
  [[GADRewardBasedVideoAd sharedInstance] loadRequest:request
                                         withAdUnitID:MDIRewardbasedVideoAdTestUnitID];
}

- (IBAction)showRewardbasedVideoAd:(id)sender {
  if ([[GADRewardBasedVideoAd sharedInstance] isReady]) {
    [[GADRewardBasedVideoAd sharedInstance] presentFromRootViewController:self];
  } else {
    [[[UIAlertView alloc] initWithTitle:@"Reward-based video ad not ready"
                                message:@"Failed to load ad."
                               delegate:nil
                      cancelButtonTitle:@"OK"
                      otherButtonTitles:nil] show];
  }
}

#pragma mark GADRewardBasedVideoAdDelegate implementation

- (void)rewardBasedVideoAdDidReceiveAd:(GADRewardBasedVideoAd *)rewardBasedVideoAd {
  NSLog(@"%s", __PRETTY_FUNCTION__);
  _textView.text = [_textView.text mdi_stringByAppendingLogString:@(__PRETTY_FUNCTION__)];
  _showRewardVideoButton.enabled = YES;
}

- (void)rewardBasedVideoAdDidOpen:(GADRewardBasedVideoAd *)rewardBasedVideoAd {
  NSLog(@"%s", __PRETTY_FUNCTION__);
  _textView.text = [_textView.text mdi_stringByAppendingLogString:@(__PRETTY_FUNCTION__)];
  _showRewardVideoButton.enabled = NO;
}

- (void)rewardBasedVideoAdDidStartPlaying:(GADRewardBasedVideoAd *)rewardBasedVideoAd {
  NSLog(@"%s", __PRETTY_FUNCTION__);
  _textView.text = [_textView.text mdi_stringByAppendingLogString:@(__PRETTY_FUNCTION__)];
}

- (void)rewardBasedVideoAdDidClose:(GADRewardBasedVideoAd *)rewardBasedVideoAd {
  NSLog(@"%s", __PRETTY_FUNCTION__);
  _textView.text = [_textView.text mdi_stringByAppendingLogString:@(__PRETTY_FUNCTION__)];
}

- (void)rewardBasedVideoAd:(GADRewardBasedVideoAd *)rewardBasedVideoAd
   didRewardUserWithReward:(GADAdReward *)reward {
  NSString *rewardMessage =
      [NSString stringWithFormat:@"Reward received with type %@ , amount %ld", reward.type,
                                 (long)[reward.amount integerValue]];
  NSLog(@"%s %@", __PRETTY_FUNCTION__, rewardMessage);
  _textView.text = [_textView.text mdi_stringByAppendingLogString:@(__PRETTY_FUNCTION__)];
}

- (void)rewardBasedVideoAdWillLeaveApplication:(GADRewardBasedVideoAd *)rewardBasedVideoAd {
  NSLog(@"%s", __PRETTY_FUNCTION__);
  _textView.text = [_textView.text mdi_stringByAppendingLogString:@(__PRETTY_FUNCTION__)];
}

- (void)rewardBasedVideoAd:(GADRewardBasedVideoAd *)rewardBasedVideoAd
    didFailToLoadWithError:(NSError *)error {
  NSLog(@"%s", __PRETTY_FUNCTION__);
  _textView.text = [_textView.text mdi_stringByAppendingLogString:@(__PRETTY_FUNCTION__)];
}

@end
