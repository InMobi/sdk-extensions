//
//  InMobiNativeStrandRenderer.m
//  InMobiMopubSampleApp
//
//  Created by Niranjan Agrawal on 1/5/16.
//
//

#import <Foundation/Foundation.h>
#import "InMobiNativeStrandRenderer.h"
#import "MPNativeAdRendererConfiguration.h"
#import "InMobiNativeStrandAdAdapter.h"
#import <InMobiSDK/IMNativeStrands.h>
#import "InMobiNativeStrandAdRenderSettings.h"

@interface InMobiNativeStrandRenderer()

@end

@implementation InMobiNativeStrandRenderer


+ (MPNativeAdRendererConfiguration *)rendererConfigurationWithRendererSettings:(id<MPNativeAdRendererSettings>)rendererSettings{
    MPNativeAdRendererConfiguration *config = [[MPNativeAdRendererConfiguration alloc] init];
    config.rendererClass = [self class];
    config.rendererSettings = rendererSettings;
    config.supportedCustomEvents = @[@"InMobiNativeStrandCustomEvent"];
    return config;
}

- (instancetype)initWithRendererSettings:(id<MPNativeAdRendererSettings>)rendererSettings{
    if (self = [super init]) {
        self.settings = (InMobiNativeStrandAdRenderSettings *)rendererSettings;
        self.viewSizeHandler = [self.settings.viewSizeHandler copy];
    }
    return self;
}

- (UIView *)retrieveViewWithAdapter:(id<MPNativeAdAdapter>)adapter error:(NSError **)error{
    InMobiNativeStrandAdAdapter *inmobiStrandAdapter = (InMobiNativeStrandAdAdapter*)adapter;
    if(inmobiStrandAdapter.nativeStrands!=nil)
        return [inmobiStrandAdapter.nativeStrands strandsView];
    return nil;
}

- (void)adViewWillMoveToSuperview:(UIView *)superview{
    
}


@end
