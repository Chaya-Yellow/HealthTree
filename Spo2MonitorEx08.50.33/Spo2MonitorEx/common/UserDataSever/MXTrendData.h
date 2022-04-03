//
//  MXTemp.h
//  BLT-Spo2Monitor
//
//  Created by luteng on 16/7/7.
//  Copyright © 2016年 luteng. All rights reserved.
//

#import <Foundation/Foundation.h>

typedef enum {
    MXTrendPartArmpit = 1,
    MXTrendPartOral,
    MXTrendPartRectu
} MXTrendPart;

@interface MXTrendData : NSObject
@property (nonatomic, assign) NSInteger dataId;
@property (nonatomic, assign) float temp;
@property (nonatomic, assign) NSInteger spo2Value;
@property (nonatomic, assign) NSInteger prValue;
@property (nonatomic, assign) NSInteger respValue;
@property (nonatomic, assign) NSInteger piValue;
@property (nonatomic, assign) NSInteger familyId;
@property (nonatomic, assign) MXTrendPart part;
@property (nonatomic, strong) NSDate *recordDate;
@end
