//
//  SimpleAdsAppDelegate.h
//
//  Copyright (c) 2013 InMobi. All rights reserved.
//

#import <UIKit/UIKit.h>

@class SimpleAdsViewController;
@class MPInterstitialAdController;

@interface SimpleAdsAppDelegate : NSObject <UIApplicationDelegate> {
    UIWindow *window;
	UITabBarController* tabBarController;
}

@property (nonatomic, retain) IBOutlet UIWindow *window;
@property (nonatomic, retain) IBOutlet UITabBarController* tabBarController;

@end

