//
//  InMobiRewardedCustomEvent.h
//  InMobiMopubSampleApp
//
//  Created by Niranjan Agrawal on 28/10/15.
//
//

#ifndef InMobiRewardedCustomEvent_h
#define InMobiRewardedCustomEvent_h


#endif /* InMobiRewardedCustomEvent_h */

#import "MPRewardedVideoCustomEvent.h"
#import <InMobiSDK/IMInterstitial.h>

@interface InMobiRewardedCustomEvent : MPRewardedVideoCustomEvent<IMInterstitialDelegate>
@property (nonatomic, retain) IMInterstitial *inMobiInterstitial;

@end
