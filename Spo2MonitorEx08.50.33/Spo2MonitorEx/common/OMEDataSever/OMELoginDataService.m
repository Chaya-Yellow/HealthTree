//
//  OMELoginDataService.m
//  
//
//  Created by sincan on 14-10-15.
//  Copyright (c) 2014年 Omesoft. All rights reserved.
//

#import "OMELoginDataService.h"
#import "AFNetworking.h"
#import "OMEDataServiceError.h"
#import "AFNetworkReachabilityManager.h"
#import "OMESoft.h"
#import "consDefine.h"
#import "Constants.h"

@implementation OMELoginDataService

#pragma mark - Public Methods

- (void)requestOMELogoutWithMemberId:(NSInteger)idNum Key:(NSString *)key
{
    NSDictionary* dic = [NSDictionary dictionaryWithObjectsAndKeys:@"Logout",@"key",
                         @(idNum),@"accountId",
                         key,@"clientKey",
                         nil];
    NSString *postString = [NSString jsonStringWithDictionary:dic];
    [self post:[NSMutableString stringWithString:postString] key:@"Logout"];
}

- (void)requestOMELogin3WithName:(NSString *)name pwd:(NSString *)pwd appId:(NSInteger)appid language:(NSString *)lan
{
    //NSLog(@"****");
    dispatch_async(dispatch_get_global_queue(DISPATCH_QUEUE_PRIORITY_DEFAULT, 0), ^{
        NSDictionary* dic = [NSDictionary dictionaryWithObjectsAndKeys:@"Login",@"key",
                             name,@"account",
                             [self ipAdress],@"loginIp",
                             @(ENGLISH),@"language",
                             [NSString md5:pwd],@"pwd",
                             @(OMEAPPID), @"appId",
                             nil];
        NSString* postString =[NSString jsonStringWithDictionary:dic];
        dispatch_async(dispatch_get_main_queue(), ^{
            [self post:[NSMutableString stringWithString:postString] key:@"Login3"];
        });
    });
}
@end
