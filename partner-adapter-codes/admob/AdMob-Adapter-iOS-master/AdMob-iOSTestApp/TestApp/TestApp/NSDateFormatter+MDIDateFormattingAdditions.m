//  Copyright © 2016 Google. All rights reserved.

#import "NSDateFormatter+MDIDateFormattingAdditions.h"

@implementation NSDateFormatter (MDIDateFormattingAdditions)

+ (NSDateFormatter *)mdi_dateFormatter {
  static NSDateFormatter *dateFormatter;
  static dispatch_once_t onceToken;
  dispatch_once(&onceToken, ^{
    dateFormatter = [[NSDateFormatter alloc] init];
    dateFormatter.dateFormat = @"yyyy-MM-dd HH:mm:ss.SSS z";
    dateFormatter.calendar = [NSCalendar currentCalendar];
    dateFormatter.locale = [NSLocale currentLocale];
    dateFormatter.timeZone = [NSTimeZone defaultTimeZone];
  });
  return dateFormatter;
}

@end
