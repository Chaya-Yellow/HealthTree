//
//  OMEVerifyRegisterDataService.h
//
//
//  Created by sincan on 14-10-15.
//  Copyright (c) 2014年 Omesoft. All rights reserved.
//

#import "OMEDataService.h"

@interface OMEVerifyRegisterDataService : OMEDataService

/** @brief 获取短信注册验证码
 *  @param phoneNum 电话号码
 *  @param appid  AppID
 */
- (void)requestGetSMSVerifyCodeForRegisterWithPhoneNum:(NSString *)phoneNum appId:(NSInteger)appid;

/** @brief 校验短信注册验证码
 *  @param phoneNum 电话号码
 *  @param code  验证码
 */
- (void)requestCheckSMSVerifyCodeForRegisterWithPhoneNum:(NSString *)phoneNum code:(int)code;

/** @brief 获取找回密码时短信验证码
 *  @param phoneNum 电话号码
 *  @param appid  AppID
 */
- (void)requestGetSMSVerifyCodeForForgotPWDWithPhone:(NSString *)phone appId:(NSInteger)appid;

/** @brief 校验找回密码时短信验证码
 *  @param phoneNum 电话号码
 *  @param code  验证码
 */
- (void)requestCheckSMSVerifyCodeForForgotPWDWithPhone:(NSString *)phone code:(int)code;

/** @brief 使用用户名、手机号、邮箱注册新用户
 *  @param json Json字符串
 */
- (void)requestRegisterWithJson:(NSString *)json;

- (void)requestChangePasswordForForgotPWDWithPwd:(NSString *)newpwd code:(int)code phone:(NSString *)phone appId:(NSInteger)appid;

/*Email 修改密码 (网页修改)*/
- (void)requestOMEResetPasswordByEmailWithEmail:(NSString *)email appId:(NSInteger)appid language:(NSString *)lan;

/*登录后直接 修改密码*/
- (void)requestOMEChangePasswordWithKey:(NSString *)key oldPwd:(NSString *)pwd newPwd:(NSString *)newpwd memberId:(int)idNum;//修改密码

@end
