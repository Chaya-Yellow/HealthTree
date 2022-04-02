//
//  OMEVerifyRegisterDataService.m
//
//
//  Created by sincan on 14-10-15.
//  Copyright (c) 2014年 Omesoft. All rights reserved.
//

#import "OMEVerifyRegisterDataService.h"
#import "OMESoft.h"
#import "Constants.h"
@implementation OMEVerifyRegisterDataService

#pragma mark - Public Methods

- (void)requestGetSMSVerifyCodeForRegisterWithPhoneNum:(NSString *)phoneNum appId:(NSInteger)appid
{
    dispatch_async(dispatch_get_global_queue(DISPATCH_QUEUE_PRIORITY_DEFAULT, 0), ^{
        NSString *postString = [NSString stringWithFormat:@"<phoneNumber>%@</phoneNumber>\n"
                                "<ip>%@</ip>\n"
                                "<appId>%ld</appId>\n",phoneNum,[self ipAdress],(long)appid];
        dispatch_async(dispatch_get_main_queue(), ^{
            [self post:[NSMutableString stringWithString:postString] key:@"GetSMSVerifyCodeForRegister2"];
        });
    });
    
    
}

- (void)requestCheckSMSVerifyCodeForRegisterWithPhoneNum:(NSString *)phoneNum code:(int)code
{
    NSString *postString = [NSString stringWithFormat:@"<phoneNumber>%@</phoneNumber>\n"
                            "<verifyCode>%d</verifyCode>\n",phoneNum,code];
    [self post:[NSMutableString stringWithString:postString] key:@"CheckSMSVerifyCodeForRegister"];
}

- (void)requestGetSMSVerifyCodeForForgotPWDWithPhone:(NSString *)phone appId:(NSInteger)appid
{
    dispatch_async(dispatch_get_global_queue(DISPATCH_QUEUE_PRIORITY_DEFAULT, 0), ^{
        NSString *postString = [NSString stringWithFormat:@"<phoneNumber>%@</phoneNumber>\n"
                                "<ip>%@</ip>\n"
                                "<appId>%ld</appId>\n",phone,[self ipAdress],(long)appid];
        dispatch_async(dispatch_get_main_queue(), ^{
            [self post:[NSMutableString stringWithString:postString] key:@"GetSMSVerifyCodeForForgotPWD2"];
        });
    });
    
    
}

- (void)requestCheckSMSVerifyCodeForForgotPWDWithPhone:(NSString *)phone code:(int)code
{
    NSString *postString = [NSString stringWithFormat:@"<phoneNumber>%@</phoneNumber>\n"
                            "<verifyCode>%d</verifyCode>\n",phone,code];
    [self post:[NSMutableString stringWithString:postString] key:@"CheckSMSVerifyCodeForForgotPWD"];
}

- (void)requestChangePasswordForForgotPWDWithPwd:(NSString *)newpwd code:(int)code phone:(NSString *)phone appId:(NSInteger)appid
{
    NSString *postString = [NSString stringWithFormat:@"<phoneNumber>%@</phoneNumber>\n"
                            "<verifyCode>%d</verifyCode>\n"
                            "<newPassword>%@</newPassword>\n"
                            "<appId>%ld</appId>\n",phone,code,newpwd,(long)appid];
    [self post:[NSMutableString stringWithString:postString] key:@"ChangePasswordForForgotPWD2"];
}

- (void)requestOMEChangePasswordWithKey:(NSString *)key oldPwd:(NSString *)pwd newPwd:(NSString *)newpwd memberId:(int)idNum
{
    NSDictionary* dic = [NSDictionary dictionaryWithObjectsAndKeys:@"ChangePwd",@"key",
                         @(idNum),@"accountId",
                         key,@"clientKey",
                         newpwd,@"newPwd",
                         pwd,@"oldPwd",
                         nil];
    NSString *postString = [NSString jsonStringWithDictionary:dic];
    [self post:[NSMutableString stringWithString:postString] key:@"ChangePwd"];
}

- (void)requestOMEResetPasswordByEmailWithEmail:(NSString *)email appId:(NSInteger)appid language:(NSString *)lan
{
    NSDictionary* dic = [NSDictionary dictionaryWithObjectsAndKeys:@"ResetPwdByEmail",@"key",
                         @(appid),@"appId",
                         @(ENGLISH),@"language",
                         email,@"email",
                         nil];
    NSString *postString = [NSString jsonStringWithDictionary:dic];
    [self post:[NSMutableString stringWithString:postString] key:@"ResetPwdByEmail"];
}

- (void)requestRegisterWithJson:(NSString *)json
{
    NSString *postString = [NSString stringWithFormat:@"%@",json];
    [self post:[NSMutableString stringWithString:postString] key:@"Register2"];
}

@end



