//
//  MXUserDefaults.h
//  Temperature
//
//  Created by omesoft on 15/5/26.
//  Copyright (c) 2015年 Omesoft. All rights reserved.
//

#import <Foundation/Foundation.h>

typedef NS_ENUM(NSInteger, TempDeviceModel) {
    TempDeviceModelNone,
    TempDeviceModelBLT,
    TempDeviceModelYuyue,
    TempDeviceModelQue
};

@interface MXUserDefaults : NSObject

+ (id)sharedInstance;

/*程序第一次打开*/
- (BOOL)isFirstStart;//判断是否是第一次打开
- (void)setFirstStart:(BOOL)isStart;//设置第一次打开设置
/*设置长度单位*/
- (BOOL)isCM;
- (void)setCM:(BOOL)isCM;
/*设置重量单位*/
- (BOOL)isKG;
- (void)setKG:(BOOL)isKG;
//邮箱地址
-(NSString*)userEmailAddress;
-(void)setUserEmailAddress:(NSString*)email;
//登陆密码MD5
-(NSString*)userAccountPwd;
-(void)setUserAccountPwd:(NSString*)pwd;
//
-(NSInteger)getSelectedFamilyID;
-(void)setSelectedFamilyID:(NSInteger)familyID;
@end
