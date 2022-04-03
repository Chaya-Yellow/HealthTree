//
//  OMEDataBase.m
//
//
//  Created by sincan on 14-10-29.
//  Copyright (c) 2014年 Xin. All rights reserved.
//

#import "OMEDataBase.h"
#import "OMESoft.h"
#import "zlib.h"
#import "JSONKit.h"
#import "NSDataEx.h"

@interface OMEDataBase ()

@end

@implementation OMEDataBase

- (id)init {
    self = [super init];
    if (self)
    {
        NSArray *paths = NSSearchPathForDirectoriesInDomains(NSDocumentDirectory, NSUserDomainMask, YES);
        NSString *dataPath = [[paths objectAtIndex:0] stringByAppendingPathComponent:dbUserFileName];
        //NSLog(@"%@",dataPath);
        FMDatabase *dataBase = [[FMDatabase alloc] initWithPath:dataPath];
        if (![dataBase open]) {
            NSLog(@"Could not open database.");
        } else {
            [dataBase beginTransaction];
            //账号
            NSString *memberSql = [NSString stringWithFormat:@"CREATE TABLE IF NOT EXISTS %@ (ID INTEGER PRIMARY KEY NOT NULL,MemberID INTEGER NOT NULL,ClientKey TEXT,ServerTimeStamp INTEGER NOT NULL DEFAULT 0,LocalTimeStamp TIMESTAMP NOT NULL DEFAULT (datetime('now','localtime')))",dbMemberTable];
            
            //家庭成员
            NSString *familySql = [NSString stringWithFormat:@"CREATE TABLE IF NOT EXISTS %@ (ID INTEGER PRIMARY KEY NOT NULL,FamilyID INTEGER NOT NULL,MemberID INTEGER NOT NULL,Name TEXT NOT NULL,Height FLOAT NOT NULL,Weight FLOAT NOT NULL,Birthday DataTime NOT NULL,Gender BIT NOT NULL,ImageName TEXT,CreatedDate DateTime NOT NULL,UpdatedDate DateTime NOT NULL)",dbFamilyTable];
            [dataBase executeUpdate:memberSql];
            [dataBase executeUpdate:familySql];
            
#if isSyncTempData
            //体温
            NSString *tempSql = [NSString stringWithFormat:@"CREATE TABLE IF NOT EXISTS %@ (ID INTEGER PRIMARY KEY NOT NULL,TEMPID TEXT NOT NULL,FamilyID INTEGER NOT NULL,Part INTEGER NOT NULL,TEMP FLOAT NOT NULL,SPO2 INTEGER NOT NULL,PR INTEGER NOT NULL,PI INTEGER NOT NULL,RESP INTEGER NOT NULL,RecordDate DateTime NOT NULL,CreatedDate DateTime NOT NULL,UpdatedDate DateTime NOT NULL,IsDeleted BIT NOT NULL DEFAULT '0',timestamp TIMESTAMP NOT NULL DEFAULT (datetime('now','localtime')))",dbTempTable, nil];
            //事件
            NSString *eventSql = [NSString stringWithFormat:@"CREATE TABLE IF NOT EXISTS %@ (ID INTEGER PRIMARY KEY NOT NULL,FamilyID INTEGER NOT NULL,EventID TEXT NOT NULL,Event INTEGER NOT NULL DEFAULT '1',RecordDate DateTime NOT NULL,CreatedDate DateTime NOT NULL,UpdatedDate DateTime NOT NULL,IsDeleted BIT NOT NULL DEFAULT '0',timestamp TIMESTAMP NOT NULL DEFAULT (datetime('now','localtime')))",dbEventTable, nil];
            [dataBase executeUpdate:tempSql];
            [dataBase executeUpdate:eventSql];
            
            //体温同步临时
            NSString *tempTmpSql = [NSString stringWithFormat:@"CREATE TABLE IF NOT EXISTS %@ (ID INTEGER PRIMARY KEY NOT NULL,TEMPID TEXT NOT NULL,FamilyID INTEGER NOT NULL,Part INTEGER NOT NULL,TEMP FLOAT NOT NULL,SPO2 INTEGER NOT NULL,PR INTEGER NOT NULL,PI INTEGER NOT NULL,RESP INTEGER NOT NULL,RecordDate DateTime NOT NULL,CreatedDate DateTime NOT NULL,UpdatedDate DateTime NOT NULL,IsDeleted BIT NOT NULL DEFAULT '0',timestamp TIMESTAMP NOT NULL DEFAULT (datetime('now','localtime')))",dbTempUpdateTmpTable, nil];
            //事件同步临时
            NSString *eventTmpSql = [NSString stringWithFormat:@"CREATE TABLE IF NOT EXISTS %@ (ID INTEGER PRIMARY KEY NOT NULL,FamilyID INTEGER NOT NULL,EventID TEXT NOT NULL,Event INTEGER NOT NULL DEFAULT '1',RecordDate DateTime NOT NULL,CreatedDate DateTime NOT NULL,UpdatedDate DateTime NOT NULL,IsDeleted BIT NOT NULL DEFAULT '0',timestamp TIMESTAMP NOT NULL DEFAULT (datetime('now','localtime')))",dbEventUpdateTmpTable, nil];
            [dataBase executeUpdate:tempTmpSql];
            [dataBase executeUpdate:eventTmpSql];
#endif
            
#if isSyncBCData
            //人体成分
            NSString *bodyCompositionSql = [NSString stringWithFormat:@"CREATE TABLE IF NOT EXISTS %@ (ID INTEGER PRIMARY KEY NOT NULL,BodyID TEXT NOT NULL,FamilyID INTEGER NOT NULL,Resistance INTEGER NOT NULL,Weight FLOAT NOT NULL,Height INTEGER NOT NULL,Age INTEGER NOT NULL,Gender INTEGER NOT NULL,Waistline INTEGER NOT NULL,Hipline INTEGER NOT NULL,RecordDate DateTime NOT NULL,CreatedDate DateTime NOT NULL,UpdatedDate DateTime NOT NULL,IsDeleted BIT NOT NULL DEFAULT '0',timestamp TIMESTAMP NOT NULL DEFAULT (datetime('now','localtime')))",dbBCTable];
            [dataBase executeUpdate:bodyCompositionSql];
            //人体同步临时
            NSString *bodyCompositionTmpSql = [NSString stringWithFormat:@"CREATE TABLE IF NOT EXISTS %@ (ID INTEGER PRIMARY KEY NOT NULL,BodyID TEXT NOT NULL,FamilyID INTEGER NOT NULL,Resistance INTEGER NOT NULL,Weight FLOAT NOT NULL,Height INTEGER NOT NULL,Age INTEGER NOT NULL,Gender INTEGER NOT NULL,Waistline INTEGER NOT NULL,Hipline INTEGER NOT NULL,RecordDate DateTime NOT NULL,CreatedDate DateTime NOT NULL,UpdatedDate DateTime NOT NULL,IsDeleted BIT NOT NULL DEFAULT '0',timestamp TIMESTAMP NOT NULL DEFAULT (datetime('now','localtime')))",dbBCUpdateTmpTable];
            [dataBase executeUpdate:bodyCompositionTmpSql];
            
            
#endif
            
#if isSyncBPData
            //血压
            NSString *bpSql = [NSString stringWithFormat:
                                     @"CREATE TABLE IF NOT EXISTS %@ (ID INTEGER PRIMARY KEY NOT NULL,BPID TEXT NOT NULL,FamilyID INTEGER NOT NULL,SBP INTEGER NOT NULL,DBP INTEGER NOT NULL,HR INTEGER NOT NULL,RecordDate DateTime NOT NULL,CreatedDate DateTime NOT NULL,UpdatedDate DateTime NOT NULL,IsDeleted BIT NOT NULL DEFAULT '0',timestamp TIMESTAMP NOT NULL DEFAULT (datetime('now','localtime')))",dbBPTable, nil];
            [dataBase executeUpdate:bpSql];
            //血压同步临时
            NSString *bpTmpSql = [NSString stringWithFormat:
                                     @"CREATE TABLE IF NOT EXISTS %@ (ID INTEGER PRIMARY KEY NOT NULL,BPID TEXT NOT NULL,FamilyID INTEGER NOT NULL,SBP INTEGER NOT NULL,DBP INTEGER NOT NULL,HR INTEGER NOT NULL,RecordDate DateTime NOT NULL,CreatedDate DateTime NOT NULL,UpdatedDate DateTime NOT NULL,IsDeleted BIT NOT NULL DEFAULT '0',timestamp TIMESTAMP NOT NULL DEFAULT (datetime('now','localtime')))",dbBPUpdateTmpTable, nil];
            [dataBase executeUpdate:bpTmpSql];
#endif
            
#if isSyncGLUData
            //血糖
            NSString *gluSql = [NSString stringWithFormat:
                                @"CREATE TABLE IF NOT EXISTS %@ (ID INTEGER PRIMARY KEY NOT NULL,GLUID TEXT NOT NULL,FamilyID INTEGER NOT NULL,GLU FLOAT NOT NULL,During INTEGER NOT NULL,RecordDate DateTime NOT NULL,CreatedDate DateTime NOT NULL,UpdatedDate DateTime NOT NULL,IsDeleted BIT NOT NULL DEFAULT '0',timestamp TIMESTAMP NOT NULL DEFAULT (datetime('now','localtime')))",dbGLUTable, nil];
            [dataBase executeUpdate:gluSql];
            
            NSString *gluTmpSql = [NSString stringWithFormat:
                                @"CREATE TABLE IF NOT EXISTS %@ (ID INTEGER PRIMARY KEY NOT NULL,GLUID TEXT NOT NULL,FamilyID INTEGER NOT NULL,GLU FLOAT NOT NULL,During INTEGER NOT NULL,RecordDate DateTime NOT NULL,CreatedDate DateTime NOT NULL,UpdatedDate DateTime NOT NULL,IsDeleted BIT NOT NULL DEFAULT '0',timestamp TIMESTAMP NOT NULL DEFAULT (datetime('now','localtime')))",dbGLUUpdateTmpTable, nil];
            [dataBase executeUpdate:gluTmpSql];
#endif
            
#if isSyncTCHData
            //胆固醇
            NSString *tchSql = [NSString stringWithFormat:
                                @"CREATE TABLE IF NOT EXISTS %@ (ID INTEGER PRIMARY KEY NOT NULL,TCHID TEXT NOT NULL,FamilyID INTEGER NOT NULL,TCH FLOAT NOT NULL,RecordDate DateTime NOT NULL,CreatedDate DateTime NOT NULL,UpdatedDate DateTime NOT NULL,IsDeleted BIT NOT NULL DEFAULT '0',timestamp TIMESTAMP NOT NULL DEFAULT (datetime('now','localtime')))",dbTCHTable, nil];
            [dataBase executeUpdate:tchSql];
            NSString *tchTmpSql = [NSString stringWithFormat:
                                @"CREATE TABLE IF NOT EXISTS %@ (ID INTEGER PRIMARY KEY NOT NULL,TCHID TEXT NOT NULL,FamilyID INTEGER NOT NULL,TCH FLOAT NOT NULL,RecordDate DateTime NOT NULL,CreatedDate DateTime NOT NULL,UpdatedDate DateTime NOT NULL,IsDeleted BIT NOT NULL DEFAULT '0',timestamp TIMESTAMP NOT NULL DEFAULT (datetime('now','localtime')))",dbTCHUpdateTmpTable, nil];
            [dataBase executeUpdate:tchTmpSql];
#endif
            
#if isSyncUAData
            //尿酸
            NSString *uaSql = [NSString stringWithFormat:
                               @"CREATE TABLE IF NOT EXISTS %@ (ID INTEGER PRIMARY KEY NOT NULL,UAID TEXT NOT NULL,FamilyID INTEGER NOT NULL,UA FLOAT NOT NULL,RecordDate DateTime NOT NULL,CreatedDate DateTime NOT NULL,UpdatedDate DateTime NOT NULL,IsDeleted BIT NOT NULL DEFAULT '0',timestamp TIMESTAMP NOT NULL DEFAULT (datetime('now','localtime')))",dbUATable, nil];
            [dataBase executeUpdate:uaSql];

            NSString *uaTmpSql = [NSString stringWithFormat:
                               @"CREATE TABLE IF NOT EXISTS %@ (ID INTEGER PRIMARY KEY NOT NULL,UAID TEXT NOT NULL,FamilyID INTEGER NOT NULL,UA FLOAT NOT NULL,RecordDate DateTime NOT NULL,CreatedDate DateTime NOT NULL,UpdatedDate DateTime NOT NULL,IsDeleted BIT NOT NULL DEFAULT '0',timestamp TIMESTAMP NOT NULL DEFAULT (datetime('now','localtime')))",dbUAUpdateTmpTable, nil];
            [dataBase executeUpdate:uaTmpSql];
            
#endif
            
            [dataBase commit];
        }
        dataBase = nil;
        self.queue = [[FMDatabaseQueue alloc] initWithPath:dataPath];
    }
    return self;
}

#pragma mark - Private Methods
- (BOOL)dbQueueExecuteUpdateSQL:(NSString *)sql
{
    __block BOOL result = NO;
    [_queue inDatabase:^(FMDatabase *db) {
        if (![db open]) {
            NSLog(@"Could not open database.");
        }else{
            result = [db executeUpdate:sql];
        }
    }];
    return result;
}

- (NSInteger)numberOfFirstColumnAtSQL:(NSString *)sql
{
    __block NSInteger number = 0;
    [_queue inDatabase:^(FMDatabase *db) {
        if (![db open]) {
            NSLog(@"Could not open database.");
        }else{
            number = [db intForQuery:sql];
        }
    }];
    return number;
}

#pragma mark - Member

- (BOOL)loginAccountWithId:(long long int)memberid key:(NSString *)key
{
    NSLog(@"member = %lld", (long long)memberid);
    if (memberid <= 0 || key.length <= 0) {
        return NO;
    }
    __block BOOL result = NO;
    [_queue inTransaction:^(FMDatabase *db, BOOL *rollback) {
        if (![db open]) {
            NSLog(@"Could not open database.");
        }else{
            NSString *sql;
            int number = [db intForQuery:[NSString stringWithFormat:@"SELECT COUNT(*) FROM %@ Where MemberID = '%lld'",dbMemberTable,(long long)memberid]];
            if (!number) {
                //插入
                sql = [NSString stringWithFormat:@"INSERT INTO %@ (MemberID,ClientKey)VALUES('%lld','%@')",dbMemberTable,(long long)memberid,key,nil];
            } else {
                //更新
                sql = [NSString stringWithFormat:@"UPDATE %@ SET ClientKey = '%@' Where MemberID = '%lld'",dbMemberTable,key,(long long)memberid,nil];
            }
            //NSLog(@"----------sql = %@", sql);
            BOOL currentInsert = [db executeUpdate:sql];
            if (!currentInsert) {
                *rollback = YES;
            } else {
                result = YES;
            }
        }
    }];
    return result;
}

- (BOOL)logoutAccount
{
    NSString *sql = [NSString stringWithFormat:@"UPDATE %@ SET ClientKey = NULL",dbMemberTable];
    return [self dbQueueExecuteUpdateSQL:sql];
}

- (NSMutableArray *)getLoginInfo
{
    NSString *sql = [NSString stringWithFormat:@"SELECT * FROM %@ Where ClientKey IS NOT NULL",dbMemberTable];
    NSMutableArray *result = [[NSMutableArray alloc] init];
    [_queue inDatabase:^(FMDatabase *db) {
        if (![db open]) {
            NSLog(@"Could not open database.");
        }else{
            FMResultSet *rs = [db executeQuery:sql];
            while ([rs next]) {
                [result addObject:[NSDictionary dictionaryWithObjectsAndKeys:
                                   [NSNumber numberWithLongLong:[rs longLongIntForColumn:@"MemberID"]],@"id",
                                   [rs stringForColumn:@"ClientKey"],@"clientKey",
                                   [NSNumber numberWithInteger:[rs intForColumn:@"ServerTimeStamp"]],@"serverTimeStamp",
                                   [rs stringForColumn:@"LocalTimeStamp"],@"localTimeStamp",nil]];
            }
            [rs close];
        }
    }];
    return result;
}

- (BOOL)updateTimeStampWithId:(NSInteger)memberId serverTimeStamp:(NSInteger)server localTimeStamp:(NSDate *)local
{
    NSString *sql = [NSString stringWithFormat:@"UPDATE %@ SET ServerTimeStamp = '%ld',LocalTimeStamp = '%@' where MemberID = '%ld'",dbMemberTable,(long)server,[local dateToStringWithDateFormate:@"yyyy-MM-dd HH:mm:ss" timeZone:[NSTimeZone systemTimeZone] locale:[NSLocale systemLocale]],(long)memberId];
    return [self dbQueueExecuteUpdateSQL:sql];
}

#pragma mark - user

- (BOOL)syncUserData:(NSArray *)familys memberId:(NSUInteger)mId
{
    NSArray *familyArray = [NSArray arrayWithArray:familys];
    //NSLog(@"familys = %@, memberID=%d", familyArray, mId);
    __block BOOL result = NO;
    [_queue inTransaction:^(FMDatabase *db, BOOL *rollback) {
        if (![db open]) {
            NSLog(@"Could not open database.");
        }else{
            NSString *sql;
            BOOL isInsert = YES;
            NSMutableArray *familyIds = [[NSMutableArray alloc] init];
            for (int i = 0; i < [familyArray count]; i++) {
                NSDictionary *familyDic = [familyArray objectAtIndex:i];
               // NSLog(@"familuDic = %@", familyDic);
                int number = [db intForQuery:[NSString stringWithFormat:@"SELECT COUNT(*) FROM %@ where FamilyID = '%ld' and MemberID = '%ld'",dbFamilyTable,(long)[[familyDic objectForKey:@"memberId"] integerValue],(long)mId]];
                [familyIds addObject:[NSString stringWithFormat:@"FamilyID <> '%ld'",(long)[[familyDic objectForKey:@"memberId"] integerValue]]];
                //NSLog(@"number = %d", number);
                NSDate *userBirthday = [[familyDic objectForKey:@"birthday"] stringWihtDateFormat:@"yyyy-MM-dd"];
                NSString *userImageName = [familyDic objectForKey:@"avatar"];
                NSString *imageNameStr = (userImageName == nil || userImageName.length == 0) ? @"NULL" : [NSString stringWithFormat:@"'%@'",userImageName];
//                NSLog(@"Number = %d, imageNameStr = %@", number, imageNameStr);
                if (!number) {
                    //插入
                    sql = [NSString stringWithFormat:@"INSERT INTO %@ (FamilyID,Name,Height,Birthday,Gender,Weight,ImageName,MemberID,CreatedDate,UpdatedDate)VALUES('%ld',?,'%0.3f','%@','%d','%0.3f',%@,'%ld','%@','%@')",dbFamilyTable,
                           (long)[[familyDic objectForKey:@"memberId"] integerValue],
                           [[familyDic objectForKey:@"height"] floatValue],
                           [userBirthday dateToStringWithDateFormate:@"yyyy-MM-dd" timeZone:[NSTimeZone systemTimeZone] locale:[NSLocale systemLocale]],
                           [[familyDic objectForKey:@"gender"] boolValue],
                           [[familyDic objectForKey:@"weight"] floatValue],
                           imageNameStr,
                           (long)mId,
                           [[NSDate date] dateToStringWithDateFormate:@"yyyy-MM-dd HH:mm:ss" timeZone:[NSTimeZone systemTimeZone] locale:[NSLocale systemLocale]],
                           [[NSDate date] dateToStringWithDateFormate:@"yyyy-MM-dd HH:mm:ss" timeZone:[NSTimeZone systemTimeZone] locale:[NSLocale systemLocale]],nil];
                } else {
                    //更新
                    sql = [NSString stringWithFormat:@"UPDATE %@ SET Name = ?,Height = '%0.3f',Birthday = '%@',Gender = '%d',Weight = '%0.3f',ImageName = %@ Where FamilyID = '%ld' and MemberID = '%ld'",dbFamilyTable,
                           [[familyDic objectForKey:@"height"] floatValue],
                           [userBirthday dateToStringWithDateFormate:@"yyyy-MM-dd" timeZone:[NSTimeZone systemTimeZone] locale:[NSLocale systemLocale]],
                           [[familyDic objectForKey:@"gender"] boolValue],
                           [[familyDic objectForKey:@"weight"] floatValue],
                           imageNameStr,
                           (long)[[familyDic objectForKey:@"memberId"] integerValue],
                           (long)mId,nil];
                    
                }
                NSString *userName =  [familyDic objectForKey:@"firstname"];
                //NSLog(@"sql = %@", sql);
                BOOL currentInsert = [db executeUpdate:sql,userName];
//                NSLog(@"CurrenrInsert  =%d", currentInsert);
                if (!currentInsert) {
                    isInsert = currentInsert;
                }
            }
            
            NSString *allIdStr = [familyIds componentsJoinedByString:@" and "];
            NSString *deleteSql = [NSString stringWithFormat:@"DELETE FROM %@ WHERE MemberID = '%ld' and ( %@ )",dbFamilyTable,(long)mId,allIdStr];
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

#pragma mark - Sync

- (void)getSyncDataWithId:(NSInteger)memberId timeStamp:(NSDate *)timestamp completion:(OMEDBCompletion)completion
{
    dispatch_async(dispatch_get_global_queue(DISPATCH_QUEUE_PRIORITY_DEFAULT, 0), ^{
        NSMutableDictionary *result = [[NSMutableDictionary alloc] init];
        [_queue inDatabase:^(FMDatabase *db) {
            if (![db open]) {
                NSLog(@"Could not open database.");
            }else{
                NSString *timestampStr = [timestamp dateToStringWithDateFormate:@"yyyy-MM-dd HH:mm:ss"];
#if isSyncTempData
                NSString *tempSql = [NSString stringWithFormat:@"select * from %@  where datetime(timestamp) > datetime('%@') and FamilyID in ( select FamilyID From %@ where MemberID = '%ld' )",dbTempTable,timestampStr,dbFamilyTable,(long)memberId,nil];
                //NSLog(@"%@",tempSql);
                FMResultSet *tempRs = [db executeQuery:tempSql];
                NSMutableArray *tempArray = [[NSMutableArray alloc] init];
                while ([tempRs next]) {
                    [tempArray addObject:[NSDictionary dictionaryWithObjectsAndKeys:
                                          @([[tempRs stringForColumn:@"TEMPID"] longLongValue]),@"valueId",
                                          @([tempRs longForColumn:@"FamilyID"]),@"memberId",
                                          @([tempRs intForColumn:@"Part"]),@"part",
                                          @([tempRs intForColumn:@"SPO2"]),@"SPO2",
                                          @([tempRs intForColumn:@"PR"]),@"PR",
                                          @([tempRs intForColumn:@"PI"]), @"PI",
                                          @([tempRs intForColumn:@"RESP"]),@"RESP",
                                          [tempRs stringForColumn:@"RecordDate"],@"record_date",
                                          [tempRs stringForColumn:@"CreatedDate"],@"created_date",
                                          [tempRs stringForColumn:@"UpdatedDate"],@"updated_date",
                                          @([tempRs boolForColumn:@"IsDeleted"]),@"is_deleted",
                                          nil]];
                }
                [tempRs close];
                //
                NSString* posStr = [NSString jsonStringWithArray:tempArray];
                NSData *PosData = [posStr dataUsingEncoding:NSUTF8StringEncoding];
                Byte* SourceBuf;
                SourceBuf = (Byte *)[PosData bytes];
                uLong sourceLen = PosData.length;
                uLong desLen = sourceLen*10;
                Byte* desByte = malloc(desLen);
                if(compress(desByte, &desLen, SourceBuf, sourceLen) != Z_OK)
                {
                    printf("compress failed!\n");
                }
                NSData *adata = [[NSData alloc] initWithBytes:desByte length:desLen];
                free(desByte);
                NSString *aString =[adata base64Encoding];
                //NSLog(@"%@",aString);
                [result setObject:aString forKey:@"value"];
#endif
                
#if isSyncBCData
                NSString *bcSql = [NSString stringWithFormat:@"select * from %@  where datetime(timestamp) >= datetime('%@') and FamilyID in ( select FamilyID From %@ where MemberID = '%ld' )",dbBCTable,timestampStr,dbFamilyTable,(long)memberId,nil];
                
                FMResultSet *bcRs = [db executeQuery:bcSql];
                NSMutableArray *bcArray = [[NSMutableArray alloc] init];
                while ([bcRs next]) {
                    [bcArray addObject:[NSDictionary dictionaryWithObjectsAndKeys:
                                        [bcRs stringForColumn:@"BodyID"],@"body_id",
                                        [bcRs stringForColumn:@"FamilyID"],@"family_id",
                                        [NSString stringWithFormat:@"%d",[bcRs intForColumn:@"Resistance"]],@"resistance",
                                        [NSString stringWithFormat:@"%0.1f",[bcRs doubleForColumn:@"Weight"]],@"weight",
                                        [NSString stringWithFormat:@"%d",[bcRs intForColumn:@"Height"]],@"height",
                                        [NSString stringWithFormat:@"%d",[bcRs intForColumn:@"Age"]],@"age",
                                        [NSString stringWithFormat:@"%d",[bcRs intForColumn:@"Gender"]],@"gender",
                                        [NSString stringWithFormat:@"%d",[bcRs intForColumn:@"Waistline"]],@"waistline",
                                        [NSString stringWithFormat:@"%d",[bcRs intForColumn:@"Hipline"]],@"hipline",
                                        [bcRs stringForColumn:@"RecordDate"],@"record_date",
                                        [bcRs stringForColumn:@"CreatedDate"],@"created_date",
                                        [bcRs stringForColumn:@"UpdatedDate"],@"updated_date",
                                        [NSString stringWithFormat:@"%@",[bcRs boolForColumn:@"IsDeleted"] ? @"true":@"false"],@"is_deleted",
                                        nil]];
                }
                [bcRs close];
                [result setObject:bcArray forKey:@"body"];
#endif
                
                
#if isSyncBPData
                NSString *bpSql = [NSString stringWithFormat:@"select * from %@  where datetime(timestamp) >= datetime('%@') and FamilyID in ( select FamilyID From %@ where MemberID = '%ld' )",dbBPTable,timestampStr,dbFamilyTable,(long)memberId,nil];
                
                FMResultSet *bpRs = [db executeQuery:bpSql];
                NSMutableArray *bpArray = [[NSMutableArray alloc] init];
                while ([bpRs next]) {
                    [bpArray addObject:[NSDictionary dictionaryWithObjectsAndKeys:
                                       [bpRs stringForColumn:@"BPID"],@"bp_id",
                                       [bpRs stringForColumn:@"FamilyID"],@"family_id",
                                       [NSString stringWithFormat:@"%d",[bpRs intForColumn:@"SBP"]],@"sbp",
                                       [NSString stringWithFormat:@"%d",[bpRs intForColumn:@"DBP"]],@"dbp",
                                       [NSString stringWithFormat:@"%d",[bpRs intForColumn:@"HR"]],@"hr",
                                       [bpRs stringForColumn:@"RecordDate"],@"record_date",
                                       [bpRs stringForColumn:@"CreatedDate"],@"created_date",
                                       [bpRs stringForColumn:@"UpdatedDate"],@"updated_date",
                                       [NSString stringWithFormat:@"%@",[bpRs boolForColumn:@"IsDeleted"] ? @"true":@"false"],@"is_deleted",
                                       nil]];
                }
                [bpRs close];
                [result setObject:bpArray forKey:@"bp"];
                
#endif
                
#if isSyncGLUData
                NSString *gluSql = [NSString stringWithFormat:@"select * from %@  where datetime(timestamp) >= datetime('%@') and FamilyID in ( select FamilyID From %@ where MemberID = '%ld' )",dbGLUTable,timestampStr,dbFamilyTable,(long)memberId,nil];
                FMResultSet *gluRs = [db executeQuery:gluSql];
                NSMutableArray *gluArray = [[NSMutableArray alloc] init];
                while ([gluRs next]) {
                    [gluArray addObject:[NSDictionary dictionaryWithObjectsAndKeys:
                                       [gluRs stringForColumn:@"GLUID"],@"bg_id",
                                       [gluRs stringForColumn:@"FamilyID"],@"family_id",
                                       [NSString stringWithFormat:@"%0.1f",[gluRs doubleForColumn:@"GLU"]],@"bg",
                                       [NSString stringWithFormat:@"%d",[gluRs intForColumn:@"During"]],@"duration",
                                       [gluRs stringForColumn:@"RecordDate"],@"record_date",
                                       [gluRs stringForColumn:@"CreatedDate"],@"created_date",
                                       [gluRs stringForColumn:@"UpdatedDate"],@"updated_date",
                                       [NSString stringWithFormat:@"%@",[gluRs boolForColumn:@"IsDeleted"] ? @"true":@"false"],@"is_deleted",
                                       nil]];
                }
                [gluRs close];
                [result setObject:gluArray forKey:@"bg"];
#endif
                
#if isSyncTCHData
                NSString *tchSql = [NSString stringWithFormat:@"select * from %@  where datetime(timestamp) >= datetime('%@') and FamilyID in ( select FamilyID From %@ where MemberID = '%ld' )",dbTCHTable,timestampStr,dbFamilyTable,(long)memberId,nil];
                FMResultSet *tchRs = [db executeQuery:tchSql];
                NSMutableArray *tchArray = [[NSMutableArray alloc] init];
                while ([tchRs next]) {
                    [tchArray addObject:[NSDictionary dictionaryWithObjectsAndKeys:
                                       [tchRs stringForColumn:@"TCHID"],@"tch_id",
                                       [tchRs stringForColumn:@"FamilyID"],@"family_id",
                                       [NSString stringWithFormat:@"%0.2f",[tchRs doubleForColumn:@"TCH"]],@"tch",
                                       [tchRs stringForColumn:@"RecordDate"],@"record_date",
                                       [tchRs stringForColumn:@"CreatedDate"],@"created_date",
                                       [tchRs stringForColumn:@"UpdatedDate"],@"updated_date",
                                       [NSString stringWithFormat:@"%@",[tchRs boolForColumn:@"IsDeleted"] ? @"true":@"false"],@"is_deleted",
                                       nil]];
                }
                [tchRs close];
                [result setObject:tchArray forKey:@"tch"];
#endif
                
#if isSyncUAData
                NSString *uaSql = [NSString stringWithFormat:@"select * from %@  where datetime(timestamp) >= datetime('%@') and FamilyID in ( select FamilyID From %@ where MemberID = '%ld' )",dbUATable,timestampStr,dbFamilyTable,(long)memberId,nil];
                FMResultSet *uaRs = [db executeQuery:uaSql];
                NSMutableArray *uaArray = [[NSMutableArray alloc] init];
                while ([uaRs next]) {
                    [uaArray addObject:[NSDictionary dictionaryWithObjectsAndKeys:
                                       [uaRs stringForColumn:@"UAID"],@"ua_id",
                                       [uaRs stringForColumn:@"FamilyID"],@"family_id",
                                       [NSString stringWithFormat:@"%0.2f",[uaRs doubleForColumn:@"UA"]],@"ua",
                                       [uaRs stringForColumn:@"RecordDate"],@"record_date",
                                       [uaRs stringForColumn:@"CreatedDate"],@"created_date",
                                       [uaRs stringForColumn:@"UpdatedDate"],@"updated_date",
                                       [NSString stringWithFormat:@"%@",[uaRs boolForColumn:@"IsDeleted"] ? @"true":@"false"],@"is_deleted",
                                       nil]];
                }
                [uaRs close];
                [result setObject:uaArray forKey:@"ua"];
#endif
                
                
            }
            
        }];
        
        dispatch_async(dispatch_get_main_queue(), ^{
            completion(result,nil);
        });
    });
    
}
- (void)updateSyncData:(NSDictionary *)aData timeStamp:(NSDate*)origalTimeStamp memberId:(NSUInteger)memberId serverTimeStamp:(NSInteger)server completion:(OMEDBSyncCompletion)completion
{
    NSString *timestampStr = [origalTimeStamp dateToStringWithDateFormate:@"yyyy-MM-dd HH:mm:ss" timeZone:[NSTimeZone systemTimeZone] locale:[NSLocale systemLocale]];
    dispatch_async(dispatch_get_global_queue(DISPATCH_QUEUE_PRIORITY_DEFAULT, 0), ^{
       __block BOOL isCleanCache = NO;
        [_queue inTransaction:^(FMDatabase *db, BOOL *rollback) {
            if (![db open]) {
                 NSLog(@"Could not open database.");
            } else {
                //清空临时数据库
                NSMutableArray  *cleanCacheArray  = [[NSMutableArray alloc] init];
#if isSyncTempData
                [cleanCacheArray addObject:dbTempUpdateTmpTable];
#endif
        if ([cleanCacheArray count]) {
                    for (NSString *dbName in  cleanCacheArray) {
                        NSString *cleanCacheTempSql = [NSString stringWithFormat:@"DELETE FROM %@",dbName];
                        isCleanCache = [db executeUpdate:cleanCacheTempSql];
                        if (!isCleanCache) {
                            return;
                        }
                    }
                }
                isCleanCache = YES;
            }

        }];
        
        if (isCleanCache) {
            
            NSDictionary *dicData = [NSDictionary dictionaryWithDictionary:aData];
            __block BOOL isInsertTmpData = NO;
            __block BOOL isUpdateData = NO;
            [_queue inTransaction:^(FMDatabase *db, BOOL *rollback) {
                if (![db open]) {
                    NSLog(@"Could not open database.");
                } else {
                    //数据插入临时数据库
#if isSyncTempData
                    //temp
                    NSString *trendArrayStr= [dicData objectForKey:@"value"];
                    //NSLog(@"trendArrayStr = %@", trendArrayStr);
                    NSData* sourceData = [[NSData alloc]initWithBase64EncodedString:trendArrayStr];
                    Byte* sourceBuf;
                    sourceBuf = (Byte *)[sourceData bytes];
                    uLong sourceLen = sourceData.length;
                    NSLog(@"压缩之后大小 = %ld", sourceLen);
                    uLong desLen = sourceLen*300;
                    Byte* desBuf = malloc(desLen);
                    if(uncompress(desBuf, &desLen, sourceBuf,sourceLen) != Z_OK)
                    {
                        printf("uncompress failed!\n");
                    }
                    NSData *adata = [[NSData alloc] initWithBytes:desBuf length:desLen];
                    NSLog(@"解压之后数据大小 = %ld", adata.length);
                    NSString *aString = [[NSString alloc] initWithData:adata encoding:NSUTF8StringEncoding];
                    free(desBuf);
                    NSArray* trendArray  = [aString objectFromJSONString];
                   // NSLog(@"trendArray＝%@",trendArray);
                    if ([trendArray count]) {
                        isUpdateData = YES;
                        for (int i = 0; i < [trendArray count];i++) {
                            NSDictionary *infoDic = [trendArray objectAtIndex:i];
                            int isDeleteValue = [[infoDic objectForKey:@"is_deleted"] boolValue] ? 1 : 0;
                            
                            NSString *tempSql = [NSString stringWithFormat:
                                                 @"INSERT INTO %@ (TEMPID,FamilyID,TEMP,Part,SPO2,PR,PI,RESP,CreatedDate,RecordDate,UpdatedDate,timestamp,IsDeleted) VALUES('%@','%ld','%ld','%ld','%ld','%ld','%ld','%ld','%@','%@','%@','%@','%@')",dbTempUpdateTmpTable,
                                                 [NSString stringWithFormat:@"%@", [infoDic objectForKey:@"valueId"]],
                                                 (long)[[infoDic objectForKey:@"memberId"] integerValue],
                                                 (long)0,
                                                 (long)[[infoDic objectForKey:@"part"] integerValue],
                                                 [[infoDic objectForKey:@"SPO2"] integerValue],
                                                 [[infoDic objectForKey:@"PR"] integerValue],
                                                 [[infoDic objectForKey:@"PI"] integerValue],
                                                 [[infoDic objectForKey:@"RESP"] integerValue],
                                                 [infoDic objectForKey:@"record_date"],
                                                 [infoDic objectForKey:@"record_date"],
                                                 [infoDic objectForKey:@"record_date"],
                                                 timestampStr,
                                                 [NSString stringWithFormat:@"%d",isDeleteValue]];
                            isInsertTmpData = [db executeUpdate:tempSql];
                            //NSLog(@"isInsertTmpData = %d", isInsertTmpData);
                            if (!isInsertTmpData) {
                                *rollback = YES;
                                return ;
                            }
                        }
                    } else {
                        isInsertTmpData = YES;
                    }
                    NSLog(@"isInsertTmData = %d", isInsertTmpData);
#endif
                }
            }];
            
            if (isInsertTmpData) {
                __block BOOL isSyncData = NO;
                [_queue inTransaction:^(FMDatabase *db, BOOL *rollback) {
                    if (![db open]) {
                        NSLog(@"Could not open database.");
                    } else {
                        //同步数据
#if isSyncTempData
                        //体温
                        NSString *setPartSql = [NSString stringWithFormat:@"Part = (select Part From %@ where %@.FamilyID = %@.FamilyID and %@.TEMPID = %@.TEMPID)",dbTempUpdateTmpTable,dbTempTable,dbTempUpdateTmpTable,dbTempTable,dbTempUpdateTmpTable];
                        NSString *setTempSql = [NSString stringWithFormat:@"TEMP = (select TEMP From %@ where %@.FamilyID = %@.FamilyID and %@.TEMPID = %@.TEMPID)",dbTempUpdateTmpTable,dbTempTable,dbTempUpdateTmpTable,dbTempTable,dbTempUpdateTmpTable];
                        NSString *setSpo2Sql = [NSString stringWithFormat:@"TEMP = (select SPO2 From %@ where %@.FamilyID = %@.FamilyID and %@.TEMPID = %@.TEMPID)",dbTempUpdateTmpTable,dbTempTable,dbTempUpdateTmpTable,dbTempTable,dbTempUpdateTmpTable];
                        NSString *setPRSql = [NSString stringWithFormat:@"TEMP = (select PR From %@ where %@.FamilyID = %@.FamilyID and %@.TEMPID = %@.TEMPID)",dbTempUpdateTmpTable,dbTempTable,dbTempUpdateTmpTable,dbTempTable,dbTempUpdateTmpTable];
                        NSString *setPISql = [NSString stringWithFormat:@"TEMP = (select PI From %@ where %@.FamilyID = %@.FamilyID and %@.TEMPID = %@.TEMPID)",dbTempUpdateTmpTable,dbTempTable,dbTempUpdateTmpTable,dbTempTable,dbTempUpdateTmpTable];
                        NSString *setRESPSql = [NSString stringWithFormat:@"TEMP = (select RESP From %@ where %@.FamilyID = %@.FamilyID and %@.TEMPID = %@.TEMPID)",dbTempUpdateTmpTable,dbTempTable,dbTempUpdateTmpTable,dbTempTable,dbTempUpdateTmpTable];
                        NSString *setIsDeletedSql = [NSString stringWithFormat:@"IsDeleted = (select IsDeleted from %@ where %@.FamilyID = %@.FamilyID and %@.TEMPID = %@.TEMPID)",dbTempUpdateTmpTable,dbTempTable,dbTempUpdateTmpTable,dbTempTable,dbTempUpdateTmpTable];
                        NSString *setTimeStampSql = [NSString stringWithFormat:@"timestamp = (select timestamp from %@ where %@.FamilyID = %@.FamilyID and %@.TEMPID = %@.TEMPID)",dbTempUpdateTmpTable,dbTempTable,dbTempUpdateTmpTable,dbTempTable,dbTempUpdateTmpTable];
                        NSString *conditionFamilyIDSql = [NSString stringWithFormat:@"FamilyID = (select FamilyID from %@ where %@.FamilyID = %@.FamilyID)",dbTempUpdateTmpTable,dbTempTable,dbTempUpdateTmpTable];
                        NSString *conditionTempIDSql = [NSString stringWithFormat:@"TEMPID = (select TEMPID from %@ where %@.TEMPID = %@.TEMPID)",dbTempUpdateTmpTable,dbTempTable,dbTempUpdateTmpTable];
                        
                        NSString *updateTempSql = [NSString stringWithFormat:@"Update %@ set %@,%@,%@,%@,%@,%@,%@,%@ where %@ and %@",dbTempTable,setPartSql,setTempSql,setSpo2Sql,setPRSql,setPISql,setRESPSql,setIsDeletedSql,setTimeStampSql,conditionFamilyIDSql,conditionTempIDSql];
                        isSyncData = [db executeUpdate:updateTempSql];
                        if (!isSyncData) {
                            NSLog(@"%@---------------------------",updateTempSql);
                            *rollback = YES;
                            return;
                        }

                        
                        NSString *insertTempSql = [NSString stringWithFormat:@"Insert into %@ (TEMPID,FamilyID,Part,TEMP,SPO2,PR,PI,RESP,CreatedDate,RecordDate,UpdatedDate,timestamp,IsDeleted) select TEMPID,FamilyID,Part,TEMP,SPO2,PR,PI,RESP,CreatedDate,RecordDate,UpdatedDate,timestamp, IsDeleted from %@ as A where not exists (select TEMPID,FamilyID from %@ where FamilyID = A.FamilyID and TEMPID = A.TEMPID)",dbTempTable,dbTempUpdateTmpTable,dbTempTable];
                        isSyncData = [db executeUpdate:insertTempSql];
                        if (!isSyncData) {
                            //NSLog(@"%@",insertTempSql);
                            *rollback = YES;
                            return;
                        }
#endif
                        //更新时间戳
                        NSString *timeStampUpdatesql = [NSString stringWithFormat:@"UPDATE %@ SET ServerTimeStamp = '%ld',LocalTimeStamp = '%@' where MemberID = '%ld'",dbMemberTable,(long)server,timestampStr,(long)memberId];
                        isSyncData = [db executeUpdate:timeStampUpdatesql];
                        
                        if (!isSyncData) {
                            //NSLog(@"%@",timeStampUpdatesql);
                            *rollback = YES;
                            return;
                        }
                    }

                }];
                NSLog(@"isSyncData = %d", isSyncData);
                if (isSyncData) {
                    dispatch_async(dispatch_get_main_queue(), ^{
                        completion([NSNumber numberWithBool:YES],[NSNumber numberWithBool:isUpdateData],nil);
                    });
                } else {
                    dispatch_async(dispatch_get_main_queue(), ^{
                        NSError *error = [self getErrorWithReson:@"Update  Error" description:@"Update Sync Error"];
                        completion(nil, nil,error);
                    });
                }
            } else {
                dispatch_async(dispatch_get_main_queue(), ^{
                    NSError *error = [self getErrorWithReson:@"Insert Error" description:@"Insert Data to Temp_UpdateTmp Error!"];
                    completion(nil, nil,error);
                });
            }
        } else {
            dispatch_async(dispatch_get_main_queue(), ^{
                NSError *error = [self getErrorWithReson:@"Clean Error" description:@"Clean Cache Error!"];
                completion(nil, nil,error);
            });
        }
    });
}

- (NSError *)getErrorWithReson:(NSString *)reson description:(NSString *)desc
{
    NSArray *dataArray = [NSArray arrayWithObjects:desc,reson, nil];
    NSArray *dataKeyArray = [NSArray arrayWithObjects:NSLocalizedDescriptionKey,NSLocalizedFailureReasonErrorKey, nil];
    return [NSError errorWithDomain:@"DataBaseErrorDomain" code:79999 userInfo:[NSDictionary dictionaryWithObjects:dataArray forKeys:dataKeyArray]];
}


@end
