//
//  MXUserData.h
//  MarkMan
//
//  Created by OMESOFT-112 on 14-2-12.
//  Copyright (c) 2014年 omesoft. All rights reserved.
//
#import "MXRemind.h"
//#import "MXTemperatureChartView.h"
#import "OMEDataBase.h"

typedef enum {
    MXTemperatureChartStyle12Hour = 0,
    MXTemperatureChartStyleDay,
    MXTemperatureChartStyle2Day,
    MXTemperatureChartStyle3Day
}MXTemperatureChartStyle;

@class MXTrendData;
@class MXEvent;
@class MXUser;
@class MXDeviceInfo;
@interface MXUserData : OMEDataBase

- (NSMutableArray *)getAllRemindTakeMedicine;
- (NSArray *)getIdWithStyle:(NSInteger)idNum;
- (MXRemind *)getRemindWithId:(NSInteger)idNum;
- (BOOL)insert:(MXRemind *)remind;
- (BOOL)changeremind:(MXRemind *)remind idNum:(NSInteger)_id;
- (BOOL)deleteRemindDataWithId:(NSInteger)dataId;
//Temp
- (BOOL)insertDataToRecordTempData:(MXTrendData *)temp familyId:(NSInteger)familyId;
- (NSMutableArray *)get5MinRecordTempDataWihtDate:(NSDate *)date familyId:(NSInteger)familyId;
- (BOOL)deleteRecordTempDataWithId:(NSInteger)dataId;
- (NSArray *)getMarksDateFromDate:(NSDate *)startDate toDate:(NSDate *)endDate family:(NSInteger)familyId;
- (NSMutableArray *)getRecordTempDataWithStyle:(MXTemperatureChartStyle)style familyId:(NSInteger)familyId targetDate:(NSDate *)date;
//- (BOOL)cleanTempCacheDataWithTimeStamp:(NSDate *)date;
//Event
- (BOOL)insertDataToEventData:(MXEvent *)event familyId:(NSInteger)familyId;
- (BOOL)deleteEventDataWithId:(NSInteger)dataId;
- (NSMutableArray *)getAllEventDataToDate:(NSDate *)date familyId:(NSInteger)familyId;
//- (BOOL)cleanTempEventCacheDataWithTimeStamp:(NSDate *)date;

//user
- (MXUser *)getUserWithFamilyId:(NSInteger)familyId;
- (NSMutableArray *)getAllUserWithMemberId:(NSUInteger)memberId;
- (BOOL)addNewUserWithData:(MXUser *)user familyId:(NSUInteger)familyId memberId:(NSUInteger)memberId;
- (BOOL)changeUserWithData:(MXUser *)user;
- (BOOL)deleteDataToUserWithId:(NSInteger)familyId memberId:(NSUInteger)memberId;

/*
//Device
- (BOOL)addDeviceBoundWithInfo:(MXDeviceInfo *)info memberId:(NSInteger)mId boundId:(NSInteger)bId;
- (NSArray *)getAllDeviceBoundInfoWithMemberId:(NSInteger)mId;
- (BOOL)insertUpdateDeviceWithData:(NSArray *)devices memberId:(NSUInteger)mId;
- (NSArray *)getAllDeviceSNWithMemberId:(NSInteger)mId;
- (MXDeviceInfo *)getDeviceInfoWithSerialNumber:(NSString *)serial memberId:(NSInteger)mId;
- (BOOL)isExistDeviceInfoWithSerialNumber:(NSString *)serial memberId:(NSInteger)mId;
- (BOOL)deleteDataToDeviceWithBoundId:(NSInteger)bId memberId:(NSUInteger)memberId;
- (BOOL)updateDeviceBoundWithInfo:(MXDeviceInfo *)info memberId:(NSInteger)mId;
*/

@end
