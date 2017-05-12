//
//  InMobiObjectCache.m
//  Unity-iPhone
//
//  Created by Vineet Srivastava on 11/22/16.
//
//

#include "InMobiObjectCache.h"

@implementation InMobiObjectCache

+ (instancetype)sharedInstance {
    static InMobiObjectCache *sharedInstance;
    static dispatch_once_t onceToken;
    dispatch_once(&onceToken, ^{
        sharedInstance = [[self alloc] init];
    });
    return sharedInstance;
}

- (id)init {
    self = [super init];
    if (self) {
        _references = [[NSMutableDictionary alloc] init];
    }
    return self;
}

@end

@implementation NSObject (InMobiOwnershipAdditions)

- (NSString *)inmobi_referenceKey {
    return [NSString stringWithFormat:@"%p", (void *)self];
}

@end
