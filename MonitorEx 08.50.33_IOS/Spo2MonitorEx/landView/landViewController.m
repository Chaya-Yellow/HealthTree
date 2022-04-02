//
//  landViewController.m
//  BLT-Spo2Monitor
//
//  Created by luteng on 16/6/30.
//  Copyright © 2016年 luteng. All rights reserved.
//

#import "landViewController.h"
#import "consDefine.h"
#import "UIColor+Bltsoft.h"
#import "registerViewController.h"
#import "ViewController.h"
#import "forgetPasswordViewController.h"
#import "changePasswordViewController.h"
#import "HTAutocompleteManager.h"
#import "HyTransitions.h"
#import "HyLoginButton.h"
#import "NSString+Omesoft.h"
#import "OMELoginDataService.h"
#import "MBProgressHUD.h"
#import "Constants.h"
#import "MXUserData.h"
#import "MXUser.h"
#import "MXUserDefaults.h"
#import "OMESyncManager.h"
#import "MXUserService.h"
#import "UIImage+Omesoft.h"
@interface landViewController ()<UITextFieldDelegate, UIViewControllerTransitioningDelegate,OMEDataServiceDelegate,MBProgressHUDDelegate>
@property(strong, nonatomic)UIImageView* backGroundView;
@property(strong, nonatomic)UIButton* landButton;
@property(strong, nonatomic)UIButton* registerButton;
@property(strong, nonatomic)UIButton* forgetButton;
@property(strong, nonatomic)HTAutocompleteTextField* emailField;
@property(strong, nonatomic)UITextField* passwordField;
@property(strong, nonatomic)UIImageView* logoView;
@property(strong, nonatomic)OMELoginDataService *dataService;
@property(strong, nonatomic)MBProgressHUD* progressedHUD;
@end

static BOOL emailBeEdit =false;
static BOOL pwdBeEdit = false;
@implementation landViewController

- (void)viewDidLoad {
    [super viewDidLoad];
    /*!
     *    @author luteng, 16-06-30 16:06:57
     *
     *    @brief  点击空白区域  收起textFieldView
     */
    self.view.userInteractionEnabled = YES;
    UITapGestureRecognizer *singleTap = [[UITapGestureRecognizer alloc] initWithTarget:self action:@selector(backgroundTap:)];
    [self.view addGestureRecognizer:singleTap];
    self.navigationController.navigationBarHidden = YES;
    [self.view addSubview:self.backGroundView];
    [self.view addSubview:self.logoView];
    //
    [self.view addSubview:self.emailField];
    self.emailField.frame = CGRectMake(20, self.logoView.frame.origin.y+self.logoView.frame.size.height+20, self.view.frame.size.width-20*2, 40);
    //邮箱自匹配补全
    [HTAutocompleteTextField setDefaultAutocompleteDataSource:[HTAutocompleteManager sharedManager]];
    self.emailField.autocompleteType = HTAutocompleteTypeEmail;
    //self.emailField.keyboardType = UIKeyboardType;
    _emailField.textColor = [UIColor whiteColor];
    _emailField.leftView =
        [self logoViewWithImage:[UIImage imageNamed:@"login_EMAIL.png"]];
    _emailField.leftViewMode = UITextFieldViewModeAlways;
    _emailField.layer.borderWidth = 1;
    _emailField.layer.borderColor = [[UIColor clearColor] CGColor];  // UUDeepGrey.CGColor;
    _emailField.clearButtonMode = UITextFieldViewModeWhileEditing;
    _emailField.placeholder = NSLocalizedString(@"Login_Email_Address", "邮箱地址");
    _emailField.delegate = self;
    _emailField.text =[[MXUserDefaults sharedInstance] userEmailAddress];
    [_emailField addTarget:self action:@selector(TextFieldEnd:) forControlEvents:UIControlEventEditingDidEnd];
    CALayer *emailLayer = [[CALayer alloc] init];
    emailLayer.frame = CGRectMake(_emailField.frame.origin.x, _emailField.frame.origin.y + _emailField.frame.size.height,_emailField.frame.size.width, 1);
    emailLayer.backgroundColor = [UIColor whiteColor].CGColor;
    [self.view.layer addSublayer:emailLayer];
    //
    [self.view addSubview:self.passwordField];
    _passwordField.frame = CGRectMake(
        20, emailLayer.frame.origin.y + emailLayer.frame.size.height + 5,
        self.view.frame.size.width - 20 * 2, 40);
    _passwordField.textColor = [UIColor whiteColor];
    _passwordField.leftView = [self logoViewWithImage:[UIImage imageNamed:@"login_LOCK.png"]];
    _passwordField.leftViewMode= UITextFieldViewModeAlways;
    _passwordField.layer.borderWidth=1;
    _passwordField.layer.borderColor=[[UIColor clearColor] CGColor];//UUDeepGrey.CGColor;
    _passwordField.clearButtonMode = UITextFieldViewModeWhileEditing;
    _passwordField.placeholder = NSLocalizedString(@"Login_password", @"密码");
    _passwordField.delegate = self;
    _passwordField.secureTextEntry = YES; //是否以密码形式显示
    _passwordField.text =[[MXUserDefaults sharedInstance] userAccountPwd];
    [_passwordField addTarget:self action:@selector(passwordEnd:) forControlEvents:UIControlEventEditingDidEnd];
    CALayer *passLayer = [[CALayer alloc] init];
    passLayer.frame = CGRectMake(_passwordField.frame.origin.x,_passwordField.frame.origin.y + _passwordField.frame.size.height,_passwordField.frame.size.width, 1);
    passLayer.backgroundColor = [UIColor whiteColor].CGColor;
    [self.view.layer addSublayer:passLayer];
    //
    [self.view addSubview:self.landButton];
    [_landButton setBackgroundImage:[UIImage createImageWithColor:[UIColor colorWithRed:0.780 green:0.282 blue:0.412 alpha:1.000]] forState:UIControlStateNormal];
     [_landButton setBackgroundImage:[UIImage createImageWithColor:[UIColor colorWithRed:199.0/255 green:102.0/255 blue:127.0/255 alpha:1.0]] forState:UIControlStateHighlighted];
    _landButton.layer.cornerRadius = 8;
    _landButton.clipsToBounds = YES;
    [_landButton setTitleColor:UUWhite forState:UIControlStateNormal];
    [_landButton setTitle:NSLocalizedString(@"Login_login",@"登录") forState:UIControlStateNormal];
    [_landButton addTarget:self action:@selector(landProc:) forControlEvents:UIControlEventTouchUpInside];
    //
    [self.view addSubview:self.registerButton];
    [_registerButton setBackgroundImage:[UIImage createImageWithColor:[UIColor colorWithRed:80.0/255 green:113.0/255 blue:171.0/255 alpha:1]] forState:UIControlStateNormal];
    [_registerButton setBackgroundImage:[UIImage createImageWithColor:[UIColor colorWithRed:117.0/255 green:136.0/255 blue:171.0/255 alpha:1]] forState:UIControlStateHighlighted];
    _registerButton.layer.cornerRadius = 8;
    _registerButton.clipsToBounds = YES;
    [_registerButton setTitleColor:UUWhite forState:UIControlStateNormal];
    [_registerButton setTitle:NSLocalizedString(@"Login_Register", @"注册") forState:UIControlStateNormal];
    [_registerButton addTarget:self action:@selector(registerProc:) forControlEvents:UIControlEventTouchUpInside];
    //
    [self.view addSubview:self.forgetButton];
    _forgetButton.backgroundColor = [UIColor clearColor];
    _forgetButton.layer.cornerRadius = 8;
    [_forgetButton setTitleColor:UUWhite forState:UIControlStateNormal];
    [_forgetButton setTitleColor:UULightGrey forState:UIControlStateHighlighted];
    [_forgetButton setTitle:NSLocalizedString(@"Login_Forget_Password",@"忘记密码?") forState:UIControlStateNormal];
    [_forgetButton addTarget:self action:@selector(forgetPasswordProc:) forControlEvents:UIControlEventTouchUpInside];
    //
    self.dataService = [[OMELoginDataService alloc] init];
    _dataService.delegate = self;
    //
    NSInteger idNum = [[MXUserService sharedInstance] getCurrentLoginedMemeberId];
    NSLog(@"--------------------******************idum=%ld", idNum);
    if (!idNum)
    {
        MXUserData *userData = [[MXUserData alloc] init];
        BOOL isInsert =[userData loginAccountWithId:DEFAULT_ACCOUNT_ID key:ACCOUNT_KEY];
        NSLog(@"accountId = %lld key = %@ bInsertDataBase = %d", (long long)idNum, ACCOUNT_KEY, isInsert);
        //
        MXUser *selectedUser = [[MXUser alloc]init];
        //初始化默认成员信息
        selectedUser.gender = 0;
        selectedUser.weight = 50;
        selectedUser.height = 168;
        selectedUser.birthday =[@"1980-01-01" stringWihtDateFormat:@"yyyy-MM-dd"];
        selectedUser.name = @"Test";
        NSArray *loginedArray = [userData getLoginInfo];
        NSLog(@"arrCount %ld", [loginedArray count]);
        if ([loginedArray count])
        {
            NSDictionary *dataDic = [loginedArray objectAtIndex:0];
            NSInteger idNum = [[dataDic objectForKey:@"id"] intValue];
            NSLog(@"familyID: %ld memberID = %ld",selectedUser.familyId, idNum);
            [userData addNewUserWithData: selectedUser familyId:DEFAULT_FAMILY_ID memberId: idNum];
        }
    }
    idNum = [[MXUserService sharedInstance] getCurrentLoginedMemeberId];
    NSLog(@"--------------------******************idum=%ld", idNum);
    if(idNum)
    {
        [self didPresentControllerButtonTouch:(NO)];
    }
}


- (void)keyboardWillShow:(NSNotification *)aNotification
{
    NSDictionary *userInfo = [aNotification userInfo];
    CGRect keyboardRect = [[userInfo objectForKey:UIKeyboardFrameEndUserInfoKey]
                           CGRectValue];
    NSTimeInterval animationDuration = [[userInfo
                                         objectForKey:UIKeyboardAnimationDurationUserInfoKey] doubleValue];
    
    CGRect newFrame = self.view.frame;
    newFrame.size.height -= keyboardRect.size.height;
    [UIView beginAnimations:@"ResizeTextView" context:nil];
    [UIView setAnimationDuration:animationDuration];
    self.view.frame = newFrame;
    [UIView commitAnimations];
}

- (void)didReceiveMemoryWarning {
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}


-(void)viewWillAppear:(BOOL)animated
{
    self.navigationController.navigationBarHidden = YES;
    self.passwordField.text =[[MXUserDefaults sharedInstance] userAccountPwd];
}

-(UIImageView*)backGroundView
{
    if(!_backGroundView)
    {
        _backGroundView =[[UIImageView alloc]initWithFrame:CGRectMake(0, 0, self.view.frame.size.width, self.view.frame.size.height)];
        UIImage* image = [UIImage imageNamed:@"login_bg.png"];
        _backGroundView.image = image;
    }
    return _backGroundView;
}

-(UIImageView*)logoView
{
    if(!_logoView)
    {
        UIImage* image = [UIImage imageNamed:@"login_logo.png"];
        _logoView = [[UIImageView alloc]initWithFrame:CGRectMake((self.view.frame.size.width - image.size.width)/2,80, image.size.width, image.size.height)];
        _logoView.image = image;
    }
    return _logoView;
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

-(UITextField*)passwordField
{
    if(!_passwordField)
    {
        _passwordField = [[UITextField alloc]init];
        _passwordField.backgroundColor = [UIColor clearColor];
    }
    return _passwordField;
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

-(UIButton*)landButton
{
    if(!_landButton)
    {
        _landButton = [[UIButton alloc]initWithFrame:CGRectMake(self.passwordField.frame.origin.x, self.passwordField.frame.origin.y+self.passwordField.frame.size.height+20,self.passwordField.frame.size.width, 45)];
    }
    return _landButton;
}

-(UIButton*)registerButton
{
    if(!_registerButton)
    {
        _registerButton = [[UIButton alloc]initWithFrame:CGRectMake(self.landButton.frame.origin.x, self.landButton.frame.origin.y+self.landButton.frame.size.height+10, self.landButton.frame.size.width, 45)];
    }
    return _registerButton;
}

-(UIButton*)forgetButton
{
    if(!_forgetButton)
    {
        _forgetButton = [[UIButton alloc]initWithFrame:CGRectMake(self.registerButton.frame.origin.x, self.registerButton.frame.origin.y+self.registerButton.frame.size.height+10, self.registerButton.frame.size.width, 45)];
    }
    return _forgetButton;
}

-(BOOL)textFieldShouldBeginEditing:(UITextField *)textField
{
    
    if(textField ==_emailField)
    {
        emailBeEdit = YES;
    }
    else if(textField == _passwordField)
    {
        pwdBeEdit = YES;
    }
    //NSLog(@"line = %d,emailBeEdit = %d, pwdBrEdit = %d",__LINE__, emailBeEdit, pwdBeEdit);
    CGFloat offset = self.view.frame.size.height-(textField.frame.origin.y+textField.frame.size.height+216+60);
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
    if(textField ==_emailField)
    {
        emailBeEdit = NO;
    }
    else if(textField == _passwordField)
    {
        pwdBeEdit = NO;
    }
    //NSLog(@"line = %d,emailBeEdit = %d, pwdBrEdit = %d",__LINE__, emailBeEdit, pwdBeEdit);
    if( (emailBeEdit==0 && pwdBeEdit ==1)||(emailBeEdit==1 && pwdBeEdit==0))
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
    [self.passwordField resignFirstResponder];
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
    [self.passwordField resignFirstResponder];
}

-(void)registerProc:(id)sender
{
    registerViewController* Register = [[registerViewController alloc] init];
    if(Register)
    {
        [self.navigationController pushViewController:Register animated:YES];
    }
}

-(void)landProc:(UIButton*)sender
{
    //[self didPresentControllerButtonTouch];
    if(_emailField.text.length>0 &&[_emailField.text isValidateEmail])
    {
        if(_passwordField.text.length>0)
        {
            [_emailField resignFirstResponder];
            [_passwordField resignFirstResponder];
            [self loginDidStart];
            [self performSelector:@selector(loginAction) withObject:nil afterDelay:0];
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

- (void)didPresentControllerButtonTouch:(BOOL)bAnimation {
//     [[OMESyncManager sharedInstance] startSync];
     ViewController* mainController =[[ViewController alloc]init];
     if(mainController)
     {
         //self.navigationController.transitioningDelegate =self;
         [self.navigationController pushViewController:mainController animated:bAnimation];
     }
}


-(void)forgetPasswordProc:(id)sender
{
    //changePasswordViewController* controller = [[changePasswordViewController  alloc]init];
    forgetPasswordViewController* controller = [[forgetPasswordViewController alloc] init];
    if(controller)
    {
        [self.navigationController pushViewController:controller animated:YES];
    }
}

- (void)loginAction
{
    //NSLog(@"*************************");
    self.progressedHUD.labelText =@"";
    self.progressedHUD.detailsLabelText =nil;
    self.progressedHUD.labelText = NSLocalizedString(@"Login_Logining", @"正在登录");
    self.progressedHUD.mode = MBProgressHUDModeIndeterminate;
    [self.progressedHUD show:YES];
    [self.dataService cancel];
    [self.dataService requestOMELogin3WithName:_emailField.text pwd:_passwordField.text appId:OMEAPPID language:@""];
}

- (void)loginDidStart
{
    _landButton.enabled = NO;
    _emailField.enabled = NO;
    _passwordField.enabled = NO;
}

- (void)loginDidStop
{
    _landButton.enabled = YES;
    _emailField.enabled = YES;
    _passwordField.enabled = YES;
}

#pragma mark -  OMEDataService

- (void)dataService:(OMEDataService *)dataService didFinishWithData:(NSDictionary *)dataDic
{
    [self loginDidStop];
    //NSLog(@"dataDic = %@", dataDic);
    NSDictionary *data = [dataDic objectForKey:@"data"];
    NSString *key = [data objectForKey:@"clientKey"];
    long long int idNum = [[data objectForKey:@"accountId"] longLongValue];
    
    MXUserData *userData = [[MXUserData alloc] init];
    BOOL isInsert =[userData loginAccountWithId:idNum key:key];
    NSLog(@"accountId = %lld key = %@", (long long)idNum, key);
    if (!isInsert) {
        [self showWarmTitle:NSLocalizedString(@"Land_Failed", @"登录失败") message:NSLocalizedString(@"Land_Exception", @"登录失败，发生未知异常。") withImageName:@"37x-Failed.png"];
    } else {
        //保存账号密码，到用户设置
        [[MXUserDefaults sharedInstance] setUserEmailAddress:_emailField.text];
        [[MXUserDefaults sharedInstance] setUserAccountPwd:_passwordField.text];
        //
        [self showWarmTitle:NSLocalizedString(@"Land_Success",@"登录成功") message:nil withImageName:@"37x-Checkmark.png"];
        [self didPresentControllerButtonTouch:YES];
        /*
        AppDelegate *app = (AppDelegate *)[[UIApplication sharedApplication] delegate];
        CATransition *animation = [CATransition animation];
        animation.duration = 0.5f;
        animation.timingFunction = UIViewAnimationCurveEaseInOut;
        animation.fillMode = kCAFillModeForwards;
        animation.type = kCATransitionReveal;
        animation.subtype = kCATransitionFromBottom;
        [app.window.layer addAnimation:animation forKey:@"animation"];
        [app appDidLogin];
        [UIView commitAnimations];
        [[NSUserDefaults standardUserDefaults] setObject:[NSString stringWithFormat:@"%@",_phoneNumTextField.text] forKey:@"LoginName"];
        [[NSUserDefaults standardUserDefaults] synchronize];
        [[OMESyncManager sharedInstance] startSync];*/
    }
}

- (void)dataService:(OMEDataService *)dataService didFailWithError:(NSError *)error
{
    [self loginDidStop];
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

/*
#pragma mark - Navigation

// In a storyboard-based application, you will often want to do a little preparation before navigation
- (void)prepareForSegue:(UIStoryboardSegue *)segue sender:(id)sender {
    // Get the new view controller using [segue destinationViewController].
    // Pass the selected object to the new view controller.
}
*/

@end
