//
//  MXUser.h
//  BLT-Spo2Monitor
//
//  Created by luteng on 16/7/4.
//  Copyright © 2016年 luteng. All rights reserved.
//

#import <Foundation/Foundation.h>

@interface MXUser : NSObject
{
    NSString *name;
    bool gender; //1:男；0：女
    float height;
    NSString *imageName;
    NSInteger idNum;
    NSDate *birthday;
    float weight;
    NSUInteger familyId;
    NSUInteger memberId;
    NSInteger age;
}
@property (strong, nonatomic) NSString *name;
@property (assign, nonatomic) bool gender;
@property (assign, nonatomic) float height;
@property (strong, nonatomic) NSString *imageName;
@property (assign, nonatomic) NSInteger idNum;
@property (strong, nonatomic) NSDate *birthday;
@property (assign, nonatomic) float weight;
@property (assign, nonatomic) NSUInteger familyId;
@property (assign, nonatomic) NSUInteger memberId;
@property (assign, nonatomic) NSInteger age;
- (NSString *)baseInfo; // eg:18岁 180cm 70kg
@end
