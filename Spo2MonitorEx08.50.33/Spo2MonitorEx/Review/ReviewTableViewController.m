//
//  ReviewTableViewController.m
//  BLT-Spo2Monitor
//
//  Created by luteng on 15/10/23.
//  Copyright © 2015年 luteng. All rights reserved.
//

#import "ReviewTableViewController.h"
#import "consDefine.h"
#import "AppDelegate.h"
#import "ReviewDataObj.h"
#import "ReviewWavesView.h"

@implementation ReviewTableViewController
{
   NSMutableArray* trendDataArray;
}

- (void)viewDidLoad {
    [super viewDidLoad];
    self.view.backgroundColor = [UIColor clearColor];
    NSLog(@"this is trend Page......");
    trendDataArray = [NSMutableArray array];
    self.tableView.dataSource = self;
    self.tableView.delegate = self;
    [self layoutCurrentFrame];
    //[self initTrendData];
    // Do any additional setup after loading the view.
}

- (void)didReceiveMemoryWarning {
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}

-(void)setTrendDataWithArray:(NSArray *)trendData
{
    trendDataArray = [(NSMutableArray*)trendData copy];
     [self.tableView reloadData];
}

-(void)layoutCurrentFrame
{
    CGRect viewRect = self.view.bounds;
    CGFloat left = SPACING_W;
    CGFloat top = TOP_SPACING_H + SPACING_W;
    CGFloat w = CGRectGetWidth(viewRect)- 2*SPACING_W;
    CGFloat h = CGRectGetHeight(viewRect)-TOP_SPACING_H-2*SPACING_W;
    CGFloat basicH = h;
    self.tableView.frame = CGRectMake(left, top, w, basicH);
    self.tableView.scrollEnabled = NO;
    self.tableView.backgroundColor = UUBackGroundColor;
    self.tableView.separatorStyle = UITableViewCellSeparatorStyleNone;
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
    return 2;
}

- (NSInteger)tableView:(UITableView *)tableView
	numberOfRowsInSection:(NSInteger)section
{
    // 获取指定分区对应集合中的元素
    return 1;
}

//设置view，将替代titleForHeaderInSection方法
//重写表头view 可实现个性化
- (UIView *)tableView:(UITableView *)tableView viewForHeaderInSection:(NSInteger)section
{
    CGRect viewRect = tableView.bounds;
    CGFloat left = SPACING_W;
    CGFloat Trendtop, colorTop;
    CGFloat paramW, colorW;
    CGFloat h;
    h =25;
    UIView *view = [[UIView alloc] initWithFrame:CGRectMake(left, 0, CGRectGetWidth(viewRect), h)];
    view.backgroundColor = UUBackGroundColor;
    UILabel* param1Label,*color1Label;
    paramW = TREND_LABEL_W;
    colorW = COLOR_LABEL_W;
    Trendtop = (h-TREND_LABEL_H)/2;
    colorTop = (h-COLOR_LABEL_H)/2;
    ///
    color1Label = [[UILabel alloc]initWithFrame:CGRectMake(left, colorTop,colorW,COLOR_LABEL_H)];
    color1Label.clipsToBounds = YES;
    color1Label.layer.cornerRadius = 20;
    left += colorW;
    param1Label =[[UILabel alloc]initWithFrame:CGRectMake(left, Trendtop, paramW,TREND_LABEL_H)];
    switch (section) {
        case 0:
            color1Label.backgroundColor = UUSpo2LabelColor;
            param1Label.textColor = UUSpo2LabelColor;
            param1Label.text = @"SpO2";
            break;
        case 1:
            color1Label.backgroundColor = UUPrLabelColor;
            param1Label.textColor = UUPrLabelColor;
            param1Label.text = @"PR";
            break;
        default:
            break;
    }
    param1Label.font=[UIFont systemFontOfSize:12];
    [view addSubview:param1Label];
    [view addSubview:color1Label];
    //view.layer.cornerRadius = CORNER_RADIUS_SIZE;
    return view;
}


- (UITableViewCell *)tableView:(UITableView *)tableView
         cellForRowAtIndexPath:(NSIndexPath *)indexPath
{
    // 获取分区号
    NSUInteger sectionNo = indexPath.section;
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
    float w = CGRectGetWidth(viewRect);
    float left = 0;
    float h;
    float top=0;
    h = (self.view.frame.size.height-120)/2;
    ReviewWavesView* view = [[ReviewWavesView alloc]initWithFrame:CGRectMake(left, top, w, h)];
    view.backgroundColor = UUBackGroundColor;
    view.clipsToBounds = YES;
    view.layer.cornerRadius = CORNER_RADIUS_SIZE;
    view.paramID = (int)sectionNo;
    view.paramDataArray = trendDataArray;
    [cell addSubview:view];
    //不可选择cell
    cell.selectionStyle = UITableViewCellSelectionStyleNone;
    return cell;
}

-(CGFloat)tableView:(UITableView *)tableView heightForRowAtIndexPath:(NSIndexPath *)indexPath
{
    return (self.view.frame.size.height-120)/2;
}


//设置表头的高度
- (CGFloat)tableView:(UITableView *)tableView heightForHeaderInSection:(NSInteger)section
{
        return 25;
}

//Section Footer的高度
- (CGFloat)tableView:(UITableView *)tableView heightForFooterInSection:(NSInteger)section
{
    return 0.0;
}

@end
