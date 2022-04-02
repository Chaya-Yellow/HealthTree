//
//  BLTStoreSpo2TrendManager.m
//  BLT-Spo2Monitor
//
//  Created by luteng on 16/8/4.
//  Copyright © 2016年 luteng. All rights reserved.
//

#import "BLTStoreSpo2TrendManager.h"

@interface BLTStoreSpo2TrendManager()
@property (retain, nonatomic) NSTimer *storeTrendTimer;
@end
@implementation BLTStoreSpo2TrendManager
{
    
}

static  BLTStoreSpo2TrendManager* manager = nil;

+(id)sharedInstance
{
    if(!manager)
    {
        manager = [[BLTStoreSpo2TrendManager alloc]init];
    }
    return manager;
}

-(void)startStoreTrend
{
    [self setIsStore:YES];
    if([_delegate respondsToSelector:@selector(managerStoreTrendData)])
    {
        [_delegate managerStoreTrendData];
    }
    [self setIsStore:FALSE];
}

-(void)stopStoreTrend
{
    if (_storeTrendTimer) {
        [_storeTrendTimer invalidate];
        _storeTrendTimer = nil;
    }
}

-(void)setIsStore:(BOOL)bStore
{
    if (_storeTrendTimer) {
        [_storeTrendTimer invalidate];
        _storeTrendTimer = nil;
    }
    if (!bStore) {
        //定时10 MIN 同步一次数据 10*2 调记录
        dispatch_async(dispatch_get_global_queue(DISPATCH_QUEUE_PRIORITY_DEFAULT, 0), ^{
            _storeTrendTimer = [NSTimer scheduledTimerWithTimeInterval:30 target:self selector:@selector(startStoreTrend) userInfo:nil repeats:NO];
            [[NSRunLoop currentRunLoop] addTimer:_storeTrendTimer forMode:NSDefaultRunLoopMode];
            [[NSRunLoop currentRunLoop] run];
        });
    }
}
@end
