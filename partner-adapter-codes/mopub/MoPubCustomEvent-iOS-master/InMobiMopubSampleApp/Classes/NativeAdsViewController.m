//
//  NativeAdsViewController.m
//  InMobiMopubSampleApp
//
//  Created by Niranjan Agrawal on 29/10/15.
//
//

#import "NativeAdsViewController.h"
#import "NativeAdsTableViewController.h"

@interface NativeAdsViewController ()

@end

@implementation NativeAdsViewController

- (void)viewDidLoad {
    [super viewDidLoad];
    // Do any additional setup after loading the view.
}

- (void)didReceiveMemoryWarning {
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}

-(IBAction) loadNative{
    NSLog(@"In load native");
    NativeAdsTableViewController *viewController = [[NativeAdsTableViewController alloc]initWithNibName:@"NativeAdsTableViewController" bundle:nil];
    [self.navigationController pushViewController:viewController animated:YES];
}
/*
#pragma mark - Navigation

// In a storyboard-based application, you will often want to do a little preparation before navigation
- (void)prepareForSegue:(UIStoryboardSegue *)segue sender:(id)sender {
    // Get the new view controller using [segue destinationViewController].
    // Pass the selected object to the new view controller.
}
*/

@end
