//
//  OMEFamilyDataService.h
//
//
//  Created by Omesoft on 13-8-2.
//  Copyright (c) 2013年 Omesoft. All rights reserved.
//

#import "OMEDataService.h"

@interface OMEFamilyDataService : OMEDataService

@property (strong, nonatomic) NSData *photoData;

- (void)requestOMEGetFamiliesByMemberId:(long long int)idNum Key:(NSString *)key;//获取家庭成员信息
- (void)requestOMEAddFamilyByMemberId:(long long int)idNum Key:(NSString *)key json:(NSString *)json;//添加一个用户的家庭成员
- (void)requestOMEDeleteFamilyByMemberId:(long long int)idNum Key:(NSString *)key familyId:(NSInteger)familyId;//删除一个用户的家庭成员
- (void)requestOMESetFamilyByMemberId:(long long int)idNum Key:(NSString *)key familyId:(NSInteger)familyId json:(NSString *)json;//设置用户的家庭成员信息
- (void)requestOMEUploadPhotoByMemberId:(long long int)idNum Key:(NSString *)key photoData:(NSData *)data;//上传照片
@end
