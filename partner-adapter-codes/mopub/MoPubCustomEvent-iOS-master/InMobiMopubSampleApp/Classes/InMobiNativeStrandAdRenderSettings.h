//
//  InMobiNativeStrandAdRenderSettings.h
//  InMobiMopubSampleApp
//
//  Created by Niranjan Agrawal on 1/5/16.
//
//

#ifndef InMobiNativeStrandAdRenderSettings_h
#define InMobiNativeStrandAdRenderSettings_h


#endif /* InMobiNativeStrandAdRenderSettings_h */

#import "MPNativeAdRendererSettings.h"
#import "MPNativeAdRenderer.h"

@interface InMobiNativeStrandAdRenderSettings : NSObject <MPNativeAdRendererSettings>

/**
 * A block that returns the size of the view given a maximum width. This needs to be set when
 * used in conjunction with ad placer classes so the ad placers can correctly size the cells
 * that contain the ads.
 *
 * viewSizeHandler is not used for manual native ad integration. You must set the
 * frame of your manually integrated native ad view.
 */
@property (nonatomic, readwrite, copy) MPNativeViewSizeHandler viewSizeHandler;

@end