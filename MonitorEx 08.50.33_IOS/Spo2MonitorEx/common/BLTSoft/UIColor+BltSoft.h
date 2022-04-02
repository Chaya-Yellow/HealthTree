//
//  UIColor+BltSoft.h
//  BLT-Spo2Monitor
//
//  Created by luteng on 16/6/30.
//  Copyright © 2016年 luteng. All rights reserved.
//
#import <UIKit/UIKit.h>

@interface UIColor (BltSoft)
+ (UIColor *)colorWithHexString: (NSString *) stringToConvert;
- (NSString *)hexRGB;
@end

