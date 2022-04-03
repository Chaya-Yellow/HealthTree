//
//  settingViewController.m
//  BLT-Spo2Monitor
//
//  Created by luteng on 15/11/24.
//  Copyright © 2015年 luteng. All rights reserved.
//

#import "settingViewController.h"
#import "consDefine.h"
#import "aboutUSViewController.h"
#import "changePasswordViewController.h"
#import "UIColor+BltSoft.h"
#import "MXBluetoothManager.h"
#import "MXUserService.h"
#import "OMELoginDataService.h"
#import "MBProgressHUD.h"
#import "MXUserData.h"
#import "AttributedLabel.h"
#import "MXUserDefaults.h"
#import "UIImage+Omesoft.h"
@interface settingViewController()<UIPickerViewDataSource,UIPickerViewDelegate,OMEDataServiceDelegate,MBProgressHUDDelegate>
@property(nonatomic, strong)UITableView* settingTableview;
@property(nonatomic, retain)NSMutableArray *sectionDatas;
@property(nonatomic, strong)UIButton* spo2UnitBtn;
@property(nonatomic, strong)UIButton* PRUnitBtn;
@property(nonatomic, strong)UIButton* RespUnitBtn;
@property(nonatomic, strong)UIButton* PIUnitBtn;
@property(nonatomic, strong)UIPickerView *tempPickerView;
@property(nonatomic, strong)UIView *contentView;
@property(nonatomic, strong)NSMutableArray *pickerSectionData;
@property(nonatomic,strong)OMELoginDataService* dataSerive;
@property(nonatomic,strong)MBProgressHUD* progressedHUD;
@property(nonatomic, strong)AttributedLabel *heightUnitLabel;
@property(nonatomic, strong)AttributedLabel *weightUnitLabel;
@end
@implementation settingViewController


- (void)viewDidLoad {
    [super viewDidLoad];
    self.view.backgroundColor = UUBackGroundColor;
    CGRect rect = self.view.bounds;
    float left = 0;
    float top = 0;
    float width = CGRectGetWidth(rect);
    float height = CGRectGetHeight(rect)-5;
    NSArray* UnitLimit = [NSArray arrayWithObjects:NSLocalizedString(@"HeightUnit", @"HeightUnit"),NSLocalizedString(@"WeightUnit",@"WeightUnit"),nil];
    //NSArray* paramLimit = [NSArray arrayWithObjects:@"SpO2",@"PR",@"Resp",@"PI", nil];
    NSArray* FirstArray = [NSArray arrayWithObjects:NSLocalizedString(@"About_us",@"关于我们"),NSLocalizedString(@"Log_Changing_pwd",@"修改密码"), nil];
    NSArray* layout = [NSArray arrayWithObjects:NSLocalizedString(@"Layout", @"退出"),nil];
    self.sectionDatas = [NSMutableArray arrayWithObjects:UnitLimit,FirstArray,layout,nil];
    //
    _settingTableview = [[UITableView alloc]initWithFrame:CGRectMake(left, top, width, height)];
    _settingTableview.backgroundColor = UUBackGroundColor;
    _settingTableview.delegate = self;
    _settingTableview.dataSource = self;
//    _settingTableview.scrollEnabled = NO;
    _settingTableview.tableFooterView = [[UIView alloc] initWithFrame:CGRectZero];
//    _settingTableview.separatorStyle = UITableViewCellSeparatorStyleNone;
    //
    [self.navigationController.navigationBar setBarTintColor:UUNavBackColor];
    [self.navigationController.navigationBar setTintColor:UUWhite];
    UILabel *customLab = [[UILabel alloc] initWithFrame:CGRectMake(0, 0, 100, 30)];
    [customLab setTextColor:UUWhite];
    [customLab setText:NSLocalizedString(@"Setting",@"设置")];
    customLab.font = [UIFont boldSystemFontOfSize:20];
    self.navigationItem.titleView = customLab;
    [self.view addSubview:_settingTableview];
    //
    UIView *view = [[[[UIApplication sharedApplication] delegate] window] rootViewController].view;
    [view addSubview:self.contentView];
    self.contentView.clipsToBounds = YES;
    self.contentView.layer.cornerRadius = 8;
    self.contentView.hidden = YES;
    //
    _dataSerive = [[OMELoginDataService alloc]init];
    _dataSerive.delegate = self;
    // Do any additional setup after loading the view, typically from a nib.
}

- (void)didReceiveMemoryWarning {
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}


-(void)viewWillAppear:(BOOL)animated
{

}

-(void)viewWillDisappear:(BOOL)animated
{
    [self cleanContentView];
}
//设置view，将替代titleForHeaderInSection方法
//重写表头view 可实现个性化

- (UIView *)tableView:(UITableView *)tableView viewForHeaderInSection:(NSInteger)section
{
    float h;
    h = TABLE_VIEW_HEAD_H_IPHONE-25;
    UIView *view = [[UIView alloc] initWithFrame:CGRectMake(0, 0, tableView.frame.size.width, h)];
    view.backgroundColor = UUBackGroundColor;
    return view;
}


// UITableViewDataSource协议中的方法，该方法的返回值决定表格包含多少个分区
- (NSInteger) numberOfSectionsInTableView:(UITableView *)tableView
{
    return [_sectionDatas count];
}

- (NSInteger)tableView:(UITableView *)tableView
	numberOfRowsInSection:(NSInteger)section
{
    return [[_sectionDatas objectAtIndex:section] count];
}

- (UITableViewCell *)tableView:(UITableView *)tableView
         cellForRowAtIndexPath:(NSIndexPath *)indexPath
{
    // 获取分区号
    NSUInteger sectionNo = indexPath.section;
    // 获取表格行的行号
    NSUInteger rowNo = indexPath.row;
    static NSString *aCellIdentifier = @"aCell";
    UITableViewCell *aCell = [tableView dequeueReusableCellWithIdentifier:aCellIdentifier];
    if (aCell == nil) {
        aCell = [[UITableViewCell alloc] initWithStyle:UITableViewCellStyleDefault reuseIdentifier:nil];
    }
    aCell.selectionStyle = UITableViewCellSelectionStyleNone;
    switch (sectionNo) {
        case 0:
            {
                if(rowNo ==0)
                {
                    aCell.textLabel.textColor = [UIColor whiteColor];
                    aCell.textLabel.text = [[_sectionDatas objectAtIndex:indexPath.section] objectAtIndex:indexPath.row];
                    UIButton* heighBtn= [[UIButton alloc] initWithFrame:CGRectMake(0, 0, 70, 37)];
                    heighBtn.backgroundColor = [UIColor clearColor];
                    self.heightUnitLabel.center = CGPointMake(heighBtn.frame.size.width / 2.0, heighBtn.frame.size.height / 2.0);
                    [heighBtn addTarget:self action:@selector(heightUnitButtonPressed) forControlEvents:UIControlEventTouchUpInside];
                    //self.heightUnitLabel.backgroundColor = [UIColor redColor];
                    [heighBtn addSubview:self.heightUnitLabel];
                    aCell.accessoryView = heighBtn;
                    //aCell.backgroundColor =UUFrontColor;
                    [self reflashHeightUnit];
                }
                else if(rowNo ==1)
                {
                    aCell.textLabel.textColor = [UIColor whiteColor];
                    aCell.textLabel.text = [[_sectionDatas objectAtIndex:indexPath.section] objectAtIndex:indexPath.row];
                    UIButton* weightBtn= [[UIButton alloc] initWithFrame:CGRectMake(0, 0, 70, 37)];
                    weightBtn.backgroundColor = [UIColor clearColor];
                    self.weightUnitLabel.center = CGPointMake(weightBtn.frame.size.width / 2.0, weightBtn.frame.size.height / 2.0);
                    [weightBtn addTarget:self action:@selector(weightUnitButtonPressed) forControlEvents:UIControlEventTouchUpInside];
                    //_weightUnitLabel.backgroundColor = [UIColor redColor];
                    [weightBtn addSubview:self.weightUnitLabel];
                    aCell.accessoryView = weightBtn;
                    //aCell.backgroundColor =UUFrontColor;
                    [self reflashWeightUnit];
                }
            }
            break;
            /*
        case 1:
             {
                 if(rowNo ==0)
                 {
                     aCell.textLabel.textColor = [UIColor whiteColor];
                     aCell.textLabel.text = [[_sectionDatas objectAtIndex:indexPath.section] objectAtIndex:indexPath.row];
                     _spo2UnitBtn = [[UIButton alloc] initWithFrame:CGRectMake(0, 0, 70, 40)];
                     _spo2UnitBtn.backgroundColor = [UIColor clearColor];
                     [_spo2UnitBtn setTitleColor:UUWhite forState:UIControlStateNormal];
                     [_spo2UnitBtn setTitle:@"100/92" forState:UIControlStateNormal];
                     _spo2UnitBtn.titleLabel.font = [UIFont systemFontOfSize:16];
                     [_spo2UnitBtn addTarget:self action:@selector(Spo2UnitProc:) forControlEvents:UIControlEventTouchUpInside];
                     aCell.accessoryView = _spo2UnitBtn;
                     //aCell.backgroundColor =UUFrontColor;
                 }
                 else if(rowNo ==1)
                 {
                     aCell.textLabel.textColor = [UIColor whiteColor];
                     aCell.textLabel.text = [[_sectionDatas objectAtIndex:indexPath.section] objectAtIndex:indexPath.row];
                     _PRUnitBtn = [[UIButton alloc] initWithFrame:CGRectMake(0, 0, 70, 40)];
                     _PRUnitBtn.backgroundColor = [UIColor clearColor];
                     [_PRUnitBtn setTitleColor:UUWhite forState:UIControlStateNormal];
                     [_PRUnitBtn setTitle:@"90/60" forState:UIControlStateNormal];
                       _PRUnitBtn.titleLabel.font = [UIFont systemFontOfSize:16];
                     [_PRUnitBtn addTarget:self action:@selector(PRUnitBtnProc:) forControlEvents:UIControlEventTouchUpInside];
                     aCell.accessoryView = _PRUnitBtn;
                    // aCell.backgroundColor =UUFrontColor;
                 }
                 else if(rowNo ==2)
                 {
                     aCell.textLabel.textColor = [UIColor whiteColor];
                     aCell.textLabel.text = [[_sectionDatas objectAtIndex:indexPath.section] objectAtIndex:indexPath.row];
                     _RespUnitBtn = [[UIButton alloc] initWithFrame:CGRectMake(0, 0, 70, 40)];
                     _RespUnitBtn.backgroundColor = [UIColor clearColor];
                     [_RespUnitBtn setTitleColor:UUWhite forState:UIControlStateNormal];
                     [_RespUnitBtn setTitle:@"30/8" forState:UIControlStateNormal];
                       _RespUnitBtn.titleLabel.font = [UIFont systemFontOfSize:16];
                     [_RespUnitBtn addTarget:self action:@selector(RESPUnitBtnProc:) forControlEvents:UIControlEventTouchUpInside];
                     aCell.accessoryView = _RespUnitBtn;
                    // aCell.backgroundColor =UUFrontColor;
                 }
                 else
                 {
                     aCell.textLabel.textColor = [UIColor whiteColor];
                     aCell.textLabel.text = [[_sectionDatas objectAtIndex:indexPath.section] objectAtIndex:indexPath.row];
                     _PIUnitBtn = [[UIButton alloc] initWithFrame:CGRectMake(0, 0, 70, 40)];
                     _PIUnitBtn.backgroundColor = [UIColor clearColor];
                     [_PIUnitBtn setTitleColor:UUWhite forState:UIControlStateNormal];
                     [_PIUnitBtn setTitle:@"20/5" forState:UIControlStateNormal];
                       _PIUnitBtn.titleLabel.font = [UIFont systemFontOfSize:16];
                     [_PIUnitBtn addTarget:self action:@selector(PIUnitBtnProc:) forControlEvents:UIControlEventTouchUpInside];
                     aCell.accessoryView = _PIUnitBtn;
                    // aCell.backgroundColor =UUFrontColor;
                 }
             }
            break;*/
        case 1:
            aCell.textLabel.text = [[_sectionDatas objectAtIndex:indexPath.section] objectAtIndex:indexPath.row];
            aCell.textLabel.textAlignment = NSTextAlignmentLeft;
            aCell.accessoryType = UITableViewCellAccessoryDisclosureIndicator;
            //aCell.backgroundColor =UUFrontColor;
            aCell.textLabel.textColor = [UIColor whiteColor];
            break;
        case 2:
        {
            UIButton* btn =[[UIButton alloc]initWithFrame:CGRectMake(0, 0, self.view.frame.size.width, 40)];
            //btn.backgroundColor = [UIColor colorWithHexString:@"#cc4452"];
            [btn setBackgroundImage:[UIImage createImageWithColor:[UIColor colorWithRed:199.0/255 green:72.0/255 blue:105.0/255 alpha:1.0]] forState:UIControlStateNormal];
            [btn setBackgroundImage:[UIImage createImageWithColor:[UIColor colorWithRed:199.0/255 green:102.0/255 blue:127.0/255 alpha:1.0]] forState:UIControlStateHighlighted];
            [btn setTitleColor:[UIColor whiteColor] forState:UIControlStateNormal];
            //[btn setTitleColor:[[UIColor whiteColor] colorWithAlphaComponent:0.7] forState:UIControlStateHighlighted];
            [btn setTitle:[[_sectionDatas objectAtIndex:indexPath.section] objectAtIndex:indexPath.row] forState:UIControlStateNormal];
            [btn addTarget:self action:@selector(layoutProc:) forControlEvents:UIControlEventTouchUpInside];
            [aCell addSubview:btn];
            [aCell setSeparatorInset:UIEdgeInsetsMake(0, 0, 0, self.view.frame.size.width)];
           // aCell.backgroundColor =UUFrontColor;
        }
            break;
        default:
            break;
    }
    aCell.textLabel.font = [UIFont systemFontOfSize:16];
    aCell.backgroundColor = UUFrontColor;
    return aCell;
}


-(void)tableView:(UITableView *)tableView didSelectRowAtIndexPath:(NSIndexPath *)indexPath
{
    [tableView deselectRowAtIndexPath:indexPath animated:YES];
    switch (indexPath.section) {
        case 0:
            break;
        case 1:
            if(indexPath.row ==0)
            {
                [self cleanContentView];
                aboutUSViewController* about = [[aboutUSViewController alloc]init];
                [self.navigationController pushViewController:about animated:YES];
            }
            else if(indexPath.row ==1)
            {
                [self cleanContentView];
                changePasswordViewController* change = [[changePasswordViewController alloc]init];
                [self.navigationController pushViewController:change animated:YES];
            }
            break;
        default:
            break;
    }
}

-(CGFloat)tableView:(UITableView *)tableView heightForRowAtIndexPath:(NSIndexPath *)indexPath
{
    return TABLE_VIEW_CELL_H_IPHONE-3;
}


//设置表头的高度
- (CGFloat)tableView:(UITableView *)tableView heightForHeaderInSection:(NSInteger)section
{
    return TABLE_VIEW_CELL_H_IPHONE-25;
}

//Section Footer的高度
- (CGFloat)tableView:(UITableView *)tableView heightForFooterInSection:(NSInteger)section
{
    return 0.01;
}

- (UIView *)contentView
{
    if (!_contentView) {
        CGRect mainScreen = [[UIScreen mainScreen] bounds];
        _contentView = [[UIView alloc] initWithFrame:CGRectMake(0, mainScreen.size.height, 220, 240)];
        UILabel *topbarBgLayer = [[UILabel alloc] init];
        topbarBgLayer.backgroundColor = UUGreen;//[UIColor colorWithHexString:@"#a14a70"];
        topbarBgLayer.frame = CGRectMake(0, 0,220,30);
        //topbarBgLayer.layer.cornerRadius = 8;
        topbarBgLayer.clipsToBounds = YES;
        [_contentView addSubview:topbarBgLayer];
        
        UIButton *doneButton = [UIButton buttonWithType:UIButtonTypeCustom];
        NSString *doneStr = NSLocalizedString(@"Done", @"完成");
        CGSize doneSize = [doneStr sizeWithFont:doneButton.titleLabel.font constrainedToSize:CGSizeMake(220, 30) lineBreakMode:NSLineBreakByWordWrapping];
        doneButton.frame = CGRectMake(220 - doneSize.width - 10, 0, doneSize.width, 30);
        doneButton.backgroundColor = [UIColor clearColor];
        [doneButton setTitle:doneStr forState:UIControlStateNormal];
        [doneButton setTitleColor:[UIColor whiteColor] forState:UIControlStateNormal];
        [doneButton setTitleColor:[[UIColor whiteColor] colorWithAlphaComponent:0.7] forState:UIControlStateHighlighted];
        [doneButton addTarget:self action:@selector(doneButtonPressed:) forControlEvents:UIControlEventTouchUpInside];
        doneButton.layer.cornerRadius = 8;
        [_contentView addSubview:doneButton];
        _contentView.backgroundColor = [UIColor whiteColor];
        _contentView.layer.cornerRadius = 8;
        self.tempPickerView = [[UIPickerView alloc] initWithFrame:CGRectMake(0, 30, 220, 210)];
        _tempPickerView.delegate = self;
        _tempPickerView.dataSource = self;
        _tempPickerView.showsSelectionIndicator = YES;
        _tempPickerView.backgroundColor = [UIColor whiteColor];
        [_contentView addSubview:_tempPickerView];
    }
    return _contentView;
}

- (AttributedLabel *)heightUnitLabel
{
    if (!_heightUnitLabel) {
        _heightUnitLabel = [[AttributedLabel alloc] initWithFrame:CGRectMake(0, 0, 60, 25)];
        NSString* string = [NSString stringWithFormat:@"%@/%@",NSLocalizedString(@"Height_Unit_CM", @"CM") ,NSLocalizedString(@"Height_Unit_IN", @"IN")];
        _heightUnitLabel.text = string;
        _heightUnitLabel.textAlignment = NSTextAlignmentRight;
        [_heightUnitLabel setFont:[UIFont systemFontOfSize:16.0f] fromIndex:0 length:_heightUnitLabel.text.length];
    }
    return _heightUnitLabel;
}

- (AttributedLabel *)weightUnitLabel
{
    if (!_weightUnitLabel) {
        _weightUnitLabel = [[AttributedLabel alloc] initWithFrame:CGRectMake(0, 0, 60, 25)];
        _weightUnitLabel.textAlignment = NSTextAlignmentRight;
        _weightUnitLabel.text = [NSString stringWithFormat:@"%@/%@",NSLocalizedString(@"Weight_Unit_KG",@"KG"),NSLocalizedString(@"Weight_Unit_LB", @"LB")];
        [_weightUnitLabel setFont:[UIFont systemFontOfSize:16.0f] fromIndex:0 length:_weightUnitLabel.text.length];
    }
    return _weightUnitLabel;
}

- (void)weightUnitButtonPressed
{
    if ([[MXUserDefaults sharedInstance] isKG]) {
        [[MXUserDefaults sharedInstance] setKG:NO];
    } else {
        [[MXUserDefaults sharedInstance] setKG:YES];
    }
    [self reflashWeightUnit];
}

- (void)heightUnitButtonPressed
{
    if ([[MXUserDefaults sharedInstance] isCM]) {
        [[MXUserDefaults sharedInstance] setCM:NO];
    } else {
        [[MXUserDefaults sharedInstance] setCM:YES];
    }
    [self reflashHeightUnit];
}

- (void)reflashWeightUnit
{
    if ([[MXUserDefaults sharedInstance] isKG]) {
        [_weightUnitLabel setColor:[UIColor whiteColor] fromIndex:0 length:2];
        [_weightUnitLabel setColor:[UIColor darkGrayColor] fromIndex:_weightUnitLabel.text.length - 3 length:3];
        [_weightUnitLabel setNeedsDisplay];
    } else {
        NSRange pos = [_heightUnitLabel.text rangeOfString:@"/"];
        [_weightUnitLabel setColor:[UIColor whiteColor] fromIndex:pos.location length:_weightUnitLabel.text.length-pos.location];
        [_weightUnitLabel setColor:[UIColor darkGrayColor] fromIndex:0 length:3];
        [_weightUnitLabel setNeedsDisplay];
    }
}

- (void)reflashHeightUnit
{
    if ([[MXUserDefaults sharedInstance] isCM]) {
        [_heightUnitLabel setColor:[UIColor whiteColor] fromIndex:0 length:2];
        [_heightUnitLabel setColor:[UIColor darkGrayColor] fromIndex:_heightUnitLabel.text.length - 3 length:3];
        [_heightUnitLabel setNeedsDisplay];
    } else {
        NSRange pos = [_heightUnitLabel.text rangeOfString:@"/"];
        [_heightUnitLabel setColor:[UIColor whiteColor] fromIndex:pos.location length:_heightUnitLabel.text.length-pos.location];
        [_heightUnitLabel setColor:[UIColor darkGrayColor] fromIndex:0 length:3];
        [_heightUnitLabel setNeedsDisplay];
    }
}


- (void)showContentView:(BOOL)isShow
{
    CGRect mainScreen = [[UIScreen mainScreen] bounds];
    mainScreen.size.height -= 20;
    if (isShow) {
        [_contentView.superview bringSubviewToFront:_contentView];
        _contentView.hidden = NO;
        [UIView animateWithDuration:0.35 animations:^{
            _contentView.frame = CGRectMake((mainScreen.size.width-_contentView.frame.size.width)/2, (mainScreen.size.height - _contentView.frame.size.height)/2+20, _contentView.frame.size.width, _contentView.frame.size.height);
        } completion:^(BOOL finished) {
            
        }];
    } else {
        [UIView animateWithDuration:0.35 animations:^{
            _contentView.frame = CGRectMake((mainScreen.size.width-_contentView.frame.size.width)/2, mainScreen.size.height, _contentView.frame.size.width, _contentView.frame.size.height);
        } completion:^(BOOL finished) {
            _contentView.hidden = YES;
        }];
    }
}

-(void)Spo2UnitProc:(id)sender
{
    self.pickerSectionData = [self getPickerOptionData:0];
    [self.tempPickerView reloadAllComponents];
   [_tempPickerView selectRow:2 inComponent:1 animated:NO];
   [_tempPickerView selectRow:2 inComponent:2 animated:NO];
    //NSLog(@"1111");
    [self showContentView:YES];
}

-(void)PRUnitBtnProc:(id)sender
{
    self.pickerSectionData = [self getPickerOptionData:1];
    [self.tempPickerView reloadAllComponents];
    [_tempPickerView selectRow:2 inComponent:1 animated:NO];
    [_tempPickerView selectRow:2 inComponent:2 animated:NO];
    [self showContentView:YES];
}

-(void)PIUnitBtnProc:(id)sender
{
    self.pickerSectionData = [self getPickerOptionData:3];
    [self.tempPickerView reloadAllComponents];
    [_tempPickerView selectRow:2 inComponent:1 animated:NO];
    [_tempPickerView selectRow:2 inComponent:2 animated:NO];
    [self showContentView:YES];
}

-(void)RESPUnitBtnProc:(id)sender
{
    self.pickerSectionData = [self getPickerOptionData:2];
    [self.tempPickerView reloadAllComponents];
    [_tempPickerView selectRow:2 inComponent:1 animated:NO];
    [_tempPickerView selectRow:2 inComponent:2 animated:NO];
    [self showContentView:YES];
}

- (void)cleanContentView
{
    if (_contentView.hidden == NO) {
        _contentView.hidden = YES;
    }
}


-(void)doneButtonPressed:(id)sender
{
    [self showContentView:NO];
}

- (NSMutableArray *)getPickerOptionData:(int)index
{
    NSMutableArray *result = [[NSMutableArray alloc] init];
    NSMutableArray *downLimit = [[NSMutableArray alloc] init];
    NSMutableArray *upLimit = [[NSMutableArray alloc] init];
    NSMutableArray* titleArray;
    switch (index) {
        case 0: //SPO2
            for (int j = 86; j < 101; j++) {
                [upLimit addObject:[NSString stringWithFormat:@"%d",j]];
            }
            for (int i = 85; i <100; i++) {
                [downLimit addObject:[NSString stringWithFormat:@"%d",i]];
            }
            titleArray = [NSMutableArray arrayWithObjects:@"SpO2", nil];
            break;
        case 1: //PR
            for (int j = 60; j < 181; j++) {
                [upLimit addObject:[NSString stringWithFormat:@"%d",j]];
            }
            for (int i = 59; i <180; i++) {
                [downLimit addObject:[NSString stringWithFormat:@"%d",i]];
            }
            titleArray = [NSMutableArray arrayWithObjects:@"Pr", nil];
            break;
        case 2: //RESP
            for (int j = 1; j < 31; j++) {
                [upLimit addObject:[NSString stringWithFormat:@"%d",j]];
            }
            for (int i = 0; i <30; i++) {
                [downLimit addObject:[NSString stringWithFormat:@"%d",i]];
            }
            titleArray = [NSMutableArray arrayWithObjects:@"Resp", nil];
            break;
        case 3: //PI
            for (int j = 1; j < 21; j++) {
                [upLimit addObject:[NSString stringWithFormat:@"%d",j]];
            }
            for (int i = 0; i <20; i++) {
                [downLimit addObject:[NSString stringWithFormat:@"%d",i]];
            }
            titleArray = [NSMutableArray arrayWithObjects:@"PI", nil];
            break;
        default:
            break;
    }
    [result addObject:titleArray];
    [result addObject:upLimit];
    [result addObject:downLimit];
    //NSLog(@"result =%@", result);
    return result;
}

//?////////////////////////////
- (NSInteger)numberOfComponentsInPickerView:(UIPickerView *)pickerView {
    return [_pickerSectionData count];
}

// pickerView 每列个数
- (NSInteger)pickerView:(UIPickerView *)pickerView numberOfRowsInComponent:(NSInteger)component {
   return [[_pickerSectionData objectAtIndex:component] count];
}

// 每列宽度
- (CGFloat)pickerView:(UIPickerView *)pickerView widthForComponent:(NSInteger)component {
    if(component == 0)
    {
        return 80;
    }
    else
        return 50;
}
// 返回选中的行
- (void)pickerView:(UIPickerView *)pickerView didSelectRow:(NSInteger)row inComponent:(NSInteger)component
{
    /*NSString* str;
    if (component == 0) {
        str = [_titleArray objectAtIndex:row];
    } else {
        str = [_dataArray objectAtIndex:row];
    }*/
    
}

//返回当前行的内容,此处是将数组中数值添加到滚动的那个显示栏上
-(NSString*)pickerView:(UIPickerView *)pickerView titleForRow:(NSInteger)row forComponent:(NSInteger)component
{
    //NSLog(@"%@", [[_pickerSectionData objectAtIndex:component] objectAtIndex:row]);
    return [[_pickerSectionData objectAtIndex:component] objectAtIndex:row];
}

-(void)layoutProc:(id)sender
{
    //
    self.progressedHUD.labelText =@"";
    self.progressedHUD.detailsLabelText =nil;
    self.progressedHUD.labelText = NSLocalizedString(@"Login_Logouting", @"正在退出");
    self.progressedHUD.mode = MBProgressHUDModeIndeterminate;
    [self.progressedHUD show:YES];
    //
    [[MXBluetoothManager sharedInstance] cancelPeripheralConnection];
    [[MXBluetoothManager sharedInstance] stopScan];
    [[MXBluetoothManager sharedInstance] setDelegate:nil];
    //[[MXBluetoothManager sharedInstance] appLayout];
    MXUserData* userData = [[MXUserData alloc] init];
    NSArray *loginedArray = [userData getLoginInfo];
    if(loginedArray && loginedArray.count>0)
    {
        NSInteger idNum = [[[loginedArray objectAtIndex:0] objectForKey:@"id"] integerValue];
        NSString *key = [[loginedArray objectAtIndex:0] objectForKey:@"clientKey"];
        [self.dataSerive cancel];
        [_dataSerive requestOMELogoutWithMemberId:idNum Key:key];
    }
    else
    {
        NSLog(@"无任何用户登陆");
        [self showWarmTitle:NSLocalizedString(@"Layout_Failed", @"退出失败") message:NSLocalizedString(@"Layout_WithoutLogin", @"无效登陆") withImageName:@"37x-Failed.png"];
    }
}

#pragma mark -  OMEDataService

- (void)dataService:(OMEDataService *)dataService didFinishWithData:(NSDictionary *)dataDic
{
    NSLog(@"dataDic = %@", dataDic);
    NSInteger codeIndex = [[dataDic objectForKey:@"error_code"] intValue];
    //NSLog(@"codeIndex = %d", codeIndex);
    if (codeIndex) {
        NSLog(@"-----");
        [self showWarmTitle:NSLocalizedString(@"Layout_Failed", @"退出失败") message:NSLocalizedString(@"Layout_Exception", @"退出失败，发生未知异常。") withImageName:@"37x-Failed.png"];
    } else {
         BOOL result = [(MXUserService *)[MXUserService sharedInstance] logout];
        //NSLog(@"result = %d",result);
        if(result)
        {
            [self.progressedHUD hide:YES afterDelay:0.5];
            [self.navigationController popToRootViewControllerAnimated:YES];
        }
        else
        {
              [self showWarmTitle:NSLocalizedString(@"Layout_Failed", @"退出失败") message:NSLocalizedString(@"Layout_Exception", @"退出失败，发生未知异常。") withImageName:@"37x-Failed.png"];
        }
    }
}

- (void)dataService:(OMEDataService *)dataService didFailWithError:(NSError *)error
{
    [self showWarmTitle:error.localizedFailureReason message:error.localizedDescription withImageName:@"37x-Failed.png"];
}

- (void)showWarmTitle:(NSString *)title message:(NSString *)msg withImageName:(NSString*)imageName
{
    self.progressedHUD.customView = [[UIImageView alloc] initWithImage:[UIImage imageNamed:imageName]];
    _progressedHUD.mode = MBProgressHUDModeCustomView;
    if(!title)
    {
        _progressedHUD.labelText =nil;
    }
    else
    {
        _progressedHUD.labelText = title;
    }
    _progressedHUD.detailsLabelText = msg;
    [_progressedHUD show:YES];
    [_progressedHUD hide:YES afterDelay:2.0];
}

-(MBProgressHUD*)progressedHUD
{
    if(!_progressedHUD)
    {
        _progressedHUD = [[MBProgressHUD alloc] initWithView:self.navigationController.view];
        [self.navigationController.view addSubview:_progressedHUD];
        //_progressedHUD.customView = [[UIImageView alloc] initWithImage:[UIImage imageNamed:@"37x-Failed.png"]];
        _progressedHUD.delegate = self;
    }
    return _progressedHUD;
}

@end
