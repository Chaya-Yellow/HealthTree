//
//  OMELoginDataService.h
//
//
//  Created by sincan on 14-10-15.
//  Copyright (c) 2014年 Omesoft. All rights reserved.
//

#import "OMEDataService.h"


@interface OMELoginDataService : OMEDataService

- (void)requestOMELogoutWithMemberId:(NSInteger)idNum Key:(NSString *)key;//注销

/*
  邮箱登陆 特用
  邮箱登陆若没哟注册则会发送Email！
 */
- (void)requestOMELogin3WithName:(NSString *)name pwd:(NSString *)pwd appId:(NSInteger)appid language:(NSString *)lan;
@end
