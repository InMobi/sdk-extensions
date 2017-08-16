//
//  NativeTableViewController.m
//  InMobiMopubSampleApp
//
//  Created by Niranjan Agrawal on 29/10/15.
//
//

#import "NativeAdsTableViewController.h"
#import "MPNativeAdRequestTargeting.h"
#import "MPTableViewAdPlacer.h"
#import "MPTableViewAdPlacer.h"
#import "MPNativeAdConstants.h"
#import "MPStaticNativeAdRendererSettings.h"
#import "MPStaticNativeAdRenderer.h"
#import "MPNativeAdRendererConfiguration.h"
#import "NativeAdView.h"
#import <InMobiSDK/IMSdk.h>

@implementation NativeAdsTableViewController

{
    NSArray *tableData;
}

- (void)viewDidLoad
{
    [super viewDidLoad];
    [IMSdk setLogLevel:kIMSDKLogLevelDebug];
    if ([self.tableView respondsToSelector:@selector(registerClass:forCellReuseIdentifier:)]) {
        [self.tableView registerClass:[UITableViewCell class] forCellReuseIdentifier:@"SimpleTableItem"];
    }
    // Initialize table data
    NSLog(@"Reached native custom event");
    tableData = [NSArray arrayWithObjects:@"Egg Benedict", @"Mushroom Risotto", @"Full Breakfast", @"Hamburger", @"Ham and Egg Sandwich", @"Creme Brelee", @"White Chocolate Donut", @"Starbucks Coffee", @"Vegetable Curry", @"Instant Noodle with Egg", @"Noodle with BBQ Pork", @"Japanese Noodle with Pork", @"Green Tea", @"Thai Shrimp Cake", @"Angry Birds Cake", @"Ham and Cheese Panini", nil];
    
    MPStaticNativeAdRendererSettings *settings = [[MPStaticNativeAdRendererSettings alloc] init];
    settings.renderingViewClass = [NativeAdView class];
    settings.viewSizeHandler = ^(CGFloat maxWidth) {
        return CGSizeMake(maxWidth, 312.0f);
    };

    //static NSString *simpleTableIdentifier = @"SimpleTableItem";
    //[self.tableView registerClass: [simpleTableIdentifier class] forCellReuseIdentifier:simpleTableIdentifier];
    MPNativeAdRendererConfiguration *config = [MPStaticNativeAdRenderer rendererConfigurationWithRendererSettings:settings];
    
    self.placer = [MPTableViewAdPlacer placerWithTableView:self.tableView viewController:self rendererConfigurations:@[config]];
    self.placer.delegate = self;
    
    [self.placer loadAdsForAdUnitID:PUB_ID_NATIVE];
}

- (NSInteger)tableView:(UITableView *)tableView numberOfRowsInSection:(NSInteger)section
{
    return [tableData count];
}

- (UITableViewCell *)tableView:(UITableView *)tableView cellForRowAtIndexPath:(NSIndexPath *)indexPath
{
    static NSString *simpleTableIdentifier = @"SimpleTableItem";
    
    UITableViewCell *cell = [tableView mp_dequeueReusableCellWithIdentifier:simpleTableIdentifier forIndexPath:indexPath];
    //UITableViewCell *cell = [tableView dequeueReusableCellWithIdentifier:simpleTableIdentifier];
    
    if (cell == nil) {
        cell = [[UITableViewCell alloc] initWithStyle:UITableViewCellStyleDefault reuseIdentifier:simpleTableIdentifier];
    }
    
    cell.textLabel.text = [tableData objectAtIndex:indexPath.row];
    return cell;
}

- (void)tableView:(UITableView *)tableView didSelectRowAtIndexPath:(NSIndexPath *)indexPath
{
    /*
     * IMPORTANT: add the mp_ prefix to deselectRowAtIndexPath:animated:.
     */
    [tableView mp_deselectRowAtIndexPath:indexPath animated:YES];
}


- (void)nativeCustomEvent:(MPNativeCustomEvent *)event didLoadAd:(MPNativeAd *)adObject
{
    NSLog(@"Native ad loaded successfully");
}

- (void)nativeCustomEvent:(MPNativeCustomEvent *)event didFailToLoadAdWithError:(NSError *)error
{
    NSLog(@"Native ad failed to load");
}

@end
