//
//  RewardedVideoViewController.h
//  InMobiMopubSampleApp
//
//  Created by Niranjan Agrawal on 29/10/15.
//
//

#import <UIKit/UIKit.h>
#import "MoPub.h"
#import "MPRewardedVideo.h"
#import "MPAdView.h"
#define PUB_ID_REWARDED @"f81e6a156ff14a55a1fb5a37fa3490a8"


@interface RewardedVideoViewController : UIViewController<MPRewardedVideoDelegate>

{
    IBOutlet UIButton* showInterstitialButton;
    
    MPRewardedVideo *rewardedVideo;
}
@property(nonatomic,retain) IBOutlet UIButton* showInterstitialButton;
@property(nonatomic,retain) MPRewardedVideo* rewardedVideo;

-(IBAction) showRewarded;
-(IBAction) loadRewarded;

@end
