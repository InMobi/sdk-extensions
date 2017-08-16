//
//  NativeStrandsAdTableViewController.h
//  InMobiMopubSampleApp
//
//  Created by Niranjan Agrawal on 1/5/16.
//
//

#ifndef NativeStrandsAdTableViewController_h
#define NativeStrandsAdTableViewController_h


#endif /* NativeStrandsAdTableViewController_h */


#import <UIKit/UIKit.h>
#import "MPNativeCustomEventDelegate.h"
#import "MPTableViewAdPlacer.h"
#define PUB_ID_NATIVE @"6edf61ef049f419a926bf1f408856f18"

@interface NativeStrandsAdTableViewController : UITableViewController <UITableViewDelegate, UITableViewDataSource, MPNativeCustomEventDelegate, MPTableViewAdPlacerDelegate>

@property (nonatomic, strong) MPTableViewAdPlacer *placer;

@end
