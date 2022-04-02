//
//  ReviewDataObj.h
//  BLT-Spo2Monitor
//
//  Created by luteng on 15/10/22.
//  Copyright © 2015年 luteng. All rights reserved.
//

#import <Foundation/Foundation.h>
#import "ReviewTrendData.h"
@interface ReviewDataObj : NSObject

@property(strong, nonatomic)NSMutableArray* paramTrendArray;    //趋势数组

//初始化数组
-(id)initDataReviewObj;
//插入趋势数据
-(void)insertAparamEvent:(ReviewTrendData*)paramTrend;
//清空所有数据
-(void)clearAllReviewData;
@end
