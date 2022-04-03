//
//  OMESoft.h
//  BLT-Spo2Monitor
//
//  Created by luteng on 16/7/5.
//  Copyright © 2016年 luteng. All rights reserved.
//
#import "NSDate_Omesoft.h"
#import "UIImage+Omesoft.h"
#import "UIColor+BltSoft.h"
#import "NSString+Omesoft.h"

//systemVersion
#define IOS6 ([[[UIDevice currentDevice] systemVersion] floatValue] >= 6.0 ? YES:NO)
#define IOS7 ([[[UIDevice currentDevice] systemVersion] floatValue] >= 7.0 ? YES:NO)
#define IOS9 ([[[UIDevice currentDevice] systemVersion] floatValue] >= 9.0 ? YES:NO)

//Device
#define iPhone5Screen ([UIScreen instancesRespondToSelector:@selector(currentMode)] ? CGSizeEqualToSize(CGSizeMake(640, 1136), [[UIScreen mainScreen] currentMode].size) : NO)
#define Retina ([UIScreen instancesRespondToSelector:@selector(currentMode)] ? CGSizeEqualToSize(CGSizeMake(640, 0), CGSizeMake([[UIScreen mainScreen] currentMode].size.width, 0)) : NO)
#define IPad ([[UIDevice currentDevice] userInterfaceIdiom] == UIUserInterfaceIdiomPad ? YES:NO)

//UIColor
#define UIColorFromRGB(hexValue) [UIColor colorWithRed:((float)((hexValue & 0xFF0000) >> 16))/255.0 green:((float)((hexValue & 0xFF00) >> 8))/255.0 blue:((float)(hexValue & 0xFF))/255.0 alpha:1.0]
#define UIColorFromRGBAlpha(hexValue,alp) [UIColor colorWithRed:((float)((hexValue & 0xFF0000) >> 16))/255.0 green:((float)((hexValue & 0xFF00) >> 8))/255.0 blue:((float)(hexValue & 0xFF))/255.0 alpha:alp]
#define RGBCOLOR(r,g,b) [UIColor colorWithRed:(r)/255.0f green:(g)/255.0f blue:(b)/255.0f alpha:1]
#define RGBACOLOR(r,g,b,a) [UIColor colorWithRed:(r)/255.0f green:(g)/255.0f blue:(b)/255.0f alpha:(a)]

//NSTimer
#define TIMER_INVA(timer){if ([timer isKindOfClass:[NSTimer class]] && timer) {[timer invalidate];timer = nil;}}

static inline double radians(double degrees) {
    return degrees * M_PI / 180;
}