//  Copyright © 2016 Google. All rights reserved.

@import GoogleMobileAds;
@import UIKit;

@interface MDIBannerViewController : UIViewController

/// Designated initializer. Initializes a new instance with ad size.
- (instancetype)initWithAdSize:(GADAdSize)size NS_DESIGNATED_INITIALIZER;

/// Unavailable.
- (instancetype)initWithCoder:(NSCoder *)aDecoder NS_UNAVAILABLE;

/// Unavailable.
- (instancetype)initWithNibName:(NSString *)nibNameOrNil
                         bundle:(NSBundle *)nibBundleOrNil NS_UNAVAILABLE;
/// Unavailable.
- (instancetype)init NS_UNAVAILABLE;

@end
