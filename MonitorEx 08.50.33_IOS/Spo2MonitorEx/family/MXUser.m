//
//  MXUser.m
//  BLT-Spo2Monitor
//
//  Created by luteng on 16/7/4.
//  Copyright © 2016年 luteng. All rights reserved.
//

#import "MXUser.h"
#import "NSDate_Omesoft.h"
#import "MXUserDefaults.h"
@implementation MXUser
- (void)dealloc
{
    name = nil;
    imageName = nil;
    birthday = nil;
}

- (NSInteger)age
{
    if (age) {
        return age;
    } else {
        if (self.birthday) {
            return [self.birthday birthdayAge];
        }
    }
    return 0;
}

- (NSString *)baseInfo
{
    BOOL iskg = [[MXUserDefaults sharedInstance] isKG];
    BOOL iscm = [[MXUserDefaults sharedInstance] isCM];
    NSString *ageStr = [NSString stringWithFormat:@"%ld%@",(long)self.age,NSLocalizedString(@"Family_Age", @"岁")];
    NSString *heightStr = self.height > 0 ? (iscm ? [NSString stringWithFormat:@"  %0.f%@",self.height, NSLocalizedString(@"Height_Unit_CM", @"CM")] : [NSString stringWithFormat:@"  %0.f%@",self.height/2.54,NSLocalizedString(@"Height_Unit_IN", @"in") ]) : @"";
    NSString *weightStr = self.weight > 0 ? (iskg ? [NSString stringWithFormat:@"  %0.f%@",self.weight, NSLocalizedString(@"Weight_Unit_KG", @"KG")] : [NSString stringWithFormat:@"  %0.f%@",self.weight/0.4535924,NSLocalizedString(@"Weight_Unit_LB",@"LB")]) : @"";
    return [NSString stringWithFormat:@"%@%@%@",ageStr,heightStr,weightStr];
}

@end
