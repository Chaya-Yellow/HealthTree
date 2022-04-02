//
//  MXRemind.h
//  BLT-Spo2Monitor
//
//  Created by luteng on 16/7/7.
//  Copyright © 2016年 luteng. All rights reserved.
//

#import <Foundation/Foundation.h>

typedef enum {
    RemindTakeMedicine = 0,
    RemindMeasureTemperature = 1
} RemindStyle;

typedef enum {
    RemindStatusClose = 0,
    RemindStatusOpen = 1
} RemindStatus;

@interface MXRemind : NSObject{
    NSString *date;
    NSMutableArray *dateArray;
    RemindStyle style;
    RemindStatus status;
    NSString *weeks;
    NSInteger idnum;
}

@property (nonatomic, retain) NSString *date;
@property (nonatomic, retain) NSMutableArray *dateArray;
@property (nonatomic, assign) RemindStyle style;
@property (nonatomic, assign) RemindStatus status;
@property (nonatomic, retain) NSString *weeks;
@property (nonatomic, assign) NSInteger idnum;

@end
