//
//  registerViewController.m
//  BLT-Spo2Monitor
//
//  Created by luteng on 16/6/30.
//  Copyright © 2016年 luteng. All rights reserved.
//

#import "registerViewController.h"
#import "consDefine.h"
#import "UIColor+BltSoft.h"
#import "OMEVerifyRegisterDataService.h"
#import "NSString+Omesoft.h"
#import "MBProgressHUD.h"
#import "HTAutocompleteManager.h"
#import "Constants.h"
#import "UIImage+Omesoft.h"
@interface registerViewController ()<UITextFieldDelegate,OMEDataServiceDelegate,UIAlertViewDelegate,MBProgressHUDDelegate>
@property(strong, nonatomic)UIImageView* logoImageView;
@property(strong, nonatomic)HTAutocompleteTextField* emailField;
@property(strong, nonatomic)UITextField* passWordField;
@property(strong, nonatomic)UITextField* confirmPasswordField;
@property(strong, nonatomic)UIButton* registerButton;
@property(strong, nonatomic)MBProgressHUD* progressedHUD;
@property (strong, nonatomic) OMEVerifyRegisterDataService *dataService;
@end

@implementation registerViewController

static BOOL GEmailEdit = false;
static BOOL GPwdEdit = false;
static bool GConEdit = false;
- (void)dealloc
{
    [_dataService cancel];
    _dataService.delegate = nil;
}

- (void)viewWillDisappear:(BOOL)animated
{
    [_dataService cancel];
    [super viewWillDisappear:animated];
}

- (void)viewDidLoad {
    [super viewDidLoad];
    self.navigationController.navigationBarHidden = NO;
    self.view.backgroundColor = [UIColor colorWithRed:47.0/255 green:62.0/255 blue:105.0/255 alpha:1.0];
    [self.navigationController.navigationBar setBarTintColor:[UIColor colorWithRed:63.0/255 green:92.0/255 blue:154.0/255 alpha:1.0]];
    [self.navigationController.navigationBar setTintColor:UUWhite];
    UILabel *customLab = [[UILabel alloc] initWithFrame:CGRectMake(0, 0, 100, 30)];
    [customLab setTextColor:UUWhite];
    [customLab setText:NSLocalizedString(@"Login_Register", @"注册")];
    customLab.font = [UIFont boldSystemFontOfSize:20];
    self.navigationItem.titleView = customLab;
    [self.view addSubview:self.logoImageView];
    //y邮箱地址
    [HTAutocompleteTextField setDefaultAutocompleteDataSource:[HTAutocompleteManager sharedManager]];
    [self.view addSubview:self.emailField];
    self.emailField.autocompleteType = HTAutocompleteTypeEmail;
    self.emailField.keyboardType = UIKeyboardTypeEmailAddress;
    self.emailField.frame = CGRectMake(20, self.logoImageView.frame.origin.y+self.logoImageView.frame.size.height+10, self.view.frame.size.width-20*2, 40);
    _emailField.textColor = [UIColor whiteColor];
    _emailField.leftView = [self logoViewWithImage:[UIImage imageNamed:@"login_EMAIL.png"]];
    _emailField.leftViewMode= UITextFieldViewModeAlways;
    _emailField.layer.borderWidth=1;
    _emailField.layer.borderColor=[[UIColor clearColor] CGColor];//UUDeepGrey.CGColor;
    _emailField.clearButtonMode = UITextFieldViewModeWhileEditing;
    _emailField.placeholder = NSLocalizedString(@"Login_Email_Address", @"邮箱地址");
    _emailField.delegate = self;
    _emailField.text = @"";
    [_emailField addTarget:self action:@selector(TextFieldEnd:) forControlEvents:UIControlEventEditingDidEnd];
    CALayer *emailLayer = [[CALayer alloc] init];
    emailLayer.frame = CGRectMake(_emailField.frame.origin.x, _emailField.frame.origin.y + _emailField.frame.size.height,_emailField.frame.size.width, 1);
    emailLayer.backgroundColor = [UIColor whiteColor].CGColor;
    [self.view.layer addSublayer:emailLayer];
    //密码
    [self.view addSubview:self.passWordField];
    _passWordField.frame =CGRectMake(20, emailLayer.frame.origin.y+emailLayer.frame.size.height+5, self.view.frame.size.width-20*2, 40);
    _passWordField.textColor = [UIColor whiteColor];
    _passWordField.leftView = [self logoViewWithImage:[UIImage imageNamed:@"login_LOCK.png"]];
    _passWordField.leftViewMode= UITextFieldViewModeAlways;
    _passWordField.layer.borderWidth=1;
    _passWordField.layer.borderColor=[[UIColor clearColor] CGColor];//UUDeepGrey.CGColor;
    _passWordField.clearButtonMode = UITextFieldViewModeWhileEditing;
    _passWordField.placeholder = NSLocalizedString(@"Login_password", @"密码");
    _passWordField.delegate = self;
    _passWordField.secureTextEntry = YES; //是否以密码形式显示
    _passWordField.text = @"";
    [_passWordField addTarget:self action:@selector(passwordEnd:) forControlEvents:UIControlEventEditingDidEnd];
    CALayer *passLayer = [[CALayer alloc] init];
    passLayer.frame = CGRectMake(_passWordField.frame.origin.x,_passWordField.frame.origin.y + _passWordField.frame.size.height,_passWordField.frame.size.width, 1);
    passLayer.backgroundColor = [UIColor whiteColor].CGColor;
    [self.view.layer addSublayer:passLayer];
    //确认密码
    [self.view addSubview:self.confirmPasswordField];
    _confirmPasswordField.frame = CGRectMake(20, passLayer.frame.origin.y+passLayer.frame.size.height+5, self.view.frame.size.width-20*2, 45);
    _confirmPasswordField.textColor = [UIColor whiteColor];
    _confirmPasswordField.leftView = [self logoViewWithImage:[UIImage imageNamed:@"login_LOCK.png"]];
    _confirmPasswordField.leftViewMode= UITextFieldViewModeAlways;
    _confirmPasswordField.layer.borderWidth=1;
    _confirmPasswordField.layer.borderColor=[[UIColor clearColor] CGColor];//UUDeepGrey.CGColor;
    _confirmPasswordField.clearButtonMode = UITextFieldViewModeWhileEditing;
    _confirmPasswordField.placeholder = NSLocalizedString(@"Login_Confirm_Password", @"确认密码");
    _confirmPasswordField.delegate = self;
    _confirmPasswordField.secureTextEntry = YES; //是否以密码形式显示
    _confirmPasswordField.text = @"";
    [_confirmPasswordField addTarget:self action:@selector(passwordEnd:) forControlEvents:UIControlEventEditingDidEnd];
    CALayer *confirmLayer = [[CALayer alloc] init];
    confirmLayer.frame = CGRectMake(_confirmPasswordField.frame.origin.x,_confirmPasswordField.frame.origin.y + _confirmPasswordField.frame.size.height,_confirmPasswordField.frame.size.width, 1);
    confirmLayer.backgroundColor = [UIColor whiteColor].CGColor;
    [self.view.layer addSublayer:confirmLayer];
    //注册
    [self.view addSubview:self.registerButton];
    //_registerButton.backgroundColor = [UIColor colorWithHexString:@"#cc4452"];
    [_registerButton setBackgroundImage:[UIImage createImageWithColor:[UIColor colorWithRed:199.0/255 green:72.0/255 blue:105.0/255 alpha:1.0]] forState:UIControlStateNormal];
    [_registerButton setBackgroundImage:[UIImage createImageWithColor:[UIColor colorWithRed:199.0/255 green:102.0/255 blue:127.0/255 alpha:1.0]] forState:UIControlStateHighlighted];
    _registerButton.layer.cornerRadius = 8;
    _registerButton.clipsToBounds = YES;
    [_registerButton setTitleColor:UUWhite forState:UIControlStateNormal];
    //[_registerButton setTitleColor:UULightGrey forState:UIControlStateHighlighted];
    [_registerButton setTitle:NSLocalizedString(@"Login_Register",@"注册") forState:UIControlStateNormal];
    [_registerButton addTarget:self action:@selector(RegisterProc:) forControlEvents:UIControlEventTouchUpInside];
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

-(UIImageView*)logoImageView
{
    if(!_logoImageView)
    {
        UIImage* image = [UIImage imageNamed:@"login_logo.png"];
        _logoImageView = [[UIImageView alloc]initWithFrame:CGRectMake((self.view.frame.size.width - image.size.width)/2,100, image.size.width, image.size.height)];
        _logoImageView.image = image;
    }
    return _logoImageView;
}

-(HTAutocompleteTextField*)emailField
{
    if(!_emailField)
    {
        _emailField = [[HTAutocompleteTextField alloc]init];
        _emailField.backgroundColor = [UIColor clearColor];
    }
    return _emailField;
}

-(UITextField*)passWordField
{
    if(!_passWordField)
    {
        _passWordField = [[UITextField alloc]init];
        _passWordField.backgroundColor = [UIColor clearColor];
    }
    return _passWordField;
}

-(UITextField*)confirmPasswordField
{
    if(!_confirmPasswordField)
    {
        _confirmPasswordField = [[UITextField alloc]init];
        _confirmPasswordField.backgroundColor = [UIColor clearColor];
    }
    return _confirmPasswordField;
}

-(UIButton*)registerButton
{
    if(!_registerButton)
    {
        _registerButton = [[UIButton alloc]initWithFrame:CGRectMake(self.confirmPasswordField.frame.origin.x, self.confirmPasswordField.frame.origin.y+self.confirmPasswordField.frame.size.height+40,self.confirmPasswordField.frame.size.width, 45)];
    }
    return _registerButton;

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

-(BOOL)textFieldShouldBeginEditing:(UITextField *)textField
{
    if(textField == _emailField)
    {
        GEmailEdit = YES;
    }
    else if(textField == _passWordField)
    {
        GPwdEdit = YES;
    }
    else if(textField == _confirmPasswordField)
    {
        GConEdit = YES;
    }
    CGFloat offset = self.view.frame.size.height-(textField.frame.origin.y+textField.frame.size.height+216+70);
    if(offset <=0)
    {
            [UIView animateWithDuration:0.3 animations:^{
            CGRect frame = self.view.frame;
            frame.origin.y = offset;
            self.view.frame = frame;
            }];
    }
    return YES;
}

-(BOOL)textFieldShouldEndEditing:(UITextField *)textField
{
    if(textField == _emailField)
    {
        GEmailEdit = NO;
    }
    else if(textField == _passWordField)
    {
        GPwdEdit = NO;
    }
    else if(textField == _confirmPasswordField)
    {
        GConEdit = NO;
    }
    //NSLog(@"GEmailEdit = %d, GPwdEdit =%d, GConEdit =%d", GEmailEdit, GPwdEdit, GConEdit);
    if( (GEmailEdit ==0 && GPwdEdit==1 &&GConEdit==0)||(GEmailEdit==0 && GPwdEdit==0 &&GConEdit==1)|| (GEmailEdit==1 && GPwdEdit==0 &&GConEdit==0))
    {
        
    }
    else
    {
        [UIView animateWithDuration:0.3 animations:^{
            CGRect frame = self.view.frame;
            frame.origin.y = 0;
            self.view.frame = frame;
        }];
    }
    return YES;
}

- (BOOL)textFieldShouldReturn:(UITextField *)textField
{
    [self.emailField resignFirstResponder];
    [self.passWordField resignFirstResponder];
    [self.confirmPasswordField resignFirstResponder];
    return YES;
}

-(void)TextFieldEnd:(id)sender {
    [sender resignFirstResponder];
}

-(void)passwordEnd:(id)sender {
    [self resignFirstResponder];
}
#pragma mark TextFieldDidChange
-(void)TextFieldDidChange
{
    if (_emailField.text.length > 0 && _emailField.text.length > 0) {
        _registerButton.enabled = YES;
    } else {
        _registerButton.enabled = NO;
    }
}


-(void)backgroundTap:(id)sender {
    [self.emailField resignFirstResponder];
    [self.passWordField resignFirstResponder];
    [self.confirmPasswordField resignFirstResponder];
}

-(void)RegisterProc:(id)sender
{
    if(_emailField.text.length>0 &&[_emailField.text isValidateEmail])
    {
        if(_passWordField.text.length>0)
        {
            if(_confirmPasswordField.text.length>0)
            {
                if([_confirmPasswordField.text isEqualToString:_passWordField.text])
                {
                    [_emailField resignFirstResponder];
                    [_passWordField resignFirstResponder];
                    [_confirmPasswordField resignFirstResponder];
                    [self loginDidStart];
                    [self performSelector:@selector(loginAction) withObject:nil afterDelay:0];
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
            UIAlertView *alertView = [[UIAlertView alloc] initWithTitle:NSLocalizedString(@"Sys_Tips","提示") message:NSLocalizedString(@"Login_Please_InputPassword",@"请输入密码")delegate:nil cancelButtonTitle:NSLocalizedString(@"Sys_Ok",@"确认") otherButtonTitles: nil];
            [alertView show];
        }
    }
    else
    {
        UIAlertView *alertView = [[UIAlertView alloc] initWithTitle:NSLocalizedString(@"Sys_Tips","提示") message:NSLocalizedString(@"Login_Please_InputEmail",@"正确的邮箱地址") delegate:nil cancelButtonTitle:NSLocalizedString(@"Sys_Ok",@"确认") otherButtonTitles: nil];
        [alertView show];
    }
}

- (void)loginDidStart
{
    _registerButton.enabled = NO;
    _emailField.enabled = NO;
    _passWordField.enabled = NO;
    _confirmPasswordField.enabled = NO;
}

- (void)loginDidStop
{
    _registerButton.enabled = YES;
    _emailField.enabled = YES;
    _passWordField.enabled = YES;
    _confirmPasswordField.enabled = YES;
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

-(void)loginAction
{
    self.progressedHUD.labelText =@"";
    self.progressedHUD.detailsLabelText =nil;
    self.progressedHUD.labelText = NSLocalizedString(@"Login_Registering", @"正在注册");
    self.progressedHUD.mode = MBProgressHUDModeIndeterminate;
    [self.progressedHUD show:YES];
    [self.dataService cancel];
    NSString* psword = [NSString md5:_passWordField.text];
    NSDictionary *dic = [NSDictionary dictionaryWithObjectsAndKeys:
                         @"Register", @"key",
                         @(303),@"type" ,
                         @(OMEAPPID),@"appId",
                         _emailField.text,@"email",
                         psword,@"pwd",
                         @(ENGLISH),@"language",nil];
    NSString *jsonStr = [NSString jsonStringWithDictionary:dic];
    [self.dataService requestRegisterWithJson:jsonStr];
}

#pragma mark -  OMEDataService

- (void)dataService:(OMEDataService *)dataService didFinishWithData:(NSDictionary *)dataDic
{
    [self loginDidStop];
    [self.progressedHUD hide:YES];
    UIAlertView *alertView = [[UIAlertView alloc] initWithTitle:NSLocalizedString(@"Login_Congratulations", @"恭喜") message:NSLocalizedString(@"Login_Register_Succss", @"邮箱注册成功!请验证邮箱!") delegate:self cancelButtonTitle:NSLocalizedString(@"Sys_Ok", @"确定") otherButtonTitles: nil];
    [alertView show];
}
- (void)dataService:(OMEDataService *)dataService didFailWithError:(NSError *)error
{
    //NSLog(@"-------error: %@", error.localizedDescription);
    [self loginDidStop];
    //[self performSelector:@selector(showWarmTitle:message:) withObject:nil afterDelay:2.0];
    [self showWarmTitle:error.localizedFailureReason message:error.localizedDescription];
}

- (void)showWarmTitle:(NSString *)title message:(NSString *)msg
{
   // NSLog(@"----------------1233434title = %@, msg =%@", title, msg);
    //[_progressedHUD hide:YES afterDelay:2.0];
    //MBProgressHUD *progressHUD = [[MBProgressHUD alloc] initWithView:self.navigationController.view];
    //[self.navigationController.view addSubview:progressHUD];
    self.progressedHUD.customView = [[UIImageView alloc] initWithImage:[UIImage imageNamed:@"37x-Failed.png"]];
    //progressHUD.delegate = self;
    //progressHUD.customView = [[UIImageView alloc] initWithImage:[UIImage imageNamed:@"37x-Checkmark.png"]];
    _progressedHUD.mode = MBProgressHUDModeCustomView;
    if(!title)
    {
        NSLog(@"111111111");
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

#pragma mark - UIAlertViewDelegate
- (void)alertView:(UIAlertView *)alertView clickedButtonAtIndex:(NSInteger)buttonIndex
{
    NSLog(@"-------------------");
    [self.navigationController popToRootViewControllerAnimated:YES];
}

#pragma mark -  Get Methods
- (OMEVerifyRegisterDataService *)dataService
{
    if (!_dataService) {
        _dataService = [[OMEVerifyRegisterDataService alloc] init];
        _dataService.delegate = self;
    }
    return _dataService;
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
