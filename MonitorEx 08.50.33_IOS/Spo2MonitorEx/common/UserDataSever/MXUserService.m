//
//  MXUserService.m
//  MediXPub
//
//  Created by Omesoft on 13-12-9.
//  Copyright (c) 2013年 Omesoft. All rights reserved.
//

#import "MXUserService.h"
#import "MXUserData.h"
#import "MXUser.h"
#import "OMELoginDataService.h"
#import "MXUserDefaults.h"
//test
#import "OMESoft.h"

static MXUserService *userService = nil;

@interface MXUserService ()
@property (assign, nonatomic) NSInteger userFamilyId;
@property (assign, nonatomic) NSInteger memberId;
@property (strong, nonatomic) MXUserData *userData;
@end
@implementation MXUserService

+ (id)sharedInstance
{
    if (!userService) {
        userService = [[MXUserService alloc] init];
    }
    return userService;
}

- (NSUInteger)getCurrentLoginedMemeberId
{
    if (self.memberId) {
        return self.memberId;
    }
    NSArray *loginedArray = [self.userData getLoginInfo];
    
    if ([loginedArray count] == 1) {
        self.memberId = [[[loginedArray objectAtIndex:0] objectForKey:@"id"] integerValue];
        return self.memberId;
    } else {
        return 0;
    }
}

- (NSDictionary *)getLoginInfo
{
    NSArray *loginedArray = [self.userData getLoginInfo];
    
    if ([loginedArray count]) {
        return [loginedArray objectAtIndex:0];
    } else {
        return nil;
    }
}

- (MXUser *)getSelectedUser
{
    NSInteger idNum = [self getCurrentLoginedMemeberId];
    if (idNum) {
        //保存到本地数据
        self.userFamilyId = [[MXUserDefaults sharedInstance]getSelectedFamilyID];
        MXUser *user = [self.userData getUserWithFamilyId:self.userFamilyId];
        NSLog(@"userFamilyID= %ld", self.userFamilyId);
        if (self.userFamilyId <= 0 || !user) {
            NSArray *userArray = [self.userData getAllUserWithMemberId:idNum];
            if ([userArray count]) {
                user = [userArray objectAtIndex:0];
                [self setFamilyId:user.familyId];
            } else {
                return nil;
            }
        }
        return user;
    } else {
        NSLog(@"idum =======00000");
        return nil;
    }
}

- (void)setFamilyId:(NSInteger)familyId
{
    [[MXUserDefaults sharedInstance]setSelectedFamilyID:familyId];
    self.userFamilyId = familyId;
}

- (BOOL)logout
{
    
    NSArray *loginedArray = [self.userData getLoginInfo];
    BOOL result;
    if ([loginedArray count] ) {
        //NSInteger idNum = [[[loginedArray objectAtIndex:0] objectForKey:@"id"] integerValue];
        //NSString *key = [[loginedArray objectAtIndex:0] objectForKey:@"clientKey"];
        //OMELoginDataService *dataService = [[OMELoginDataService alloc] init];
        //[dataService requestOMELogoutWithMemberId:idNum Key:key];
        result = [self.userData logoutAccount];
    } else {
        result = YES;
    }
    if (result) {
        self.userFamilyId = 0;
        [[MXUserDefaults sharedInstance]setSelectedFamilyID:self.userFamilyId];
        self.memberId = 0;
    }
    return result;
}

- (MXUserData *)userData
{
    if (!_userData) {
        _userData = [[MXUserData alloc] init];
    }
    return _userData;
}
@end
