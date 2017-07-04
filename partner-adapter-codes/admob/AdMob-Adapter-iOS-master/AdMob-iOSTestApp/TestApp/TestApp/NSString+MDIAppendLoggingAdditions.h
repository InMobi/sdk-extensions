//  Copyright © 2016 Google. All rights reserved.

@import Foundation;

@interface NSString (MDIAppendLoggingAdditions)

/// Appends timestamp to the provided string.
- (NSString *)mdi_stringByAppendingLogString:(NSString *)string;

@end
