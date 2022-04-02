//
//  MXEvent.h
//  BLT-Spo2Monitor
//
//  Created by luteng on 16/7/7.
//  Copyright © 2016年 luteng. All rights reserved.
//

#import <Foundation/Foundation.h>

typedef enum {
    MXEventStateMedication = 1,
    MXEventStateColdCompress,
    MXEventStateSpongeBath,
    MXEventStateVisit
} MXEventState;

@interface MXEvent : NSObject
@property (nonatomic, assign) NSInteger dataId;
@property (nonatomic, assign) NSInteger eventId;
@property (nonatomic, assign) NSInteger familyId;
@property (nonatomic, assign) MXEventState state;
@property (nonatomic, strong) NSDate *recordDate;
@end
