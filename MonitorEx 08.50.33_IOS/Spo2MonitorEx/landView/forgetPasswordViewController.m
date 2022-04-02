//
//  forgetPasswordViewController.m
//  BLT-Spo2Monitor
//
//  Created by luteng on 16/6/30.
//  Copyright © 2016年 luteng. All rights reserved.
//

#import "forgetPasswordViewController.h"
#import "consDefine.h"
#import "UIColor+BltSoft.h"
#import "OMEVerifyRegisterDataService.h"
#import "OMEDataService.h"
#import "OMESoft.h"
#import "MBProgressHUD.h"
#import "MXUserData.h"
#import "consDefine.h"
#import "Constants.h"
#import "HTAutocompleteManager.h"

@interface forgetPasswordViewController ()<UITextFieldDelegate,OMEDataServiceDelegate>
@property(strong, nonatomic)HTAutocompleteTextField* emailField;
@property(strong, nonatomic)UIButton* getCodeButton;
@property (strong, nonatomic)OMEVerifyRegisterDataService *dataService;
@property (strong, nonatomic)MBProgressHUD *progressHUD;
@end

@implementation forgetPasswordViewController

- (void)viewDidLoad {
    [super viewDidLoad];
    self.navigationController.navigationBarHidden = NO;
    self.view.backgroundColor = [UIColor colorWithRed:47.0/255 green:62.0/255 blue:105.0/255 alpha:1.0];
    [self.navigationController.navigationBar setBarTintColor:[UIColor colorWithRed:63.0/255 green:92.0/255 blue:154.0/255 alpha:1.0]];
    [self.navigationController.navigationBar setTintColor:UUWhite];
    UILabel *customLab = [[UILabel alloc] initWithFrame:CGRectMake(0, 0, 100, 30)];
    [customLab setTextColor:UUWhite];
    [customLab setText:NSLocalizedString(@"Login_Forget_Password", @"忘记密码")];
    customLab.font = [UIFont boldSystemFontOfSize:20];
    self.navigationItem.titleView = customLab;
    //
    //y邮箱地址
    //邮箱自匹配补全
    [HTAutocompleteTextField setDefaultAutocompleteDataSource:[HTAutocompleteManager sharedManager]];
    self.emailField.autocompleteType = HTAutocompleteTypeEmail;
    [self.view addSubview:self.emailField];
    self.emailField.frame = CGRectMake(20, 100, self.view.frame.size.width-20*2, 40);
    _emailField.textColor = [UIColor whiteColor];
    _emailField.leftView = [self logoViewWithImage:[UIImage imageNamed:@"login_EMAIL.png"]];
    _emailField.leftViewMode= UITextFieldViewModeAlways;
    _emailField.layer.borderWidth=1;
    _emailField.layer.borderColor=[[UIColor clearColor] CGColor];//UUDeepGrey.CGColor;
    _emailField.clearButtonMode = UITextFieldViewModeWhileEditing;
    _emailField.placeholder = NSLocalizedString(@"Login_Email_Address", @"邮箱地址");
    _emailField.delegate = self;
    [_emailField addTarget:self action:@selector(TextFieldEnd:) forControlEvents:UIControlEventEditingDidEnd];
    CALayer *emailLayer = [[CALayer alloc] init];
    emailLayer.frame = CGRectMake(_emailField.frame.origin.x, _emailField.frame.origin.y + _emailField.frame.size.height,_emailField.frame.size.width, 1);
    emailLayer.backgroundColor = [UIColor whiteColor].CGColor;
    [self.view.layer addSublayer:emailLayer];
    //
    [self.view addSubview:self.getCodeButton];
    [_getCodeButton setBackgroundImage:[UIImage createImageWithColor:[UIColor colorWithRed:199.0/255 green:72.0/255 blue:105.0/255 alpha:1.0]] forState:UIControlStateNormal];
    [_getCodeButton setBackgroundImage:[UIImage createImageWithColor:[UIColor colorWithRed:199.0/255 green:102.0/255 blue:127.0/255 alpha:1.0]] forState:UIControlStateHighlighted];
    //_getCodeButton.backgroundColor = [UIColor colorWithHexString:@"#cc4452"];
    _getCodeButton.layer.cornerRadius = 8;
    _getCodeButton.clipsToBounds = YES;
    [_getCodeButton setTitleColor:UUWhite forState:UIControlStateNormal];
    //[_getCodeButton setTitleColor:UULightGrey forState:UIControlStateHighlighted];
    [_getCodeButton setTitle:NSLocalizedString(@"Login_Get_Verfication_Code", "获取邮箱验证信息") forState:UIControlStateNormal];
    [_getCodeButton addTarget:self action:@selector(getCodeBtnProc) forControlEvents:UIControlEventTouchUpInside];
    //
    self.view.userInteractionEnabled = YES;
    UITapGestureRecognizer *singleTap = [[UITapGestureRecognizer alloc] initWithTarget:self action:@selector(backgroundTap:)];
    [self.view addGestureRecognizer:singleTap];
    //
    _dataService = [[OMEVerifyRegisterDataService alloc]init];
    _dataService.delegate = self;
    // Do any additional setup after loading the view.
}

- (void)didReceiveMemoryWarning {
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}

-(void)viewWillDisappear:(BOOL)animated
{
    _dataService.delegate = nil;
}


-(UITextField*)emailField
{
    if(!_emailField)
    {
        _emailField = [[HTAutocompleteTextField alloc]init];
        _emailField.backgroundColor = [UIColor clearColor];
    }
    return _emailField;
}

-(UIButton*)getCodeButton
{
    if(!_getCodeButton)
    {
        _getCodeButton = [[UIButton alloc]initWithFrame:CGRectMake(self.emailField.frame.origin.x,self.emailField.frame.origin.y+self.emailField.frame.size.height+40, self.emailField.frame.size.width, 45)];
    }
    return _getCodeButton;
}

- (UIView *)logoViewWithImage:(UIImage *)aImage
{
    UIView *result = [[UIView alloc] initWithFrame:CGRectMake(0, 0, aImage.size.width + 16, 40)];
    UIImageView *imageView = [[UIImageView alloc] initWithImage:aImage];
    imageView.frame = CGRectMake(0, 0, aImage.size.width, aImage.size.height);
    imageView.center = CGPointMake(result.frame.size.width / 2.0, result.frame.size.height / 2.0);
    [result addSubview:imageView];
    result.backgroundColor = [UIColor clearColor];
    return result;
}

- (BOOL)textFieldShouldReturn:(UITextField *)textField
{
    [self.emailField resignFirstResponder];
    return YES;
}

-(void)TextFieldEnd:(id)sender {
    [sender resignFirstResponder];
}

-(void)passwordEnd:(id)sender {
    [self resignFirstResponder];
}

-(void)backgroundTap:(id)sender {
    [self.emailField resignFirstResponder];
}


- (void)loginDidStart
{
    _getCodeButton.enabled = NO;
    _emailField.enabled = NO;
}

- (void)loginDidStop
{
    _getCodeButton.enabled = YES;
    _emailField.enabled = YES;
}


-(void)getCodeBtnProc
{
    if(_emailField.text.length>0 &&[_emailField.text isValidateEmail])
    {
            [_emailField resignFirstResponder];
            [self loginDidStart];
            [self performSelector:@selector(resetPwdAction) withObject:nil afterDelay:0];
    }
    else
    {
        UIAlertView *alertView = [[UIAlertView alloc] initWithTitle:NSLocalizedString(@"Sys_Tips","提示") message:NSLocalizedString(@"Login_Please_InputEmail",@"请输入正确的邮箱地址") delegate:nil cancelButtonTitle:NSLocalizedString(@"Sys_Ok",@"确认") otherButtonTitles: nil];
        [alertView show];
    }
}

-(void)resetPwdAction
{
    NSLog(@"----------------*******************_______________________");
    [_dataService cancel];
    self.progressHUD.mode = MBProgressHUDModeIndeterminate;
    self.progressHUD.detailsLabelText =nil;
    self.progressHUD.labelText = NSLocalizedString(@"Log_Get_Email_Code", @"正在验证邮箱");
    [self.progressHUD show:YES];
    [_dataService requestOMEResetPasswordByEmailWithEmail:_emailField.text appId:OMEAPPID language:@"en"];
}

#pragma mark - OMEDataService

- (void)dataService:(OMEDataService *)dataService didFinishWithData:(NSDictionary *)dataDic
{
    [self loginDidStop];
    //NSLog(@"datDic =%@", dataDic);
    [self showWarmTitle:NSLocalizedString(@"Log_Determine_Email_Success",@"邮箱验证成功,请查收邮件") message:nil withImageName:@"37x-Checkmark.png"];
    [self.navigationController popViewControllerAnimated:YES];
}

- (void)dataService:(OMEDataService *)dataService didFailWithError:(NSError *)error
{
     [self loginDidStop];
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
