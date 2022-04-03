//
//  MXUserData.m
//  MarkMan
//
//  Created by OMESOFT-112 on 14-2-12.
//  Copyright (c) 2014年 omesoft. All rights reserved.
//

#import "MXUserData.h"
#import "MXRemind.h"
#import "OMESoft.h"
#import "FMDatabase.h"
#import "FMDatabaseQueue.h"
#import "MXTrendData.h"
#import "Constants.h"
#import "MXEvent.h"
#import "MXUser.h"
#import "EGOImageLoader.h"
#import "FMDatabaseAdditions.h"
#import "MXDeviceInfo.h"


#define dbRemindTable @"MX_Temp_Remind"
#define dbDeviceBoundTable @"MX_Temp_DeviceBound"


@implementation MXUserData

- (id)init {
    self = [super init];
    if (self)
	{
        NSArray *paths = NSSearchPathForDirectoriesInDomains(NSDocumentDirectory, NSUserDomainMask, YES);
        NSString *dataPath = [[paths objectAtIndex:0] stringByAppendingPathComponent:dbUserFileName];
        FMDatabase *dataBase = [[FMDatabase alloc] initWithPath:dataPath];
        if (![dataBase open]) {
            NSLog(@"Could not open database.");
        }
        else {
            NSString *remindSql = [NSString stringWithFormat:@"CREATE TABLE IF NOT EXISTS %@ (ID INTEGER PRIMARY KEY, Style INTEGER, Weeks TEXT, RecordDate DateTime,Status INTEGER)",dbRemindTable, nil];

            //设备绑定
            NSString *deviceBoundSql = [NSString stringWithFormat:@"CREATE TABLE IF NOT EXISTS %@ (ID INTEGER PRIMARY KEY NOT NULL,BoundID INTEGER NOT NULL,MemberID INTEGER NOT NULL,DeviceID INTEGER NOT NULL,SN Text,Remark Text,ProtocolVer Text,FirmwareVer Text,CreatedDate DateTime NOT NULL,UpdatedDate DateTime NOT NULL)",dbDeviceBoundTable];
            
            [dataBase beginTransaction];
            [dataBase executeUpdate:remindSql];
            [dataBase executeUpdate:deviceBoundSql];
            [dataBase commit];
        }
    }
    return self;
}


#pragma mark - Remind
- (NSMutableArray *)getAllRemindTakeMedicine
{
    NSString *sql = [NSString stringWithFormat:@"SELECT * FROM %@ Order by RecordDate",dbRemindTable,nil];
    NSMutableArray *result = [[NSMutableArray alloc] init];
    [self.queue inDatabase:^(FMDatabase *db) {
        if (![db open]) {
            NSLog(@"Could not open database.");
        }else{
            FMResultSet *rs = [db executeQuery:sql];
            while ([rs next]) {
                MXRemind *remind = [[MXRemind alloc] init];
                remind.date = [rs stringForColumn:@"RecordDate"];
                remind.idnum = [rs intForColumn:@"ID"];
                remind.weeks = [rs stringForColumn:@"Weeks"];
                remind.status = [rs intForColumn:@"Status"];
                remind.style = [rs intForColumn:@"Style"];
                [result addObject:remind];
            }
        }
    }];
    
    return result;
}

- (MXRemind *)getRemindWithId:(NSInteger)idNum
{
    NSString *sql = [NSString stringWithFormat:@"SELECT * FROM %@ WHERE ID = '%ld'",dbRemindTable,(long)idNum,nil];
    MXRemind *remind = [[MXRemind alloc] init];
    [self.queue inDatabase:^(FMDatabase *db) {
        if (![db open]) {
            NSLog(@"Could not open database.");
        }else{
            FMResultSet *rs = [db executeQuery:sql];
            while ([rs next]) {
                remind.date = [rs stringForColumn:@"RecordDate"];
                remind.idnum = [rs intForColumn:@"ID"];
                remind.weeks = [rs stringForColumn:@"Weeks"];
                remind.status = [rs intForColumn:@"Status"];
                remind.style = [rs intForColumn:@"Style"];
            }
        }
    }];
    
    return remind;
}

- (NSArray *)getIdWithStyle:(NSInteger)idNum
{
    NSString *sql = [NSString stringWithFormat:@"SELECT ID FROM %@ WHERE Style = '%ld'",dbRemindTable,(long)idNum,nil];
    NSMutableArray *array = [[NSMutableArray alloc] init];
    
    [self.queue inDatabase:^(FMDatabase *db) {
        
        if (![db open]) {
            NSLog(@"Could not open database.");
        }
        else {
            FMResultSet *rs = [db executeQuery:sql];
            while ([rs next]) {
                NSMutableDictionary *dic = [[NSMutableDictionary alloc] init];
                [dic setValue:[rs stringForColumn:@"ID"] forKey:@"ID"];
                [array addObject:dic];
            }
        }
    }];
    
    return array;
}

- (BOOL)insert:(MXRemind *)remind
{
    NSString *sql = [NSString stringWithFormat:
                     @"INSERT INTO %@ (Style, RecordDate,Weeks, Status) VALUES('%d','%@','%@','%@')",dbRemindTable,remind.style,remind.date,remind.weeks,@"0",nil];
    return [self dbQueueExecuteUpdateSQL:sql];
}

- (BOOL)changeremind:(MXRemind *)remind idNum:(NSInteger)_id
{
    NSString *sqlStr  = [NSString stringWithFormat:@"UPDATE %@ SET Style = '%d', RecordDate= '%@',Weeks= '%@' WHERE ID= %ld ",dbRemindTable,remind.style,remind.date,remind.weeks,(long)_id,nil];
    return [self dbQueueExecuteUpdateSQL:sqlStr];
}

- (BOOL)deleteRemindDataWithId:(NSInteger)dataId
{
    NSString *sql = [NSString stringWithFormat:@"DELETE FROM %@ Where ID = '%ld'",dbRemindTable,(long)dataId];
    return [self dbQueueExecuteUpdateSQL:sql];
}

#pragma mark - user

- (MXUser *)getUserWithFamilyId:(NSInteger)familyId
{
    __block MXUser *result = nil;
    NSString *sql = [NSString stringWithFormat:@"select * from %@ where FamilyID = '%ld'",dbFamilyTable,(long)familyId];
    [self.queue inDatabase:^(FMDatabase *db) {
        if (![db open]) {
            NSLog(@"Could not open database.");
        }else{
            FMResultSet *rs = [db executeQuery:sql];
            while ([rs next]) {
                MXUser *user = [[MXUser alloc] init];
                user.name = [rs stringForColumn:@"Name"];
                user.height = [rs doubleForColumn:@"Height"];
                user.weight = [rs doubleForColumn:@"Weight"];
                user.gender = [rs boolForColumn:@"Gender"];
                user.birthday = [[rs stringForColumn:@"Birthday"] stringWihtDateFormat:@"yyyy-MM-dd"];
                user.imageName = [rs stringForColumn:@"ImageName"];
                user.idNum = [rs intForColumn:@"ID"];
                user.familyId = [rs intForColumn:@"FamilyID"];
                user.memberId = [rs intForColumn:@"MemberID"];
                result = user;
            }
            [rs close];
        }
    }];
    return result;
}

- (NSMutableArray *)getAllUserWithMemberId:(NSUInteger)memberId
{
    NSString *sql = [NSString stringWithFormat:@"SELECT * FROM %@ Where MemberID = '%lu' order by FamilyID",dbFamilyTable,(long)memberId, nil];
    //NSLog(@"sql = %@", sql);
    NSMutableArray *result = [[NSMutableArray alloc] init];
    [self.queue inDatabase:^(FMDatabase *db) {
        if (![db open]) {
            NSLog(@"Could not open database.");
        }else{
            FMResultSet *rs = [db executeQuery:sql];
            while ([rs next]) {
                //NSLog(@"-------***********");
                MXUser *user = [[MXUser alloc] init];
                user.name = [rs stringForColumn:@"Name"];
                user.height = [rs doubleForColumn:@"Height"];
                user.weight = [rs doubleForColumn:@"Weight"];
                user.gender = [rs boolForColumn:@"Gender"];
                user.birthday = [[rs stringForColumn:@"Birthday"] stringWihtDateFormat:@"yyyy-MM-dd"];
                user.imageName = [rs stringForColumn:@"ImageName"];
                user.idNum = [rs intForColumn:@"ID"];
                user.familyId = [rs intForColumn:@"FamilyID"];
                user.memberId = [rs intForColumn:@"MemberID"];
                //NSLog(@"user.name=%@ imageName=%@, idum=%d,familyID=%ld, memberID=%ld", user.name,user.imageName,user.idNum,user.familyId,user.memberId);
                [result addObject:user];
            }
            [rs close];
        }
    }];
    return result;
}

- (BOOL)addNewUserWithData:(MXUser *)user familyId:(NSUInteger)familyId memberId:(NSUInteger)memberId
{
    NSString *imageNameStr = (user.imageName == nil || user.imageName.length == 0) ? @"NULL" : [NSString stringWithFormat:@"'%@'",user.imageName];
    BOOL isNewImage = (user.imageName == nil || user.imageName.length == 0) ? NO : YES;
    NSString *sql = [NSString stringWithFormat:@"INSERT INTO %@ (Name,Height,Weight,Birthday,Gender,ImageName,FamilyID,MemberID,CreatedDate,UpdatedDate)VALUES(?,'%0.3f','%0.3f','%@','%d',%@,'%ld','%ld','%@','%@')",dbFamilyTable,user.height,user.weight,[user.birthday dateToStringWithDateFormate:@"yyyy-MM-dd" timeZone:[NSTimeZone systemTimeZone] locale:[NSLocale systemLocale]],user.gender,imageNameStr,(long)familyId,(long)memberId,[[NSDate date] dateToStringWithDateFormate:@"yyyy-MM-dd HH:mm:ss" timeZone:[NSTimeZone systemTimeZone] locale:[NSLocale systemLocale]],[[NSDate date] dateToStringWithDateFormate:@"yyyy-MM-dd HH:mm:ss" timeZone:[NSTimeZone systemTimeZone] locale:[NSLocale systemLocale]],nil];
    __block BOOL result = NO;
    [self.queue inDatabase:^(FMDatabase *db) {
        if (![db open]) {
            NSLog(@"Could not open database.");
        }else{
            [db beginTransaction];
            result = [db executeUpdate:sql,user.name];
            [db commit];
            if (result) {
                if (isNewImage) {
                    NSString *tmpPath = tmpDirectory();
                    NSString *fromImageFilePath = [tmpPath stringByAppendingPathComponent:user.imageName];
                    NSString *toImageFilePath = [imageDirectory() stringByAppendingPathComponent:user.imageName];
                    [[NSFileManager defaultManager] moveItemAtPath:fromImageFilePath toPath:toImageFilePath error:nil];
                    
                }
                
                NSFileManager *fileManager = [NSFileManager defaultManager];
                NSString *documentDir = tmpDirectory();
                NSError *error = nil;
                NSArray *fileList = [fileManager contentsOfDirectoryAtPath:documentDir error:&error];
                for (int i = 0; i < [fileList count]; i++) {
                    NSString *path = [documentDir stringByAppendingPathComponent:[fileList objectAtIndex:i]];
                    [fileManager removeItemAtPath:path error:nil];
                }
            }
            
        }
    }];
    return result;
}

- (BOOL)changeUserWithData:(MXUser *)user
{
    NSString *sqlStr = nil;
    NSString *imageNameStr = (user.imageName == nil || user.imageName.length == 0) ? @"NULL" : [NSString stringWithFormat:@"'%@'",user.imageName];
    //NSLog(@"imageNameStr = %@, idum = %d", imageNameStr, user.idNum);
    BOOL isNewImage = (user.imageName == nil || user.imageName.length == 0) ? NO : YES;
    NSLog(@"isNewImage = %d", isNewImage);
    sqlStr = [NSString stringWithFormat:@"UPDATE %@ SET Name=?,Height='%0.3f',Birthday='%@',Gender='%d', ImageName = %@ , Weight = '%0.3f' WHERE ID=%ld",dbFamilyTable,user.height,[user.birthday dateToStringWithDateFormate:@"yyyy-MM-dd" timeZone:[NSTimeZone systemTimeZone] locale:[NSLocale systemLocale]],user.gender,imageNameStr,user.weight,(long)user.idNum];
    __block BOOL result = NO;
    
    [self.queue inDatabase:^(FMDatabase *db) {
        
        if (![db open]) {
            NSLog(@"Could not open database.");
        }else{
            NSString *originalImageName = nil;
            //FMResultSet *rs = [db executeQuery:@"select ImageName from %@ where ID = '%d'",dbFamilyTable,user.idNum];
            NSString* sql = [NSString stringWithFormat:@"select ImageName from %@ where ID = '%d'",dbFamilyTable,(int)user.idNum];
            FMResultSet *rs = [db executeQuery:sql];
            while ([rs next]) {
                //NSLog(@"rs.result = %@", rs.resultDictionary);
                originalImageName = [rs stringForColumnIndex:0];
            }
            
            result = [db executeUpdate:sqlStr,user.name];
           // NSLog(@"----result = %d", result);
            if (result) {
                //NSLog(@"originalImageName = %@", originalImageName);
                //删除原始图片
                if (originalImageName) {
                    NSString *imagePath = imageDirectory();
                    NSString *imageFilePath = [imagePath stringByAppendingPathComponent:originalImageName];
                    [[NSFileManager defaultManager] removeItemAtPath:imageFilePath error:nil];
                }
                //调度新图片
                if (isNewImage) {
                    NSString *tmpPath = tmpDirectory();
                    NSString *fromImageFilePath = [tmpPath stringByAppendingPathComponent:user.imageName];
                    NSString *toImageFilePath = [imageDirectory() stringByAppendingPathComponent:user.imageName];
                    [[NSFileManager defaultManager] moveItemAtPath:fromImageFilePath toPath:toImageFilePath error:nil];
                    
                }
                
                //清除tmp
                NSFileManager *fileManager = [NSFileManager defaultManager];
                NSString *documentDir = tmpDirectory();
                NSError *error = nil;
                NSArray *fileList = [fileManager contentsOfDirectoryAtPath:documentDir error:&error];
                for (int i = 0; i < [fileList count]; i++) {
                    NSString *path = [documentDir stringByAppendingPathComponent:[fileList objectAtIndex:i]];
                    [fileManager removeItemAtPath:path error:nil];
                }
            }
        }
    }];
    
    
    return result;
}

- (BOOL)deleteDataToUserWithId:(NSInteger)familyId memberId:(NSUInteger)memberId
{
    NSString *sql = [NSString stringWithFormat:@"Delete From %@ where FamilyID = '%ld'",dbFamilyTable,(long)familyId];
    __block BOOL result = NO;
    
    [self.queue inTransaction:^(FMDatabase *db, BOOL *rollback) {
        
        if (![db open]) {
            NSLog(@"Could not open database.");
        }else{
            [db open];
            NSString *originalImageName = nil;
            FMResultSet *rs = [db executeQuery:@"select ImageName from %@ where FamilyID = '%d'",dbFamilyTable,familyId];
            while ([rs next]) {
                originalImageName = [rs stringForColumnIndex:0];
            }
            
            BOOL isDeleteUesr = [db executeUpdate:sql];
            
            
//            NSString *deleteDataSql = [NSString stringWithFormat:@"Delete From %@ where FamilyID = '%d'",dbRecordBPTable,familyId];
//            BOOL isDeleteData = [db executeUpdate:deleteDataSql];
            BOOL isDeleteData = YES;
            if (!isDeleteData || !isDeleteUesr) {
                *rollback = YES;
                return;
            } else {
                result = YES;
            }
            if (result) {
                if (originalImageName) {
                    NSString *imageFilePath = [imageDirectory() stringByAppendingPathComponent:originalImageName];
                    BOOL isExist = [[NSFileManager defaultManager] isExecutableFileAtPath:imageFilePath];
                    if (isExist) {
                        [[NSFileManager defaultManager] removeItemAtPath:imageFilePath error:nil];
                    } else {
                        NSString *imageCachePath = [[imageServerPath() stringByAppendingPathComponent:[NSString stringWithFormat:@"%ld",(long)memberId]] stringByAppendingPathComponent:originalImageName];
                        [[EGOImageLoader sharedImageLoader] clearCacheForURL:[NSURL URLWithString:imageCachePath]];
                    }
                    
                }
            }
        }
    }];
    
    
    return result;
}
#pragma mark - Temp

- (NSMutableArray *)getRecordTempDataWithStyle:(MXTemperatureChartStyle)style familyId:(NSInteger)familyId targetDate:(NSDate *)date
{
    NSString *sql;
    NSString *startDayStr;
    NSString *endDayStr;
    switch (style) {
        case MXTemperatureChartStyle12Hour:
            startDayStr = [[date startOfHour] dateToStringWithDateFormate:@"yyyy-MM-dd HH:mm:ss.SSS" timeZone:[NSTimeZone systemTimeZone] locale:[NSLocale systemLocale]];
            endDayStr = [[[date startOfHour] addHours:12] dateToStringWithDateFormate:@"yyyy-MM-dd HH:mm:ss.SSS" timeZone:[NSTimeZone systemTimeZone] locale:[NSLocale systemLocale]];
            break;
        case MXTemperatureChartStyleDay:
            startDayStr = [[date startOfDay] dateToStringWithDateFormate:@"yyyy-MM-dd HH:mm:ss.SSS"];
            endDayStr = [[[date startOfDay] tomorrow] dateToStringWithDateFormate:@"yyyy-MM-dd HH:mm:ss.SSS"];
            break;
        case MXTemperatureChartStyle2Day:
            startDayStr = [[[date startOfDay] previousDays:1] dateToStringWithDateFormate:@"yyyy-MM-dd HH:mm:ss.SSS" ];
            endDayStr = [[[date startOfDay] tomorrow] dateToStringWithDateFormate:@"yyyy-MM-dd HH:mm:ss.SSS"];
            break;
        case MXTemperatureChartStyle3Day:
            startDayStr = [[[date startOfDay] previousDays:2] dateToStringWithDateFormate:@"yyyy-MM-dd HH:mm:ss.SSS"];
            endDayStr = [[[date startOfDay] tomorrow] dateToStringWithDateFormate:@"yyyy-MM-dd HH:mm:ss.SSS"];
            break;
    }
    if (startDayStr.length == 0 || endDayStr.length == 0) {
        return nil;
    }
    
    
    sql = [NSString stringWithFormat:@"select SPO2,PR,PI,RESP,RecordDate,strftime('%%s',RecordDate) - strftime('%%s','%@') as Interval  from %@ where strftime('%%Y-%%m-%%d %%H:%%M',RecordDate)  >= strftime('%%Y-%%m-%%d %%H:%%M','%@') and strftime('%%Y-%%m-%%d %%H:%%M',RecordDate) < strftime('%%Y-%%m-%%d %%H:%%M','%@') and FamilyID = '%ld' and IsDeleted = '0'  order by RecordDate ASC",startDayStr,dbTempTable,startDayStr,endDayStr,(long)familyId, nil];
    __block NSMutableArray *result = [[NSMutableArray alloc] init];
    
    [self.queue inDatabase:^(FMDatabase *db) {
        
        if (![db open]) {
            NSLog(@"Could not open database.");
        }else{
            FMResultSet *rs = [db executeQuery:sql];
            NSDateFormatter *formatter = [[NSDateFormatter alloc] init];
            [formatter setTimeZone:[NSTimeZone timeZoneWithAbbreviation:@"UTC"]];
            [formatter setLocale:[NSLocale systemLocale]];
            [formatter setDateFormat:@"yyyy-MM-dd HH:mm:ss.SSS"];
            
            //数据对应时间段
            while ([rs next]) {
                //NSLog(@"resResult = %@", rs.resultDictionary);
                NSDate *date = [formatter dateFromString:[rs stringForColumn:@"RecordDate"]];
                //NSLog(@"Date = %@", date);
                if(!date)
                {
                    NSDateFormatter *formatter = [[NSDateFormatter alloc] init];
                    [formatter setTimeZone:[NSTimeZone timeZoneWithAbbreviation:@"UTC"]];
                    [formatter setLocale:[NSLocale systemLocale]];
                    [formatter setDateFormat:@"yyyy-MM-dd HH:mm:ss"];
                    date = [formatter dateFromString:[rs stringForColumn:@"RecordDate"]];
                }
                [result addObject:[NSDictionary dictionaryWithObjectsAndKeys:
                                   [NSNumber numberWithInt:[rs intForColumn:@"SPO2"]],@"SPO2",
                                   [NSNumber numberWithInt:[rs intForColumn:@"PR"]],@"PR",
                                   [NSNumber numberWithInt:[rs intForColumn:@"PI"]],@"PI",
                                   [NSNumber numberWithInt:[rs intForColumn:@"RESP"]],@"RESP",
                                   [date dateToStringWithDateFormate:@"yyyy-MM-dd HH:mm:ss"], @"Date",
                                   nil]];
                
            }
        }
        
    }];
    return result;
}


- (NSMutableArray *)get5MinRecordTempDataWihtDate:(NSDate *)date familyId:(NSInteger)familyId
{
    NSString *sql;
    NSString *endDayStr = [[date UTCDate] dateToStringWithDateFormate:@"yyyy-MM-dd HH:mm:ss.SSS"];
    NSString *startDayStr = [[[date UTCDate] reduceMinWithMin:5] dateToStringWithDateFormate:@"yyyy-MM-dd HH:mm:ss.SSS"];
    if (startDayStr.length == 0 || endDayStr.length == 0) {
        return nil;
    }
    
    sql = [NSString stringWithFormat:@"select TEMP,SPO2,PR,PI,RESP,RecordDate,strftime('%%Y-%%m-%%d %%H:%%M:%%S',RecordDate) as cd from %@ where strftime('%%Y-%%m-%%d %%H:%%M:%%S',RecordDate)  >= strftime('%%Y-%%m-%%d %%H:%%M:%%S','%@') and strftime('%%Y-%%m-%%d %%H:%%M:%%S',RecordDate) <= strftime('%%Y-%%m-%%d %%H:%%M:%%S','%@') and FamilyID = '%ld' and IsDeleted = '0' Group by cd order by RecordDate DESC",dbTempTable,startDayStr,endDayStr,(long)familyId, nil];
    NSMutableArray *result = [[NSMutableArray alloc] init];
    
    [self.queue inDatabase:^(FMDatabase *db) {
        
        if (![db open]) {
            NSLog(@"Could not open database.");
        }else{
            FMResultSet *rs = [db executeQuery:sql];
            //数据对应时间段
            while ([rs next]) {
                NSDate *date = [[rs stringForColumn:@"RecordDate"] stringWihtDateFormat:@"yyyy-MM-dd HH:mm:ss.SSS"];
                [result addObject:[NSDictionary dictionaryWithObjectsAndKeys:
                                   [NSNumber numberWithFloat:[rs doubleForColumn:@"TEMP"]],@"value",
                                   date,@"time",nil]];
                
            }
        }
        
    }];
    
    
    
    return result;
}

- (BOOL)insertDataToRecordTempData:(MXTrendData *)temp familyId:(NSInteger)familyId
{
    NSString *tempId = [NSString stringWithFormat:@"%@%@",[[NSDate date] dateToStringWithDateFormate:@"yyMMddHHmmssSSS" timeZone:[NSTimeZone systemTimeZone] locale:[NSLocale systemLocale]],random3BitNum()];
    NSString *sql = [NSString stringWithFormat:
                     @"INSERT INTO %@ (Part,TEMP,SPO2,PR,PI,RESP,RecordDate,CreatedDate,UpdatedDate,FamilyID,TEMPID) VALUES('%ld','%0.4f','%ld','%ld','%ld','%ld','%@','%@','%@','%ld','%@')",dbTempTable,(NSInteger)temp.part,temp.temp,
                     temp.spo2Value,
                     temp.prValue,
                     temp.piValue,
                     temp.respValue,
                     [temp.recordDate dateToStringWithDateFormate:@"yyyy-MM-dd HH:mm:ss.SSS" timeZone:[NSTimeZone systemTimeZone] locale:[NSLocale systemLocale]],
                     [[NSDate date] dateToStringWithDateFormate:@"yyyy-MM-dd HH:mm:ss.SSS" timeZone:[NSTimeZone systemTimeZone] locale:[NSLocale systemLocale]],
                     [[NSDate date] dateToStringWithDateFormate:@"yyyy-MM-dd HH:mm:ss.SSS" timeZone:[NSTimeZone systemTimeZone] locale:[NSLocale systemLocale]],
                     (long)familyId,
                     tempId,
                     nil];
    return [self dbQueueExecuteUpdateSQL:sql];
}

- (BOOL)deleteRecordTempDataWithId:(NSInteger)dataId
{
    NSString *sql = [NSString stringWithFormat:@"UPDATE %@ SET IsDeleted = '1',timestamp = '%@' Where ID = '%ld' ",dbTempTable,[[NSDate date] dateToStringWithDateFormate:@"yyyy-MM-dd HH:mm:ss" timeZone:[NSTimeZone systemTimeZone] locale:[NSLocale systemLocale]],(long)dataId];
    return [self dbQueueExecuteUpdateSQL:sql];
}

- (NSArray *)getMarksDateFromDate:(NSDate *)startDate toDate:(NSDate *)endDate family:(NSInteger)familyId
{
    NSString *sql = [NSString stringWithFormat:@"Select strftime('%%Y-%%m-%%d',RecordDate) as date from %@ where  strftime('%%Y-%%m-%%d',RecordDate)  >= strftime('%%Y-%%m-%%d','%@') and strftime('%%Y-%%m-%%d',RecordDate) <= strftime('%%Y-%%m-%%d','%@') and FamilyID = '%ld' and IsDeleted = '0' group by date",dbTempTable,
                     [startDate dateToStringWithDateFormate:@"yyyy-MM-dd HH:mm:ss.SSS" timeZone:[NSTimeZone systemTimeZone] locale:[NSLocale systemLocale]],
                     [endDate dateToStringWithDateFormate:@"yyyy-MM-dd HH:mm:ss.SSS" timeZone:[NSTimeZone systemTimeZone] locale:[NSLocale systemLocale]],(long)familyId];
    NSMutableArray *result = [[NSMutableArray alloc] init];
    
    [self.queue inDatabase:^(FMDatabase *db) {
        
        if (![db open]) {
            NSLog(@"Could not open database.");
        }else{
            FMResultSet *rs = [db executeQuery:sql];
            while ([rs next]) {
                NSString *dateStr = [rs stringForColumn:@"date"];
                NSDate *date = [dateStr stringWihtDateFormat:@"yyyy-MM-dd"];
                [result addObject:date];
                
            }
            
        }
    }];
    
   // NSLog(@"result = %@", result);
    return result;
}


- (BOOL)cleanTempCacheDataWithTimeStamp:(NSDate *)date
{
    NSString *sql = [NSString stringWithFormat:@"DELETE FROM %@ Where IsDeleted  = '1' and datetime(timestamp) <= datetime('%@')",dbTempTable,[date dateToStringWithDateFormate:@"yyyy-MM-dd HH:mm:ss" timeZone:[NSTimeZone systemTimeZone] locale:[NSLocale systemLocale]]];
    return [self dbQueueExecuteUpdateSQL:sql];
}


#pragma mark - Event

- (BOOL)insertDataToEventData:(MXEvent *)event familyId:(NSInteger)familyId
{
    NSString *eventId = [NSString stringWithFormat:@"%@%@",[[NSDate date] dateToStringWithDateFormate:@"yyMMddHHmmssSSS" timeZone:[NSTimeZone systemTimeZone] locale:[NSLocale systemLocale]],random3BitNum()];
    NSString *sql = [NSString stringWithFormat:
                     @"INSERT INTO %@ (Event,RecordDate,CreatedDate,UpdatedDate,FamilyID,EventID) VALUES('%ld','%@','%@','%@','%ld','%@')",dbEventTable,(NSInteger)event.state,
                     [event.recordDate dateToStringWithDateFormate:@"yyyy-MM-dd HH:mm:ss.SSS" timeZone:[NSTimeZone systemTimeZone] locale:[NSLocale systemLocale]],
                     [[NSDate date] dateToStringWithDateFormate:@"yyyy-MM-dd HH:mm:ss.SSS" timeZone:[NSTimeZone systemTimeZone] locale:[NSLocale systemLocale]],
                     [[NSDate date] dateToStringWithDateFormate:@"yyyy-MM-dd HH:mm:ss.SSS" timeZone:[NSTimeZone systemTimeZone] locale:[NSLocale systemLocale]],(long)familyId,eventId,nil];
    return [self dbQueueExecuteUpdateSQL:sql];
}

- (BOOL)deleteEventDataWithId:(NSInteger)dataId
{
    NSString *sql = [NSString stringWithFormat:@"UPDATE %@ SET IsDeleted = '1',timestamp = '%@' Where ID = '%ld' ",dbEventTable,[[NSDate date] dateToStringWithDateFormate:@"yyyy-MM-dd HH:mm:ss" timeZone:[NSTimeZone systemTimeZone] locale:[NSLocale systemLocale]],(long)dataId];
    return [self dbQueueExecuteUpdateSQL:sql];
}

- (NSMutableArray *)getAllEventDataToDate:(NSDate *)date familyId:(NSInteger)familyId
{
    NSString *dateStr = [date dateToStringWithDateFormate:@"yyyy-MM-dd HH:mm:ss.SSS" timeZone:[NSTimeZone systemTimeZone] locale:[NSLocale systemLocale]];
    NSString *sql = [NSString stringWithFormat:@"SELECT * FROM %@ where strftime('%%Y-%%m-%%d',RecordDate) == strftime('%%Y-%%m-%%d','%@') and FamilyID = '%ld' and IsDeleted = '0' order by RecordDate DESC",dbEventTable,dateStr,(long)familyId, nil];
    NSMutableArray *result = [[NSMutableArray alloc] init];
    
    [self.queue inDatabase:^(FMDatabase *db) {
        
        if (![db open]) {
            NSLog(@"Could not open database.");
        }else{
            FMResultSet *rs = [db executeQuery:sql];
            while ([rs next]) {
                MXEvent *event = [[MXEvent alloc] init];
                event.dataId = [rs intForColumn:@"ID"];
                event.state = [rs intForColumn:@"Event"];
                event.recordDate = [[rs stringForColumn:@"RecordDate"] stringWihtDateFormat:@"yyyy-MM-dd HH:mm:ss.SSS"];
                [result addObject:event];
                
            }
        }
    }];
    //NSLog(@"result = %@", result);
    return result;
}

- (BOOL)cleanTempEventCacheDataWithTimeStamp:(NSDate *)date
{
    NSString *sql = [NSString stringWithFormat:@"DELETE FROM %@ Where IsDeleted  = '1' and datetime(timestamp) <= datetime('%@')",dbEventTable,[date dateToStringWithDateFormate:@"yyyy-MM-dd HH:mm:ss" timeZone:[NSTimeZone systemTimeZone] locale:[NSLocale systemLocale]]];
    return [self dbQueueExecuteUpdateSQL:sql];
}

/*
#pragma mark - DeviceBound

- (BOOL)addDeviceBoundWithInfo:(MXDeviceInfo *)info memberId:(NSInteger)mId boundId:(NSInteger)bId
{
    NSString *remarkStr = info.nickName.length ? [NSString stringWithFormat:@"'%@'",info.nickName] : @"NULL";
    NSString *firmwareVersionStr = info.firmwareVersion.length ? [NSString stringWithFormat:@"'%@'",info.firmwareVersion] : @"NULL";
    NSString *sql = [NSString stringWithFormat:@"INSERT INTO %@ (BoundID,MemberID,DeviceID,SN,Remark,ProtocolVer,FirmwareVer,CreatedDate,UpdatedDate) VALUES('%d','%d','3','%@',%@,'%@',%@,'%@','%@')",dbDeviceBoundTable,bId,mId,info.serialNumber,remarkStr,[NSString stringWithFormat:@"%0.1f",(float)info.protocolVersion],firmwareVersionStr,
                     [[NSDate date] dateToStringWithDateFormate:@"yyyy-MM-dd HH:mm:ss.SSS" timeZone:[NSTimeZone systemTimeZone] locale:[NSLocale systemLocale]],
                     [[NSDate date] dateToStringWithDateFormate:@"yyyy-MM-dd HH:mm:ss.SSS" timeZone:[NSTimeZone systemTimeZone] locale:[NSLocale systemLocale]]];
    return [self dbQueueExecuteUpdateSQL:sql];
}

- (BOOL)updateDeviceBoundWithInfo:(MXDeviceInfo *)info memberId:(NSInteger)mId
{
    NSString *remarkStr = info.nickName.length ? [NSString stringWithFormat:@"'%@'",info.nickName] : @"NULL";
    NSString *firmwareVersionStr = info.firmwareVersion.length ? [NSString stringWithFormat:@"'%@'",info.firmwareVersion] : @"NULL";
    int deviceId = info.deviceModel == 1 ? 3 : 4;
    NSString *sql = [NSString stringWithFormat:@"UPDATE %@ SET DeviceID = '%d', SN = '%@',Remark = %@,ProtocolVer = '%0.1f',FirmwareVer = %@ Where BoundID = '%d' and MemberID = '%d'",dbDeviceBoundTable,deviceId,info.serialNumber,remarkStr,(float)info.protocolVersion,firmwareVersionStr,info.boundId,mId,nil];
    return [self dbQueueExecuteUpdateSQL:sql];
}

- (NSArray *)getAllDeviceBoundInfoWithMemberId:(NSInteger)mId
{
    NSString *sql = [NSString stringWithFormat:@"select * from %@  where MemberID = '%d'",dbDeviceBoundTable,mId,nil];
    NSMutableArray *result = [[NSMutableArray alloc] init];
    
    [self.queue inDatabase:^(FMDatabase *db) {
        
        if (![db open]) {
            NSLog(@"Could not open database.");
        }else{
            
            FMResultSet *rs = [db executeQuery:sql];
            while ([rs next]) {
                MXDeviceInfo *info = [[MXDeviceInfo alloc] init];
                info.boundId = [rs intForColumn:@"BoundID"];
                info.serialNumber = [rs stringForColumn:@"SN"];
                info.nickName = [rs stringForColumn:@"Remark"];
                info.protocolVersion = [rs intForColumn:@"ProtocolVer"];
                info.firmwareVersion = [rs stringForColumn:@"FirmwareVer"];
                int deviceId = [rs intForColumn:@"DeviceID"];
                info.deviceModel = deviceId == 4 ? 2 : 1;
                [result addObject:info];
            }
        }
    }];
    
    
    return result;
}

- (BOOL)insertUpdateDeviceWithData:(NSArray *)devices memberId:(NSUInteger)mId
{
//    if (![devices count]) {
//        return NO;
//    }
    __block BOOL result = NO;
    
    [self.queue inTransaction:^(FMDatabase *db, BOOL *rollback) {
        
        if (![db open]) {
            NSLog(@"Could not open database.");
        }else{
            NSString *sql;
            BOOL isInsert = YES;
            
            NSMutableArray *familyIds = [[NSMutableArray alloc] init];
            for (MXDeviceInfo *devi in devices) {
                int number = [db intForQuery:[NSString stringWithFormat:@"SELECT COUNT(*) FROM %@ where BoundID = '%d' and MemberID = '%d'",dbDeviceBoundTable,devi.boundId,mId]];
                
                [familyIds addObject:[NSString stringWithFormat:@"BoundID <> '%d'",devi.boundId]];
                if (!number) {
                    //插入
                    sql = [NSString stringWithFormat:@"INSERT INTO %@ (BoundID,MemberID,DeviceID,SN,Remark,ProtocolVer,FirmwareVer,CreatedDate,UpdatedDate) VALUES('%d','%d','3','%@','%@','%@','%@','%@','%@')",dbDeviceBoundTable,devi.boundId,mId,devi.serialNumber,devi.nickName,[NSString stringWithFormat:@"%d.0",devi.protocolVersion],devi.firmwareVersion,
                           [[NSDate date] dateToStringWithDateFormate:@"yyyy-MM-dd HH:mm:ss" timeZone:[NSTimeZone systemTimeZone] locale:[NSLocale systemLocale]],
                           [[NSDate date] dateToStringWithDateFormate:@"yyyy-MM-dd HH:mm:ss" timeZone:[NSTimeZone systemTimeZone] locale:[NSLocale systemLocale]],nil];
                } else {
                    //更新
                    int deviceId = devi.deviceModel == 1 ? 3 : 4;
                    sql = [NSString stringWithFormat:@"UPDATE %@ SET DeviceID = '%d', SN = '%@',Remark = '%@',ProtocolVer = '%0.1f',FirmwareVer = '%@' Where BoundID = '%d' and MemberID = '%d'",dbDeviceBoundTable,deviceId,devi.serialNumber,devi.nickName,(float)devi.protocolVersion,devi.firmwareVersion,devi.boundId,mId,nil];
                    
                }
                
                BOOL currentInsert = [db executeUpdate:sql];
                if (!currentInsert) {
                    isInsert = currentInsert;
                }
            }
            NSString *deleteSql = @"";
            if ([familyIds count]) {
                NSString *allIdStr = [familyIds componentsJoinedByString:@" and "];
                deleteSql = [NSString stringWithFormat:@"DELETE FROM %@ WHERE MemberID = '%d' and ( %@ )",dbDeviceBoundTable,mId,allIdStr];
            } else {
                deleteSql = [NSString stringWithFormat:@"DELETE FROM %@ WHERE MemberID = '%d'",dbDeviceBoundTable,mId];
            }
            
            BOOL isDelete = [db executeUpdate:deleteSql];
            if (!isInsert || !isDelete) {
                *rollback = YES;
                return;
            } else {
                result = YES;
            }
        }
    }];
    
    
    return result;
}

- (NSArray *)getAllDeviceSNWithMemberId:(NSInteger)mId
{
    NSString *sql = [NSString stringWithFormat:@"select * from %@  where MemberID = '%d'",dbDeviceBoundTable,mId,nil];
    NSMutableArray *result = [[NSMutableArray alloc] init];
    
    [self.queue inDatabase:^(FMDatabase *db) {
        
        if (![db open]) {
            NSLog(@"Could not open database.");
        }else{
            
            FMResultSet *rs = [db executeQuery:sql];
            while ([rs next]) {
                NSString *SN = [rs stringForColumn:@"SN"];
                [result addObject:SN];
            }
        }
    }];
    
    
    return result;
}


- (MXDeviceInfo *)getDeviceInfoWithSerialNumber:(NSString *)serial memberId:(NSInteger)mId
{
    __block MXDeviceInfo *result = nil;
    NSString *sql = [NSString stringWithFormat:@"Select * from %@ where SN = '%@' and MemberID = '%d'",dbDeviceBoundTable,serial,mId];
    
    [self.queue inDatabase:^(FMDatabase *db) {
        
        if (![db open]) {
            NSLog(@"Could not open database.");
        }else{
            FMResultSet *rs = [db executeQuery:sql];
            while ([rs next]) {
                MXDeviceInfo *info = [[MXDeviceInfo alloc] init];
                info.boundId = [rs intForColumn:@"BoundID"];
                info.serialNumber = [rs stringForColumn:@"SN"];
                info.nickName = [rs stringForColumn:@"Remark"];
                info.protocolVersion = [rs intForColumn:@"ProtocolVer"];
                info.firmwareVersion = [rs stringForColumn:@"FirmwareVer"];
                int deviceId = [rs intForColumn:@"DeviceID"];
                info.deviceModel = deviceId == 4 ? 2 : 1;
                result = info;
                break;
            }
        }
    }];
    
    
    return result;
}

- (BOOL)isExistDeviceInfoWithSerialNumber:(NSString *)serial memberId:(NSInteger)mId
{
    NSString *sql = [NSString stringWithFormat:@"Select count(*) from %@ where SN = '%@' and MemberID = '%d'",dbDeviceBoundTable,serial,mId];
    int number = [self numberOfFirstColumnAtSQL:sql];
    return number ? YES : NO;
}

- (BOOL)deleteDataToDeviceWithBoundId:(NSInteger)bId memberId:(NSUInteger)memberId
{
    NSString *sql = [NSString stringWithFormat:@"Delete From %@ where MemberID = '%d' and BoundID = '%d'",dbDeviceBoundTable,memberId,bId];
    return [self dbQueueExecuteUpdateSQL:sql];
}
 */
@end
