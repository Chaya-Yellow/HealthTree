//
//  ReviewTrendData.h
//  BLT-Spo2Monitor
//
//  Created by luteng on 15/10/22.
//  Copyright © 2015年 luteng. All rights reserved.
//

#import <Foundation/Foundation.h>

@interface ReviewTrendData : NSObject
@property(nonatomic) int spo2Value;
@property(nonatomic) int prValue;
@property(nonatomic, strong) NSString* time;
@end
