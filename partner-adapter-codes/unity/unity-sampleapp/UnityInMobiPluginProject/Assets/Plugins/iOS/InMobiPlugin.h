//
//  InMobiPlugin.h
//  Unity-iPhone
//
//  Created by Vineet Srivastava on 11/22/16.
//
//

@import Foundation;

@interface InMobiPlugin : NSObject

/// Returns the Unity view controller.
+ (UIViewController *)unityGLViewController;

+ (NSString*)JSONfromObject:(id)object;

@end
