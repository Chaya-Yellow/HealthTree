//
//  MXTemp.m
//  BLT-Spo2Monitor
//
//  Created by luteng on 16/7/7.
//  Copyright © 2016年 luteng. All rights reserved.
//

#import "MXTrendData.h"

@implementation MXTrendData
@synthesize recordDate = _recordDate;

- (id)init
{
    self = [super init];
    if (self) {
        self.part = MXTrendPartArmpit;
    }
    return self;
}
@end
