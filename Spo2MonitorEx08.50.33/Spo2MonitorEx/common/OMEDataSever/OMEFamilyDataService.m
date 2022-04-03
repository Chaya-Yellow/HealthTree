//
//  OMEFamilyDataService.m
//
//
//  Created by sincan on 14-10-15.
//  Copyright (c) 2014年 Omesoft. All rights reserved.
//

#import "OMEFamilyDataService.h"
#import "NSDataEx.h"
#import "OMESoft.h"
@implementation OMEFamilyDataService

#pragma mark - Public Methods

- (void)requestOMEGetFamiliesByMemberId:(long long int)idNum Key:(NSString *)key
{
    NSDictionary* dic = [NSDictionary dictionaryWithObjectsAndKeys:@"GetFamiliesByAccountID",@"key",
                         @(idNum),@"accountId",
                         key,@"clientKey",
                         nil];
    NSString *postString = [NSString jsonStringWithDictionary:dic];
    [self post:postString key:@"GetFamiliesByMemberId"];
}

- (void)requestOMEAddFamilyByMemberId:(long long int)idNum Key:(NSString *)key json:(NSString *)json
{
    [self post:[NSMutableString stringWithString:json] key:@"AddFamily"];
}

- (void)requestOMEDeleteFamilyByMemberId:(long long int)idNum Key:(NSString *)key familyId:(NSInteger)familyId
{
    NSDictionary* dic = [NSDictionary dictionaryWithObjectsAndKeys:@"DeleteFamMem",@"key",
                         @(familyId),@"memberId",
                         key,@"clientKey",
                         @(idNum),@"accountId",
                         nil];
    NSString *postString = [NSString jsonStringWithDictionary:dic];
    
    [self post:[NSMutableString stringWithString:postString] key:@"DeleteFamMem"];
}

- (void)requestOMESetFamilyByMemberId:(long long int)idNum Key:(NSString *)key familyId:(NSInteger)familyId json:(NSString *)json
{
    [self post:[NSMutableString stringWithString:json] key:@"UpdateMemInfo"];
}

- (void)requestOMEUploadPhotoByMemberId:(long long int)idNum Key:(NSString *)key photoData:(NSData *)data
{
    NSString *postString = [NSString stringWithFormat:@"<memberId>%ld</memberId>\n"
                            "<clientKey>%@</clientKey>\n"
                            "<photoStream>%@</photoStream>\n",(long)idNum,key,[data base64Encoding]];
    [self post:[NSMutableString stringWithString:postString] key:@"UploadPhoto"];
}
@end
