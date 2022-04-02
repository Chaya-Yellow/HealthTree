//
//  OMEDataBase.h
//
//
//  Created by sincan on 14-10-29.
//  Copyright (c) 2014年 Xin. All rights reserved.
//

#import <Foundation/Foundation.h>
#import "OMEConfigure.h"
#import "FMDatabaseQueue.h"
#import "FMDatabase.h"
#import "FMDatabaseAdditions.h"

typedef void (^OMEDBCompletion) (id object,NSError *error);
typedef void (^OMEDBSyncCompletion) (id object,id isUpdateData,NSError *error);

@class OMEUser;
@interface OMEDataBase : NSObject

@property (nonatomic, strong) FMDatabaseQueue *queue;
/*MEMBER*/
- (BOOL)loginAccountWithId:(long long int)memberid key:(NSString *)key;
- (BOOL)logoutAccount;
- (NSMutableArray *)getLoginInfo;
- (BOOL)updateTimeStampWithId:(NSInteger)memberId serverTimeStamp:(NSInteger)server localTimeStamp:(NSDate *)local;

/*USER*/
- (BOOL)syncUserData:(NSArray *)familys memberId:(NSUInteger)mId;

/*SYNC*/
- (void)getSyncDataWithId:(NSInteger)memberId timeStamp:(NSDate *)timestamp completion:(OMEDBCompletion)completion;
- (void)updateSyncData:(NSDictionary *)aData timeStamp:(NSDate*)origalTimeStamp memberId:(NSUInteger)memberId serverTimeStamp:(NSInteger)server completion:(OMEDBSyncCompletion)completion;

/*Common*/
- (BOOL)dbQueueExecuteUpdateSQL:(NSString *)sql;
- (NSInteger)numberOfFirstColumnAtSQL:(NSString *)sql;
@end
