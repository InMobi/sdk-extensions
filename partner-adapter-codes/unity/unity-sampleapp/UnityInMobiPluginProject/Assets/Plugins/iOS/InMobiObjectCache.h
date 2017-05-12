//
//  InMobiObjectCache.h
//  Unity-iPhone
//
//  Created by Vineet Srivastava on 11/22/16.
//
//

@import Foundation;

/// A cache to hold onto objects while Unity is still referencing them.
@interface InMobiObjectCache : NSObject

+ (instancetype)sharedInstance;

/// References to objects Google Mobile ads objects created from Unity.
@property(nonatomic, strong) NSMutableDictionary *references;

@end

@interface NSObject (InMobiOwnershipAdditions)

/// Returns a key used to lookup a Google Mobile Ads object. This method is intended to only be used
/// by Google Mobile Ads objects.
- (NSString *)inmobi_referenceKey;

@end
