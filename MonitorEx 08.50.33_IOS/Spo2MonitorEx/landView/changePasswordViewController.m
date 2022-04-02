//
//  changePasswordViewController.m
//  BLT-Spo2Monitor
//
//  Created by luteng on 16/7/1.
//  Copyright © 2016年 luteng. All rights reserved.
//

#import "changePasswordViewController.h"
#import "UIColor+BltSoft.h"
#import "consDefine.h"
#import "MXUserData.h"
#import "OMEVerifyRegisterDataService.h"
#import "OMEDataService.h"
#import "MBProgressHUD.h"
#import "OMESoft.h"
#import "MXUserDefaults.h"
@interface changePasswordViewController ()<UITextFieldDelegate,OMEDataServiceDelegate>
@property(strong, nonatomic)UITextField* OldPassword;
@property(strong, nonatomic)UITextField* NewPassword;
@property(strong, nonatomic)UITextField* ConfirmPassword;
@property(strong, nonatomic)UIButton* OkButton;
@property (strong, nonatomic)   OMEVerifyRegisterDataService *dataService;
@property (strong, nonatomic)   MBProgressHUD *progressHUD;
@end

@implementation changePasswordViewController


- (void)dealloc
{
    _dataService.delegate = nil;
}

- (void)viewDidLoad {
    [super viewDidLoad];
    self.navigationController.navigationBarHidden = NO;
    self.view.backgroundColor = UUBackGroundColor;
    [self.navigationController.navigationBar setBarTintColor:UUNavBackColor];
    [self.navigationController.navigationBar setTintColor:UUWhite];
    UILabel *customLab = [[UILabel alloc] initWithFrame:CGRectMake(0, 0, 100, 30)];
    [customLab setTextColor:UUWhite];
    [customLab setText:NSLocalizedString(@"Login_Rest_Password", @"更改密码")];
    customLab.font = [UIFont boldSystemFontOfSize:20];
    self.navigationItem.titleView = customLab;
    //旧密码
    [self.view addSubview:self.OldPassword];
    self.OldPassword.frame = CGRectMake(20,80, self.view.frame.size.width-20*2, 40);
    _OldPassword.textColor = [UIColor whiteColor];
    _OldPassword.leftViewMode= UITextFieldViewModeAlways;
    _OldPassword.layer.borderWidth=1;
    _OldPassword.layer.borderColor=[[UIColor colorWithHexString:@"#384562"] CGColor];
    _OldPassword.clearButtonMode = UITextFieldViewModeWhileEditing;
    _OldPassword.placeholder = NSLocalizedString(@"Login_Old_Password","旧密码");
    _OldPassword.delegate = self;
    _OldPassword.secureTextEntry = YES;
    _OldPassword.layer.cornerRadius = 8;
    [_OldPassword addTarget:self action:@selector(passwordEnd:) forControlEvents:UIControlEventEditingDidEnd];
    //新密码
    [self.view addSubview:self.NewPassword];
    _NewPassword.frame =CGRectMake(20, _OldPassword.frame.origin.y+_OldPassword.frame.size.height+10, self.view.frame.size.width-20*2, 40);
    _NewPassword.textColor = [UIColor whiteColor];
    _NewPassword.leftViewMode= UITextFieldViewModeAlways;
    _NewPassword.layer.borderWidth=1;
    _NewPassword.layer.borderColor=[[UIColor colorWithHexString:@"#384562"] CGColor];
    _NewPassword.clearButtonMode = UITextFieldViewModeWhileEditing;
    _NewPassword.placeholder = NSLocalizedString(@"Login_New_Password", @"新密码");
    _NewPassword.delegate = self;
    _NewPassword.secureTextEntry = YES; //是否以密码形式显示
    _NewPassword.layer.cornerRadius = 8;
    [_NewPassword addTarget:self action:@selector(passwordEnd:) forControlEvents:UIControlEventEditingDidEnd];
    //确认密码
    [self.view addSubview:self.ConfirmPassword];
    _ConfirmPassword.frame = CGRectMake(20, _NewPassword.frame.origin.y+_NewPassword.frame.size.height+10, self.view.frame.size.width-20*2, 40);
    _ConfirmPassword.textColor = [UIColor whiteColor];
    _ConfirmPassword.leftViewMode= UITextFieldViewModeAlways;
    _ConfirmPassword.layer.borderWidth=1;
    _ConfirmPassword.layer.borderColor=[[UIColor colorWithHexString:@"#384562"] CGColor];//UUDeepGrey.CGColor;
    _ConfirmPassword.clearButtonMode = UITextFieldViewModeWhileEditing;
    _ConfirmPassword.placeholder = NSLocalizedString(@"Login_Confirm_New_Password",@"确认新密码");
    _ConfirmPassword.delegate = self;
    _ConfirmPassword.secureTextEntry = YES; //是否以密码形式显示
    _ConfirmPassword.layer.cornerRadius = 8;
    [_ConfirmPassword addTarget:self action:@selector(passwordEnd:) forControlEvents:UIControlEventEditingDidEnd];
    //
    [self.view addSubview:self.OkButton];
    [_OkButton setBackgroundImage:[UIImage createImageWithColor:[UIColor colorWithRed:199.0/255 green:72.0/255 blue:105.0/255 alpha:1.0]] forState:UIControlStateNormal];
    [_OkButton setBackgroundImage:[UIImage createImageWithColor:[UIColor colorWithRed:199.0/255 green:102.0/255 blue:127.0/255 alpha:1.0]] forState:UIControlStateHighlighted];
    //_OkButton.backgroundColor = [UIColor colorWithHexString:@"#cc4452"];
    _OkButton.layer.cornerRadius = 8;
    _OkButton.clipsToBounds = YES;
    [_OkButton setTitleColor:UUWhite forState:UIControlStateNormal];
    //[_OkButton setTitleColor:UULightGrey forState:UIControlStateHighlighted];
    [_OkButton setTitle:NSLocalizedString(@"Login_Determine",@"确定") forState:UIControlStateNormal];
    [_OkButton addTarget:self action:@selector(resetPWDByEmailAction) forControlEvents:UIControlEventTouchUpInside];
    //
    self.view.userInteractionEnabled = YES;
    UITapGestureRecognizer *singleTap = [[UITapGestureRecognizer alloc] initWithTarget:self action:@selector(backgroundTap:)];
    [self.view addGestureRecognizer:singleTap];
    // Do any additional setup after loading the view.
}

- (void)didReceiveMemoryWarning {
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}

- (BOOL)textFieldShouldReturn:(UITextField *)textField
{
    [self.OldPassword resignFirstResponder];
    [self.NewPassword resignFirstResponder];
    [self.ConfirmPassword resignFirstResponder];
    return YES;
}

- (void)viewWillDisappear:(BOOL)animated
{
    [_dataService cancel];
    [super viewWillDisappear:animated];
}


-(void)passwordEnd:(id)sender {
    [self resignFirstResponder];
}

-(void)backgroundTap:(id)sender {
    [self.OldPassword resignFirstResponder];
    [self.NewPassword resignFirstResponder];
    [self.ConfirmPassword resignFirstResponder];
}

-(UITextField*)OldPassword
{
    if(!_OldPassword)
    {
        _OldPassword = [[UITextField alloc]init];
        _OldPassword.backgroundColor = [UIColor colorWithHexString:@"#384562"];
    }
    return _OldPassword;
}

-(UITextField*)NewPassword
{
    if(!_NewPassword)
    {
        _NewPassword = [[UITextField alloc]init];
        _NewPassword.backgroundColor = [UIColor colorWithHexString:@"#384562"];
    }
    return _NewPassword;
}

-(UITextField*)ConfirmPassword
{
    if(!_ConfirmPassword)
    {
        _ConfirmPassword = [[UITextField alloc]init];
        _ConfirmPassword.backgroundColor = [UIColor colorWithHexString:@"#384562"];
    }
    return _ConfirmPassword;
}

-(UIButton*)OkButton
{
    if(!_OkButton)
    {
        _OkButton = [[UIButton alloc]initWithFrame:CGRectMake(self.ConfirmPassword.frame.origin.x, self.ConfirmPassword.frame.origin.y+self.ConfirmPassword.frame.size.height+20, self.ConfirmPassword.frame.size.width, 45)];
    }
    return _OkButton;
}

-(void)changePwdAction
{
    [_dataService cancel];
    self.progressHUD.mode = MBProgressHUDModeIndeterminate;
    self.progressHUD.detailsLabelText =nil;
    self.progressHUD.labelText = NSLocalizedString(@"Log_Changing_pwd", @"更改密码");
    [self.progressHUD show:YES];
    MXUserData *userData = [[MXUserData alloc] init];
    NSArray *loginedArray = [userData getLoginInfo];
    if ([loginedArray count] ) {
        NSInteger idNum = [[[loginedArray objectAtIndex:0] objectForKey:@"id"] integerValue];
        NSString *key = [[loginedArray objectAtIndex:0] objectForKey:@"clientKey"];
        [self.dataService requestOMEChangePasswordWithKey:key oldPwd:[NSString md5:_OldPassword.text] newPwd:[NSString md5: _NewPassword.text] memberId:idNum];
    }
}

- (void)resetPWDByEmailAction
{
    if(_OldPassword.text.length>0)
    {
        if(_NewPassword.text.length>0)
        {
            if(_ConfirmPassword.text.length>0)
            {
                if([_ConfirmPassword.text isEqualToString:_NewPassword.text])
                {
                    [_OldPassword resignFirstResponder];
                    [_NewPassword resignFirstResponder];
                    [_ConfirmPassword resignFirstResponder];
                    [self performSelector:@selector(changePwdAction) withObject:nil afterDelay:0];
                }
                else
                {
                    UIAlertView *alertView = [[UIAlertView alloc] initWithTitle:NSLocalizedString(@"Sys_Tips","提示") message:NSLocalizedString(@"Login_Confirm_Error",@"确认密码有误") delegate:nil cancelButtonTitle:NSLocalizedString(@"Sys_Ok",@"确认") otherButtonTitles: nil];
                    [alertView show];
                }
            }
            else{
                UIAlertView *alertView = [[UIAlertView alloc] initWithTitle:NSLocalizedString(@"Sys_Tips","提示") message:NSLocalizedString(@"Login_Please_InputConfirm",@"请确认密码") delegate:nil cancelButtonTitle:NSLocalizedString(@"Sys_Ok",@"确认") otherButtonTitles: nil];
                [alertView show];
            }
        }
        else
        {
            UIAlertView *alertView = [[UIAlertView alloc] initWithTitle:NSLocalizedString(@"Sys_Tips","提示") message:NSLocalizedString(@"Login_Please_InputNewPassword",@"请输入新密码")delegate:nil cancelButtonTitle:NSLocalizedString(@"Sys_Ok",@"确认") otherButtonTitles: nil];
            [alertView show];
        }
    }
    else
    {
        UIAlertView *alertView = [[UIAlertView alloc] initWithTitle:NSLocalizedString(@"Sys_Tips","提示") message:NSLocalizedString(@"Login_Please_InputOldPwd",@"请输入旧密码") delegate:nil cancelButtonTitle:NSLocalizedString(@"Sys_Ok",@"确认") otherButtonTitles: nil];
        [alertView show];
    }
}

#pragma mark - OMEDataService

- (void)dataService:(OMEDataService *)dataService didFinishWithData:(NSDictionary *)dataDic
{
    //NSLog(@"datDic =%@", dataDic);
    //保存新密码
     [[MXUserDefaults sharedInstance] setUserAccountPwd:_NewPassword.text];
    [self showWarmTitle:NSLocalizedString(@"Log_ChangePwd_Success",@"密码更改成功") message:nil withImageName:@"37x-Checkmark.png"];
    [self.navigationController popViewControllerAnimated:YES];
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


#pragma mark - Get Methods
- (OMEVerifyRegisterDataService *)dataService
{
    if (!_dataService) {
        _dataService = [[OMEVerifyRegisterDataService alloc] init];
        _dataService.delegate = self;
    }
    return _dataService;
}

- (MBProgressHUD *)progressHUD
{
    
    if(!_progressHUD)
    {
        _progressHUD = [[MBProgressHUD alloc] initWithView:self.navigationController.view];
        [self.navigationController.view addSubview:_progressHUD];
        //_progressHUD.delegate = self;
    }
    return _progressHUD;
}

/*
#pragma mark - Navigation

// In a storyboard-based application, you will often want to do a little preparation before navigation
- (void)prepareForSegue:(UIStoryboardSegue *)segue sender:(id)sender {
    // Get the new view controller using [segue destinationViewController].
    // Pass the selected object to the new view controller.
}
*/

@end
