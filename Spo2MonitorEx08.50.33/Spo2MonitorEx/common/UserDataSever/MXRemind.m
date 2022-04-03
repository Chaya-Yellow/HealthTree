//
//  MXRemind.m
//  BLT-Spo2Monitor
//
//  Created by luteng on 16/7/7.
//  Copyright © 2016年 luteng. All rights reserved.
//

#import "MXRemind.h"

@interface MXRemind()
@end

@implementation MXRemind
@synthesize date;
@synthesize dateArray;
@synthesize style;
@synthesize status;
@synthesize weeks;
@synthesize idnum;

- (void)dealloc
{
    date = nil;
    dateArray = nil;
    weeks = nil;
}
@end
