//
//  BLTSpo2ChartViewController.m
//  BLT-Spo2Monitor
//
//  Created by luteng on 16/7/27.
//  Copyright © 2016年 luteng. All rights reserved.
//

#import "BLTSpo2ChartViewController.h"
#import "Spo2MonitorEx-Bridging-Header.h"
#import "Spo2MonitorEx-Swift.h"
#import "consDefine.h"
#import "DateValueFormatter.h"
@interface BLTSpo2ChartViewController()<ChartViewDelegate>
@property (nonatomic, strong)LineChartView* Spo2chartView;
@property (nonatomic, strong)LineChartView* PRChartView;
@property (nonatomic, strong)UILabel*       Spo2TipLabel;
@property (nonatomic, strong)UILabel*       Spo2NoDataLabel;
@property (nonatomic, strong)UILabel*       PrTipLabel;
@property (nonatomic, strong)UILabel*       PrNoDataLabel;
@end
@implementation BLTSpo2ChartViewController

#define CHART_OFF_H 180

- (void)viewDidLoad {
    [super viewDidLoad];
    self.view.backgroundColor = UUBackGroundColor;
    //
    
    _Spo2chartView = [[LineChartView alloc]initWithFrame:CGRectMake(0, 5, self.view.frame.size.width, (self.view.frame.size.height-CHART_OFF_H)/2)];
    [self.view addSubview:_Spo2chartView];
    _Spo2chartView.gridBackgroundColor = [UIColor colorWithRed:233/255.0 green:46/255.0 blue:53/255.0 alpha: 1.0];
    _Spo2chartView.delegate = self;
    _Spo2chartView.chartDescription.enabled = YES;
    _Spo2chartView.chartDescription.text = @"";
//    _Spo2chartView.noDataTextDescription = NSLocalizedString(@"Review_No_Spo2Data",@"You need to provide data for the chart.");
    _Spo2chartView.noDataText = NSLocalizedString(@"Review_No_Data", @"没有数据");
//    _Spo2chartView.infoTextColor = [UIColor whiteColor];
    _Spo2chartView.dragEnabled = YES;
    [_Spo2chartView setScaleEnabled:YES];
    _Spo2chartView.pinchZoomEnabled = YES;
    _Spo2chartView.drawGridBackgroundEnabled = NO;
    _Spo2chartView.tag = 0;
    //
    _Spo2TipLabel = [[UILabel alloc]initWithFrame:CGRectMake((self.view.frame.size.width-130)/2, 40, 130, 15)];
    _Spo2TipLabel.backgroundColor = [UIColor clearColor];
    _Spo2TipLabel.font = [UIFont systemFontOfSize:12];
    _Spo2TipLabel.textAlignment = NSTextAlignmentCenter;
    _Spo2TipLabel.text = NSLocalizedString(@"Review_No_Data", @"没有数据");
    _Spo2TipLabel.textColor = [UIColor whiteColor];
    _Spo2TipLabel.hidden = YES;
    [_Spo2chartView addSubview:_Spo2TipLabel];
    //
    _Spo2NoDataLabel = [[UILabel alloc]initWithFrame:CGRectMake((self.view.frame.size.width-130)/2, 55, 130, 15)];
    _Spo2NoDataLabel.backgroundColor = [UIColor clearColor];
    _Spo2NoDataLabel.font = [UIFont systemFontOfSize:12];
    _Spo2NoDataLabel.textAlignment = NSTextAlignmentCenter;
    _Spo2NoDataLabel.text = NSLocalizedString(@"Review_No_Spo2Data",@"You need to provide data for the chart.");
    _Spo2NoDataLabel.textColor = [UIColor whiteColor];
    _Spo2NoDataLabel.hidden = YES;
    [_Spo2chartView addSubview:_Spo2NoDataLabel];
    // x-axis limit line
//    ChartLimitLine *llXAxis = [[ChartLimitLine alloc] initWithLimit:10.0 label:@"Index 10"];
//    llXAxis.lineWidth = 4.0;
//    llXAxis.lineDashLengths = @[@(10.f), @(10.f), @(0.f)];
//    llXAxis.labelPosition = ChartLimitLabelPositionRightBottom;
//    llXAxis.valueFont = [UIFont systemFontOfSize:10.f];
//    [_chartView.xAxis addLimitLine:llXAxis];
    
//    ChartLimitLine *ll1 = [[ChartLimitLine alloc] initWithLimit:97.0 label:@"Upper Limit"];
//    ll1.lineWidth = 4.0;
//    ll1.lineDashLengths = @[@5.f, @5.f];
//    ll1.labelPosition = ChartLimitLabelPositionRightTop;
//    ll1.valueFont = [UIFont systemFontOfSize:10.0];
//
    ChartLimitLine *ll2 = [[ChartLimitLine alloc] initWithLimit:85.0 label:NSLocalizedString(@"Spo2_Low_Saturation", @"Low saturation limit")];
    ll2.lineWidth = 4.0;
    ll2.lineDashLengths = @[@5.f, @5.f];
    ll2.labelPosition = ChartLimitLabelPositionRightBottom;
    ll2.valueFont = [UIFont systemFontOfSize:10.0];
    ChartYAxis *leftAxis = _Spo2chartView.leftAxis;
    [leftAxis removeAllLimitLines];
//    [leftAxis addLimitLine:ll1];
//    [leftAxis addLimitLine:ll2];
    leftAxis.axisMaximum = 100.0;
    leftAxis.axisMinimum = 80.0;
    leftAxis.inverted = NO;
    leftAxis.gridLineDashLengths = @[@5.f, @5.f];
    leftAxis.drawZeroLineEnabled = NO;
    leftAxis.drawLimitLinesBehindDataEnabled = YES;
    leftAxis.labelTextColor = [UIColor whiteColor];

    _Spo2chartView.xAxis.labelTextColor = [UIColor whiteColor];

    _Spo2chartView.rightAxis.enabled = NO;
    [_Spo2chartView.viewPortHandler setMaximumScaleY: 3.f];
    //[_Spo2chartView.viewPortHandler setMaximumScaleX: 2.f];
    
    //    BalloonMarker *marker = [[BalloonMarker alloc] initWithColor:[UIColor colorWithRed:255.0/255 green:238.0/255 blue:153.0/255 alpha:1] font:[UIFont systemFontOfSize:12.0] insets: UIEdgeInsetsMake(8.0, 8.0, 20.0, 8.0) showStyle:1];
    
    BalloonMarker *marker = [[BalloonMarker alloc]
                             initWithColor: [UIColor colorWithRed:255.0/255 green:238.0/255 blue:153.0/255 alpha:0.7]
                             font: [UIFont systemFontOfSize:12.0]
                             textColor: UIColor.whiteColor
                             insets: UIEdgeInsetsMake(8.0, 8.0, 20.0, 8.0)];
    marker.minimumSize = CGSizeMake(80.f, 40.f);
    marker.chartView = _Spo2chartView;
    _Spo2chartView.marker = marker;
    //
    _Spo2chartView.legend.form = ChartLegendFormLine;
    _Spo2chartView.legend.textColor = UUBlack;
    [_Spo2chartView animateWithXAxisDuration:2.5 easingOption:ChartEasingOptionEaseInOutQuart];
    //
    _PRChartView = [[LineChartView alloc]initWithFrame:CGRectMake(0, self.Spo2chartView.frame.origin.y +self.Spo2chartView.frame.size.height, self.view.frame.size.width, (self.view.frame.size.height-CHART_OFF_H)/2)];
    [self.view addSubview:_PRChartView];
    _PRChartView.gridBackgroundColor = [UIColor colorWithRed:208.0/255.0 green:46.0/255.0 blue:61.0/255.0 alpha:1.0];
    _PRChartView.delegate = self;
    _PRChartView.chartDescription.enabled = YES;
    _PRChartView.chartDescription.text = @"";
//    _PRChartView.noDataText = NSLocalizedString(@"Review_No_PrData",@"You need to provide data for the chart.");
    _PRChartView.noDataText = NSLocalizedString(@"Review_No_Data", @"没有数据");
    _PRChartView.xAxis.labelTextColor = [UIColor whiteColor];

//    _PRChartView.infoTextColor = [UIColor blackColor];
    //
    _PRChartView.dragEnabled = YES;
    [_PRChartView setScaleEnabled:YES];
    _PRChartView.pinchZoomEnabled = YES;
    _PRChartView.drawGridBackgroundEnabled = NO;
    _PRChartView.tag = 1;
    //
    _PrTipLabel = [[UILabel alloc]initWithFrame:CGRectMake((self.view.frame.size.width-130)/2, 40, 130, 15)];
    _PrTipLabel.backgroundColor = [UIColor clearColor];
    _PrTipLabel.font = [UIFont systemFontOfSize:12];
    _PrTipLabel.textAlignment = NSTextAlignmentCenter;
    _PrTipLabel.text = NSLocalizedString(@"Review_No_Data", @"没有数据");
    _PrTipLabel.textColor = [UIColor whiteColor];
    _PrTipLabel.hidden = YES;
    [_PRChartView addSubview:_PrTipLabel];
    //
    _PrNoDataLabel = [[UILabel alloc]initWithFrame:CGRectMake((self.view.frame.size.width-130)/2,55, 130, 15)];
    _PrNoDataLabel.backgroundColor = [UIColor clearColor];
    _PrNoDataLabel.font = [UIFont systemFontOfSize:12];
    _PrNoDataLabel.textAlignment = NSTextAlignmentCenter;
    _PrNoDataLabel.text = NSLocalizedString(@"Review_No_PrData",@"You need to provide data for the chart.");
    _PrNoDataLabel.textColor = [UIColor whiteColor];
    _PrNoDataLabel.hidden = YES;
    [_PRChartView addSubview:_PrNoDataLabel];
    //
//    ChartLimitLine *PRll1 = [[ChartLimitLine alloc] initWithLimit:180.0 label:@"Upper Limit"];
//    ll1.lineWidth = 4.0;
//    ll1.lineDashLengths = @[@5.f, @5.f];
//    ll1.labelPosition = ChartLimitLabelPositionRightTop;
//    ll1.valueFont = [UIFont systemFontOfSize:10.0];
    //
//    ChartLimitLine *PRll2 = [[ChartLimitLine alloc] initWithLimit:40.0 label:@"Lower Limit"];
//    ll2.lineWidth = 4.0;
//    ll2.lineDashLengths = @[@5.f, @5.f];
//    ll2.labelPosition = ChartLimitLabelPositionRightBottom;
//    ll2.valueFont = [UIFont systemFontOfSize:10.0];
    ChartYAxis *PRleftAxis = _PRChartView.leftAxis;
    [PRleftAxis removeAllLimitLines];
    //[PRleftAxis addLimitLine:PRll1];
    //[PRleftAxis addLimitLine:PRll2];
    PRleftAxis.axisMaximum = 255.0;
    PRleftAxis.axisMinimum = 0.0;
    PRleftAxis.inverted = NO;
    PRleftAxis.gridLineDashLengths = @[@5.f, @5.f];
    PRleftAxis.drawZeroLineEnabled = NO;
    PRleftAxis.drawLimitLinesBehindDataEnabled = YES;
    PRleftAxis.labelTextColor = [UIColor whiteColor];
    //
    _PRChartView.rightAxis.enabled = NO;
    [_PRChartView.viewPortHandler setMaximumScaleY: 8.f];
    //
    BalloonMarker *PRmarker = [[BalloonMarker alloc]
                             initWithColor: [UIColor colorWithRed:69.0/255 green:189.0/255 blue:77.0/255 alpha:0.7] font: [UIFont systemFontOfSize:12.0]
                             textColor: UIColor.whiteColor
                             insets: UIEdgeInsetsMake(8.0, 8.0, 20.0, 8.0)];
    PRmarker.chartView = _PRChartView;
    PRmarker.minimumSize = CGSizeMake(80.f, 40.f);
    _PRChartView.marker = PRmarker;
    _PRChartView.legend.form = ChartLegendFormLine;
    _PRChartView.legend.textColor = [UIColor whiteColor];
    [_PRChartView animateWithXAxisDuration:2.5 easingOption:ChartEasingOptionEaseInOutQuart];
}

- (void)didReceiveMemoryWarning {
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}

-(void)setSpo2TrendData:(NSArray *)Spo2TrendData
{
    NSLog(@"updateChartData-----------------");
    if (self.shouldHideData)
    {
        _Spo2chartView.data = nil;
        _PRChartView.data = nil;
        return;
    }
    NSMutableArray *Spo2xVals = [[NSMutableArray alloc] init];
    NSMutableArray *Spo2yVals = [[NSMutableArray alloc] init];
    NSMutableArray *PRyValues = [[NSMutableArray alloc]init];
        NSLog(@"Spo2TrendData = %@", Spo2TrendData);
    NSMutableArray *spo2DataVals = [[NSMutableArray alloc] init];
    NSMutableArray *prDataVals = [[NSMutableArray alloc] init];

    if(Spo2TrendData && Spo2TrendData.count>0)
    {
        _PrNoDataLabel.hidden = YES;
        _PrTipLabel.hidden = YES;
        _Spo2NoDataLabel.hidden = YES;
        _Spo2TipLabel.hidden = YES;
        NSInteger Spo2Value;
        NSInteger PrValue;
        for(int i=0; i<Spo2TrendData.count; i++)
        {

            NSDictionary* dic = [Spo2TrendData objectAtIndex:i];
            NSString* str = [dic objectForKey:@"Date"];
            str = [str substringFromIndex:10];
            [Spo2xVals addObject:str];
            Spo2Value =[[dic objectForKey:@"SPO2"] integerValue];
            PrValue = [[dic objectForKey:@"PR"] integerValue];
            
            if(Spo2Value ==127 || Spo2Value>100)
            {
                //[Spo2yVals addObject:[[ChartDataEntry alloc] initWithValue:0 xIndex:i]];
            }
            else
            {
                [spo2DataVals addObject:@(Spo2Value)];
                 [Spo2yVals addObject:[[ChartDataEntry alloc] initWithX:i y:Spo2Value icon: [UIImage imageNamed:@"icon"]]];
//                [Spo2yVals addObject:[[ChartDataEntry alloc] initWithValue:Spo2Value xIndex:i]];
            }
            //
            if(PrValue == 255 || PrValue<0)
            {
                //[PRyValues addObject:[[ChartDataEntry alloc] initWithValue:0 xIndex:i]];
            }
            else
            {
                [prDataVals addObject:@(PrValue)];
                [PRyValues addObject:[[ChartDataEntry alloc] initWithX:i y:PrValue icon: [UIImage imageNamed:@"icon"]]];
//                [PRyValues addObject:[[ChartDataEntry alloc] initWithValue:PrValue xIndex:i]];
            }
        }
        self.Spo2DataVals = [NSArray arrayWithArray:spo2DataVals];
        self.PRDataValues = [NSArray arrayWithArray:prDataVals];
        [self updateSpo2ChartWithXValues:Spo2xVals andYValus:Spo2yVals];
        [self updatePRChartWithXValues:Spo2xVals andYValues:PRyValues];
    }
    else
    {
        [Spo2xVals addObject:@" "];
//        [Spo2yVals addObject:[[ChartDataEntry alloc] initWithX:0 y:0 icon:[UIImage imageNamed:@"icon"]]];
//        [PRyValues addObject:[[ChartDataEntry alloc] initWithX:0 y:0 icon:[UIImage imageNamed:@"icon"]]];
        _PrNoDataLabel.hidden = NO;
        _PrTipLabel.hidden = NO;
        _Spo2NoDataLabel.hidden = NO;
        _Spo2TipLabel.hidden = NO;
        self.Spo2DataVals = [NSArray arrayWithArray:spo2DataVals];
        self.PRDataValues = [NSArray arrayWithArray:prDataVals];
        [self updateSpo2ChartWithXValues:Spo2xVals andYValus:Spo2yVals];
        [self updatePRChartWithXValues:Spo2xVals andYValues:PRyValues];
    }
  
}

-(void)updatePRChartWithXValues:(NSMutableArray*)xValus andYValues:(NSMutableArray*)yValues
{
//    NSLog(@"XValues=%@", xValus);
//    NSLog(@"yYValues=%@", yValues);
    LineChartDataSet *set1 = nil;
    if (_PRChartView.data.dataSetCount > 0)
    {
        set1 = (LineChartDataSet *)_PRChartView.data.dataSets[0];
        set1.values = yValues;
//        _PRChartView.data.xValsObjc = xValus;
        [_PRChartView.data notifyDataChanged];
        [_PRChartView notifyDataSetChanged];
    }
    else
    {
        set1 = [[LineChartDataSet alloc] initWithValues:yValues label:NSLocalizedString(@"Review_PrTrend_Data", @"PR趋势数据")];
        set1.drawIconsEnabled = false;
        set1.lineDashLengths = @[@5.f, @5.f];
        set1.highlightLineDashLengths = @[@5.f, @5.f];
        [set1 setColor:[UIColor colorWithRed:233.0/255 green:46.0/255 blue:53.0/255 alpha:1]];
        [set1 setCircleColor:[UIColor colorWithRed:233.0/255 green:46.0/255 blue:53.0/255 alpha:1]];
        set1.lineWidth = 1.0;
        set1.circleRadius = 5.0;
        set1.drawCircleHoleEnabled = NO;
        set1.valueFont = [UIFont systemFontOfSize:9.f];
        set1.valueTextColor = [UIColor clearColor];
        //set1.fillAlpha = 65/255.0;
        //set1.fillColor = UIColor.blackColor;
        
        NSArray *gradientColors = @[
                                    (id)[UIColor colorWithRed:208.0/255.0 green:46.0/255.0 blue:61.0/255.0 alpha:0].CGColor,
                                    (id)[UIColor colorWithRed:208.0/255.0 green:46.0/255.0 blue:61.0/255.0 alpha:1].CGColor
                                    ];
        CGGradientRef gradient = CGGradientCreateWithColors(nil, (CFArrayRef)gradientColors, nil);
        
        set1.fillAlpha = 1.f;
        set1.fill = [ChartFill fillWithLinearGradient:gradient angle:90.f];
        set1.drawFilledEnabled = YES;
        
        CGGradientRelease(gradient);
        
        NSMutableArray *dataSets = [[NSMutableArray alloc] init];
        [dataSets addObject:set1];
        
        LineChartData *data = [[LineChartData alloc] initWithDataSets:dataSets];
        _PRChartView.data = data;
    }
//    NSLog(@"XYalues=%@", xValus);
    ChartIndexAxisValueFormatter *formart = [[ChartIndexAxisValueFormatter alloc]initWithValues:xValus];
    _PRChartView.xAxis.valueFormatter = formart;
}

-(void)updateSpo2ChartWithXValues:(NSMutableArray*)xValus andYValus:(NSMutableArray*)yValues
{
    LineChartDataSet *set1 = nil;
    if (_Spo2chartView.data.dataSetCount > 0)
    {
        set1 = (LineChartDataSet *)_Spo2chartView.data.dataSets[0];
        set1.values = yValues;
//        _Spo2chartView.data.xValsObjc = xValus;
        [_Spo2chartView.data notifyDataChanged];
        [_Spo2chartView notifyDataSetChanged];
    }
    else
    {
        set1 = [[LineChartDataSet alloc] initWithValues:yValues label:NSLocalizedString(@"Review_Spo2Trend_Data", @"SpO2趋势数据")];
        set1.drawIconsEnabled = false;
//        set1.lineDashLengths = @[@5.f, @5.f];
//        set1.highlightLineDashLengths = @[@5.f, @2.5f];
        [set1 setColor:[UIColor colorWithRed:69.0/255.0 green:189.0/255.0 blue:177.0/255.0 alpha:1]];
        [set1 setCircleColor:[UIColor colorWithRed:69.0/255.0 green:189.0/255.0 blue:177.0/255.0 alpha:1]];
        set1.lineWidth = 1.0;
        set1.circleRadius = 5.0;
        set1.drawCircleHoleEnabled = NO;
        set1.valueFont = [UIFont systemFontOfSize:9.f];
        set1.valueTextColor = [UIColor colorWithRed:208.0/255.0 green:46.0/255.0 blue:61.0/255.0 alpha:0];
//        set1.fillAlpha = 65/255.0;
        
        NSArray *gradientColors = @[
                                    (id)[UIColor colorWithRed:208.0/255.0 green:46.0/255.0 blue:61.0/255.0 alpha:0].CGColor,
                                    (id)[UIColor colorWithRed:216.0/255.0 green:46.0/255.0 blue:57.0/255.0 alpha:0.2].CGColor,
                                    (id)[UIColor colorWithRed:223.0/255.0 green:46.0/255.0 blue:53.0/255.0 alpha:1].CGColor
                                    ];
        CGGradientRef gradient = CGGradientCreateWithColors(nil, (CFArrayRef)gradientColors, nil);
        
        set1.fillAlpha = 1.f;
        set1.fill = [ChartFill fillWithLinearGradient:gradient angle:90.f];
        set1.drawFilledEnabled = YES;
        
        CGGradientRelease(gradient);
        
        NSMutableArray *dataSets = [[NSMutableArray alloc] init];
        [dataSets addObject:set1];
        
        LineChartData *data = [[LineChartData alloc] initWithDataSets:dataSets];
        _Spo2chartView.data = data;
    }
    ChartIndexAxisValueFormatter *formart = [[ChartIndexAxisValueFormatter alloc]initWithValues:xValus];
    _Spo2chartView.xAxis.valueFormatter = formart;
    
}

#pragma mark - ChartViewDelegate

- (void)chartValueSelected:(ChartViewBase * __nonnull)chartView entry:(ChartDataEntry * __nonnull)entry dataSetIndex:(NSInteger)dataSetIndex highlight:(ChartHighlight * __nonnull)highlight
{
    //NSLog(@"chartValueSelected");
}

- (void)chartValueNothingSelected:(ChartViewBase * __nonnull)chartView
{
    //NSLog(@"chartValueNothingSelected");
}

@end
