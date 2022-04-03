//
//  MXEvent.m
//  BLT-Spo2Monitor
//
//  Created by luteng on 16/7/7.
//  Copyright © 2016年 luteng. All rights reserved.
//

#import "MXEvent.h"

@implementation MXEvent
@synthesize recordDate = _recordDate;

- (id)init
{
    self = [super init];
    if (self) {
        self.state = MXEventStateMedication;
    }
    return self;
}
@end
