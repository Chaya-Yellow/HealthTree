//
//  BLTChartsBaseViewController.h
//  BLT-Spo2Monitor
//
//  Created by luteng on 16/7/27.
//  Copyright © 2016年 luteng. All rights reserved.
//

#import <UIKit/UIKit.h>
#import "Spo2MonitorEx-Bridging-Header.h"
//#import "BLT-Spo2Monitor-Bridging-Header.h"

@interface BLTChartsBaseViewController : UIViewController
{
@protected
    NSArray *parties;
}

@property (nonatomic, strong) IBOutlet UIButton *optionsButton;
@property (nonatomic, strong) IBOutlet NSArray *options;

@property (nonatomic, assign) BOOL shouldHideData;

- (void)handleOption:(NSString *)key forChartView:(ChartViewBase *)chartView;

- (void)updateChartData;

- (void)setupPieChartView:(PieChartView *)chartView;
- (void)setupRadarChartView:(RadarChartView *)chartView;
- (void)setupBarLineChartView:(BarLineChartViewBase *)chartView;

@end
