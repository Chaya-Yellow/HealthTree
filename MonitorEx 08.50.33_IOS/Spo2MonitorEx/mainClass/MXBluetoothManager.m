//
//  MXBluetoothManager.m
//  BLT-Spo2Monitor
//
//  Created by luteng on 15/10/20.
//  Copyright © 2015年 luteng. All rights reserved.
//

#import "MXBluetoothManager.h"
#import "MXBTInfo.h"
#import "OMESoft.h"
#import "MXDeviceInfo.h"
//#import "MXUserData.h"
#import "AppDelegate.h"
#import "NSString+Omesoft.h"
#import "Constants.h"
#define SEARCH_TIME 3.0
#define TIMER_INVA(timer){if ([timer isKindOfClass:[NSTimer class]] && timer) {[timer invalidate];timer = nil;}}
#define CENTRAL_MANAGER_SERVICE_UUID                    @"0xFFE0"
#define CENTRAL_MANAGER_CHARACTERISTIC_COMMAND_UUID     @"0xFFE2"
#define CENTRAL_MANAGER_CHARACTERISTIC_DATA_UUID        @"0xFFE1"

static MXBluetoothManager *manager;

@interface MXBluetoothManager ()<CBCentralManagerDelegate, CBPeripheralDelegate>
@property (nonatomic, strong) CBCentralManager  *centralManager;
@property (nonatomic, strong) CBPeripheral      *discoveredPeripheral;
@property (nonatomic, retain) NSMutableData     *bufferData;
@property (nonatomic, strong) NSMutableArray    *foundedBTInfos;
@property (nonatomic, strong) NSTimer           *foundedTimer;
@property (nonatomic, strong) MXBTInfo          *connectedBTInfo;
@property (nonatomic, strong) NSTimer           *connectingTimer;
@property (nonatomic, strong) MXBTInfo          *bgConnectedBTInfo;
@end

@implementation MXBluetoothManager
@synthesize state = _state;
@synthesize delegate = _delegate;

- (void)dealloc
{
    [[NSNotificationCenter defaultCenter] removeObserver:self];
    if (_foundedTimer) {
        [_foundedTimer invalidate];
        _foundedTimer = nil;
    }
}

+ (id)sharedInstance
{
    if (!manager) {
        manager = [[MXBluetoothManager alloc] init];
        
    }
    return manager;
}

- (id)init
{
    self = [super init];
    if (self) {
        self.centralManager = [[CBCentralManager alloc] initWithDelegate:self queue:nil];
        self.bufferData = [[NSMutableData alloc] init];
        self.foundedBTInfos = [[NSMutableArray alloc] init];
        self.isBinding = NO;
        [[NSNotificationCenter defaultCenter] addObserver:self selector:@selector(enterBackgroundNotifi) name:UIApplicationDidEnterBackgroundNotification object:nil];
        [[NSNotificationCenter defaultCenter] addObserver:self selector:@selector(becomeActiveNotifi) name:UIApplicationDidBecomeActiveNotification object:nil];
        
    }
    return self;
}

#pragma mark - Notifi
- (void)enterBackgroundNotifi
{
    NSLog(@"-----enterBa1ckgroundNotifi-----");
    if (self.connectedBTInfo) {
        self.bgConnectedBTInfo = self.connectedBTInfo;
        TIMER_INVA(_foundedTimer);
    } else {
        [self stopScan];
    }
}

- (void)becomeActiveNotifi
{
    self.bgConnectedBTInfo = nil;
    if (self.connectedBTInfo == nil) {
        [self scan];
    }
}
#pragma mark -  Public Methods

- (CBCentralManagerState)state
{
    return self.centralManager.state;
}

- (void)scan
{
    ////     if ([[UIApplication sharedApplication] applicationState] == UIApplicationStateBackground) {
    //         [self performSelector:@selector(scanAction) withObject:nil afterDelay:1.0f];
    //         return;
    //     }
    if (self.centralManager.state == CBCentralManagerStatePoweredOn && _delegate) {
        
        [self.centralManager scanForPeripheralsWithServices:@[[CBUUID UUIDWithString:CENTRAL_MANAGER_SERVICE_UUID]]
                                                    options:@{ CBCentralManagerScanOptionAllowDuplicatesKey : @YES }];
        NSLog(@"Scanning started");
        
        if (!self.isBinding && [[UIApplication sharedApplication] applicationState] == UIApplicationStateActive) {
            _foundedTimer = [NSTimer scheduledTimerWithTimeInterval:SEARCH_TIME target:self selector:@selector(foundedTimerDidEnd) userInfo:nil repeats:NO];
        }
    }
}


- (void)reDiscovere
{
    [self.foundedBTInfos removeAllObjects];
    if (!self.isBinding && [[UIApplication sharedApplication] applicationState] == UIApplicationStateActive) {
        _foundedTimer = [NSTimer scheduledTimerWithTimeInterval:SEARCH_TIME target:self selector:@selector(foundedTimerDidEnd) userInfo:nil repeats:NO];
    }
}

- (void)stopScan
{
    NSLog(@"stop scanning........");
    if (self.centralManager.state == CBCentralManagerStatePoweredOn) {
        [self.centralManager stopScan];
    }
    
    if (_foundedTimer) {
        [_foundedTimer invalidate];
        _foundedTimer = nil;
    }
}

- (void)cancelPeripheralConnection
{
    if (self.discoveredPeripheral != nil) {
        [self.centralManager cancelPeripheralConnection:self.discoveredPeripheral];
        self.discoveredPeripheral = nil;
        self.connectedBTInfo = nil;
    }
}

- (NSString *)connectedPeripheralManufacturer
{
    if (self.connectedBTInfo) {
        return [NSString byteToString:self.connectedBTInfo.manufacturerData];
    }
    return nil;
}

- (void)foundedTimerDidEnd
{
    if (_foundedTimer) {
        [_foundedTimer invalidate];
        _foundedTimer = nil;
    }
    
    if ([self.foundedBTInfos count] == 0) {
        
        _foundedTimer = [NSTimer scheduledTimerWithTimeInterval:SEARCH_TIME target:self selector:@selector(foundedTimerDidEnd) userInfo:nil repeats:NO];
    } else if ([self.foundedBTInfos count] == 1) {
        MXBTInfo *info = [self.foundedBTInfos objectAtIndex:0];
        [self connectPeripheralWithInfo:info];
        [self stopScan];
    } else {
        if ([_delegate respondsToSelector:@selector(manager:discoverBTInfo:)]) {
            [_delegate manager:self discoverBTInfo:self.foundedBTInfos];
        }
    }
}

- (void)connectPeripheralWithInfo:(MXBTInfo *)info
{
    if (info == nil || info.peripheral == nil) {
        return;
    }
    if (self.discoveredPeripheral == nil) {
        [self stopScan];
        // Save a local copy of the peripheral, so CoreBluetooth doesn't get rid of it
        self.connectedBTInfo = info;
        self.discoveredPeripheral = info.peripheral;
        // And connect
        NSLog(@"func=%s line = %i Connecting to peripheral %@",__func__,__LINE__,self.discoveredPeripheral);
        TIMER_INVA(_connectingTimer);
        _connectingTimer = [NSTimer scheduledTimerWithTimeInterval:10.0 target:self selector:@selector(connectingTimerDidEnd) userInfo:nil repeats:NO];
        [self.centralManager connectPeripheral:self.discoveredPeripheral options:nil];
    }
}

- (void)connectingTimerDidEnd
{
    if (self.centralManager.state == CBCentralManagerStatePoweredOn) {
        [self.centralManager cancelPeripheralConnection:self.discoveredPeripheral];
    }
}

- (NSString *)getDeviceserialNumberWithHex:(NSString *)hexStr
{
    if (hexStr == nil || hexStr.length <=0) {
        return nil;
    }
    
    int lenth = 2;
    int i = 4;
    NSMutableString *currentResult = [[NSMutableString alloc] init];
    while (i * lenth < hexStr.length) {
        NSString *targetStr;
        if (i * lenth + lenth > hexStr.length) {
            targetStr = [hexStr substringWithRange:NSMakeRange(i * lenth, hexStr.length - i * lenth)];
        } else {
            targetStr = [hexStr substringWithRange:NSMakeRange(i * lenth, lenth)];
        }
        int value =  [[NSString stringWithFormat:@"%lu",strtoul([targetStr UTF8String], 0, 16)] intValue];
        [currentResult appendString:[NSString stringWithFormat:@"%c",value]];
        i++;
    }
    return [NSString stringWithString:currentResult];;
    
}
#pragma mark - CBCentralManager Delegate

/** centralManagerDidUpdateState is a required protocol method.
 *  Usually, you'd check for other states to make sure the current device supports LE, is powered on, etc.
 *  In this instance, we're just using it to wait for CBCentralManagerStatePoweredOn, which indicates
 *  the Central is ready to be used.
 */
- (void)centralManagerDidUpdateState:(CBCentralManager *)central
{
    switch (central.state) {
        case CBCentralManagerStateUnknown:
        {
            /* The current state of the central manager is unknown; an update is imminent. */
            NSLog(@"centralManagerDidUpdateState CBCentralManagerStateUnknown");
            break;
        }
        case CBCentralManagerStateResetting:
        {
            /* The connection with the system service was momentarily lost; an update is imminent. */
            NSLog(@"centralManagerDidUpdateState CBCentralManagerStateResetting");
            break;
        }
        case CBCentralManagerStateUnsupported:
        {
            /* The platform does not support Bluetooth low energy. */
            NSLog(@"centralManagerDidUpdateState CBCentralManagerStateUnsupported");
            _DeviceState = DeviceConnectionStatusNoSupport;
            break;
        }
        case CBCentralManagerStateUnauthorized:
        {
            /* The app is not authorized to use Bluetooth low energy. */
            NSLog(@"centralManagerDidUpdateState CBCentralManagerStateUnauthorized");
            break;
        }
        case CBCentralManagerStatePoweredOff:
        {
            _DeviceState = DeviceConnectionStatusPowerOff;
            /* Bluetooth is currently powered off. */
            NSLog(@"centralManagerDidUpdateState CBCentralManagerStatePoweredOff");
            if (self.discoveredPeripheral != nil) {
                TIMER_INVA(_connectingTimer);
                [self.centralManager cancelPeripheralConnection:self.discoveredPeripheral];
                self.discoveredPeripheral = nil;
                self.connectedBTInfo = nil;
            }
            // Stop scanning
            [self.centralManager stopScan];
            
            [self.foundedBTInfos removeAllObjects];
            
            
            /* Tell user to power ON BT for functionality, but not on first run - the Framework will alert in that instance. */
            //            if (previousState != -1) {
            //                [discoveryDelegate discoveryStatePoweredOff];
            //            }
            
            break;
        }
        case CBCentralManagerStatePoweredOn:
        {
            NSLog(@"centralManagerDidUpdateState CBCentralManagerStatePoweredOn");
            /* Bluetooth is currently powered on and available to use. */
            // Scans for any peripheral
            // ... so start scanning
            [self scan];
            break;
        }
            
        default:
            NSLog(@"Central Manager did change state");
            break;
    }
    if ([_delegate respondsToSelector:@selector(managerStateDidChanged:)]) {
        [_delegate managerStateDidChanged:central.state];
    }
    
}

/** Scan for peripherals - specifically for our service's 128bit CBUUID
 */

/** This callback comes whenever a peripheral that is advertising the CENTRAL_MANAGER_SERVICE_UUID is discovered.
 *  We check the RSSI, to make sure it's close enough that we're interested in it, and if it is,
 *  we start the connection process
 */
- (void)centralManager:(CBCentralManager *)central didDiscoverPeripheral:(CBPeripheral *)peripheral advertisementData:(NSDictionary *)advertisementData RSSI:(NSNumber *)RSSI
{
    // Reject any where the value is above reasonable range
    if (RSSI.integerValue > -15) {
        return;
    }
    
    // Reject if the signal strength is too low to be close enough (Close is around -22dB)
    if (RSSI.integerValue < -35) {
        //return;
    }
    
    //if (IOS7) {
        if (![[advertisementData objectForKey:CBAdvertisementDataIsConnectable] boolValue]) {
            return;
        }
    //}
    
    NSString *peripheralName = [advertisementData objectForKey:CBAdvertisementDataLocalNameKey];
//    NSLog(@"function= %s, line = %i, %@",__FUNCTION__,__LINE__, advertisementData);
    NSData *manufacturerStr = [advertisementData objectForKey:CBAdvertisementDataManufacturerDataKey];
    //    peripheral.manuFacturer = [advertisementData objectForKey:CBAdvertisementDataManufacturerDataKey];
    
    
    if ([[UIApplication sharedApplication] applicationState] == UIApplicationStateBackground) {
        if ([self.bgConnectedBTInfo.peripheral isEqual:peripheral]) {
            NSLog(@"same! connecting!!");
            [central connectPeripheral:peripheral options:nil];
            [self stopScan];
        }
        return;
    }
    if ([peripheralName isEqualToString:@"OXIMETER"] && manufacturerStr.length > 0) {
        // Ok, it's in range - have we already seen it?
        MXBTInfo *info = [[MXBTInfo alloc] init];
        info.peripheral = peripheral;
        info.manufacturerData = manufacturerStr;
        BOOL isExist = NO;
        for (MXBTInfo *btinfo in self.foundedBTInfos) {
            if ([btinfo.manufacturerData isEqualToData:manufacturerStr]) {
                isExist = YES;
            }
        }
        if (isExist) {
            return;
        } else {
            [self.foundedBTInfos addObject:info];
        }
    }
}

- (void)centralManager:(CBCentralManager *)central didRetrieveConnectedPeripherals:(NSArray *)peripherals
{
    for (CBPeripheral *peripheral in peripherals) {
        if (!_delegate) {
            [central cancelPeripheralConnection:peripheral];
        }
    }
}

/** If the connection fails for whatever reason, we need to deal with it.
 */
- (void)centralManager:(CBCentralManager *)central didFailToConnectPeripheral:(CBPeripheral *)peripheral error:(NSError *)error
{
    NSLog(@"Failed to connect to %@. (%@)", peripheral, [error localizedDescription]);
    self.discoveredPeripheral = nil;
    self.connectedBTInfo = nil;
    [self.foundedBTInfos removeAllObjects];
    [self cleanup];
    [self scan];
}


/** We've connected to the peripheral, now we need to discover the services and characteristics to find the 'transfer' characteristic.
 */
- (void)centralManager:(CBCentralManager *)central didConnectPeripheral:(CBPeripheral *)peripheral
{
    _DeviceState = DeviceConnectionStatusConnected;
    NSLog(@"func = %s line =%i Peripheral Connected",__FUNCTION__, __LINE__);
    // Stop scanning
    TIMER_INVA(_connectingTimer);
    [self.centralManager stopScan];
    
    // Make sure we get the discovery callbacks
    peripheral.delegate = self;
    
    // Search only for services that match our UUID
    //[peripheral discoverServices:nil];
    [peripheral discoverServices:@[[CBUUID UUIDWithString:CENTRAL_MANAGER_SERVICE_UUID]]];
    
    if ([_delegate respondsToSelector:@selector(managerDidConnected:)]) {
        [_delegate managerDidConnected:self];
    }
}

/** Once the disconnection happens, we need to clean up our local copy of the peripheral
 */
- (void)centralManager:(CBCentralManager *)central didDisconnectPeripheral:(CBPeripheral *)peripheral error:(NSError *)error
{
    NSLog(@"Peripheral Disconnected");
    self.discoveredPeripheral = nil;
    self.connectedBTInfo = nil;
    [self.foundedBTInfos removeAllObjects];
    if ([_delegate respondsToSelector:@selector(managerDidDisconnect:)]) {
        [_delegate managerDidDisconnect:self];
    }
    // We're disconnected, so start scanning again
    [self scan];
    
    if ([[UIApplication sharedApplication] applicationState] == UIApplicationStateBackground && self.bgConnectedBTInfo) {
        [central connectPeripheral:self.bgConnectedBTInfo.peripheral options:nil];
    }
}


#pragma mark - CBPeripheralDelegate
/** The CentralManager Service was discovered
 */
- (void)peripheral:(CBPeripheral *)peripheral didDiscoverServices:(NSError *)error
{
    if (error) {
        NSLog(@"Error discovering services: %@", [error localizedDescription]);
        [self cleanup];
        return;
    }
    
    // Loop through the newly filled peripheral.services array, just in case there's more than one.
    for (CBService *service in peripheral.services) {
        NSLog(@"func = %s, line = %i, Service found with UUID: %@",__FUNCTION__,__LINE__,service.UUID);
        
        // Discovers the characteristics for a given service
        if ([service.UUID isEqual:[CBUUID UUIDWithString:CENTRAL_MANAGER_SERVICE_UUID]]) {
            [peripheral discoverCharacteristics:@[[CBUUID UUIDWithString:CENTRAL_MANAGER_CHARACTERISTIC_COMMAND_UUID],
                                                  [CBUUID UUIDWithString:CENTRAL_MANAGER_CHARACTERISTIC_DATA_UUID]]
                                     forService:service];
        }
//        [peripheral discoverCharacteristics:nil forService:service];
    }
}

/** The CentralManager characteristic was discovered.
 *  Once this has been found, we want to subscribe to it, which lets the peripheral know we want the data it contains
 */
- (void)peripheral:(CBPeripheral *)peripheral didDiscoverCharacteristicsForService:(CBService *)service error:(NSError *)error
{
    // Deal with errors (if any)
    if (error) {
        NSLog(@"Error discovering characteristics: %@", [error localizedDescription]);
        [self cleanup];
        return;
    }
    
    // Again, we loop through the array, just in case.
    for (CBCharacteristic *characteristic in service.characteristics) {
        /**
         * 打开以下两个characteristic的notification属性
         * 串口服务(UUID:0xFFB0)当中的命令属性(UUID:0xFFB1)和数据属性(UUID:0xFFB2)
         */
        //        if ([characteristic.UUID isEqual:[CBUUID UUIDWithString:CENTRAL_MANAGER_CHARACTERISTIC_COMMAND_UUID]]) {
        //            [peripheral setNotifyValue:YES forCharacteristic:characteristic];
        //        }
        
        if ([characteristic.UUID isEqual:[CBUUID UUIDWithString:CENTRAL_MANAGER_CHARACTERISTIC_DATA_UUID]]) {
            [peripheral setNotifyValue:YES forCharacteristic:characteristic];
        }
    }
    
    // Once this is complete, we just need to wait for the data to come in.
}

/** The peripheral letting us know whether our subscribe/unsubscribe happened or not
 */
- (void)peripheral:(CBPeripheral *)peripheral didUpdateNotificationStateForCharacteristic:(CBCharacteristic *)characteristic error:(NSError *)error
{
    if (error) {
        NSLog(@"Error changing notification state: %@", error.localizedDescription);
    }
    
    // Exit if it's not the my characteristic
    //    if (![characteristic.UUID isEqual:[CBUUID UUIDWithString:CENTRAL_MANAGER_CHARACTERISTIC_COMMAND_UUID]] ||
    //        [characteristic.UUID isEqual:[CBUUID UUIDWithString:CENTRAL_MANAGER_CHARACTERISTIC_DATA_UUID]]) {
    //        return;
    //    }
    
    // Notification has started
    if (characteristic.isNotifying) {
        NSLog(@"func = %s, line = %i, Notification began on %@",__FUNCTION__,__LINE__, characteristic);
    }
    
    // Notification has stopped
    else {
        // so disconnect from the peripheral
        NSLog(@"Notification stopped on %@.  Disconnecting", characteristic);
        //[self.centralManager cancelPeripheralConnection:peripheral];
    }
}

/** This callback lets us know more data has arrived via notification on the characteristic
 */
- (void)peripheral:(CBPeripheral *)peripheral didUpdateValueForCharacteristic:(CBCharacteristic *)characteristic error:(NSError *)error
{
    if (error) {
        NSLog(@"Error discovering characteristics: %@", [error localizedDescription]);
        return;
    }
    
    NSData *data = characteristic.value;
    //NSString *stringFromData = [self byteToString:data];
    //NSLog(@"Get characteristics value: %@", stringFromData);
    
    /**
     * 正常收发数据
     * a 发送数据(write property): 向模块中串口服务(UUID:0xFFB0)当中的数据属性(UUID:0xFFB2)写数据
     * b 接收数据(notification property): 模块向串口服务(UUID:0xFFB0)当中的数据属性(UUID:0xFFB2)发送数据
     */
    if ([characteristic.UUID isEqual:[CBUUID UUIDWithString:CENTRAL_MANAGER_CHARACTERISTIC_DATA_UUID]]) {
        //        NSLog(@"Get characteristics(uuid:%@) value: %@", CENTRAL_MANAGER_CHARACTERISTIC_DATA_UUID, data);
        if (data) {
            [self receive:data];
        }
    }
    
}


/** Call this when things either go wrong, or you're done with the connection.
 *  This cancels any subscriptions if there are any, or straight disconnects if not.
 *  (didUpdateNotificationStateForCharacteristic will cancel the connection if a subscription is involved)
 */
- (void)cleanup
{
    // Don't do anything if we're not connected
    if (self.discoveredPeripheral.state != CBPeripheralStateConnected) {
        return;
    }
    
    // See if we are subscribed to a characteristic on the peripheral
    if (self.discoveredPeripheral.services != nil) {
        for (CBService *service in self.discoveredPeripheral.services) {
            if (service.characteristics != nil) {
                for (CBCharacteristic *characteristic in service.characteristics) {
                    if ([characteristic.UUID isEqual:[CBUUID UUIDWithString:CENTRAL_MANAGER_CHARACTERISTIC_COMMAND_UUID]] ||
                        [characteristic.UUID isEqual:[CBUUID UUIDWithString:CENTRAL_MANAGER_CHARACTERISTIC_DATA_UUID]]) {
                        if (characteristic.isNotifying) {
                            // It is notifying, so unsubscribe
                            [self.discoveredPeripheral setNotifyValue:NO forCharacteristic:characteristic];
                        }
                    }
                    
                }
            }
        }
        // And we're done.
        return;
    }
    
    //
    for (__strong CBService *service in self.discoveredPeripheral.services) {
        // Discovers the characteristics for a given service
        if ([service.UUID isEqual:[CBUUID UUIDWithString:CENTRAL_MANAGER_SERVICE_UUID]]) {
            if (service != nil) {
                service = nil;
            }
        }
    }
    
    // If we've got this far, we're connected, but we're not subscribed, so we just disconnect
    [self.centralManager cancelPeripheralConnection:self.discoveredPeripheral];
}

- (void)writeValue:(NSData *)data
{
    if (self.discoveredPeripheral.state != CBPeripheralStateConnected) {
        return;
    }
    
    // See if we are subscribed to a characteristic on the peripheral
    if (self.discoveredPeripheral.services != nil) {
        for (CBService *service in self.discoveredPeripheral.services) {
            if (service.characteristics != nil) {
                for (CBCharacteristic *characteristic in service.characteristics) {
                    if ([characteristic.UUID isEqual:[CBUUID UUIDWithString:CENTRAL_MANAGER_CHARACTERISTIC_DATA_UUID]]) {
                        //                        if (characteristic.isNotifying) {
                        [self.discoveredPeripheral writeValue:data forCharacteristic:characteristic type:CBCharacteristicWriteWithoutResponse];
                        return;
                        //                        }
                    }
                    
                }
            }
        }
    }
    
}


- (void)receive:(NSData *)value
{
    [_bufferData appendData:value];

    NSLog(@"Buffer Data: %@", _bufferData);
    Byte *byte = (Byte *)[_bufferData bytes];
    int length = (int)[_bufferData length];
    
    //解析数据
    for (int i = 0; i < length; i++) {
        
//        NSLog(@"length::%d  %d",i,length);
        /**
         * 包头判断方法：包头固定为0xFF，其它可能出现0xFF的地方为：数据段、校验Checksum，找出区别
         * 1.包头为0xFF时，紧跟一个非0xFF
         * 2.数据为0xFF时，紧跟一个0xFF
         * 3.校验为0xFF时，紧跟下一个包头0xFF，或者已到包尾
         */
        if (byte[i] == 255 && i < length-1 && byte[i+1] != 255) {
            @try {
                
                int n = byte[i+1];  //总长度，数据中连续的0xFF，计一个长度，总长度包括校验和字节
                int sum = n; //累加和，包头和Checksum字节间所有数据的累加，包括总长度字节
                                
                //如果数据不完整，结束循环。
                if (i+1+n > length) {

                    NSLog(@"如果数据不完整，结束循环0   %d   %d   %d",i,n,length);
                    break;
                }

                // 找到校验位，同时计算累加和
                for (int j = 0; j < n-2; j++) {
                    //如果数据不完整，结束循环。
                    if (i+1+n > length) {
                        NSLog(@"如果数据不完整，结束循环1");
                        break;
                    }

                    //数据中存在0xFF，跳过下一个0xFF，并且n+1
                    if (byte[i+1+j+1] == 255) {
                        if (byte[i+1+j+2] == 255) {
                            j++;
                            n++;
                            NSLog(@"数据中存在0xFF，跳过下一个0xFF，并且n+1");
                        } else  {
                            //数据异常----
                            NSLog(@"数据异常,数据中存在不连续的0xFF");
                        }
                    }
                    sum = sum + byte[i+1+j+1];
                }
                
//                如果数据不完整，结束循环。
                if (i+1+n > length) {
                    NSLog(@"如果数据不完整，结束循环2");
                    break;
                }

                //获取校验和字节
                int checksum = byte[i+n];   //此时的n为包括了多余0xFF的实际长度
                NSLog(@"总长度n:%d, 累加和sum:%d, 余数:%d, checksum:%d", n, sum, sum%256, checksum);
                
                //如果 Checksum = 0xFF，则要减 1，避免出现 0xFF，与 Header 混淆。
                                
                //校验
                 if (checksum != sum%256) {
//                     校验失败，数据异常
                     NSLog(@"/校验失败，数据异常");
                     continue;
                 }
                
                for (int i = 0; i < length; i++) {

                    NSLog(@"data:::::%d  %d",byte[i],i);

                }

                //校验成功，提取数据段
                NSMutableData *data = [[NSMutableData alloc] init];
                for (int m = 0; m < n-2; m++) {
                    [data appendBytes:&byte[i+1+m+1] length:sizeof(byte[i+1+m+1])];
                    
//                    NSLog(@"当出现连续的0xFF时，m== %d   %d",byte[i+1+m+1],byte[i+1+m+2]);
                    //当出现连续的0xFF时，去掉多余的0xFF
                    if (byte[i+1+m+1] == 255 && byte[i+1+m+2] == 255) {
                        m++;
                        NSLog(@"当出现连续的0xFF时，去掉多余的0xFF");
                    }
                }
                
                Byte *dataByte = (Byte *)[data bytes];  //数据
                int dataLength = (int)[data length];
//                NSLog(@"DATA: %@, length=%i", data, dataLength);
                
                //获取数据，i+2至i+n为数据位
                /**
                 * 数据段结构：ID、数据
                 * 长度：为该数据段中的数据长度，包括ID和数据，但不包括长度自己。
                 * ID：用于区分各参数模块。
                 * 数据：该模块上传的数据或主机下传的命令数据
                 */
                int pID = dataByte[0];
                
                /**
                 * 通过pID分辨命令
                 * 上行数据
                 * 0x41 血氧 数据包
                 * 0x42 请求重发数据
                 * 0x43	血氧状态包
                 * 下行数据
                 * 0XB1 血氧命令包
                 * OXB2	数据重发请求
                 */
                NSLog(@"--------------------------------%d",pID);
                switch (pID) {
//                    case 67:
//                    {
//                        //电池电量
//                        NSString *byte1 = [self decimalToBinary:dataByte[1] backLength:8];
//                        NSString *byte2 = [self decimalToBinary:dataByte[2] backLength:8];
//                        NSString *byte3 = [self decimalToBinary:dataByte[3] backLength:8];
//                        NSString *byte1_bit_3_0 = [byte1 substringWithRange:NSMakeRange(4,4)];
//                        int tValue1 = (int)strtol([byte1_bit_3_0 cStringUsingEncoding:NSUTF8StringEncoding], NULL, 2);
//                        NSString *byte2_bit_7_4 = [byte2 substringWithRange:NSMakeRange(0,4)];
//                        NSString *byte2_bit_3_0 = [byte2 substringWithRange:NSMakeRange(4,4)];
//                        int vValueX = (int)strtol([byte2_bit_7_4 cStringUsingEncoding:NSUTF8StringEncoding], NULL, 2);
//                        int vValueY = (int)strtol([byte2_bit_3_0 cStringUsingEncoding:NSUTF8StringEncoding], NULL, 2);
//                        //
//                        NSString* byte3_bit_7_4 =[byte3 substringWithRange:NSMakeRange(0,4)];
//                        NSString *byte3_bit_3_0 = [byte2 substringWithRange:NSMakeRange(4,4)];
//                        int subX =(int)strtol([byte3_bit_7_4 cStringUsingEncoding:NSUTF8StringEncoding], NULL, 2);
//                        int subY =(int)strtol([byte3_bit_3_0 cStringUsingEncoding:NSUTF8StringEncoding], NULL, 2);
//                        NSString *version = [NSString stringWithFormat:@"%d.%d",vValueX,vValueY];
//                        NSString* subVersion =[NSString stringWithFormat:@"%d.%d",subX,subY];
//                        NSLog(@"固件：%@ @从MCU版本：%@",version,subVersion);
//                        if ([_delegate respondsToSelector:@selector(manager:firmwareVersion:withSubVersion:)]) {
//                            [_delegate manager:self firmwareVersion:version withSubVersion:subVersion];
//                        }
//                        NSLog(@"电量:%d---", tValue1);//电量:0 1 2 3  脱落:0 1
//                        if ([_delegate respondsToSelector:@selector(manager:electricityStatus:)]) {
//                            [_delegate manager:self electricityStatus:tValue1];
//                        }
//                        //删除缓存中的此数据段信息
//                        [_bufferData replaceBytesInRange:NSMakeRange(0, i+1+n+1) withBytes:NULL length:0];
//                        break;
//                    }
                    //spo2
                    case 1:
                    {
                        //byte2
//                        NSString* byte2 =[self decimalToBinary:dataByte[3] backLength:8];
//                        NSString* byte2_bit_0 = [byte2 substringWithRange:NSMakeRange(7, 1)];
//                        NSString* byte2_bit_1 = [byte2 substringWithRange:NSMakeRange(6, 1)];
//                        NSString* byte2_bit_2 = [byte2 substringWithRange:NSMakeRange(5, 1)];
//                        NSString* byte2_bit_3 = [byte2 substringWithRange:NSMakeRange(4, 1)];
//                        NSString* byte2_bit_4 = [byte2 substringWithRange:NSMakeRange(3, 1)];
//                        NSString* byte2_bit_5 = [byte2 substringWithRange:NSMakeRange(2, 1)];
//                        NSString* byte2_bit_7_6 = [byte2 substringWithRange:NSMakeRange(0, 2)];
                        //探头是否脱落
//                        bool blose =(int)strtol([byte2_bit_0 cStringUsingEncoding:NSUTF8StringEncoding], NULL, 2);
                        //血氧值是否在下降
//                        bool bSpo2Reduce =(int)strtol([byte2_bit_1 cStringUsingEncoding:NSUTF8StringEncoding], NULL, 2);
//                        //脉搏搜索时间太长
//                        bool bSearchTooLong =(int)strtol([byte2_bit_2 cStringUsingEncoding:NSUTF8StringEncoding], NULL, 2);
//                        //正在检测脉动
//                        bool bTestPR =(int)strtol([byte2_bit_3 cStringUsingEncoding:NSUTF8StringEncoding], NULL, 2);
//                        //探头是否错误
//                        bool bProbeError =(int)strtol([byte2_bit_4 cStringUsingEncoding:NSUTF8StringEncoding], NULL, 2);
//                        //正在搜索脉搏
//                        bool bSearchSpo2 =(int)strtol([byte2_bit_5 cStringUsingEncoding:NSUTF8StringEncoding], NULL, 2);
                       // NSLog(@"blose = %i， bSpo2Reduce = %i, bSearchTooLong = %i, bTestPr=%i, bProbeError=%i, bSearchSpo2=%i",blose, bSpo2Reduce, bSearchTooLong, bTestPR, bProbeError, bSearchSpo2);
                        //Byte血注强度
                         NSMutableArray* BloodArray = [NSMutableArray array];
                        for(int i=6; i<36; i++)
                        {
                            NSString* byte3 = [self decimalToBinary:dataByte[i] backLength:8];
                            NSString* byte3_bit_3_0 = [byte3 substringWithRange:NSMakeRange(4, 4)];     //血注强度
                            int bloodBar =(int)strtol([byte3_bit_3_0 cStringUsingEncoding:NSUTF8StringEncoding], NULL, 2);
                            NSNumber* aa =[NSNumber numberWithInt:bloodBar];
                            [BloodArray addObject:aa];
                        }
//                        NSLog(@"blood array = %@", BloodArray);
                        //byte4 PI灌注值
                        NSString* byte4 = [self decimalToBinary:dataByte[4] backLength:8];
                        NSString* byte4_bit_6_0 =[byte4 substringWithRange:NSMakeRange(1, 7)];
                        int byte_4_6 =(int)strtol([byte4_bit_6_0 cStringUsingEncoding:NSUTF8StringEncoding], NULL, 2);
                        
                        NSString *byte5 =[self decimalToBinary:dataByte[5] backLength:8];
                        NSString* byte5_bit_3_0 =[byte5 substringWithRange:NSMakeRange(1, 7)];
                        int byte5_7 =(int)strtol([byte5_bit_3_0 cStringUsingEncoding:NSUTF8StringEncoding], NULL, 2);
                        
                        CGFloat piValue = ((byte_4_6 & 0x7F) + (byte5_7 & 0x0F) * 128)*0.1/100;
                                                                        
                        //byte2 spo2
                        NSString* byte2 =[self decimalToBinary:dataByte[2] backLength:8];
                        NSString* byte2_bit_6_0 =[byte2 substringWithRange:NSMakeRange(1, 7)];
                        int spo2Value =(int)strtol([byte2_bit_6_0 cStringUsingEncoding:NSUTF8StringEncoding], NULL, 2);
                        //byte3 PR
                        NSString* byte3 =[self decimalToBinary:dataByte[3] backLength:8];
                        int byte3_5 =(int)strtol([byte3 cStringUsingEncoding:NSUTF8StringEncoding], NULL, 2);
                                                
                        int prValue = (byte3_5 & 0x7F) + ((byte5_7 & 0x10)<<3);
                                                
                        //byte36
//                        NSString* byte36 =[self decimalToBinary:dataByte[36] backLength:8];
//                        int respValue =(int)strtol([byte36 cStringUsingEncoding:NSUTF8StringEncoding], NULL, 2);
                        
//                        NSLog(@"SPO2::%D  PR:::%D  pi:::%d",spo2Value,prValue,piValue);
                        //spo2Wave
                        NSLog(@"dataLength:::%ld",dataLength);
                        NSMutableArray* array = [NSMutableArray array];
//                        NSArray *exitArray = @[@(44),@(50),@(56),@(62)];
                        for(int x= 41; x<dataLength; x++)
                        {

//                            if ([exitArray containsObject:@(x+2)]) {
//
//                                continue;
//                            }
//
                            NSString* byte_i = [self decimalToBinary:dataByte[x] backLength:8];
                            NSString* byte_i_6_0 =[byte_i substringWithRange:NSMakeRange(1, 7)];
                            //波形
                            int waveValue = (int)strtol([byte_i_6_0 cStringUsingEncoding:NSUTF8StringEncoding], NULL, 2);
                            NSNumber* aa =[NSNumber numberWithInt:waveValue]; //旧设备
                            //新设备
//                            NSNumber* aa =[NSNumber numberWithInt:(100-waveValue)];
                            [array addObject:aa];
                                
                        }
                           
                        NSLog(@"array:::%@",array);
                        if ([_delegate respondsToSelector:@selector(manager:didUpdateValues:withPrValue:withBloodValues: withPIValue:withRespValue:)]) {
                            [_delegate manager:self didUpdateValues:spo2Value withPrValue:prValue withBloodValues:BloodArray withPIValue:piValue withRespValue:BloodArray];
                        }
                        if ([_delegate respondsToSelector:@selector(manager:didUpdateSpo2Waves:)]) {
                            [_delegate manager:self didUpdateSpo2Waves:array];
                        }
                        //NSLog(@"血注= %i, 灌注值=%i, spo2=%i, PR=%i",bloodBar, piValue, spo2Value,prValue);
                        //删除缓存中的此数据段信息
                        [_bufferData replaceBytesInRange:NSMakeRange(0, i+n+1) withBytes:NULL length:0];
                       break;
                    }
                    default:
                        [_bufferData replaceBytesInRange:NSMakeRange(0, i+n+1) withBytes:NULL length:0];
                        break;
                }
                //释放数据段内存
                i = i+n+1;
            }
            @catch (NSException *exception) {
                //数据解析异常
                NSLog(@"数据异常,%@",exception);
            }
            @finally {
                
            }
            
        }
    }
}

//将十进制转化为二进制,设置返回NSString 长度
- (NSString *)decimalToBinary:(uint16_t)tmpid backLength:(int)length
{
    NSString *a = @"";
    while (tmpid)
    {
        a = [[NSString stringWithFormat:@"%d",tmpid%2] stringByAppendingString:a];
        if (tmpid/2 < 1)
        {
            break;
        }
        tmpid = tmpid/2 ;
    }
    
    if (a.length <= length)
    {
        NSMutableString *b = [[NSMutableString alloc]init];;
        for (int i = 0; i < length - a.length; i++)
        {
            [b appendString:@"0"];
        }
        
        a = [b stringByAppendingString:a];
    }
    
    return a;
}

#pragma mark - Set Methods

- (void)setIsBinding:(BOOL)is
{
    _isBinding = is;     
    if (_isBinding) {
        if (_foundedTimer) {
            [_foundedTimer invalidate];
            _foundedTimer = nil;
        }
    } else {
        [self scan];
    }
    [self.foundedBTInfos removeAllObjects];
}

@end

