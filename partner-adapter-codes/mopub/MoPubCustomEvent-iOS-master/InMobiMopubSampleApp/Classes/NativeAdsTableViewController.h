//
//  NativeTableViewController.h
//  InMobiMopubSampleApp
//
//  Created by Niranjan Agrawal on 29/10/15.
//
//

#import <UIKit/UIKit.h>
#import "MPNativeCustomEventDelegate.h"
#import "MPTableViewAdPlacer.h"
#define PUB_ID_NATIVE @"48a8f6ad17c94f37b1c30f33a0bdeeed"

@interface NativeAdsTableViewController : UITableViewController <UITableViewDelegate, UITableViewDataSource, MPNativeCustomEventDelegate, MPTableViewAdPlacerDelegate>

@property (nonatomic, strong) MPTableViewAdPlacer *placer;

@end
