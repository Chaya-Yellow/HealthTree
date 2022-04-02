
//
//  OMESyncManager.m
//  
//
//  Created by sincan on 14-10-29.
//  Copyright (c) 2014年 Xin. All rights reserved.
//

#import "OMESyncManager.h"
#import "OMEFamilyDataService.h"
#import "AFNetworkReachabilityManager.h"
#import "OMESoft.h"
#import "OMEDataBase.h"
#import "OMESyncDataService.h"
#import "Constants.h"
//

static OMESyncManager *manager = nil;
@interface OMESyncManager () <OMEDataServiceDelegate>
@property (assign, nonatomic) BOOL isSync;
@property (retain, nonatomic) NSDate *syncDateTime;
@property (retain, nonatomic) NSTimer *syncTimer;
@property (strong, nonatomic) OMEFamilyDataService *familyDataService;
@property (strong, nonatomic) OMESyncDataService *syncDataService;
@property (strong, nonatomic) OMEDataBase *userData;
@property (strong, nonatomic) NSDictionary *loginInfoDic;
@end

@implementation OMESyncManager

- (void)dealloc
{
    [_familyDataService cancel];
    _familyDataService.delegate = nil;
    
    [_syncDataService cancel];
    _syncDataService = nil;
}
+(id)sharedInstance
{
    if (!manager) {
        manager = [[OMESyncManager alloc] init];
    }
    return manager;
}

- (void)startSync
{
    NSLog(@"syncStart");
    if (![self isConnnected]) {
        NSLog(@"do not connected....");
        return;
    }
    NSArray *loginedArray = [self.userData getLoginInfo];
    BOOL comfirmTimeStamp = YES;
    if ([loginedArray count] == 1) {
        NSDictionary *data = [loginedArray objectAtIndex:0];
        NSString *localTimeStamp = [data objectForKey:@"localTimeStamp"];
        NSDate *localTime = [localTimeStamp stringWihtDateFormat:@"yyyy-MM-dd HH:mm:ss" timeZone:[NSTimeZone systemTimeZone] locale:[NSLocale systemLocale]];
        if ([localTime compare:[NSDate date]] == NSOrderedDescending) {
            //            comfirmTimeStamp = [userData timestampDidChanged:[NSDate date] origal:localTime];
        }
    }
    if (comfirmTimeStamp) {
        if (!self.isSync) {
            if ([loginedArray count] == 1) {
                self.loginInfoDic = [loginedArray objectAtIndex:0];
                NSInteger idNum = [[self.loginInfoDic objectForKey:@"id"] intValue];
                NSString *clientKey = [self.loginInfoDic objectForKey:@"clientKey"];
                //NSLog(@"11111111111111111");
                [self.familyDataService requestOMEGetFamiliesByMemberId:idNum Key:clientKey];
                self.isSync = YES;
                self.syncDateTime = [NSDate date];
            }
        }
    }
}

- (void)stopSync
{
    [_familyDataService cancel];
    [_syncDataService cancel];
    if (_syncTimer) {
        [_syncTimer invalidate];
        _syncTimer = nil;
    }
}

#pragma mark - Private Method
- (BOOL)isConnnected
{
    AFNetworkReachabilityManager *r = [AFNetworkReachabilityManager sharedManager];
    if ([r networkReachabilityStatus] == AFNetworkReachabilityStatusNotReachable) {
        return NO;
    } else {
        return YES;
    }
}

#pragma mark - DataService
- (void)dataService:(OMEDataService *)dataService didFinishWithData:(NSDictionary *)dataDic
{
    //NSLog(@"-----------------------------dataDic = %@", dataDic);
    if (dataService == _familyDataService) {
        NSDictionary *data = [dataDic objectForKey:@"data"];
        NSArray *familiesData = [data objectForKey:@"families"];
        if ([familiesData count]) {
            //setFamily
            NSInteger idNum = [[self.loginInfoDic objectForKey:@"id"] intValue];
            BOOL isInsert =[self.userData syncUserData:familiesData memberId:idNum];
            if (isInsert) {
                [[NSNotificationCenter defaultCenter] postNotificationName:familyDidUpdatedNotification object:nil];
                NSDate *date = [[self.loginInfoDic objectForKey:@"localTimeStamp"] stringWihtDateFormat:@"yyyy-MM-dd HH:mm:ss"];
                NSUInteger timestamp = [[self.loginInfoDic objectForKey:@"serverTimeStamp"] integerValue];
                NSString *key = [self.loginInfoDic objectForKey:@"clientKey"];
                [self.userData getSyncDataWithId:idNum timeStamp:date completion:^(id object, NSError *error) {
                    NSMutableDictionary *dic = (NSMutableDictionary *)object;
                     [dic setObject:@"SyncData" forKey:@"key"];
                     [dic setObject:key forKey:@"clientKey"];
                    [dic setObject:@(idNum) forKey:@"accountId"];
                    [dic setObject:[NSString stringWithFormat:@"%ld",(long)timestamp] forKey:@"timestamp"];
                    NSString *json = [NSString jsonStringWithDictionary:dic];
                    //NSLog(@"-------------------->>>>>>>>>>>>>>>>>>>>>timestamp =%@", dic);
                    [_syncDataService cancel];
                    //NSLog(@"syncDataService start");
                    [self.syncDataService requestOMESyncByMemberId:idNum Key:key json:json];
                    self.isSync = YES;
                }];
                return;
            }
        }
        self.isSync = NO;
    } else if (dataService == _syncDataService) {
        //NSDictionary *data = [dataDic objectForKey:@"data"];
        NSDictionary *data = [dataDic objectForKey:@"data"];
        //NSLog(@"服务器返回历史趋势数据......data=%@", data);
        NSInteger serverTimeStamp = [[data objectForKey:@"timestamp"] longValue];
        NSInteger idNum = [[self.loginInfoDic objectForKey:@"id"] intValue];
        //NSLog(@"begin update-----------------------------------------------------------**************************");
        [self.userData updateSyncData:data timeStamp:self.syncDateTime memberId:idNum serverTimeStamp:serverTimeStamp completion:^(id object, id isUpdateData, NSError *error) {
            BOOL isInsert = [(NSNumber *)object boolValue];
            BOOL isUpdate = [(NSNumber *)isUpdateData boolValue];
            NSLog(@"isInsert = %i, isUpdate=%i", isInsert, isUpdate);
            if (isInsert && isUpdate) {
                [[NSNotificationCenter defaultCenter] postNotificationName:syncDidUpdatedNotification object:nil];
            }
            if (!isInsert) {
                NSLog(@"%@",error);
            }
            NSLog(@"finish");
            self.isSync = NO;
        }];
        self.isSync = NO;
    }
}

- (void)dataService:(OMEDataService *)dataService didFailWithError:(NSError *)error
{
    NSLog(@"FILE=%s error = %ld",__FILE__, error.code);
    self.isSync = NO;
}

#pragma mark - Get Methods

- (OMESyncDataService *)syncDataService
{
    if (!_syncDataService) {
        _syncDataService = [[OMESyncDataService alloc] init];
        _syncDataService.delegate = self;
    }
    return _syncDataService;
}

- (OMEFamilyDataService *)familyDataService
{
    if (!_familyDataService) {
        _familyDataService = [[OMEFamilyDataService alloc] init];
        _familyDataService.delegate = self;
    }
    return _familyDataService;
}

- (OMEDataBase *)userData
{
    if (!_userData) {
        _userData = [[OMEDataBase alloc] init];
    }
    return _userData;
}

#pragma mark - Set Methods
- (void)setIsSync:(BOOL)sync
{
    _isSync = sync;
    if (_syncTimer) {
        [_syncTimer invalidate];
        _syncTimer = nil;
    }
    if (!_isSync) {
        //定时10 MIN 同步一次数据 10*60 调记录
        dispatch_async(dispatch_get_global_queue(DISPATCH_QUEUE_PRIORITY_DEFAULT, 0), ^{
            _syncTimer = [NSTimer scheduledTimerWithTimeInterval:10*60 target:self selector:@selector(startSync) userInfo:nil repeats:NO];
            [[NSRunLoop currentRunLoop] addTimer:_syncTimer forMode:NSDefaultRunLoopMode];
            [[NSRunLoop currentRunLoop] run];
        });
    }
}

- (void)startSyncDelay
{
    self.isSync = _isSync;
}
@end
