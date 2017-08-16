//
//  InterstitialViewController.m
//
//  Copyright (c) 2013 InMobi. All rights reserved.
//

#import "InterstitialViewController.h"

@implementation InterstitialViewController

@synthesize showInterstitialButton;
@synthesize interstitialAdController;

- (void)viewDidLoad {
    [super viewDidLoad];
	self.title = @"Interstitials";
	self.showInterstitialButton.hidden = YES;
}

#pragma mark Basic Interstitials

- (IBAction) getModalInterstitial{
	self.interstitialAdController = [MPInterstitialAdController interstitialAdControllerForAdUnitId:PUB_ID_INTERSTITIAL];	
	self.interstitialAdController.delegate = self;
	[self.interstitialAdController loadAd];
}

- (IBAction) showModalInterstitial{
	[interstitialAdController showFromViewController:self];
}

#pragma mark Interstitial delegate methods
- (UIViewController *)viewControllerForPresentingModalView{
	return self;
}

- (void)interstitialDidLoadAd:(MPInterstitialAdController *)interstitial{
	NSLog(@"Interstitial did load Ad: %@",interstitial);
	
        self.showInterstitialButton.hidden = NO;
}

- (void)dismissInterstitial:(MPInterstitialAdController *)interstitial{
    self.showInterstitialButton.hidden = YES;
}

- (void)interstitialDidFailToLoadAd:(MPInterstitialAdController *)interstitial{
	NSLog(@"Interstitial did fail to return ad %@",interstitial);
}

- (void)interstitialWillAppear:(MPInterstitialAdController *)interstitial{
	NSLog(@"Interstitial will appear: %@",interstitial);
}

- (void)interstitialDidDisappear:(MPInterstitialAdController *)interstitial {
    NSLog(@"Interstitial did disappear");
    self.showInterstitialButton.hidden = YES;
}
- (void)interstitialDidExpire:(MPInterstitialAdController *)interstitial {
    // Reload the interstitial ad, if desired.
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
