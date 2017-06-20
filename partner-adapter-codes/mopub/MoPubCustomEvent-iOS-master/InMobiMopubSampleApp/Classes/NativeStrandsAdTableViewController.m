//
//  NativeStrandsAdTableViewController.m
//  InMobiMopubSampleApp
//
//  Created by Niranjan Agrawal on 1/5/16.
//
//

#import <Foundation/Foundation.h>
#import "NativeStrandsAdTableViewController.h"
#import "MPNativeAdRequestTargeting.h"
#import "MPTableViewAdPlacer.h"
#import "MPTableViewAdPlacer.h"
#import "MPNativeAdConstants.h"
#import "InMobiNativeStrandAdRenderSettings.h"
#import "MPNativeAdRendererConfiguration.h"
#import "NativeAdView.h"
#import "InMobiNativeStrandRenderer.h"

@implementation NativeStrandsAdTableViewController

{
    NSArray *tableData;
}

- (void)viewDidLoad
{
    [super viewDidLoad];
    
    if ([self.tableView respondsToSelector:@selector(registerClass:forCellReuseIdentifier:)]) {
        [self.tableView registerClass:[UITableViewCell class] forCellReuseIdentifier:@"SimpleTableItem"];
    }
    NSLog(@"Reached native strand custom event");
    
    tableData = [NSArray arrayWithObjects:@"Egg Benedict", @"Mushroom Risotto", @"Full Breakfast", @"Hamburger", @"Ham and Egg Sandwich", @"Creme Brelee", @"White Chocolate Donut", @"Starbucks Coffee", @"Vegetable Curry", @"Instant Noodle with Egg", @"Noodle with BBQ Pork", @"Japanese Noodle with Pork", @"Green Tea", @"Thai Shrimp Cake", @"Angry Birds Cake", @"Ham and Cheese Panini", nil];
    
    InMobiNativeStrandAdRenderSettings *settings = [[InMobiNativeStrandAdRenderSettings alloc] init];
    
    settings.viewSizeHandler = ^(CGFloat maxWidth) {
        return CGSizeMake(maxWidth, 450.0f);
    };
    
    MPNativeAdRendererConfiguration *config = [InMobiNativeStrandRenderer rendererConfigurationWithRendererSettings:settings];
    
    self.placer = [MPTableViewAdPlacer placerWithTableView:self.tableView viewController:self rendererConfigurations:@[config]];
    self.placer.delegate = self;
    
//    [self.placer loadAdsForAdUnitID:PUB_ID_NATIVE];
}

-(void) viewDidAppear:(BOOL)animated{
    NSLog(@"View reloaded");
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
