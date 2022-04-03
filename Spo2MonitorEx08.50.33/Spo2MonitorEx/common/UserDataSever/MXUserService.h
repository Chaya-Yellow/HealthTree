//
//  MXUserService.h
//  MediXPub
//
//  Created by Omesoft on 13-12-9.
//  Copyright (c) 2013年 Omesoft. All rights reserved.
//

#import <Foundation/Foundation.h>

static NSString * const FamilyIdDidChangedNotification = @"FamilyIdDidChangedNotification";

@class MXUser;
@interface MXUserService : NSObject

+ (id)sharedInstance;
- (NSUInteger)getCurrentLoginedMemeberId;
- (NSDictionary *)getLoginInfo;
- (MXUser *)getSelectedUser;
- (void)setFamilyId:(NSInteger)familyid;
- (BOOL)logout;
@end