//
//  MXUserDefaults.m
//  Temperature
//
//  Created by omesoft on 15/5/26.
//  Copyright (c) 2015年 Omesoft. All rights reserved.
//

#import "MXUserDefaults.h"

#define FirstStart @"firstStart"
#define UserLengthSettings @"UserLengthSettings"
#define UserWeightSettings @"UserWeightSettings"
#define UserEmailAddress @"UserEmailAddress"
#define UserAccountPwd @"UserAccountPwd"
#define UserSelectFamilyID @"UserSelectFamilyID"
static MXUserDefaults *userDefaults = nil;
@implementation MXUserDefaults


#pragma mark - Init

+ (id)sharedInstance
{
    if (!userDefaults) {
        userDefaults = [[MXUserDefaults alloc] init];
    }
    return userDefaults;
}

- (BOOL)isFirstStart
{
    return [[NSUserDefaults standardUserDefaults] boolForKey:FirstStart];
}

- (void)setFirstStart:(BOOL)isStart
{
    [[NSUserDefaults standardUserDefaults] setBool:isStart forKey:FirstStart];
    [[NSUserDefaults standardUserDefaults] synchronize];
}

- (BOOL)isCM{
    return [[NSUserDefaults standardUserDefaults] boolForKey:UserLengthSettings];
}
- (void)setCM:(BOOL)isCM{
    [[NSUserDefaults standardUserDefaults] setBool:isCM forKey:UserLengthSettings];
    [[NSUserDefaults standardUserDefaults] synchronize];
}


- (BOOL)isKG{
    return [[NSUserDefaults standardUserDefaults] boolForKey:UserWeightSettings];
}
- (void)setKG:(BOOL)isKG{
    [[NSUserDefaults standardUserDefaults] setBool:isKG forKey:UserWeightSettings];
    [[NSUserDefaults standardUserDefaults] synchronize];
}

-(NSString*)userEmailAddress
{
    return [[NSUserDefaults standardUserDefaults] stringForKey:UserEmailAddress];
}

-(void)setUserEmailAddress:(NSString*)email
{
    [[NSUserDefaults standardUserDefaults] setValue:email forKey:UserEmailAddress];
    [[NSUserDefaults standardUserDefaults] synchronize];
}

-(NSString*)userAccountPwd
{
    return [[NSUserDefaults standardUserDefaults] stringForKey:UserAccountPwd];
}

-(void)setUserAccountPwd:(NSString*)pwd
{
    [[NSUserDefaults standardUserDefaults] setValue:pwd forKey:UserAccountPwd];
    [[NSUserDefaults standardUserDefaults] synchronize];
}

-(NSInteger)getSelectedFamilyID
{
    return [[NSUserDefaults standardUserDefaults] integerForKey:UserSelectFamilyID];
}

-(void)setSelectedFamilyID:(NSInteger)familyID
{
    [[NSUserDefaults standardUserDefaults] setInteger:familyID forKey:UserSelectFamilyID];
    [[NSUserDefaults standardUserDefaults] synchronize];
}

#pragma mark - Private Methods

- (void)integerValue:(NSInteger)value key:(NSString *)key
{
    if (key == nil || key.length <= 0) {
        return;
    }
    NSUserDefaults *userdefaults = [NSUserDefaults standardUserDefaults];
    [userdefaults setInteger:value forKey:key];
    [userdefaults synchronize];
}

- (NSInteger)integerValueWithKey:(NSString *)key
{
    if (key == nil || key.length <= 0) {
        return 0;
    }
    return [[NSUserDefaults standardUserDefaults] integerForKey:key];
}

- (void)boolValue:(BOOL)value key:(NSString *)key
{
    if (key == nil || key.length <= 0) {
        return;
    }
    NSUserDefaults *userdefaults = [NSUserDefaults standardUserDefaults];
    [userdefaults setBool:value forKey:key];
    [userdefaults synchronize];
}

- (BOOL)boolValueWithKey:(NSString *)key
{
    if (key == nil || key.length <= 0) {
        return 0;
    }
    return [[NSUserDefaults standardUserDefaults] boolForKey:key];
}

@end
