//
//  ViewController.m
//  BLT-Spo2Monitor
//
//  Created by luteng on 15/10/19.
//  Copyright © 2015年 luteng. All rights reserved.
//

#import "ViewController.h"
#import "SCActivityIndicatorView.h"
#import "MXBluetoothManager.h"
#import "MXSelectDeviceView.h"
#import "MXDeviceInfo.h"
#import "MXDeviceInfoViewController.h"
#import "MXSpo2WavesView.h"
#import "consDefine.h"
#import "MXSpo2ParamView.h"
#import "AppDelegate.h"
#import "reviewController.h"
#import "MXPRParamView.h"
#import "settingViewController.h"
#import "ReviewTableViewController.h"
#import "UIColor+BltSoft.h"
#import "Constants.h"
#import "MXUser.h"
#import "MXUserService.h"
#import "familyListViewController.h"
#import "OMEImageView.h"
#import "MXTrendData.h"
#import "MXUserData.h"
#import "BLTStoreSpo2TrendManager.h"
#import "Spo2MonitorEx-Bridging-Header.h"
#import "OMESoft.h"
#import "aboutUSViewController.h"
#import <HealthKit/HealthKit.h>
#import "AlarmSetupViewController.h"
#import "MBProgressHUD.h"
#import <AVFoundation/AVFoundation.h> //音频视频框架

#define CLEAN_TIMER(timer){if (timer) {[timer invalidate];timer = nil;}}

@interface ViewController ()<MXBluetoothManagerDelegate,MXSelectDeviceViewDelegate,BLTStoreSpo2TrendDelegate>
@property (strong , nonatomic) UIControl* deviceControl;
@property (strong , nonatomic) UILabel* deviceNameLabel;
@property (assign , nonatomic) NSInteger selectedFamilyId;
@property (strong , nonatomic) UIView *deviceStateView;
@property (strong , nonatomic) MXSpo2WavesView* wavesView;
@property (strong , nonatomic) MXSpo2ParamView* paramView;
@property (strong , nonatomic) MXPRParamView*   prView;
//@property (strong , nonatomic) UIImageView *eleStateImageView;
//@property (strong , nonatomic) UIImageView *eleArrowImageView;
@property (strong , nonatomic) MXSelectDeviceView *selectDeviceView;
@property (strong , nonatomic) NSTimer *selectDeviceTimer;//选择蓝牙设备timer
@property (strong , nonatomic) NSTimer *changeSearchTimer;//断开连接继续2s后更新界面为搜索状态，若关闭蓝牙则不需要。
@property (strong , nonatomic) NSTimer *drawWavesTimes;     //画波形线程
@property (strong , nonatomic) NSTimer *drawBloodBarTimer;  //画灌注强度定时器
@property (strong , nonatomic) NSMutableArray* wavesBuffers;    //波形缓存区
@property (strong , nonatomic) NSMutableArray* bloodBarValues;  //血注强度缓存
@property (nonatomic)BOOL isEnterBack;
@property (nonatomic)BOOL bDrawBaseLine;
@property (strong , nonatomic) MXDeviceInfo *connectedDeviceInfo;
@property (strong , nonatomic) MXUserData *userData;
@property (nonatomic,strong)HKHealthStore *healthStore;    //ihealth 对象
@property (nonatomic,strong)UIImageView *refreshImg; /**< 刷新图标 */
@property (nonatomic, strong)MBProgressHUD *myHud;
@property(nonatomic,strong)AVAudioPlayer *player; //音频播放器

@end

@implementation ViewController
{
        NSMutableArray* Spo2InvalArry;
        int saveTrendCount;
        int Gspo2Value;
        int GprValue;
        CGFloat GpiValue;
        int GrespValue;
        bool bDrawWaves;
        bool bRecive;
        bool bStoreTrend;
}

static int lastBlueToothSate = DeviceConnectionStatusSearching;

- (id)initWithNibName:(NSString *)nibNameOrNil bundle:(NSBundle *)nibBundleOrNil
{
    self = [super initWithNibName:nibNameOrNil bundle:nibBundleOrNil];
    if (self) {
        // Custom initialization
    }
    return self;
}


-(void)initParamValue
{
    Gspo2Value = INVAL_VALUE;
    GprValue = INVAL_VALUE;
    GrespValue = INVAL_VALUE;
    GpiValue = INVAL_VALUE;
    bDrawWaves = FALSE;
    bRecive = true;
    _bDrawBaseLine = FALSE;
    Spo2InvalArry  = [NSMutableArray array];
    _wavesBuffers  = [NSMutableArray array];
    _bloodBarValues = [NSMutableArray array];
    for (int i = 0; i < 3; i++)
    {
        NSNumber* aa =[NSNumber numberWithInt:INVAL_VALUE];
        [Spo2InvalArry addObject:aa];
    }
}

- (void)viewDidLoad {
    [super viewDidLoad];
//    NSLog(@"%s   --------------------------$$$$$$$$$$$$$$$$$$$$$$$$$",__func__);
    self.navigationController.navigationBarHidden = NO;
    [self initParamValue];
    self.view.backgroundColor = [UIColor whiteColor];
    CGRect mainScreen = [[UIScreen mainScreen] applicationFrame];
    mainScreen.size.height -= self.navigationController.navigationBar.frame.size.height + 50;
    [self.navigationController.navigationBar setBarTintColor:UUNavBackColor];
    [self.navigationController.navigationBar setTintColor:UUWhite];
    
    UILabel *customLab = [[UILabel alloc] initWithFrame:CGRectMake(0, 0, 100, 30)];
    [customLab setTextColor:UUWhite];
    [customLab setText:NSLocalizedString(@"OXIMETER", "智能血氧")];
    customLab.font = [UIFont boldSystemFontOfSize:20];
    self.navigationItem.titleView = customLab;
    //数据回顾
    UIButton* btn = [[UIButton alloc]initWithFrame:CGRectMake(self.navigationController.navigationBar.frame.size.width - 40, 0, 40, self.navigationController.navigationBar.frame.size.height)];
    UIImageView* imageView = [[UIImageView alloc]initWithFrame:CGRectMake(5, 5, 30, 30)];
    imageView.image = [UIImage imageNamed:@"trending.png"];
    [btn addSubview:imageView];
    btn.titleLabel.shadowOffset = CGSizeMake(0, -1);
    [btn addTarget: self action: @selector(doclickDataReview) forControlEvents: UIControlEventTouchUpInside];
    self.navigationItem.rightBarButtonItem = [[UIBarButtonItem alloc]initWithCustomView:btn];
    
    //about
    UIButton* leftbtn = [[UIButton alloc]initWithFrame:CGRectMake(self.navigationController.navigationBar.frame.origin.x, 0, 40, self.navigationController.navigationBar.frame.size.height)];
    UIImageView* leftimageView = [[UIImageView alloc]initWithFrame:CGRectMake(5, 5, 30, 30)];
    leftimageView.image = [UIImage imageNamed:@"HomeBell.png"];
    [leftbtn addSubview:leftimageView];
    leftbtn.titleLabel.shadowOffset = CGSizeMake(0, -1);
//    [leftbtn addTarget: self action: @selector(doclickSet) forControlEvents: UIControlEventTouchUpInside];
    [leftbtn addTarget:self action:@selector(doclickSet) forControlEvents:UIControlEventTouchUpInside];
    self.navigationItem.leftBarButtonItem = [[UIBarButtonItem alloc]initWithCustomView:leftbtn];
    
    
    UIImageView *backImg = [[UIImageView alloc] initWithFrame:CGRectMake(0, TOP_SPACING_H, SCREEN_W, 129)];
    backImg.backgroundColor = UUSpo2LabelColor;
    [self.view addSubview:backImg];
    
    //
    [self.view addSubview:self.deviceControl];
    //waves
    NSString* Devicestr = [NSString getDeviceType];
    float heightSpace = 0;
    
//    if([Devicestr isEqualToString:@"Simulator"])
    if([Devicestr isEqualToString:@"IPhone_X"])
    {
        heightSpace = 28;
    }
    int viewHeight = self.view.frame.size.height - (self.deviceControl.frame.origin.y+self.deviceControl.frame.size.height + 0.5) - heightSpace;
    
    UIView *waves_backView = [[UIView alloc] initWithFrame:CGRectMake(BORDER_WIDTH, self.deviceControl.frame.origin.y + self.deviceControl.frame.size.height + BORDER_WIDTH, mainScreen.size.width - 2 * BORDER_WIDTH, viewHeight * 0.3)];
    waves_backView.backgroundColor = [UIColor clearColor];

    CALayer *shadowLayer0 = [[CALayer alloc] init];
    shadowLayer0.frame = waves_backView.bounds;
    shadowLayer0.shadowColor = [UIColor colorWithRed:0.0f/255.0f green:0.0f/255.0f blue:0.0f/255.0f alpha:0.2f].CGColor;
    shadowLayer0.shadowOpacity = 1;
    shadowLayer0.shadowOffset = CGSizeMake(0, 2);
    shadowLayer0.shadowRadius = 3;
    CGFloat shadowSize0 = -1;
    CGRect shadowSpreadRect0 = CGRectMake(-shadowSize0, -shadowSize0, waves_backView.bounds.size.width+shadowSize0*2, waves_backView.bounds.size.height+shadowSize0*2);
    CGFloat shadowSpreadRadius0 =  waves_backView.layer.cornerRadius == 0 ? 0 : waves_backView.layer.cornerRadius+shadowSize0;
    UIBezierPath *shadowPath0 = [UIBezierPath bezierPathWithRoundedRect:shadowSpreadRect0 cornerRadius:shadowSpreadRadius0];
    shadowLayer0.shadowPath = shadowPath0.CGPath;
    [waves_backView.layer addSublayer:shadowLayer0];
    [self.view addSubview:waves_backView];
    
    _wavesView = [[MXSpo2WavesView alloc]initWithFrame:waves_backView.frame];
    _wavesView.backgroundColor = [UIColor whiteColor];
    _wavesView.alpha = 1;
    ViewRadius(_wavesView, CORNER_RADIUS_SIZE);

    //spo2param
    int paramLeft, top, width, height;
    paramLeft = BORDER_WIDTH;
    top = _wavesView.frame.origin.y + viewHeight * 0.3 + BORDER_WIDTH;
    width = mainScreen.size.width - 2 * BORDER_WIDTH;
    height = (viewHeight * 0.7 - 3 * BORDER_WIDTH) * 0.6;
    _paramView = [[MXSpo2ParamView alloc]initWithFrame:CGRectMake(paramLeft, top, width, height)];
    _paramView.backgroundColor = UUCEllFrontColor;
    _paramView.clipsToBounds = YES;
    _paramView.layer.cornerRadius = CORNER_RADIUS_SIZE;
    //pr
    paramLeft = BORDER_WIDTH;
    top += ( (viewHeight * 0.7 - 3 * BORDER_WIDTH) * 0.6 + BORDER_WIDTH);
    width = (mainScreen.size.width- 2 * BORDER_WIDTH);
    height =(viewHeight * 0.7 - 3 * BORDER_WIDTH) * 0.4;
    _prView = [[MXPRParamView alloc]initWithFrame:CGRectMake(paramLeft, top, width, height)];
    _prView.backgroundColor =UUCEllFrontColor;
    _prView.clipsToBounds = YES;
    _prView.layer.cornerRadius = CORNER_RADIUS_SIZE;
    [self.view addSubview:_prView];
    [self.view addSubview:_paramView];
    [self.view addSubview:_wavesView];
    MXBluetoothManager *bluetoothManager = [MXBluetoothManager sharedInstance];
    bluetoothManager.delegate = self;
    [bluetoothManager scan];
    NSLog(@"lastSatte = %i",bluetoothManager.DeviceState);
    switch (bluetoothManager.DeviceState) {
        case DeviceConnectionStatusSearching:
        case DeviceConnectionStatusConnected:
        case DeviceConnectionStatusDisconnected:
            lastBlueToothSate =DeviceConnectionStatusSearching;
            break;
        case DeviceConnectionStatusPowerOff:
            lastBlueToothSate = DeviceConnectionStatusPowerOff;
            break;
        case DeviceConnectionStatusNoSupport:
            lastBlueToothSate = DeviceConnectionStatusNoSupport;
            break;
        default:
            lastBlueToothSate = DeviceConnectionStatusSearching;
            break;
    }
    //
    BLTStoreSpo2TrendManager *manager = [BLTStoreSpo2TrendManager sharedInstance];
    manager.delegate = self;
    [manager startStoreTrend];
    //
    [[NSNotificationCenter defaultCenter] addObserver:self selector:@selector(enterBackgroundNotifi) name:UIApplicationDidEnterBackgroundNotification object:nil];
    [[NSNotificationCenter defaultCenter] addObserver:self selector:@selector(becomeActiveNotifi) name:UIApplicationDidBecomeActiveNotification object:nil];
    [[NSNotificationCenter defaultCenter] addObserver:self selector:@selector(updateFamilyMemInfo) name:familyDidUpdatedNotification object:nil];
    // Do any additional setup after loading the view, typically from a nib.
    // ihealth
    if (![HKHealthStore isHealthDataAvailable])
    {
        NSLog(@"该设备不支持HealthKit");
    }
    else
    {
        NSLog(@"支持HealthKit");
        //创建healthStore对象
        self.healthStore = [[HKHealthStore alloc]init];
        //设置需要获取的权限 这里仅设置了血氧
        // Share body mass, height and body mass index
        NSSet *shareObjectTypes = [NSSet setWithObjects:
                                   [HKObjectType quantityTypeForIdentifier:HKQuantityTypeIdentifierOxygenSaturation],[HKObjectType quantityTypeForIdentifier:HKQuantityTypeIdentifierHeartRate],nil];
        //
        HKObjectType *stepType = [HKObjectType quantityTypeForIdentifier:HKQuantityTypeIdentifierOxygenSaturation];
        HKObjectType *stepType2 = [HKObjectType quantityTypeForIdentifier:HKQuantityTypeIdentifierHeartRate];
        NSSet *healthSet = [NSSet setWithObjects:stepType,stepType2,nil];
        
        //从健康应用中获取权限
        [self.healthStore requestAuthorizationToShareTypes:shareObjectTypes readTypes:healthSet completion:^(BOOL success, NSError * _Nullable error) {
            if (success) {
                //获取步数后我们调用获取步数的方法
                NSLog(@"获取权限成功");
            }
            else
            {
                NSLog(@"获取步数权限失败");
            }
        }];
    }
    
    //创建播放器
    NSURL* url = [NSURL fileURLWithPath:[[NSBundle mainBundle] pathForResource:@"报警声" ofType:@"mp3"]];
    self.player = [[AVAudioPlayer alloc] initWithContentsOfURL:url error:nil];
    [self.player prepareToPlay];
    
}

-(void)sendSpo2DataToIhealth:(float)spo2Value
{
    NSLog(@"Health spo2Value = %.2f", spo2Value);
    //define unit.
    if (spo2Value * 100 > 0)
    {
        // spo2
        NSString *unitIdentifier = HKQuantityTypeIdentifierOxygenSaturation;
        //define quantityType.
        HKQuantityType *quantityTypeIdentifier = [HKObjectType quantityTypeForIdentifier:unitIdentifier];
        
        //init quantity.
        HKQuantity *quantity = [HKQuantity quantityWithUnit:[HKUnit percentUnit] doubleValue:spo2Value];
        
        //init quantity sample.
        
        HKQuantitySample *temperatureSample = [HKQuantitySample quantitySampleWithType:quantityTypeIdentifier quantity:quantity startDate:[NSDate date] endDate:[NSDate date] metadata:nil];
        
        [self.healthStore saveObject:temperatureSample withCompletion:^(BOOL success, NSError *error) {
            if (success) {
                NSLog(@"保存成功....");
            }else {
                NSLog(@"保存失败");
            }
        }];
    }
}

-(void)sendPRDataToIhealth:(float)prValue
{
    NSLog(@"Health spo2Value = %.2f", prValue);
    //define unit.
     // pr
    NSString *unitIdentifier = HKQuantityTypeIdentifierHeartRate;
    //define quantityType.
    HKQuantityType *quantityTypeIdentifier = [HKObjectType quantityTypeForIdentifier:unitIdentifier];

    //init quantity.
    HKQuantity *quantity = [HKQuantity quantityWithUnit:[[HKUnit countUnit] unitDividedByUnit:[HKUnit minuteUnit]] doubleValue:prValue];
    
    //init quantity sample.

    NSDate *endDate = [NSDate date];
    
    HKQuantitySample *temperatureSample = [HKQuantitySample quantitySampleWithType:quantityTypeIdentifier quantity:quantity startDate:endDate endDate:endDate metadata:nil];
    //save.
    [self.healthStore saveObject:temperatureSample withCompletion:^(BOOL success, NSError *error) {
        if (success) {
            NSLog(@"pr保存成功....");
        }else {
            NSLog(@"pr保存失败");
        }
    }];
}

-(void)enterBackgroundNotifi
{
//     NSLog(@"%s   --------------------------$$$$$$$$$$$$$$$$$$$$$$$$$",__func__);
    _isEnterBack = TRUE;
    if (lastBlueToothSate == DeviceConnectionStatusConnected)
    {
        [_wavesView canDrawBaseLine:false];
    }
}

-(void)becomeActiveNotifi
{
//     NSLog(@"%s   --------------------------$$$$$$$$$$$$$$$$$$$$$$$$$",__func__);
    if (lastBlueToothSate == DeviceConnectionStatusConnected && _isEnterBack)
    {
        _isEnterBack = !_isEnterBack;
        [_wavesView canDrawBaseLine:YES];
    }
}

-(void)doclickSet
{
//    aboutUSViewController* about = [[aboutUSViewController alloc]init];
    AlarmSetupViewController *setUp = [[AlarmSetupViewController alloc] init];
    [self.navigationController pushViewController:setUp animated:YES];
}

-(void)doclickDataReview
{
    reviewController* review = [[reviewController alloc]init];
    [self.navigationController pushViewController:review animated:YES];
}

-(void)viewWillAppear:(BOOL)animated
{
//     NSLog(@"%s   --------------------------$$$$$$$$$$$$$$$$$$$$$$$$$",__func__);
    [super viewWillAppear:animated];
    bRecive = true;
    _drawWavesTimes = [NSTimer scheduledTimerWithTimeInterval:0.085 target:self selector:@selector(pushWavesData) userInfo:nil repeats:YES];

    _drawBloodBarTimer =[NSTimer scheduledTimerWithTimeInterval:0.017 target:self selector:@selector(drawBloodBarProc) userInfo:nil repeats:YES];

}

-(void)viewDidAppear:(BOOL)animated
{
//     NSLog(@"%s   --------------------------$$$$$$$$$$$$$$$$$$$$$$$$$",__func__);
    [super viewDidAppear:animated];
    [self updateFamilyMemInfo];
}

-(void)viewWillDisappear:(BOOL)animated
{
//     NSLog(@"%s   --------------------------$$$$$$$$$$$$$$$$$$$$$$$$$",__func__);
    [super viewWillDisappear:animated];
    bRecive = false;
    CLEAN_TIMER(_drawWavesTimes);
    CLEAN_TIMER(_drawBloodBarTimer);
}

-(void)viewDidDisappear:(BOOL)animated
{
//     NSLog(@"%s   --------------------------$$$$$$$$$$$$$$$$$$$$$$$$$",__func__);
}

- (void)didReceiveMemoryWarning {
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}

-(void)drawBloodBarProc
{
//     NSInteger value;
//     if(_bloodBarValues.count > 1)
//     {
//         value = [[_bloodBarValues objectAtIndex:0] integerValue];
//         [_bloodBarValues removeObjectAtIndex:0];
//     }
//     else
//     {
//         value = 0;
//     }
//     [_paramView drawBloodBar:(int)value];
}

-(void)pushWavesData
{
    //demoBloodValue++;
    NSMutableArray* arry = [NSMutableArray array];
    //发送波形数
//    NSLog(@"wavesBuffs.count = %ld, bDrawWaves = %d", _wavesBuffers.count,bDrawWaves);
    if(_wavesBuffers.count >10 && bDrawWaves== TRUE)
    {
//        if(_wavesBuffers.count >300)
//        {
//            arry = (NSMutableArray*)[_wavesBuffers subarrayWithRange:NSMakeRange(0, 4)];
//            [_wavesBuffers removeObjectsInRange:NSMakeRange(0, 4)];
//        }
//        else
//        {
            arry = (NSMutableArray*)[_wavesBuffers subarrayWithRange:NSMakeRange(0, 3)];
            [_wavesBuffers removeObjectsInRange:NSMakeRange(0, 3)];

//        }
    }
    else
    {
        arry = Spo2InvalArry;
//        NSLog(@"waves len = %ld", (long)_wavesBuffers.count);
    }
    NSLog(@"------bDrawBaseLine=%i-----", _bDrawBaseLine);
    [_wavesView drawWaves:arry];
    

}

- (UIControl *)deviceControl
{
    if (!_deviceControl) {
        
        float heightSpace = 28;
        
        _deviceControl = [[UIControl alloc] initWithFrame:CGRectMake(0, SafeAreaTopHeight+44 + heightSpace,self.view.frame.size.width, 60)];
        _deviceControl.backgroundColor = [UIColor clearColor];
        _deviceControl.layer.cornerRadius = CORNER_RADIUS_SIZE;
        [_deviceControl addTarget:self action:@selector(deviceButtonPressed) forControlEvents:UIControlEventTouchUpInside];
        
        UIImageView *deviceImageView = [[UIImageView alloc] initWithFrame:CGRectMake(44, 7, 46, 46)];
        deviceImageView.image = [UIImage imageNamed:@"img_blt_logo.png"];
        [_deviceControl addSubview:deviceImageView];
        
        UIView *deviceInfoContentView = [[UIView alloc] initWithFrame:CGRectMake(2 * deviceImageView.frame.origin.x + deviceImageView.frame.size.width, 0, 220, 10)];
        deviceInfoContentView.userInteractionEnabled = NO;
        self.deviceNameLabel = [[UILabel alloc] initWithFrame:CGRectMake(0, 0, deviceInfoContentView.frame.size.width, 18)];
        _deviceNameLabel.textAlignment = NSTextAlignmentLeft;
        _deviceNameLabel.textColor = UUWhite;
        _deviceNameLabel.text = @"";
        [deviceInfoContentView addSubview:_deviceNameLabel];
        
        self.deviceStateView = [[UIView alloc] initWithFrame:CGRectMake(0, _deviceNameLabel.frame.origin.y + _deviceNameLabel.frame.size.height + 4, deviceInfoContentView.frame.size.width, 14)];
        _deviceStateView.backgroundColor = [UIColor clearColor];
        [deviceInfoContentView addSubview:_deviceStateView];
        
        [self updateDeviceInfo];
        [self updateDeviceConnectedStatus:lastBlueToothSate];
        
        deviceInfoContentView.frame = CGRectMake(deviceInfoContentView.frame.origin.x, deviceInfoContentView.frame.origin.y, deviceInfoContentView.frame.size.width, _deviceStateView.frame.origin.y + _deviceStateView.frame.size.height);
        deviceInfoContentView.center = CGPointMake(deviceInfoContentView.center.x, deviceImageView.center.y);
        [_deviceControl addSubview:deviceInfoContentView];
        
//        UIImage *eleStateImage = [UIImage imageNamed:@"device_battery_full.png"];
//        self.eleStateImageView = [[UIImageView alloc] initWithFrame:CGRectMake(_deviceControl.frame.size.width - 35 - eleStateImage.size.width, 0, eleStateImage.size.width, eleStateImage.size.height)];
//        _eleStateImageView.image = eleStateImage;
//        _eleStateImageView.center = CGPointMake(_eleStateImageView.center.x, deviceImageView.center.y);
//        _eleStateImageView.hidden = YES;
//        [_deviceControl addSubview:_eleStateImageView];
//
//        UIImage *arrowImage = [UIImage imageNamed:@"img_temp_arrow.png"];
//        self.eleArrowImageView = [[UIImageView alloc] initWithFrame:CGRectMake(_deviceControl.frame.size.width - 15 - arrowImage.size.width, 0, arrowImage.size.width, arrowImage.size.height)];
//        _eleArrowImageView.image = arrowImage;
//        _eleArrowImageView.center = CGPointMake(_eleArrowImageView.center.x, deviceImageView.center.y);
//        _eleArrowImageView.hidden = _eleStateImageView.hidden;
//        [_deviceControl addSubview:_eleArrowImageView];
        
        self.refreshImg = [[UIImageView alloc] initWithFrame:CGRectMake(SCREEN_W - 75, 15, 30, 30)];
        self.refreshImg.image = [UIImage imageNamed:@"HomeRefresh"];
        [_deviceControl addSubview:self.refreshImg];
        
    }
    return _deviceControl;
}

- (void)deviceButtonPressed
{
//    NSLog(@"@-----------------");
    
    switch (lastBlueToothSate) {
        case DeviceConnectionStatusPowerOff:{
            
            MXBluetoothManager *manager = [[MXBluetoothManager alloc] init];
            
        }
            break;
            
        case DeviceConnectionStatusSearching:{
            
            
        }
            break;
                        
        default:{
            
            self.myHud.mode = MBProgressHUDModeText;
            self.myHud.labelText = NSLocalizedString(@"Unconnected_device", @"设备未连接。");
            [self.myHud hide:YES afterDelay:2];
            
        }
            break;
    }
    
//    NSString *adstr = [[MXBluetoothManager sharedInstance] connectedPeripheralManufacturer];
//    CBPeripheral *peripheral = [[MXBluetoothManager sharedInstance] discoveredPeripheral];
//    if (adstr == nil || adstr.length <= 0 || peripheral.state != CBPeripheralStateConnected) {
//        return;
//    }
//    MXDeviceInfoViewController *deviceInfoViewControl =
//        [[MXDeviceInfoViewController alloc]
//            initWithDeviceInfo:self.connectedDeviceInfo];
//    [self.navigationController pushViewController:deviceInfoViewControl
//                                         animated:YES];
}

/*更新设备电池状态*/
- (void)updateDeviceEleStatus:(DeviceElectricityStatus)status
{
    NSLog(@"dev ele status = %i", status);
//    UIImage *stausIamge;
//    switch (status) {
//        case DeviceElectricityStatusEmpty:
//            stausIamge = [UIImage imageNamed:@"device_battery_empty.png"];
//            break;
//        case DeviceElectricityStatusLow:
//            stausIamge = [UIImage imageNamed:@"device_battery_low.png"];
//            break;
//        case DeviceElectricityStatusHalf:
//            stausIamge = [UIImage imageNamed:@"device_battery_half.png"];
//            break;
//        case DeviceElectricityStatusFull:
//            stausIamge = [UIImage imageNamed:@"device_battery_full.png"];
//            break;
//        default:
//            stausIamge = [UIImage imageNamed:@"device_battery_empty.png"];
//            break;
//    }
//    _eleStateImageView.image = stausIamge;
}


- (void)updateDeviceInfo
{
    NSString *adstr = [[MXBluetoothManager sharedInstance] connectedPeripheralManufacturer];
    if (adstr != nil && adstr.length > 0) {
        self.connectedDeviceInfo = [MXDeviceInfo DeviceInfoWithHex:adstr];
        /*
        TempDeviceModel currentDeviceModel = [self.connectedDeviceInfo isOEMTempDevice] ? TempDeviceModelYuyue : TempDeviceModelBLT;
        TempDeviceModel beforeDeviceModel = [(MXUserDefaults *)[MXUserDefaults sharedInstance] deviceModel];
        [[MXUserDefaults sharedInstance] setDeviceModel:currentDeviceModel];
        if (currentDeviceModel != beforeDeviceModel) {
            //Notification
        }*/
    }
    NSString* deviceName = NSLocalizedString(@"OXIMETER", @"智能血氧");
    if (_connectedDeviceInfo) {
        [self updateDeviceName:deviceName note:self.connectedDeviceInfo.nickName];
    }
    else {
        [self updateDeviceName:deviceName note:@""];
    }
}


- (void)updateDeviceName:(NSString *)deviceName note:(NSString *)note
{
    if (deviceName.length > 0) {
        if (note.length >  0) {
            _deviceNameLabel.text = [NSString stringWithFormat:@"%@(%@)",deviceName,note];
        } else {
            _deviceNameLabel.text = deviceName;
        }
    }
}


/*更新设备连接状态*/
- (void)updateDeviceConnectedStatus:(DeviceConnectionStatus)status
{
     lastBlueToothSate = status;
    for (UIView *subView in _deviceStateView.subviews) {
        [subView removeFromSuperview];
    }
    [self.refreshImg.layer removeAllAnimations];
    
    NSLog(@"----------------------%i", status);
    switch (status) {
        case DeviceConnectionStatusDisconnected:
        {
            UILabel *textLabel = [[UILabel alloc] initWithFrame:_deviceStateView.bounds];
            textLabel.font = [UIFont systemFontOfSize:12.0f];
            textLabel.backgroundColor = [UIColor clearColor];
            textLabel.textColor = UUWhite;
            textLabel.adjustsFontSizeToFitWidth = YES;
            textLabel.text = NSLocalizedString(@"Home_State_Disconnected", @"设备已断开");
            [_deviceStateView addSubview:textLabel];
            bStoreTrend = false;
            [self resetWavesBuff];
            _bDrawBaseLine = FALSE;
        }
            break;
        case DeviceConnectionStatusConnected:
        {
            UIImage *image = [UIImage imageNamed:@"img_device_signal.png"];
            UIImageView *imageView = [[UIImageView alloc] initWithFrame:CGRectMake(0, 0, image.size.width, image.size.height)];
            imageView.image = image;
            imageView.center = CGPointMake(imageView.center.x, _deviceStateView.frame.size.height / 2.0);
            
            UILabel *textLabel = [[UILabel alloc] initWithFrame:CGRectMake(imageView.frame.size.width + imageView.frame.origin.x + 4, 0, _deviceStateView.frame.size.width - (imageView.frame.size.width + 4), _deviceStateView.frame.size.height)];
            textLabel.font = [UIFont systemFontOfSize:12.0f];
            textLabel.backgroundColor = [UIColor clearColor];
            textLabel.textColor = UUWhite;
            textLabel.adjustsFontSizeToFitWidth = YES;
            textLabel.text = NSLocalizedString(@"Home_DeviceConnected", @"设备已连接");
            bStoreTrend = TRUE;
            [_deviceStateView addSubview:imageView];
            [_deviceStateView addSubview:textLabel];
            _bDrawBaseLine = TRUE;
        }
            break;
        case DeviceConnectionStatusSearching:
        {
            SCActivityIndicatorView *activityView = [[SCActivityIndicatorView alloc] initWithActivityIndicatorStyle:UIActivityIndicatorViewStyleWhite];
            
            activityView.center = CGPointMake(activityView.frame.size.width / 2.0, _deviceStateView.frame.size.height / 2.0);
            [_deviceStateView addSubview:activityView];
            [activityView startAnimating];
            
            UILabel *textLabel = [[UILabel alloc] initWithFrame:CGRectMake(activityView.frame.size.width + activityView.frame.origin.x + 4, 0, _deviceStateView.frame.size.width - (activityView.frame.size.width + 4), _deviceStateView.frame.size.height)];
            textLabel.font = [UIFont systemFontOfSize:12.0f];
            textLabel.backgroundColor = [UIColor clearColor];
            textLabel.adjustsFontSizeToFitWidth = YES;
            textLabel.textColor = UUWhite;
            textLabel.text = NSLocalizedString(@"Home_State_Searching",@"搜索设备中...");
            [_deviceStateView addSubview:textLabel];
            bStoreTrend = false;
            [self resetWavesBuff];
            _bDrawBaseLine = FALSE;
            [self refreshBtnClick];
        }
            break;
        case DeviceConnectionStatusNoSupport:
        {
            UILabel *textLabel = [[UILabel alloc] initWithFrame:_deviceStateView.bounds];
            textLabel.font = [UIFont systemFontOfSize:12.0f];
            textLabel.backgroundColor = [UIColor clearColor];
            textLabel.textColor = UUWhite;
            textLabel.text =NSLocalizedString(@"Home_State_NoSupport", @"设备不支持蓝牙4.0");
            textLabel.adjustsFontSizeToFitWidth = YES;
            [_deviceStateView addSubview:textLabel];
            [self resetWavesBuff];
            bStoreTrend = false;
            _bDrawBaseLine = FALSE;
        }
            break;
        case DeviceConnectionStatusPowerOff:
        {
            UILabel *textLabel = [[UILabel alloc] initWithFrame:_deviceStateView.bounds];
            textLabel.font = [UIFont systemFontOfSize:12.0f];
            textLabel.backgroundColor = [UIColor clearColor];
            textLabel.textColor = UUWhite;
            textLabel.text = NSLocalizedString(@"Home_State_PowerOff", @"系统蓝牙未开启");
            textLabel.adjustsFontSizeToFitWidth = YES;
            [_deviceStateView addSubview:textLabel];
            [self resetWavesBuff];
            bStoreTrend = false;
            _bDrawBaseLine = FALSE;
            
        }
            break;
        default:
            break;
    }
    [_wavesView canDrawBaseLine:_bDrawBaseLine];
}

- (MXUserData *)userData
{
    if (!_userData) {
        _userData = [[MXUserData alloc] init];
    }
    return _userData;
}

-(void)updateFamilyMemInfo
{
    MXUser *user = [[MXUserService sharedInstance] getSelectedUser];
    [self updateUserInfo:user];
}

-(void)updateUserInfo:(MXUser*)user
{
    if ([[MXUserService sharedInstance] getCurrentLoginedMemeberId])
    {
        if (user)
        {
            if (self.selectedFamilyId != user.familyId)
            {
                self.selectedFamilyId = user.familyId;
            }
        }
        else
        {
        }
    }
    else
    {
        NSLog(@"无有效家庭成员。。。");
    }
}

-(void)memberButtonPressed:(id)sender
{
    NSLog(@"memberBUttonPressed!!!");
    familyListViewController* controller = [[familyListViewController alloc]init];
    [self.navigationController pushViewController:controller animated:YES];
}

-(void)insertTrendDataWithSpo2Value:(NSInteger)spo2Value withPrValue:(NSInteger)prValue withPIValue:(NSInteger)PIValue withRespValue:(NSInteger)respValue
{
    NSDate *currrentDate = [NSDate date];
    MXUser *user = [[MXUserService sharedInstance] getSelectedUser];
    if (user) {
        MXTrendData *trend = [[MXTrendData alloc] init];
        trend.recordDate = currrentDate;
        trend.spo2Value = spo2Value;
        trend.prValue = prValue;
        trend.piValue = PIValue;
        trend.respValue = respValue;
        trend.temp = 37;
        BOOL isInsert =  [self.userData insertDataToRecordTempData:trend familyId:user.familyId];
        //NSLog(@"--------***********))))))))%f", value);
        if (isInsert) {
            [[NSNotificationCenter defaultCenter] postNotificationName:newTempDidUpdatedNotification object:nil];
            NSLog(@"存储本地数据库成功");
        }
        else
        {
            NSLog(@"存储本地数据库 失败");
        }
    }
    else
    {
        
    }
}

-(void)resetWavesBuff
{
    Gspo2Value = INVAL_VALUE;
    GprValue = INVAL_VALUE;
    GrespValue = INVAL_VALUE;
    GpiValue = INVAL_VALUE;
    bDrawWaves = FALSE;
    [_wavesBuffers removeAllObjects];
    [_bloodBarValues removeAllObjects];
}

- (void)selectDeviceTimerDidEnd
{
    [[MXBluetoothManager sharedInstance] reDiscovere];
}

- (void)changeToSearchState
{
    [self updateDeviceConnectedStatus:DeviceConnectionStatusSearching];
}

- (void)reloadShowTextMessage {
 
    NSUserDefaults *user = [NSUserDefaults standardUserDefaults];
    BOOL sp_value = NO;
    BOOL pr_value = NO;
    BOOL pi_value = NO;
    NSString* string;
    //spo2
    if (Gspo2Value == INVAL_VALUE || Gspo2Value == 127)
        string = @"--";
    else {
        string = [NSString stringWithFormat:@"%i", Gspo2Value];
        NSNumber *number_h = [user objectForKey:@"SP_H_swi"];
        if (number_h.boolValue) {
            
            NSNumber *sp_h = [user objectForKey:@"SP_H"];
            
            if (sp_h.floatValue < Gspo2Value) {
                
                sp_value = YES;
            }
            
        }
        
        NSNumber *number_l = [user objectForKey:@"SP_L_swi"];
        if (number_l.boolValue) {
            
            NSNumber *sp_l = [user objectForKey:@"SP_L"];
            
            if (sp_l.floatValue > Gspo2Value) {
                
                sp_value = YES;
            }
            
        }

    }
    _paramView.paramSpo2Value.text = string;

    //pr
    if (GprValue == INVAL_VALUE || GprValue == 255)
        string = @"---";
    else{
        
        string = [NSString stringWithFormat:@"%i", GprValue];
        NSNumber *number_h = [user objectForKey:@"PR_H_swi"];
        if (number_h.boolValue) {
           
           NSNumber *pr_h = [user objectForKey:@"PR_H"];
           
           if (pr_h.floatValue < GprValue) {
               
               pr_value = YES;
           }
           
        }
        
        NSNumber *number_l = [user objectForKey:@"PR_L_swi"];
        if (number_l.boolValue) {
            
            NSNumber *pr_l = [user objectForKey:@"PR_L"];
            
            if (pr_l.floatValue > GprValue) {
                
                pr_value = YES;
            }
            
        }
        
    }
    _prView.prValue.text = string;
    //resp
//    if (GrespValue == INVAL_VALUE || GrespValue == 255)
//        string = @"--";
//    else {
//        string = [NSString stringWithFormat:@"%i", GrespValue];
//       _paramView.respView.respValue.text = string;
//    }
    //pi
    if (GpiValue == INVAL_VALUE || GpiValue == 255 || GpiValue==0)
    {
        string = @"-.-";
    }
    else
    {
        CGFloat piValue = GpiValue*10;
        if (piValue  < 10) {
            
            string =[NSString stringWithFormat:@"%.2f", piValue];
            
        }else {
            
            string =[NSString stringWithFormat:@"%.1f", piValue];
        }
        NSNumber *number = [user objectForKey:@"PI_H_swi"];
        if (number.boolValue) {
           
           NSNumber *pi_h = [user objectForKey:@"PI_H"];
           
           if (pi_h.floatValue < piValue) {
               
               pi_value = YES;
           }
           
        }
        
        NSNumber *number_l = [user objectForKey:@"PI_L_swi"];
        if (number_l.boolValue) {
             
             NSNumber *pi_l = [user objectForKey:@"PI_L"];
             
             if (pi_l.floatValue > piValue) {
                 
                 pi_value = YES;
             }
             
        }
        
    }
    _paramView.paramPIValue.text = string;
    
    if (!pi_value && !pr_value && !sp_value) {
        
        if (self.player.isPlaying) {

            [self.player stop];

        }
        
    }else {
        
        if (!self.player.isPlaying) {

            [self.player play];

        }
        
    }
}

- (MXSelectDeviceView *)selectDeviceView
{
    if (!_selectDeviceView) {
        CGRect mainScreen = [[UIScreen mainScreen] applicationFrame];
        //mainScreen.size.height -= self.navigationController.navigationBar.frame.size.height;
        _selectDeviceView = [[MXSelectDeviceView alloc] initWithFrame:CGRectMake(0, 60, mainScreen.size.width, mainScreen.size.height)];
        _selectDeviceView.delegate = self;
        [self.view addSubview:_selectDeviceView];
    }
    return _selectDeviceView;
}

-(MBProgressHUD*)myHud
{
    if (!_myHud)
    {
        _myHud = [MBProgressHUD showHUDAddedTo:self.view animated:YES];
    }
    return _myHud;
}

#pragma mark - Action -
- (void)refreshBtnClick {
        
    CABasicAnimation *rotationAnimation;

    rotationAnimation = [CABasicAnimation animationWithKeyPath:@"transform.rotation.z"];

    rotationAnimation.toValue = [NSNumber numberWithFloat:M_PI*2.0];

    rotationAnimation.duration = 1;

    rotationAnimation.repeatCount = HUGE_VALF;

    [self.refreshImg.layer addAnimation:rotationAnimation forKey:@"rotationAnimation"];

}


#pragma mark - MXSelectDeviceViewDelegate
//选择设备tableview 协议
- (void)deviceViewCancelDidSelected:(MXSelectDeviceView *)view
{
    CLEAN_TIMER(_selectDeviceTimer);
    _selectDeviceTimer = [NSTimer scheduledTimerWithTimeInterval:5.0 target:self selector:@selector(selectDeviceTimerDidEnd) userInfo:nil repeats:NO];

}

#pragma mark - MXBluetoothManagerDelegate
//蓝牙设备管理协议
- (void)managerStateDidChanged:(CBCentralManagerState)state
{
    
    switch (state) {
        case CBCentralManagerStatePoweredOff:
            CLEAN_TIMER(_changeSearchTimer);
            [self updateDeviceConnectedStatus:DeviceConnectionStatusPowerOff];
//            _eleStateImageView.hidden = YES;
//            _eleArrowImageView.hidden = _eleStateImageView.hidden;
            [self updateDeviceInfo];
            break;
        case CBCentralManagerStatePoweredOn:
        case CBCentralManagerStateResetting:
            [self updateDeviceConnectedStatus:DeviceConnectionStatusSearching];
            break;
        default:
            [self updateDeviceConnectedStatus:DeviceConnectionStatusNoSupport];
            break;
    }
}

- (void)managerDidConnected:(MXBluetoothManager *)aManager
{
    CLEAN_TIMER(_changeSearchTimer);
    [self updateDeviceConnectedStatus:DeviceConnectionStatusConnected];
    [self updateDeviceInfo];
//    _eleStateImageView.hidden = NO;
//    _eleArrowImageView.hidden = _eleStateImageView.hidden;
}

- (void)managerDidDisconnect:(MXBluetoothManager *)aManager
{
    
    [self updateDeviceConnectedStatus:DeviceConnectionStatusDisconnected];
//    _eleStateImageView.hidden = YES;
//    _eleArrowImageView.hidden = _eleStateImageView.hidden;
    _changeSearchTimer = [NSTimer scheduledTimerWithTimeInterval:2.0 target:self selector:@selector(changeToSearchState) userInfo:nil repeats:NO];
    [self updateDeviceInfo];
    /*
    if ([[self.navigationController.viewControllers lastObject] isKindOfClass:[MXDeviceInfoViewController class]]) {
        [self.navigationController popToRootViewControllerAnimated:YES];
    }*/
}

- (void)manager:(MXBluetoothManager *)aManager didUpdateValues:(int)value withPrValue:(int)prValue withBloodValues:(NSArray*)bloodValue withPIValue:(CGFloat)piValue withRespValue:(int)respValue
{
    Gspo2Value = value;
    GprValue = prValue;
    GpiValue = piValue;
    GrespValue = respValue;
    [_bloodBarValues addObjectsFromArray:bloodValue];
    
    [self reloadShowTextMessage];
}

- (void)manager:(MXBluetoothManager *)aManager didUpdateSpo2Waves:(NSArray *)waves
{
    if(bRecive)

        [_wavesBuffers addObjectsFromArray:waves];
//    NSLog(@"_waveBuffers:::%@",_wavesBuffers);
//    if(_wavesBuffers.count >240)
        bDrawWaves = TRUE;
}

- (void)manager:(MXBluetoothManager *)aManager electricityStatus:(NSInteger)status
{
    [self updateDeviceEleStatus:(DeviceElectricityStatus)status];
}

- (void)manager:(MXBluetoothManager *)aManager discoverBTInfo:(NSArray *)btInfos
{
    NSLog(@"lastBlueToothState------------ = %i", lastBlueToothSate);
    if( lastBlueToothSate!= DeviceConnectionStatusConnected)
    {
        CLEAN_TIMER(_selectDeviceTimer);
        self.selectDeviceView.selectDevices = btInfos;
        self.selectDeviceView.hidden = NO;
    }
    else{
        CLEAN_TIMER(_selectDeviceTimer);
        _selectDeviceTimer = [NSTimer scheduledTimerWithTimeInterval:5.0 target:self selector:@selector(selectDeviceTimerDidEnd) userInfo:nil repeats:NO];
        
    }
}

- (void)manager:(MXBluetoothManager *)aManager firmwareVersion:(NSString *)version withSubVersion:(NSString *)subVersion
{
    self.connectedDeviceInfo.firmwareVersion = version;
    self.connectedDeviceInfo.firmSubVersion = subVersion;
}

#pragma mark -BLTStoreSpo2TrendDelegate
-(void)managerStoreTrendData
{
    if(lastBlueToothSate == DeviceConnectionStatusConnected)
    {
        NSLog(@"InsertTrendData.........");
        [self insertTrendDataWithSpo2Value:Gspo2Value withPrValue:GprValue withPIValue:GpiValue withRespValue:GrespValue];
        if (Gspo2Value < 100 && Gspo2Value > 0)
        {
            [self sendSpo2DataToIhealth: Gspo2Value * 1.0 / 100];
        }
        
        if (GprValue < 255 && GprValue > 0) {
            
            [self sendPRDataToIhealth:GprValue];
        }
    }
}

@end
