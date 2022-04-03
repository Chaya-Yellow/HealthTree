//
//  BLTStoreSpo2TrendManager.h
//  BLT-Spo2Monitor
//
//  Created by luteng on 16/8/4.
//  Copyright © 2016年 luteng. All rights reserved.
//

#import <Foundation/Foundation.h>

@protocol BLTStoreSpo2TrendDelegate;

@interface BLTStoreSpo2TrendManager : NSObject
@property(nonatomic, weak) id<BLTStoreSpo2TrendDelegate> delegate;
+(id)sharedInstance;
-(void)startStoreTrend;
-(void)stopStoreTrend;
@end

@protocol BLTStoreSpo2TrendDelegate<NSObject>
@optional
-(void)managerStoreTrendData;
@end