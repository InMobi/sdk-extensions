//
//  SimpleAdsViewController.m
//
//  Copyright (c) 2013 InMobi. All rights reserved.
//

#import "SimpleAdsViewController.h"
#import "MPAdView.h"
#import <CoreLocation/CoreLocation.h>

@implementation SimpleAdsViewController

@synthesize mpAdView;

- (void)viewDidLoad {
    [super viewDidLoad];
	
	// 320x50 size
	mpAdView = [[MPAdView alloc] initWithAdUnitId:PUB_ID_320x50 size:MOPUB_BANNER_SIZE];
	mpAdView.delegate = self;
    mpAdView.frame = CGRectMake(0, 30, 320, 50);
//	[mpAdView loadAd];
	[self.view addSubview:mpAdView];
	
}

- (IBAction) refreshAd {
	
	// refresh ad 
	[self.mpAdView loadAd];
		
}

- (void)didReceiveMemoryWarning {
	// Releases the view if it doesn't have a superview.
    [super didReceiveMemoryWarning];
	
	// Release any cached data, images, etc that aren't in use.
}

- (void)viewDidUnload {
	// Release any retained subviews of the main view.
	// e.g. self.myOutlet = nil;
}

- (UIViewController *)viewControllerForPresentingModalView
{
	return self;
}

- (BOOL)shouldAutorotateToInterfaceOrientation:(UIInterfaceOrientation)interfaceOrientation
{
    return (interfaceOrientation == UIInterfaceOrientationPortrait);
}

- (BOOL)shouldAutorotate
{
    return NO;
}

- (NSUInteger)supportedInterfaceOrientations
{
    return UIInterfaceOrientationMaskPortrait;
}

@end
