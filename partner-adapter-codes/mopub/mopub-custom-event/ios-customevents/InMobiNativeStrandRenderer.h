//
//  InMobiNativeStrandRenderer.h
//  InMobiMopubSampleApp
//
//  Created by Niranjan Agrawal on 1/5/16.
//
//

#ifndef InMobiNativeStrandRenderer_h
#define InMobiNativeStrandRenderer_h


#endif /* InMobiNativeStrandRenderer_h */

#import <UIKit/UIKit.h>
#import "MPNativeAdRenderer.h"
#import "InMobiNativeStrandAdRenderSettings.h"

@class MPNativeAdRendererConfiguration;

@interface InMobiNativeStrandRenderer : NSObject <MPNativeAdRenderer>

@property (nonatomic, strong) MPNativeViewSizeHandler viewSizeHandler;
@property (nonatomic, strong) InMobiNativeStrandAdRenderSettings *settings;

+ (MPNativeAdRendererConfiguration *)rendererConfigurationWithRendererSettings:(id<MPNativeAdRendererSettings>)rendererSettings;

@end