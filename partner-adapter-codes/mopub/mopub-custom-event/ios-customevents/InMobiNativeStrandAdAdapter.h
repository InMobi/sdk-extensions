//
//  InMobiNativeStrandAdAdapter.h
//  InMobiMopubSampleApp
//
//  Created by Niranjan Agrawal on 1/5/16.
//
//

#ifndef InMobiNativeStrandAdAdapter_h
#define InMobiNativeStrandAdAdapter_h


#endif /* InMobiNativeStrandAdAdapter_h */

#import "MPNativeAdAdapter.h"
#import <InMobiSDK/IMNativeStrands.h>

@class IMNativeStrands;
@interface InMobiNativeStrandAdAdapter : NSObject <MPNativeAdAdapter,IMNativeStrandsDelegate>
@property (nonatomic, weak) id<MPNativeAdAdapterDelegate> delegate;
@property (nonatomic, strong) IMNativeStrands *nativeStrands;

- (instancetype)initWithInMobiNativeStrandsAd:(IMNativeStrands *)nativeStrands;

@end
