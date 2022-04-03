//
//  MXDeviceInfoViewController.m
//  BLT-Spo2Monitor
//
//  Created by luteng on 15/10/20.
//  Copyright © 2015年 luteng. All rights reserved.
//

#import "MXDeviceInfoViewController.h"
#import "MXDeviceInfo.h"
#import "consDefine.h"
#define kTitleKey @"Title"
#define kInfoKey @"Info"
@interface MXDeviceInfoViewController()
@property (nonatomic, strong) UITableView *infoTableView;
@property (nonatomic, strong) NSMutableArray *sectionData;
@property (nonatomic, strong) MXDeviceInfo *selectedDeviecInfo;
@end
@implementation MXDeviceInfoViewController

- (id)initWithDeviceInfo:(MXDeviceInfo *)info
{
    self = [super init];
    if (self) {
        self.selectedDeviecInfo = info;
    }
    return self;
}

- (id)initWithNibName:(NSString *)nibNameOrNil bundle:(NSBundle *)nibBundleOrNil
{
    self = [super initWithNibName:nibNameOrNil bundle:nibBundleOrNil];
    if (self) {
        // Custom initialization
    }
    return self;
}

- (void)viewDidLoad
{
    [super viewDidLoad];
    [self.navigationController.navigationBar setBarTintColor:UUNavBackColor];
    [self.navigationController.navigationBar setTintColor:UUWhite];
    UILabel *customLab = [[UILabel alloc] initWithFrame:CGRectMake(0, 0, 100, 30)];
    [customLab setTextColor:UUWhite];
    [customLab setText:NSLocalizedString(@"Device_Infomation",@"设备详情")];
    customLab.font = [UIFont boldSystemFontOfSize:20];
    self.navigationItem.titleView = customLab;
     //
    CGRect mainScreen = self.view.frame;
    NSArray *sArray;
        sArray = [NSArray arrayWithObjects:
                  [NSDictionary dictionaryWithObjectsAndKeys:NSLocalizedString(@"Device_Name",@"设备名"),kTitleKey,NSLocalizedString(@"OXIMETER",@"智能血氧"),kInfoKey, nil],
                  [NSDictionary dictionaryWithObjectsAndKeys:NSLocalizedString(@"Brand",@"品牌"),kTitleKey,NSLocalizedString(@"wearcare",@"微康"),kInfoKey, nil],
                  [NSDictionary dictionaryWithObjectsAndKeys:NSLocalizedString(@"Model",@"型号"),kTitleKey,self.selectedDeviecInfo.deviceModelName,kInfoKey, nil],
                  [NSDictionary dictionaryWithObjectsAndKeys:NSLocalizedString(@"Serial_Number",@"序列号"),kTitleKey,self.selectedDeviecInfo.serialNumber,kInfoKey, nil],
                  [NSDictionary dictionaryWithObjectsAndKeys:NSLocalizedString(@"Firmware_Version",@"固件版本"),kTitleKey, self.selectedDeviecInfo.firmwareVersion,kInfoKey, nil],
                  [NSDictionary dictionaryWithObjectsAndKeys:NSLocalizedString(@"FirmSub_Version",@"从固件版本"),kTitleKey, self.selectedDeviecInfo.firmSubVersion,kInfoKey, nil],
                  nil];
    self.sectionData = [NSMutableArray arrayWithObjects:sArray, nil];
    self.infoTableView = [[UITableView alloc] initWithFrame:CGRectMake(0, 0, mainScreen.size.width, mainScreen.size.height) style:UITableViewStyleGrouped];
    _infoTableView.scrollEnabled = NO;
    _infoTableView.delegate = self;
    _infoTableView.dataSource = self;
    _infoTableView.backgroundColor = [UIColor whiteColor];
    [self.view addSubview:_infoTableView];
}

- (void)didReceiveMemoryWarning
{
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}

#pragma mark  TableView delegate and datesources

- (NSInteger)numberOfSectionsInTableView:(UITableView *)tableView
{
    return [_sectionData count];
}

- (NSInteger)tableView:(UITableView *)tableView numberOfRowsInSection:(NSInteger)section
{
    return [[_sectionData objectAtIndex:section] count];
}

- (UITableViewCell *)tableView:(UITableView *)tableView cellForRowAtIndexPath:(NSIndexPath *)indexPath
{
    static NSString *CellIdentifier = @"Cell";
    
    UITableViewCell *cell = [tableView dequeueReusableCellWithIdentifier:CellIdentifier];
    
    if (cell == nil) {
        cell = [[UITableViewCell alloc] initWithStyle:indexPath.section == 2 ? UITableViewCellStyleDefault :UITableViewCellStyleValue1 reuseIdentifier:nil];
    }
    
    if (indexPath.section == 2) {
        cell.textLabel.text = [[[_sectionData objectAtIndex:indexPath.section] objectAtIndex:indexPath.row] objectForKey:kTitleKey];
        cell.textLabel.textAlignment = NSTextAlignmentCenter;
        cell.textLabel.textColor = [UIColor redColor];
        cell.textLabel.backgroundColor = [UIColor clearColor];
    } else {
        cell.textLabel.text = [[[_sectionData objectAtIndex:indexPath.section] objectAtIndex:indexPath.row] objectForKey:kTitleKey];
        cell.backgroundColor = [UIColor whiteColor];
        cell.textLabel.textColor = [UIColor blackColor];
        cell.textLabel.font = [UIFont systemFontOfSize:16.0f];
        cell.textLabel.highlightedTextColor = [UIColor whiteColor];
        cell.detailTextLabel.textColor = [UIColor darkGrayColor];
        cell.detailTextLabel.text = [[[_sectionData objectAtIndex:indexPath.section] objectAtIndex:indexPath.row] objectForKey:kInfoKey];
        cell.detailTextLabel.highlightedTextColor = [UIColor whiteColor];
        cell.textLabel.textAlignment = NSTextAlignmentLeft;
    }
    cell.accessoryType = UITableViewCellAccessoryNone;
    cell.selectionStyle = UITableViewCellSelectionStyleNone;
    /*
    if (!IOS7) {
        NSInteger totalRow = [tableView numberOfRowsInSection:indexPath.section];
        UIImage *rowCellImage = nil;
        if (totalRow == 1) {
            rowCellImage = [[UIImage imageNamed:@"bg_cell_full.png"] stretchableImageWithLeftCapWidth:5 topCapHeight:5];
        } else {
            if (indexPath.row == 0) {
                rowCellImage = [[UIImage imageNamed:@"bg_cell_top.png"] stretchableImageWithLeftCapWidth:5 topCapHeight:5];
            } else if (indexPath.row == totalRow - 1) {
                rowCellImage = [[UIImage imageNamed:@"bg_cell_bottom.png"] stretchableImageWithLeftCapWidth:5 topCapHeight:5];
            } else {
                rowCellImage = [[UIImage imageNamed:@"bg_cell_middle.png"] stretchableImageWithLeftCapWidth:5 topCapHeight:5];
            }
        }
        cell.backgroundView = [[UIImageView alloc] initWithImage:rowCellImage];
        cell.selectedBackgroundView = [[UIImageView alloc] initWithImage:[rowCellImage fillColor:[UIColor colorWithHexString:@"#59b9c6"] backgroundColor:[UIColor clearColor]]];
    }*/
    return cell;
}

- (void)tableView:(UITableView *)tableView didSelectRowAtIndexPath:(NSIndexPath *)indexPath
{
    [tableView deselectRowAtIndexPath:indexPath animated:YES];
    
}
@end
