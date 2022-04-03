//
//  ReviewListViewController.m
//  BLT-Spo2Monitor
//
//  Created by luteng on 15/10/28.
//  Copyright © 2015年 luteng. All rights reserved.
//

#import "ReviewListViewController.h"
#import "consDefine.h"
#import "AppDelegate.h"
#import "ReviewDataObj.h"
#import "ReviewWavesView.h"
#import "ReviewTrendData.h"

@interface ReviewListViewController()
@property(nonatomic, strong)UITableView* tableView;
@end

@implementation ReviewListViewController
{
    NSMutableArray* trendDataArray;
}
- (void)viewDidLoad {
    [super viewDidLoad];
    self.view.backgroundColor = [UIColor clearColor];
    NSLog(@"this is trend Page......");
    trendDataArray = [NSMutableArray array];
    [self layoutCurrentFrame];
    //[self initTrendData];
    // Do any additional setup after loading the view.
}

- (void)didReceiveMemoryWarning {
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}

-(void)layoutCurrentFrame
{
    CGRect viewRect = self.view.bounds;
    CGFloat left = SPACING_W;
    CGFloat top = SPACING_W;
    CGFloat w = CGRectGetWidth(viewRect)- 2*SPACING_W;
    CGFloat h = CGRectGetHeight(viewRect)-TOP_SPACING_H-2*SPACING_W-100;
    CGFloat basicH = h;
    _tableView = [[UITableView alloc]init];
    self.tableView.frame = CGRectMake(left, top,w, basicH);
    self.tableView.dataSource = self;
    self.tableView.delegate = self;
    self.tableView.separatorStyle = UITableViewCellSeparatorStyleNone;
    //self.tableView.scrollEnabled = NO;
    self.tableView.backgroundColor = UUBackGroundColor;
    [self.view addSubview:_tableView];
}

-(void)setTrendDataWithArray:(NSArray*)trendArray
{
    //NSLog(@"trendData = %@", trendArray);
    trendDataArray = (NSMutableArray*)trendArray;
    [self.tableView reloadData];
}

/*
-(void)initTrendData
{
    AppDelegate *appDelegate = [UIApplication sharedApplication].delegate;
    ReviewDataObj* patObj = appDelegate.objManage;
    trendDataArray = [patObj.paramTrendArray copy];
    [self.tableView reloadData];
}*/

// UITableViewDataSource协议中的方法，该方法的返回值决定表格包含多少个分区
- (NSInteger) numberOfSectionsInTableView:(UITableView *)tableView
{
    return 1;
}

- (NSInteger)tableView:(UITableView *)tableView
	numberOfRowsInSection:(NSInteger)section
{
    // 获取指定分区对应集合中的元素
    return trendDataArray.count;
}

//设置view，将替代titleForHeaderInSection方法
//重写表头view 可实现个性化
- (UIView *)tableView:(UITableView *)tableView viewForHeaderInSection:(NSInteger)section
{
    CGRect viewRect = tableView.bounds;
    CGFloat left,top,width,height;
    left = 0;
    top = 0;
    width = CGRectGetWidth(viewRect);
    height = 35;
    UIView *view = [[UIView alloc] initWithFrame:CGRectMake(left, top, CGRectGetWidth(viewRect), height)];
    view.backgroundColor =UUBackGroundColor;
    UILabel* timeLabel,*spo2Label, *prLabel;
    ///time
    left = 0;
    top = (35-TREND_LABEL_H)/2;
    width = CGRectGetWidth(viewRect)*0.5;
    height = TREND_LABEL_H;
    timeLabel = [[UILabel alloc]initWithFrame:CGRectMake(left, top,width,height)];
    timeLabel.clipsToBounds = YES;
    timeLabel.textAlignment = NSTextAlignmentCenter;
    timeLabel.textColor = UUTimeLabelColor;
    timeLabel.text =NSLocalizedString(@"Time",@"时间");
    timeLabel.font = [UIFont systemFontOfSize:16];
    //timeLabel.layer.cornerRadius = 20;
    //spo2
    left = CGRectGetWidth(viewRect)*0.5;
    width =CGRectGetWidth(viewRect)*0.25;
    spo2Label =[[UILabel alloc]initWithFrame:CGRectMake(left, top, width,height)];
    spo2Label.textAlignment = NSTextAlignmentCenter;
    spo2Label.textColor = UUSpo2LabelColor;
    spo2Label.text = @"SpO2(%)";
    spo2Label.font = [UIFont systemFontOfSize:16];
    //pr
    left = CGRectGetWidth(viewRect)*0.75;
    width =CGRectGetWidth(viewRect)*0.25;
    prLabel =[[UILabel alloc]initWithFrame:CGRectMake(left, top, width,height)];
    prLabel.textAlignment = NSTextAlignmentCenter;
    prLabel.textColor = UUPrLabelColor;
    prLabel.text = @"PR(bpm)";
    prLabel.font = [UIFont systemFontOfSize:16];
    [view addSubview:prLabel];
    [view addSubview:timeLabel];
    [view addSubview:spo2Label];
    //view.layer.cornerRadius = CORNER_RADIUS_SIZE;
    return view;
}


- (UITableViewCell *)tableView:(UITableView *)tableView
         cellForRowAtIndexPath:(NSIndexPath *)indexPath
{
    // 获取分区号
    //NSUInteger sectionNo = indexPath.section;
    NSInteger row = indexPath.row;
    // 获取表格行的行号
    //NSUInteger rowNo = indexPath.row;
    static NSString* cellId = @"TrendReviewID";
    UITableViewCell* cell;
    if(cell == nil)
    {
        cell = [[UITableViewCell alloc] initWithStyle:
                UITableViewCellStyleValue1 reuseIdentifier:cellId];
    }
    // 将单元格的边框设置为圆角
    cell.backgroundColor = [UIColor clearColor];
    cell.layer.cornerRadius = 4;
    cell.layer.masksToBounds = YES;
    CGRect viewRect = self.tableView.bounds;
    //
    float w = CGRectGetWidth(viewRect)/2;
    float left = 0;
    float h= TABLE_VIEW_CELL_H_IPHONE-2*BORDER_WIDTH;
    float top=BORDER_WIDTH;
    //time
    UILabel* timeLabel = [[UILabel alloc]initWithFrame:CGRectMake(left, top, w, h)];
    timeLabel.textColor = UUTimeLabelColor;
    timeLabel.textAlignment = NSTextAlignmentCenter;
    timeLabel.backgroundColor = [UIColor clearColor];
    timeLabel.font = [UIFont systemFontOfSize:PARAM_LABEL_ID_FONT];
    //spo2
    left += CGRectGetWidth(viewRect)/2;
    w = CGRectGetWidth(viewRect)/4;
    UILabel* spo2Value =[[UILabel alloc]initWithFrame:CGRectMake(left, top, w, h)];
    spo2Value.textColor = UUSpo2LabelColor;
    spo2Value.textAlignment = NSTextAlignmentCenter;
    spo2Value.backgroundColor = [UIColor clearColor];
    spo2Value.font = [UIFont systemFontOfSize:PARAM_LABEL_ID_FONT];
    //pr
    left +=CGRectGetWidth(viewRect)/4;
    w =CGRectGetWidth(viewRect)/4;
    UILabel* prValue =[[UILabel alloc]initWithFrame:CGRectMake(left, top, w, h)];
    prValue.textColor = UUPrLabelColor;
    prValue.textAlignment = NSTextAlignmentCenter;
    prValue.backgroundColor = [UIColor clearColor];
    prValue.font = [UIFont systemFontOfSize:PARAM_LABEL_ID_FONT];
    //
    NSDictionary* data = trendDataArray[trendDataArray.count-row-1];
    //NSLog(@"data = %@", data);
    NSInteger Spo2Value =[[data objectForKey:@"SPO2"]integerValue];
    NSInteger PRValue =   [[data objectForKey:@"PR"] integerValue];
    if(Spo2Value ==127 || Spo2Value>100 || Spo2Value == INVAL_VALUE)
    {
        spo2Value.text = @"--";
    }
    else
    {
        spo2Value.text =[NSString stringWithFormat:@"%ld",Spo2Value];
    }
    //
    if(PRValue == 255 || PRValue<0 ||PRValue==INVAL_VALUE)
    {
        prValue.text =@"--";
    }
    else
    {
        prValue.text =[NSString stringWithFormat:@"%ld", PRValue];
    }
    timeLabel.text = [data objectForKey:@"Date"];
    [cell addSubview:timeLabel];
    [cell addSubview:spo2Value];
    [cell addSubview:prValue];
    //不可选择cell
    cell.selectionStyle = UITableViewCellSelectionStyleNone;
    return cell;
}

-(CGFloat)tableView:(UITableView *)tableView heightForRowAtIndexPath:(NSIndexPath *)indexPath
{
    return  TABLE_VIEW_CELL_H_IPHONE;
}


//设置表头的高度
- (CGFloat)tableView:(UITableView *)tableView heightForHeaderInSection:(NSInteger)section
{
    return 34;
}

//Section Footer的高度
- (CGFloat)tableView:(UITableView *)tableView heightForFooterInSection:(NSInteger)section
{
    return 0.0;
}

@end
