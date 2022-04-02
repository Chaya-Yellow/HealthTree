//
//  MXSelectDeviceView.m
//  BLT-Spo2Monitor
//
//  Created by luteng on 15/10/20.
//  Copyright © 2015年 luteng. All rights reserved.
//

#import "MXSelectDeviceView.h"
#import "MXBTInfo.h"
#import "MXBluetoothManager.h"
#import "EHCardMessageTableViewCell.h"

@interface MXSelectDeviceView()<UITableViewDataSource, UITableViewDelegate>
@property (nonatomic, strong) UITableView *deviceTable;
@property (nonatomic, strong) UIImageView *loadingView;
@property (nonatomic, strong) NSTimer *timer;
@property (nonatomic, assign) NSInteger count;
@end

@implementation MXSelectDeviceView
@synthesize delegate = _delegate;

- (id)initWithFrame:(CGRect)frame
{
    self = [super initWithFrame:frame];
    if (self) {
        // Initialization code
        self.backgroundColor = [[UIColor blackColor]colorWithAlphaComponent:0.3f];
        _selectDevices = [[NSArray alloc] init];
        //选择设备
        self.deviceTable = [[UITableView alloc]initWithFrame: CGRectMake(25, 100, 270, 240) style: UITableViewStyleGrouped];
        _deviceTable.dataSource = self;
        _deviceTable.delegate = self;
        _deviceTable.center = CGPointMake(_deviceTable.center.x, frame.size.height / 2.0);
        _deviceTable.backgroundColor = [UIColor whiteColor];
        _deviceTable.layer.cornerRadius = 6.0f;
        [self addSubview: _deviceTable];
        //取消按钮
        UIImage *btnNormalImg = [UIImage imageNamed: @"btn_cancle_normal.png"];
        UIImage *btnPressedImg = [UIImage imageNamed: @"btn_cancle_pressed.png"];
        UIButton *cancelButton = [UIButton buttonWithType: UIButtonTypeCustom];
        cancelButton.frame = CGRectMake(_deviceTable.frame.size.width + _deviceTable.frame.origin.x - 22, _deviceTable.frame.origin.y - 22, btnNormalImg.size.width, btnNormalImg.size.height);
        [cancelButton setImage: btnNormalImg forState: UIControlStateNormal];
        [cancelButton setImage: btnPressedImg forState: UIControlStateHighlighted];
        [cancelButton addTarget: self action: @selector(cancelButtonPressed) forControlEvents: UIControlEventTouchUpInside];
        [self addSubview: cancelButton];
        //动画间隔
        _timer = [NSTimer scheduledTimerWithTimeInterval:0.003 target:self selector:@selector(rotateAnimation) userInfo:nil repeats:YES];
    }
    return self;
}

#pragma mark TableView Delegate && DataSource
- (NSInteger)numberOfSectionsInTableView:(UITableView *)tableView
{
    return 1;
}

- (NSInteger)tableView:(UITableView *)tableView numberOfRowsInSection:(NSInteger)section
{
    return [_selectDevices count];
}

- (CGFloat)tableView:(UITableView *)tableView heightForHeaderInSection:(NSInteger)section
{
    return 45;
}

- (UIView *)tableView:(UITableView *)tableView viewForHeaderInSection:(NSInteger)section
{
    UIView *headerView = [[UIView alloc]initWithFrame: CGRectMake(0, 0, tableView.frame.size.width, 45)];
    headerView.backgroundColor = [UIColor whiteColor];
    //菊花图
    //    UIImage *loadingImage = [UIImage imageNamed: @"loading.png"];
    //    self.loadingView = [[UIImageView alloc]initWithFrame: CGRectMake(74, 15, loadingImage.size.width, loadingImage.size.height)];
    //    _loadingView.image = loadingImage;
    //    [headerView addSubview: _loadingView];
    //提示语
    UILabel *tipLabel = [[UILabel alloc]initWithFrame: CGRectMake(0, 15, tableView.frame.size.width, 18)];
    tipLabel.text =NSLocalizedString(@"Select_device", @"选择设备");
    tipLabel.font = [UIFont systemFontOfSize: 17.0f];
    tipLabel.textColor = [UIColor darkGrayColor];
    tipLabel.textAlignment = NSTextAlignmentCenter;
    [headerView addSubview: tipLabel];
    //分割线
    UIImageView *lineImgView = [[UIImageView alloc]initWithFrame: CGRectMake(0, 44.5, self.frame.size.width, 0.5)];
    lineImgView.backgroundColor = [UIColor lightGrayColor];
    [headerView addSubview: lineImgView];
    return headerView;
}

- (UITableViewCell *)tableView:(UITableView *)tableView cellForRowAtIndexPath:(NSIndexPath *)indexPath
{
    NSString *identifier = @"cellIdentifier";
    EHCardMessageTableViewCell *cell = (EHCardMessageTableViewCell *)[tableView cellForRowAtIndexPath: indexPath];
    if (cell == nil) {
        cell = [[EHCardMessageTableViewCell alloc] initWithStyle:UITableViewCellStyleDefault reuseIdentifier:identifier];
        cell.tag = indexPath.row;
    }
    cell.btInfo = (MXBTInfo *)[_selectDevices objectAtIndex:indexPath.row];
    cell.accessoryType = UITableViewCellAccessoryDisclosureIndicator;
    
    
    return cell;
}

- (void)tableView:(UITableView *)tableView didSelectRowAtIndexPath:(NSIndexPath *)indexPath
{
    NSLog(@"did select");
    [tableView deselectRowAtIndexPath:indexPath animated:YES];
    MXBTInfo *btInfo = (MXBTInfo *)[_selectDevices objectAtIndex:indexPath.row];
    [[MXBluetoothManager sharedInstance] connectPeripheralWithInfo:btInfo];
    [self cancelButtonPressed];
}

#pragma mark Button Event
- (void)cancelButtonPressed
{
    if ([_delegate respondsToSelector:@selector(deviceViewCancelDidSelected:)]) {
        [_delegate deviceViewCancelDidSelected:self];
    }
    self.hidden = YES;
}

#pragma mark Timer Delegate
- (void)rotateAnimation
{
    _count++;
    CGFloat angel = (CGFloat)_count/360.00;
    _loadingView.transform = CGAffineTransformMakeRotation(angel);
    if (_count == 2250) {
        _count = 0;
    }
}

#pragma mark - Set Methods

- (void)setSelectDevices:(NSArray *)s
{
    _selectDevices = s;
    [_deviceTable reloadData];
}

@end
