//
//  BLTSpo2ChartViewController.h
//  BLT-Spo2Monitor
//
//  Created by luteng on 16/7/27.
//  Copyright © 2016年 luteng. All rights reserved.
//

#import <UIKit/UIKit.h>
#import "BLTChartsBaseViewController.h"
//#import <Charts/Charts.h>
@interface BLTSpo2ChartViewController : BLTChartsBaseViewController
-(void)setSpo2TrendData:(NSArray*)Spo2TrendData;

@property(nonatomic,strong)NSArray *Spo2DataVals;
@property(nonatomic,strong)NSArray *PRDataValues;

@end
