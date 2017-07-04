//
//  SimpleAdsAppDelegate.m
//
//  Copyright (c) 2013 InMobi. All rights reserved.
//

#import "SimpleAdsAppDelegate.h"
#import "SimpleAdsViewController.h"
#import "MPInterstitialAdController.h"
#import "MPAdConversionTracker.h"
#import <InMobiSDK/IMSdk.h>

@implementation SimpleAdsAppDelegate

@synthesize window;
@synthesize tabBarController;

- (BOOL)application:(UIApplication *)application didFinishLaunchingWithOptions:(NSDictionary *)launchOptions {    
	
    // Override point for customization after app launch.
    
    if ([self.window respondsToSelector:@selector(setRootViewController:)]) {
        [self.window setRootViewController:self.tabBarController];
    } else {
        [window addSubview:self.tabBarController.view];
    }
        
    [window makeKeyAndVisible];
//    [IMSdk initWithAccountID:@"4028cb8b2c3a0b45012c406824e800ba"];
	return YES;
}

@end
