//
//  ReviewDataObj.m
//  BLT-Spo2Monitor
//
//  Created by luteng on 15/10/22.
//  Copyright © 2015年 luteng. All rights reserved.
//

#import "ReviewDataObj.h"

@implementation ReviewDataObj

#define MAX_PARAM_NO 17280  //5s 一次，，最多24小时
-(id)initDataReviewObj
{
    if(self ==[super init])
    {
        NSLog(@"%s", __FUNCTION__);
        _paramTrendArray = [NSMutableArray array];
    }
    return self;
}

//插入趋势数据
-(void)insertAparamEvent:(ReviewTrendData*)paramTrend
{
    if(_paramTrendArray.count >= MAX_PARAM_NO)
        [_paramTrendArray removeObjectAtIndex:0];
    [_paramTrendArray addObject:paramTrend];
    //NSLog(@"paramArrayLen = %li", _paramTrendArray.count);
}
//清空所有数据
-(void)clearAllReviewData
{
    [_paramTrendArray removeAllObjects];
}

@end
