//
//  RewardedVideoViewController.m
//  InMobiMopubSampleApp
//
//  Created by Niranjan Agrawal on 29/10/15.
//
//

#import "RewardedVideoViewController.h"

@interface RewardedVideoViewController ()

@end

@implementation RewardedVideoViewController
@synthesize showInterstitialButton;
@synthesize rewardedVideo;


- (void)viewDidLoad {
    [super viewDidLoad];
    self.title = @"Rewarded Video";
    self.showInterstitialButton.hidden = YES;

    // Do any additional setup after loading the view.
}

- (void)didReceiveMemoryWarning {
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}

- (IBAction)loadRewarded{
    [[MoPub sharedInstance] initializeRewardedVideoWithGlobalMediationSettings:nil delegate:self];
    [MPRewardedVideo loadRewardedVideoAdWithAdUnitID:PUB_ID_REWARDED withMediationSettings:nil];
}

- (IBAction)showRewarded{
        [MPRewardedVideo presentRewardedVideoAdForAdUnitID:PUB_ID_REWARDED fromViewController:self];
}

- (UIViewController *)viewControllerForPresentingModalView{
    return self;
}


- (void)rewardedVideoAdDidLoadForAdUnitID:(NSString *)adUnitID{
    NSLog(@"Rewarded video did load Ad: %@",adUnitID);
    
    self.showInterstitialButton.hidden = NO;
   
}

- (void)rewardedVideoAdDidFailToPlayForAdUnitID:(NSString *)adUnitID error:(NSError *)error{
    NSLog(@"Rewarded video did fail to play for: %@",adUnitID);
}

- (void)rewardedVideoAdWillAppearForAdUnitID:(NSString *)adUnitID{
    NSLog(@"Rewarded video will appear for: %@",adUnitID);

}

- (void)rewardedVideoAdDidAppearForAdUnitID:(NSString *)adUnitID{
    NSLog(@"Rewarded video did appear for: %@",adUnitID);

    
}

- (void)rewardedVideoAdWillDisappearForAdUnitID:(NSString *)adUnitID{
    NSLog(@"Rewarded video will disappear for: %@",adUnitID);

}

- (void)rewardedVideoAdDidDisappearForAdUnitID:(NSString *)adUnitID{
    NSLog(@"Rewarded video did disappear for: %@",adUnitID);
    self.showInterstitialButton.hidden = YES;
}

- (void)rewardedVideoAdDidExpireForAdUnitID:(NSString *)adUnitID{
    NSLog(@"Rewarded video did expire for: %@",adUnitID);

}

- (void)rewardedVideoAdDidReceiveTapEventForAdUnitID:(NSString *)adUnitID{
    NSLog(@"Rewarded video did receive tap event: %@",adUnitID);
    
}

- (void)rewardedVideoAdShouldRewardForAdUnitID:(NSString *)adUnitID reward:(MPRewardedVideoReward *)reward{
    NSLog(@"Rewarded video ad should reward for: %@",adUnitID);
    NSLog(@"Currency:%@",[reward currencyType]);
    NSLog(@"Amount:%@",[reward amount]);
}

- (void)rewardedVideoAdWillLeaveApplicationForAdUnitID:(NSString *)adUnitID{
    NSLog(@"Rewarded video will leave application for: %@",adUnitID);

}


/*
#pragma mark - Navigation

// In a storyboard-based application, you will often want to do a little preparation before navigation
- (void)prepareForSegue:(UIStoryboardSegue *)segue sender:(id)sender {
    // Get the new view controller using [segue destinationViewController].
    // Pass the selected object to the new view controller.
}
*/

@end
