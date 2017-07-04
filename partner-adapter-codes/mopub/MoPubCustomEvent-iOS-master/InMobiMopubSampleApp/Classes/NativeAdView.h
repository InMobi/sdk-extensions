//
//  NativeAdView.h
//  InMobiMopubSampleApp
//
//  Created by Niranjan Agrawal on 29/10/15.
//
//

#ifndef NativeAdView_h
#define NativeAdView_h


#endif /* NativeAdView_h */
#import <UIKit/UIKit.h>
#import "MPNativeAdRendering.h"

@interface NativeAdView : UIView <MPNativeAdRendering>

@property (strong, nonatomic) UILabel *titleLabel;
@property (strong, nonatomic) UILabel *mainTextLabel;
@property (strong, nonatomic) UILabel *callToActionLabel;
@property (strong, nonatomic) UIImageView *iconImageView;
@property (strong, nonatomic) UIImageView *mainImageView;

@end