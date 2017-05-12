//
//  InMobiPlugin.m
//  Unity-iPhone
//
//  Created by Vineet Srivastava on 11/22/16.
//
//

#import "InMobiPlugin.h"
#import "UnityAppController.h"


@implementation InMobiPlugin

+ (UIViewController *)unityGLViewController {
    return ((UnityAppController *)[UIApplication sharedApplication].delegate).rootViewController;
}


+ (NSString*)JSONfromObject:(id)object
{
    if( [NSJSONSerialization isValidJSONObject:object] )
    {
        NSError *error = nil;
        NSData *data = [NSJSONSerialization dataWithJSONObject:object options:0 error:&error];
        
        if( error )
            NSLog( @"error serializing object to JSON: %@", error );
        
        return [[NSString alloc] initWithData:data encoding:NSUTF8StringEncoding];
    }
    
    return @"{}";
}
@end
