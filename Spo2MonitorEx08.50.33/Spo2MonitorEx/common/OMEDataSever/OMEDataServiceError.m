//
//  OMEDataServiceError.m
//
//
//  Created by sincan on 14-10-14.
//  Copyright (c) 2013年 Omesoft. All rights reserved.
//

#import "OMEDataServiceError.h"

@implementation OMEDataServiceError
+ (BOOL)isDatsServiceValidWithData:(NSDictionary *)resultDic error:(NSError **)aError
{
//    NSDictionary *resultDic = [NSJSONSerialization JSONObjectWithData:[aString dataUsingEncoding:NSUTF8StringEncoding] options:NSJSONReadingMutableContainers error:nil];
    //NSLog(@"111111111resultDickies= %@", resultDic);
    //NSLog(@"resultDic = %@", resultDic);
    NSString *codeStr = [resultDic objectForKey:@"error_code"];
    if (!codeStr || !resultDic) {
        //NSLog(@"22222222");
        NSString *dataDescription = NSLocalizedString(@"Alert_Access", @"获取数据出现异常。");
        NSString *dataReson = NSLocalizedString(@"Alert_Data", @"数据异常");
        NSArray *dataArray = [NSArray arrayWithObjects:dataDescription,dataReson, nil];
        NSArray *dataKeyArray = [NSArray arrayWithObjects:NSLocalizedDescriptionKey,NSLocalizedFailureReasonErrorKey, nil];
        NSDictionary *dic = [NSDictionary dictionaryWithObjects:dataArray forKeys:dataKeyArray];
        if (aError) {
            *aError = [NSError errorWithDomain:GetValidationServiceDataErrorDomain code:88889 userInfo:dic];
        }
        return NO;
    } else {
        NSInteger code = [codeStr integerValue];
        NSLog(@"code = %d", code);
        if (code) {
            NSString* description = NSLocalizedString(@"Alert_Unknown", @"未知异常");
            NSString* reson = NSLocalizedString(@"Alert_Incorrectly", @"输入有误");
            switch (code) {
                case 401: //未知的异常
                    description = NSLocalizedString(@"Alert_Unknown", @"未知的异常。");
                    break;
                case 402: //账户已经被注册，请更换其他账户名称
                    description = NSLocalizedString(@"Alert_Replace", @"账户已经被注册，请更换其他账户名称。");
                    break;
                case 403: //账户非法，账户被禁止使用
                    description = NSLocalizedString(@"Alert_Banned", @"账户非法，账户被禁止使用。");
                    break;
                case 404: //账户不存在
                    description = NSLocalizedString(@"Alert_Exist", @"账户不存在。");
                    break;
                case 405: //用户名或密码错误
                    description = NSLocalizedString(@"Alert_BadPassword", @"用户名或密码错误。");
                    break;
                case 406: //没有权限
                    description = NSLocalizedString(@"Alert_Unable", @"无法获取权限。");
                    break;
                case 407: //命中脏字，昵称或者介绍中存在脏字，请检查和替换
                    description = NSLocalizedString(@"Alert_Nickname", @"命中脏字，昵称或者介绍中存在脏字，请检查和替换。");
                    break;
                case 408: //邮箱已经被注册
                    description = NSLocalizedString(@"Alert_Registered", @"邮箱已经被注册。");
                    break;
                case 409: //解析Json出错
                    description = NSLocalizedString(@"Alert_Analytical", @"解析出错。");
                    break;
                case 410:
                    description = NSLocalizedString(@"Alert_Wrong", @"邮箱不对或不存在。");
                    break;
                case 411:
                    description = NSLocalizedString(@"Alert_Nonexistent", @"数据不存在。");
                    break;
                case 412://DeleteFamily 不是此用户的家庭成员。
                    description = NSLocalizedString(@"Alert_Family", @"不是此用户的家庭成员。");
                    break;
                case 413:
                    description = NSLocalizedString(@"Alert_Synchronization", @"同步已被锁住。");
                    break;
                case 414:
                    description = NSLocalizedString(@"Alert_Occured", @"解析或更新数据时发生异常。");
                    break;
                case 415://数据库异常
                    description = NSLocalizedString(@"Alert_Database", @"数据库异常。") ;
                    break;
                case 416:
                    description = NSLocalizedString(@"Alert_Pictures", @"不是此会员上传的图片");
                    break;
                case 417:
                    description = NSLocalizedString(@"Alert_haveRegister", @"该手机号已注册，请直接登录");
                    break;
                case 418://发送短信验证码失败
                    description = NSLocalizedString(@"Alert_SendSMSFailed", @"发送短信验证码失败。");
                    break;
                case 419://生成短信验证码失败
                    description = NSLocalizedString(@"Alert_GenerationFailed", @"生成短信验证码失败。");
                    break;
                case 420:
                    description = NSLocalizedString(@"Alert_PhoneNotRegistered", @"手机号尚未注册。");
                    break;
                case 421:
                    description = @"绑定设备失败，不存在这种设备";
                    break;
                case 422:
                    description = @"绑定设备失败，我已绑定了此设备";
                    break;
                case 105:
                    description = @"绑定设备失败，设备已被其他用户绑定，请先解除绑定";
                    break;
                case 423:
                    description = @"解除设备绑定失败，尚未绑定此设备";
                    break;
                case 425:
                    description = NSLocalizedString(@"Alert_Identification", @"ID错误");
                    break;
                case 424:
                    description = NSLocalizedString(@"Alert_Display", @"显示数量不在正确的范围之内。");
                    break;
                case 426:
                    description = NSLocalizedString(@"Alert_Page", @"页码出错。");
                    break;
                case 427:
                    description = NSLocalizedString(@"Alert_Sex", @"性别错误");
                    break;
                case 428:
                    description = NSLocalizedString(@"Alert_String", @"字符串错误，字符串不能为空");
                    break;
                case 429://不是正确的Email
                    description = NSLocalizedString(@"Alert_Filled", @"Email填写错误。");
                    break;
                case 430://不是正确的IP
                    description = NSLocalizedString(@"Alert_Resolution", @"地址解析错误。");
                    break;
                case 431://参数不能为空
                    description = NSLocalizedString(@"Alert_InformationError", @"信息异常错误。");
                    break;
                case 432://不是正确的手机号码
                    description = NSLocalizedString(@"Alert_IncorrectPhone", @"手机号码有误。");
                    break;
                case 433://此手机号请求激活码过于频繁
                    description = NSLocalizedString(@"Alert_RequestFrequently", @"请求过于频繁。");
                    break;
                case 434://验证码错误
                    description = NSLocalizedString(@"Alert_CaptchaError", @"验证码错误。");
                    break;
                case 435://验证码过期
                    description = NSLocalizedString(@"Alert_CaptchaDue", @"验证码过期。");
                    break;
                case 436://密码不符合要求
                    description = NSLocalizedString(@"Alert_PwdNotRequirment", @"密码不符合要求");
                    break;
                case 437://不是正确的短信验证码类型
                    description = NSLocalizedString(@"Alert_SMSTypeNotCorrent", @"短信验证码类型不是正确。");
                    break;
                case 438:
                    description = NSLocalizedString(@"Alert_HasLogined", @"账号已经登录");
                    break;
                default:
                    
                    break;
            }
            NSArray *objArray = [NSArray arrayWithObjects:description,reson, nil];
            NSArray *keyArray = [NSArray arrayWithObjects:NSLocalizedDescriptionKey,NSLocalizedFailureReasonErrorKey, nil];
            NSDictionary *dic = [NSDictionary dictionaryWithObjects:objArray forKeys:keyArray];
           //NSLog(@"objArray = %@, keyArray = %@, dic = %@", objArray, keyArray, dic);
            if (aError) {
             //   NSLog(@"+++++++++++++++++++++");
                *aError = [NSError errorWithDomain:GetValidationServiceDataErrorDomain code:code userInfo:dic];
            }
            return NO;
        } else {
            return YES;
        }
    }
}
@end
