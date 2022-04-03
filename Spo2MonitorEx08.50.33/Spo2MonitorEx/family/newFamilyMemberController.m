//
//  newFamilyMemberController.m
//  BLT-Spo2Monitor
//
//  Created by luteng on 16/7/5.
//  Copyright © 2016年 luteng. All rights reserved.
//

#import "newFamilyMemberController.h"
#import "MXUser.h"
#import "OMEImageView.h"
#import "OMESoft.h"
#import "consDefine.h"
#import "MBProgressHUD.h"
#import "WGPickerView.h"
#import "MXUserData.h"
#import "Constants.h"
#import "OMEFamilyDataService.h"
#import "MXUserDefaults.h"
#import "NSDataEx.h"
@interface newFamilyMemberController()<UITableViewDelegate, UITableViewDataSource,UITextFieldDelegate,UIActionSheetDelegate,UIImagePickerControllerDelegate,UINavigationControllerDelegate,MBProgressHUDDelegate,UIPickerViewDelegate, UIPickerViewDataSource,OMEDataServiceDelegate>
@property (retain, nonatomic) UITableView *tableView;
@property (retain, nonatomic) OMEImageView *userImageView;
@property (nonatomic, retain) UITextField *nameTextField;
@property (retain, nonatomic) MXUser *selectedUser;
@property (nonatomic, retain) UIButton* saveButton;
@property (nonatomic, retain) MBProgressHUD *progressHUD;
@property (retain, nonatomic) UIDatePicker *datePicker;
@property (strong, nonatomic)UIView* contentView;
@property (retain, nonatomic)WGPickerView *pickerView;
@property (retain, nonatomic)NSMutableArray *pickerDatas;
@property (retain, nonatomic)UILabel* userBirthLabel;
@property (retain, nonatomic)UILabel* userGenderLabel;
@property (retain, nonatomic)UILabel* userHeightLabel;
@property (retain, nonatomic)UILabel* userWeightLabel;
@property (strong, nonatomic)OMEFamilyDataService *dataService;
@property (nonatomic)int    pickerIndex;
@property (strong, nonatomic)NSData* imageData;
@property (retain, nonatomic) NSString *imageFileName;
@end

@implementation newFamilyMemberController


- (void)dealloc
{
    _tableView.delegate = nil;
    _tableView.dataSource = nil;
    _nameTextField.delegate = nil;
    _progressHUD.delegate = nil;
    _nameTextField = nil;
    _saveButton = nil;
    _tableView= nil;
    _datePicker = nil;
    _progressHUD = nil;
}

- (id)initWithUser:(MXUser *)user
{
    self = [super init];
    if (self) {
        self.selectedUser = user;
        //nameStr = user.name;
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

-(void)viewWillAppear:(BOOL)animated
{
    
}

-(void)viewWillDisappear:(BOOL)animated
{
    [self cleanContentView];
}

- (void)didReceiveMemoryWarning
{
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}

-(void)viewDidLoad
{
    [super  viewDidLoad];
    //
    [self.navigationController.navigationBar setBarTintColor:UUNavBackColor];
    [self.navigationController.navigationBar setTintColor:UUWhite];
    UILabel *customLab = [[UILabel alloc] initWithFrame:CGRectMake(0, 0, 100, 30)];
    [customLab setTextColor:UUWhite];
    [customLab setText:NSLocalizedString(@"Family_Mem_Adding",@"添加家庭成员")];
    customLab.font = [UIFont boldSystemFontOfSize:20];
    self.navigationItem.titleView = customLab;
    //
    self.selectedUser = [[MXUser alloc]init];
    //初始化默认成员信息
    self.selectedUser.gender = 0;
    self.selectedUser.weight = 50;
    self.selectedUser.height = 168;
    self.selectedUser.birthday =[@"1980-01-01" stringWihtDateFormat:@"yyyy-MM-dd"];
    self.selectedUser.name = @"";
    //
    self.view.backgroundColor =UUBackGroundColor;//[UIColor colorWithHexString:@"#efeff4"];
    CGRect mainScreen = [[UIScreen mainScreen] bounds];
    mainScreen.size.height -= 20 + self.navigationController.navigationBar.frame.size.height;
    self.tableView = [[UITableView alloc] initWithFrame:CGRectMake(0, 0, mainScreen.size.width, mainScreen.size.height-10) style:UITableViewStyleGrouped];
    _tableView.delegate = self;
    _tableView.dataSource = self;
    _tableView.scrollEnabled = NO;
    _tableView.backgroundView = [[UIImageView alloc] initWithImage:[UIImage createImageWithColor:UUBackGroundColor]];
    [self.view addSubview:_tableView];
    
    _saveButton = [[UIButton alloc]initWithFrame:CGRectMake(10, self.tableView.frame.origin.y+self.tableView.frame.size.height, self.view.frame.size.width-10*2, 45)];
    [_saveButton setBackgroundImage:[UIImage createImageWithColor:[UIColor colorWithRed:199.0/255 green:72.0/255 blue:105.0/255 alpha:1.0]] forState:UIControlStateNormal];
    [_saveButton setBackgroundImage:[UIImage createImageWithColor:[UIColor colorWithRed:199.0/255 green:102.0/255 blue:127.0/255 alpha:1.0]] forState:UIControlStateHighlighted];
    _saveButton.clipsToBounds = YES;
    [_saveButton setTitle:NSLocalizedString(@"Family_Add_Action",@"添加") forState:UIControlStateNormal];
    [_saveButton setTitleColor:[UIColor whiteColor] forState:UIControlStateNormal];
    [_saveButton setTitleColor:[UIColor lightGrayColor] forState:UIControlStateHighlighted];
    _saveButton.layer.cornerRadius = 8;
    [_saveButton addTarget:self action:@selector(saveButtonProc:) forControlEvents:UIControlEventTouchUpInside];
    [self.view addSubview:_saveButton];
    //
    UIView *view = [[[[UIApplication sharedApplication] delegate] window] rootViewController].view;
    [view addSubview:self.contentView];
    self.contentView.clipsToBounds = YES;
    self.contentView.layer.cornerRadius = 8;
    self.contentView.hidden = YES;
    //
    [self.contentView addSubview:self.datePicker];
    [self.contentView addSubview:self.pickerView];
    
    //
    _dataService = [[OMEFamilyDataService alloc] init];
    _dataService.delegate = self;
}

-(void)datePickerChanged
{
    self.userBirthLabel.text = [_datePicker.date dateToStringWithDateFormate:@"yyyy-MM-dd" timeZone:[NSTimeZone systemTimeZone] locale:[NSLocale systemLocale]];
    self.selectedUser.birthday = _datePicker.date;
}

-(UIDatePicker*)datePicker
{
    if(!_datePicker)
    {
        NSDate *nowDate = [NSDate date];
        _datePicker = [[UIDatePicker alloc] initWithFrame:CGRectMake(0, 30, 265, 210)];
        _datePicker.date = [@"1980-01-01" stringWihtDateFormat:@"yyyy-MM-dd"];
        _datePicker.datePickerMode = UIDatePickerModeDate;
        //[_datePicker setValue:[UIFont systemFontOfSize:10] forKey:@"textFont"];
        _datePicker.maximumDate = [[NSString stringWithFormat:@"%d-%d-%d",[[nowDate dateToStringWithDateFormate:@"yyyy"] intValue],[[nowDate dateToStringWithDateFormate:@"MM"] intValue],[[nowDate dateToStringWithDateFormate:@"dd"] intValue]] stringWihtDateFormat:@"yyyy-MM-dd"];
        _datePicker.minimumDate = [[NSString stringWithFormat:@"%d-%d-%d",[[nowDate dateToStringWithDateFormate:@"yyyy"] intValue]-80,[[nowDate dateToStringWithDateFormate:@"MM"] intValue],[[nowDate dateToStringWithDateFormate:@"dd"] intValue]] stringWihtDateFormat:@"yyyy-MM-dd"];
        _datePicker.hidden = YES;
        [_datePicker addTarget:self action:@selector(datePickerChanged) forControlEvents:UIControlEventValueChanged];
    }
    return _datePicker;
}

-(WGPickerView *)pickerView
{
    if(!_pickerView)
    {
        _pickerView = [[WGPickerView alloc] initWithFrame:CGRectMake(0, 30,250,210)];
        _pickerView.dataSource = self;
        _pickerView.delegate = self;
        _pickerView.showsSelectionIndicator = YES;
        _pickerView.hidden = YES;
    }
    return _pickerView;
}

- (UIView *)contentView
{
    if (!_contentView) {
        CGRect mainScreen = [[UIScreen mainScreen] bounds];
        _contentView = [[UIView alloc] initWithFrame:CGRectMake(0, mainScreen.size.height, 265, 240)];
        UILabel *topbarBgLayer = [[UILabel alloc] init];
        topbarBgLayer.backgroundColor = UUGreen;//[UIColor colorWithHexString:@"#a14a70"];
        topbarBgLayer.frame = CGRectMake(0, 0,265,30);
        //topbarBgLayer.layer.cornerRadius = 8;
        topbarBgLayer.clipsToBounds = YES;
        [_contentView addSubview:topbarBgLayer];
        
        UIButton *doneButton = [UIButton buttonWithType:UIButtonTypeCustom];
        NSString *doneStr = NSLocalizedString(@"Family_Edit_End", @"完成");
        CGSize doneSize = [doneStr sizeWithFont:doneButton.titleLabel.font constrainedToSize:CGSizeMake(265, 30) lineBreakMode:NSLineBreakByWordWrapping];
        doneButton.frame = CGRectMake(265 - doneSize.width - 10, 0, doneSize.width, 30);
        doneButton.backgroundColor = [UIColor clearColor];
        [doneButton setTitle:doneStr forState:UIControlStateNormal];
        [doneButton setTitleColor:[UIColor whiteColor] forState:UIControlStateNormal];
        [doneButton setTitleColor:[[UIColor whiteColor] colorWithAlphaComponent:0.7] forState:UIControlStateHighlighted];
        [doneButton addTarget:self action:@selector(doneButtonPressed:) forControlEvents:UIControlEventTouchUpInside];
        doneButton.layer.cornerRadius = 8;
        [_contentView addSubview:doneButton];
        _contentView.backgroundColor = [UIColor whiteColor];
        _contentView.layer.cornerRadius = 8;
    }
    return _contentView;
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

- (void)cleanContentView
{
    if (_contentView.hidden == NO) {
        _contentView.hidden = YES;
    }
}

- (NSMutableArray *)getPickerOptionData:(int)index
{
    NSMutableArray *result = [[NSMutableArray alloc] init];
    NSMutableArray *first = [[NSMutableArray alloc] init];
    //NSMutableArray *second = [[NSMutableArray alloc] init];
    BOOL isCm = [[MXUserDefaults sharedInstance] isCM];
    BOOL isKg = [[MXUserDefaults sharedInstance]isKG];
    switch (index) {
        case 0://性别
            [first addObject:NSLocalizedString(@"Family_Male", @"男")];
            [first addObject:NSLocalizedString(@"Family_Female",@"女")];
            break;
        case 1: //height
            if(isCm)
            {
                for (int i = HEIGHT_MIN; i <=HEIGHT_MAX; i++) {
                    [first addObject:[NSString stringWithFormat:@"%d",i]];
                }
            }
            else
            {
                for (int i = 12; i <=90; i++) {
                    [first addObject:[NSString stringWithFormat:@"%d",i]];
                }
            }
            break;
        case 2: //weight
            if(isKg)
            {
                for (int i = WEIGHT_MIN; i <=WEIGHT_MAX; i++) {
                    [first addObject:[NSString stringWithFormat:@"%d",i]];
                }
            }
            else
            {
                for (int i =3; i <=396; i++) {
                    [first addObject:[NSString stringWithFormat:@"%d",i]];
                }
            }
        default:
            break;
    }
    [result addObject:first];
    //NSLog(@"result =%@", result);
    return result;
}

-(void)doneButtonPressed:(id)sender
{
    [self showContentView:NO];
}

-(OMEImageView*)userImageView
{
    if(!_userImageView)
    {
        self.userImageView = [[OMEImageView alloc] init];
        _userImageView.frame = CGRectMake(self.view.frame.size.width-60,5,50,50);
        _userImageView.backgroundColor = [UIColor clearColor];
        _userImageView.placeholderImage = [[UIImage imageNamed:@"avatar_default"] roundImage];
    }
    return _userImageView;
}

#pragma mark  TableView delegate and datesources
- (NSInteger)numberOfSectionsInTableView:(UITableView *)tableView
{
    return 1;
}

- (NSInteger)tableView:(UITableView *)tableView numberOfRowsInSection:(NSInteger)section
{
    return 6;
}

-(CGFloat)tableView:(UITableView *)tableView heightForRowAtIndexPath:(NSIndexPath *)indexPath
{
    if (indexPath.section == 0 && indexPath.row == 0) {
        return 60;
    }
    return 44;
}

- (UITableViewCell *)tableView:(UITableView *)tableView cellForRowAtIndexPath:(NSIndexPath *)indexPath
{
    static NSString *CellIdentifier = @"Cell";
    UITableViewCell *cell = [tableView dequeueReusableCellWithIdentifier:CellIdentifier];
    
    if (cell == nil) {
        cell = [[UITableViewCell alloc] initWithStyle:UITableViewCellStyleValue1 reuseIdentifier:nil];
    }
    BOOL isKg= [[MXUserDefaults sharedInstance]isKG];
    BOOL isCm = [[MXUserDefaults sharedInstance] isCM];
    NSString *titleStr = @"";
    NSString *detailStr = @"";
    if (indexPath.section == 0) {
        switch (indexPath.row) {
            case 0:
                titleStr =NSLocalizedString(@"Family_Avatar",@"头像");
                [cell.contentView addSubview:self.userImageView];
                break;
            case 1:
                titleStr =NSLocalizedString(@"Family_Name",@"姓名");
                self.nameTextField = [[UITextField alloc] init];
                _nameTextField.frame = CGRectMake( self.view.frame.size.width-215-10,0,215,44);
                _nameTextField.textAlignment = NSTextAlignmentRight;
                _nameTextField.backgroundColor = [UIColor clearColor];
                _nameTextField.contentVerticalAlignment = UIControlContentVerticalAlignmentCenter;
                _nameTextField.contentHorizontalAlignment = UIControlContentHorizontalAlignmentRight;
                _nameTextField.leftViewMode = UITextFieldViewModeAlways;
                _nameTextField.textColor =  [UIColor lightGrayColor];
                _nameTextField.font = [UIFont systemFontOfSize:15];
                _nameTextField.returnKeyType = UIReturnKeyDone;
                _nameTextField.text = self.selectedUser.name;
                _nameTextField.delegate = self;
                [_nameTextField addTarget:self action:@selector(nameInputEnd:) forControlEvents:UIControlEventEditingDidEnd];
                [cell.contentView addSubview:_nameTextField];
                break;
            case 2:
            {
                titleStr =NSLocalizedString(@"Family_Gender",@"性别");
                //detailStr = NSLocalizedString(@"More_Picker", @"请选择");
                _userGenderLabel = [[UILabel alloc]initWithFrame:CGRectMake(0, 0, 100, 40)];
                _userGenderLabel.backgroundColor = [UIColor clearColor];
                _userGenderLabel.textAlignment = NSTextAlignmentRight;
                NSString* gender;
                if(self.selectedUser.gender==0)
                {
                    gender = NSLocalizedString(@"Family_Male",@"男");
                }
                else
                {
                    gender = NSLocalizedString(@"Family_Female",@"女");
                }
                _userGenderLabel.text =gender;
                _userGenderLabel.textColor =  [UIColor lightGrayColor];
                _userGenderLabel.font = [UIFont systemFontOfSize:15];
                cell.accessoryView = _userGenderLabel;
            }
                break;
            case 3:
                titleStr = NSLocalizedString(@"Family_BirthDay", @"出生日期");
                //detailStr = NSLocalizedString(@"More_Picker", @"请选择");
                _userBirthLabel = [[UILabel alloc]initWithFrame:CGRectMake(0, 0, 100, 40)];
                _userBirthLabel.backgroundColor = [UIColor clearColor];
                _userBirthLabel.textAlignment = NSTextAlignmentRight;
                _userBirthLabel.text = [self.selectedUser.birthday dateToStringWithDateFormate:@"yyyy-MM-dd" timeZone:[NSTimeZone systemTimeZone] locale:[NSLocale systemLocale]];
                _userBirthLabel.textColor =  [UIColor lightGrayColor];
                _userBirthLabel.font = [UIFont systemFontOfSize:15];
                cell.accessoryView = _userBirthLabel;
                break;
            case 4:
                titleStr = NSLocalizedString(@"Family_Height", @"身高");
                if(isCm)
                    detailStr = [NSString stringWithFormat:@"%ld%@",(long)self.selectedUser.height, NSLocalizedString(@"Height_Unit_CM", @"CM")];
                else
                    detailStr = [NSString stringWithFormat:@"%.f%@",(self.selectedUser.height/HEIGHT_TO_IN), NSLocalizedString(@"Height_Unit_IN", @"IN")];
                _userHeightLabel = [[UILabel alloc]initWithFrame:CGRectMake(0, 0, 100, 40)];
                _userHeightLabel.backgroundColor = [UIColor clearColor];
                _userHeightLabel.textAlignment = NSTextAlignmentRight;
                _userHeightLabel.text =detailStr;
                _userHeightLabel.textColor =  [UIColor lightGrayColor];
                _userHeightLabel.font = [UIFont systemFontOfSize:15];
                cell.accessoryView = _userHeightLabel;
                break;
            case 5:
                titleStr = NSLocalizedString(@"Family_Weight", @"体重");
                if(isKg)
                    detailStr = [NSString stringWithFormat:@"%.f%@",self.selectedUser.weight, NSLocalizedString(@"Weight_Unit_KG", @"KG")];
                else
                    detailStr = [NSString stringWithFormat:@"%.f%@",(self.selectedUser.weight/WEIGHT_TO_LB), NSLocalizedString(@"Weight_Unit_LB", @"LB")];
                _userWeightLabel = [[UILabel alloc]initWithFrame:CGRectMake(0, 0, 100, 40)];
                _userWeightLabel.backgroundColor = [UIColor clearColor];
                _userWeightLabel.textAlignment = NSTextAlignmentRight;
                _userWeightLabel.text =detailStr;
                _userWeightLabel.textColor =  [UIColor lightGrayColor];
                _userWeightLabel.font = [UIFont systemFontOfSize:15];
                cell.accessoryView = _userWeightLabel;
                break;
        }
    }else
    {
        
    }
    cell.textLabel.text = titleStr;
    cell.textLabel.font = [UIFont systemFontOfSize:15.0f];
    cell.textLabel.textColor = UUWhite;
    cell.textLabel.backgroundColor = UUBackGroundColor;
    cell.textLabel.highlightedTextColor = UIColorFromRGB(0xffffff);
    cell.backgroundColor = UUBackGroundColor;
    cell.selectedBackgroundView = [[UIImageView alloc] initWithImage:[UIImage createImageWithColor:[UIColor colorWithRed:117.0/255 green:136.0/255 blue:171.0/255 alpha:1.0]]];
    return cell;
}

- (void)tableView:(UITableView *)tableView didSelectRowAtIndexPath:(NSIndexPath *)indexPath
{
    [tableView deselectRowAtIndexPath:indexPath animated:YES];
    BOOL isCm = [[MXUserDefaults sharedInstance] isCM];
    BOOL isKG = [[MXUserDefaults sharedInstance]isKG];
    if(indexPath.section ==0)
    {
        switch (indexPath.row) {
            case 0:
                //隐藏键盘
                self.datePicker.hidden = YES;
                self.pickerView.hidden = YES;
                [self showContentView:NO];
                [_nameTextField resignFirstResponder];
                [self avatarChangeProc];
                break;
            case 1:
                self.datePicker.hidden = YES;
                self.pickerView.hidden = YES;
                [self showContentView:NO];
                [_nameTextField becomeFirstResponder];
                break;
            case 2:
                //隐藏键盘
                [_nameTextField resignFirstResponder];
                self.pickerIndex = 0;
                self.pickerDatas = [self getPickerOptionData:self.pickerIndex];
                self.datePicker.hidden = YES;
                self.pickerView.hidden = NO;
                [self.pickerView reloadAllComponents];
                [_pickerView selectRow:0 inComponent:0 animated:NO];
                [_pickerView showsSelectionTitle:@""];
                [self showContentView:YES];
                break;
            case 3:
                //隐藏键盘
                [_nameTextField resignFirstResponder];
                self.datePicker.hidden = NO;
                self.pickerView.hidden = YES;
                [self showContentView:YES];
                break;
            case 4:
                //隐藏键盘
                [_nameTextField resignFirstResponder];
                self.pickerIndex = 1;
                self.pickerDatas = [self getPickerOptionData:_pickerIndex];
                self.datePicker.hidden = YES;
                self.pickerView.hidden = NO;
                [self.pickerView reloadAllComponents];
                int index;
                if(isCm)
                {
                    index =(int)self.selectedUser.height-HEIGHT_MIN;
                    [_pickerView showsSelectionTitle:NSLocalizedString(@"Height_Unit_CM", @"CM")];
                }
                else
                {
                    index = (int)(self.selectedUser.height/HEIGHT_TO_IN+0.5)-12;
                    [_pickerView showsSelectionTitle:NSLocalizedString(@"Height_Unit_IN", @"IN")];
                }
                [_pickerView selectRow:index inComponent:0 animated:NO];
                [self showContentView:YES];
                break;
            case 5:
                //隐藏键盘
                [_nameTextField resignFirstResponder];
                self.pickerIndex = 2;
                int WeightIndex;
                self.pickerDatas = [self getPickerOptionData:_pickerIndex];
                self.datePicker.hidden = YES;
                self.pickerView.hidden = NO;
                if(isKG)
                {
                    WeightIndex = (int)self.selectedUser.weight-WEIGHT_MIN;
                    [_pickerView showsSelectionTitle:NSLocalizedString(@"Weight_Unit_KG", @"KG")];
                }
                else
                {
                    WeightIndex = (int)(self.selectedUser.weight/WEIGHT_TO_LB+0.5)-3;
                    [_pickerView showsSelectionTitle:NSLocalizedString(@"Weight_Unit_LB", @"LB")];
                }
                [self.pickerView reloadAllComponents];
                [_pickerView selectRow:WeightIndex inComponent:0 animated:NO];
                [self showContentView:YES];
                break;
            default:
                break;
        }
    }
    else
    {
        
    }
}

-(void)nameInputEnd:(id)sender
{
    [sender resignFirstResponder];
}


-(void)textFieldDidBeginEditing:(UITextField *)textField
{
    self.datePicker.hidden = YES;
    self.pickerView.hidden = YES;
    [self showContentView:NO];
}

- (BOOL)textFieldShouldReturn:(UITextField *)textField
{
    [self.nameTextField resignFirstResponder];
    return YES;
}

-(void)avatarChangeProc
{
    UIActionSheet* mySheet = [[UIActionSheet alloc]
                              initWithTitle:NSLocalizedString(@"Family_Change_Avatar",@"修改头像")
                              delegate:self
                              cancelButtonTitle:NSLocalizedString(@"Sys_Cancel",@"取消")
                              destructiveButtonTitle:nil
                              otherButtonTitles:NSLocalizedString(@"Family_Photograph",@"照像"),NSLocalizedString(@"Family_From_Album", @"从相册获取"), nil];
    mySheet.actionSheetStyle = UIActionSheetStyleDefault;
    [mySheet showInView:[UIApplication sharedApplication].keyWindow];
}

-(void)saveButtonProc:(id)sender
{
    if(_nameTextField.text &&_nameTextField.text.length>0)
    {
        self.selectedUser.name =_nameTextField.text;
        NSLog(@"name = %@, weight = %f, height = %f, birthday = %@, gender = %d",_selectedUser.name, _selectedUser.weight, _selectedUser.height, _selectedUser.birthday, _selectedUser.gender);
        [self performSelector:@selector(saveAction) withObject:nil afterDelay:0];
    }
    else
    {
        UIAlertView *alertView = [[UIAlertView alloc] initWithTitle:NSLocalizedString(@"Sys_Tips","提示") message:NSLocalizedString(@"Add_Please_InputName",@"请输入姓名")delegate:nil cancelButtonTitle:NSLocalizedString(@"Sys_Ok",@"确认") otherButtonTitles: nil];
        [alertView show];
    }
}

-(void)saveAction
{
    self.progressHUD.mode = MBProgressHUDModeIndeterminate;
    self.progressHUD.detailsLabelText =nil;
    self.progressHUD.labelText = NSLocalizedString(@"Add_Saving", @"正在保存");
    [self.progressHUD show:YES];
    //
    [self.dataService cancel];
    MXUserData *userData = [[MXUserData alloc] init];
    NSArray *loginedArray = [userData getLoginInfo];
    if ([loginedArray count]) {
        NSDictionary *dataDic = [loginedArray objectAtIndex:0];
        //NSLog(@"DatDic =%@", dataDic);
        long long int idNum = [[dataDic objectForKey:@"id"] longLongValue];
        NSString *key = [dataDic objectForKey:@"clientKey"];
        //NSString *imageNameStr =@"";
        NSDictionary* memeInfo =[NSDictionary dictionaryWithObjectsAndKeys:self.selectedUser.name,@"name",
                                 @(self.selectedUser.gender),@"gender",
                                  [self.selectedUser.birthday dateToStringWithDateFormate:@"yyyy-MM-dd"],@"birthday",
                                 @(self.selectedUser.weight),@"weight",
                                   @(self.selectedUser.height),@"height",
                                 @"",@"phone",
                                  [_imageData base64Encoding],@"avatar",
                                 nil];
        NSDictionary *userInfoDic = [NSDictionary dictionaryWithObjectsAndKeys:
                                     @"AddFamMem", @"key",
                                     @(idNum), @"accountId",
                                     key, @"clientKey",
                                     memeInfo, @"memInfo",
                                     nil];
        //self.selectedUser.memberId = idNum;
        [_dataService cancel];
        //NSLog(@"dic = %@", userInfoDic);
        NSString *jsonStr = [NSString jsonStringWithDictionary:userInfoDic];
        [_dataService requestOMEAddFamilyByMemberId:idNum Key:key json:jsonStr];
    }
}

#pragma mark ActionSheet delegate
- (void) actionSheet:(UIActionSheet *)actionSheet clickedButtonAtIndex:(NSInteger)buttonIndex
{
    UIImagePickerController *ipc = [[UIImagePickerController alloc] init];
    ipc.delegate = self;
    ipc.allowsEditing = YES;
    switch (buttonIndex) {
        case 0:  //拍照
            if ([UIImagePickerController isSourceTypeAvailable:UIImagePickerControllerSourceTypeCamera]) {
                ipc.sourceType = UIImagePickerControllerSourceTypeCamera;
                [self presentViewController:ipc animated:YES completion:^{
                    NSLog(@"--------------111");
                }];
            }else{
                 NSLog(@"该设备不支持拍照功能");
            }
            break;
        case 1:  //从相册选择
            ipc.sourceType = UIImagePickerControllerSourceTypePhotoLibrary;
            [self presentViewController:ipc animated:YES completion:^{
                NSLog(@"******----------+++++++++++");
            }];
            break;
        default:
            ipc.delegate = nil;
            break;
    }
}

#pragma mark - MBProgressHUD
- (void)uploadPhotoDidFinish
{
    [self showWarmTitle:NSLocalizedString(@"Service_UploadSuccess", @"上传成功") message:nil withImageName:@"37x-Checkmark.png"];
}

- (void)uploadPhotoDidFailed
{
     [self showWarmTitle:NSLocalizedString(@"Service_UploadFailed", @"上传失败") message:nil withImageName:@"37x-Failed.png"];
}

#pragma mark - UIImagePickerController Delegate
- (void) imagePickerController:(UIImagePickerController *)picker didFinishPickingMediaWithInfo:(NSDictionary *)info
{
    NSString *mediaType = [info objectForKey:UIImagePickerControllerMediaType];
    //NSData *data;
    
    if ([mediaType isEqualToString:@"public.image"]){
        UIImage *editedImage = [info objectForKey:UIImagePickerControllerEditedImage];
        UIImage *scaleImage = [UIImage scaleImage:editedImage ToSize:CGSizeMake(200, 200)];
        _imageData = UIImageJPEGRepresentation(scaleImage, 0.8);
        UIImage *currentImage = [scaleImage addMaskImage:[UIImage roundImageWithDiameter:scaleImage.size.width]];
        self.userImageView.image = currentImage;
        self.userImageView.placeholderImage = currentImage;
    }
    [picker dismissViewControllerAnimated:YES completion:nil];
}

#pragma mark - pickerView delegate and datasources
- (NSInteger)numberOfComponentsInPickerView:(UIPickerView *)pickerView
{
    return [_pickerDatas count];
}

- (NSInteger)pickerView:(UIPickerView *)pickerView numberOfRowsInComponent:(NSInteger)component
{
    return [[_pickerDatas objectAtIndex:component] count];
}

- (NSString *)pickerView:(UIPickerView *)pickerView titleForRow:(NSInteger)row forComponent:(NSInteger)component
{
    return [[_pickerDatas objectAtIndex:component] objectAtIndex:row];
}

- (void)pickerView:(UIPickerView *)pickerView didSelectRow:(NSInteger)row inComponent:(NSInteger)component
{
    NSString* str;
    str =[[_pickerDatas objectAtIndex:component] objectAtIndex:row];
    BOOL isCm = [[MXUserDefaults sharedInstance]isCM];
    BOOL isKg = [[MXUserDefaults sharedInstance]isKG];
    switch (_pickerIndex) {
        case 0:             //gender
            _userGenderLabel.text = str;
            self.selectedUser.gender = row;
            break;
        case 1:             //height
            if(isCm)
            {
                self.selectedUser.height =[str floatValue];
                _userHeightLabel.text = [NSString stringWithFormat:@"%ld%@",(long)self.selectedUser.height, NSLocalizedString(@"Height_Unit_CM", @"CM")];
            }
            else//IN
            {
                self.selectedUser.height =([str floatValue]*HEIGHT_TO_IN);
                _userHeightLabel.text = [NSString stringWithFormat:@"%ld%@",(long)[str integerValue], NSLocalizedString(@"Height_Unit_IN", @"IN")];
            }
            break;
        case 2:             //weight
            if(isKg)
            {
                self.selectedUser.weight = [str floatValue];
                _userWeightLabel.text =[NSString stringWithFormat:@"%ld%@", (long)self.selectedUser.weight,NSLocalizedString(@"Weight_Unit_KG", @"KG")];
            }
            else
            {
                self.selectedUser.weight = ([str floatValue]*WEIGHT_TO_LB);
                _userWeightLabel.text =[NSString stringWithFormat:@"%ld%@", (long)[str integerValue], NSLocalizedString(@"Weight_Unit_LB", @"LB")];
            }
            break;
        default:
            break;
    }
}


#pragma mark -  OMEDataService

- (void)dataService:(OMEDataService *)dataService didFinishWithData:(NSDictionary *)dataDic
{
    NSLog(@"dataDic = %@", dataDic);
    NSDictionary *dic = [dataDic objectForKey:@"data"];
    NSInteger familyId = [[dic objectForKey:@"memberId"] integerValue];
    NSFileManager *fileManager = [NSFileManager defaultManager];
    NSString *filePath = tmpDirectory();
    NSString *fileName = [dic objectForKey:@"avatarDir"];
    filePath = [filePath stringByAppendingPathComponent:fileName];
    [fileManager createFileAtPath:filePath contents:_imageData attributes:nil];
    self.imageFileName = fileName;
    self.userImageView.imageName = self.imageFileName;
    self.selectedUser.imageName = self.imageFileName;
    //
    MXUserData *userData = [[MXUserData alloc] init];
    NSArray *loginedArray = [userData getLoginInfo];
    if ([loginedArray count]) {
        NSDictionary *dataDic = [loginedArray objectAtIndex:0];
        NSInteger idNum = [[dataDic objectForKey:@"id"] intValue];
//        NSLog(@"familyID: %ld memberID = %ld ***%ld &&&&%ld idNum%ld", familyId, idNum, self.selectedUser.familyId, self.selectedUser.memberId, self.selectedUser.idNum);
        if([userData addNewUserWithData:self.selectedUser familyId:familyId memberId:idNum])
        {
            [[NSNotificationCenter defaultCenter] postNotificationName:familyDidUpdatedNotification object:nil];
            [self showWarmTitle:NSLocalizedString(@"Family_Addmem_Success",@"添加成功") message:nil withImageName:@"37x-Checkmark.png"];
            [self.navigationController popViewControllerAnimated:YES];
        }
        else
        {
            [self showWarmTitle:NSLocalizedString(@"Family_Addmem_Fail", @"添加失败") message:nil withImageName:@"37x-Failed.png"];
        }
    }
    else
    {
        [self showWarmTitle:NSLocalizedString(@"Family_Addmem_Fail", @"添加失败") message:nil withImageName:@"37x-Failed.png"];
    }
}

- (void)dataService:(OMEDataService *)dataService didFailWithError:(NSError *)error
{
    [self showWarmTitle:error.localizedFailureReason message:error.localizedDescription withImageName:@"37x-Failed.png"];
}

- (void)showWarmTitle:(NSString *)title message:(NSString *)msg withImageName:(NSString*)imageName
{
    self.progressHUD.customView = [[UIImageView alloc] initWithImage:[UIImage imageNamed:imageName]];
    _progressHUD.mode = MBProgressHUDModeCustomView;
    if(!title)
    {
        _progressHUD.labelText =nil;
    }
    else
    {
        _progressHUD.labelText = title;
    }
    _progressHUD.detailsLabelText = msg;
    [_progressHUD show:YES];
    [_progressHUD hide:YES afterDelay:2.0];
}

-(MBProgressHUD*)progressHUD
{
    if(!_progressHUD)
    {
        _progressHUD = [[MBProgressHUD alloc] initWithView:self.navigationController.view];
        [self.navigationController.view addSubview:_progressHUD];
        //_progressedHUD.customView = [[UIImageView alloc] initWithImage:[UIImage imageNamed:@"37x-Failed.png"]];
        _progressHUD.delegate = self;
    }
    return _progressHUD;
}

@end
