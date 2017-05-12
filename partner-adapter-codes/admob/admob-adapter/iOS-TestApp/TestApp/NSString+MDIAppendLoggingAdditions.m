//  Copyright © 2016 Google. All rights reserved.

#import "NSString+MDIAppendLoggingAdditions.h"

#import "NSDateFormatter+MDIDateFormattingAdditions.h"

@implementation NSString (MDIAppendLoggingAdditions)

- (NSString *)mdi_stringByAppendingLogString:(NSString *)string {
  NSString *dateString = [[NSDateFormatter mdi_dateFormatter] stringFromDate:[NSDate date]];
  return [[NSString alloc] initWithFormat:@"%@ \n %@ %@", self, dateString, string];
}

@end
