//
//  Constants.h
//  BLT-Spo2Monitor
//
//  Created by luteng on 16/7/5.
//  Copyright © 2016年 luteng. All rights reserved.
//

#ifndef Temperature_Contants_h
#define Temperature_Contants_h

#define  DEFAULT_ACCOUNT_ID 99999999
#define  DEFAULT_FAMILY_ID  88888888
#define  ACCOUNT_KEY        @"77777777"


#define kIdKey @"Id"
#define kClassNameKey @"ClassName"
#define kTitleKey @"Title"
#define kContentKey @"Content"

#define UserSettings @"UserSettings"
#define Setting_AlarmTemp @"AlarmTemp"
#define Setting_Sound @"Sound"
#define Setting_Celcius @"Celcius"
#define DEFAULT_Temp_MIN 34.0
#define DEFAULT_Temp_MAX 42.0
#define kValueKey @"Value"
#define kDateTimeKey @"DateTime"
#define kUnitINIndex 0.39370078740157
#define NotifiSettings @"NotifiSettings"
#define Setting_Phone @"Phone"
#define Setting_Email @"Email"
#define BTDisconnectedNotifiSetting @"BTDisconnectedNotifiSetting"

#define OMEAPPID 201
#define ENGLISH  101
#define CHINESE  102
#define HEIGHT_MIN 30
#define HEIGHT_MAX  230
#define WEIGHT_MIN  1
#define WEIGHT_MAX  180
#define HEIGHT_TO_IN 2.54
#define WEIGHT_TO_LB 0.4535924


#define SCREEN_LESS_IPHONE5 ([[UIScreen mainScreen]bounds].size.height < 568.0 ? YES : NO)
typedef enum {
    DeviceElectricityStatusEmpty = 0,
    DeviceElectricityStatusLow,
    DeviceElectricityStatusHalf,
    DeviceElectricityStatusFull
} DeviceElectricityStatus;

typedef enum {
    DeviceConnectionStatusSearching = 0,
    DeviceConnectionStatusDisconnected,
    DeviceConnectionStatusConnected,
    DeviceConnectionStatusPowerOff,
    DeviceConnectionStatusNoSupport
} DeviceConnectionStatus;

static NSString * const familyDidUpdatedNotification = @"FamilyDidUpdatedNotification";
static NSString * const memberDidUpdateNotification = @"MemberDidUpdateNotification";
//static NSString * const tempUnitDidChangedNotification = @"TempUnitDidChangedNotification";
static NSString * const newTempDidUpdatedNotification = @"NewTempDidUpdatedNotification";
static NSString * const deviceBoundDidUpdatedNotification = @"DeviceBoundDidUpdatedNotification";
static NSString * const alarmTempValueDidUpdatedNotification = @"AlarmTempValueDidUpdatedNotification";
static NSString * const btDisConnectedAlarmDidUpdatedNotification = @"BTDisConnectedAlarmDidUpdatedNotification";
static NSString * const tempAlarmDidUpdatedNotification = @"TempAlarmDidUpdatedNotification";
#define USER_PHOTO_IP_ENGLISH @"http://47.88.25.86:8888/Home/GetImg/?memberId="

static inline NSString* random3BitNum()
{
    return [NSString stringWithFormat:@"%d%d%d",arc4random() % 10,arc4random() % 10,arc4random() % 10,nil];
}

static inline NSString* imageDirectory()
{
    NSArray *searchPaths = NSSearchPathForDirectoriesInDomains(NSDocumentDirectory, NSUserDomainMask, YES);
    NSString *docuPath = [searchPaths objectAtIndex: 0];
    NSString *result = [[docuPath stringByAppendingPathComponent:[[NSProcessInfo processInfo] processName]] stringByAppendingPathComponent:@"image"];
    NSFileManager *fileManager = [NSFileManager defaultManager];
    NSString *filePath = [NSString stringWithString:result];
    [fileManager createDirectoryAtPath:filePath withIntermediateDirectories:YES attributes:nil error:nil];
    return result;
}

static inline NSString* tmpDirectory()
{
    NSArray *searchPaths = NSSearchPathForDirectoriesInDomains(NSDocumentDirectory, NSUserDomainMask, YES);
    NSString *docuPath = [searchPaths objectAtIndex: 0];
    NSString *result = [[docuPath stringByAppendingPathComponent:[[NSProcessInfo processInfo] processName]] stringByAppendingPathComponent:@"tmp"];
    NSFileManager *fileManager = [NSFileManager defaultManager];
    NSString *filePath = [NSString stringWithString:result];
    [fileManager createDirectoryAtPath:filePath withIntermediateDirectories:YES attributes:nil error:nil];
    return result;
}


static inline NSString* imageServerPath()
{
    return [NSString stringWithFormat:USER_PHOTO_IP_ENGLISH];
}

#endif

