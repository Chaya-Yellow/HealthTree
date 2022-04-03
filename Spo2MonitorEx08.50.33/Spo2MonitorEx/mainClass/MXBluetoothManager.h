//
//  MXBluetoothManager.h
//  BLT-Spo2Monitor
//
//  Created by luteng on 15/10/20.
//  Copyright © 2015年 luteng. All rights reserved.
//

#import <Foundation/Foundation.h>
#import <CoreBluetooth/CoreBluetooth.h>

@protocol MXBluetoothManagerDelegate;
@class MXBTInfo;
@interface MXBluetoothManager : NSObject

@property(readonly) CBCentralManagerState state;
@property(nonatomic, weak) id <MXBluetoothManagerDelegate> delegate;
@property (nonatomic, readonly) CBPeripheral      *discoveredPeripheral;
@property (nonatomic, assign) BOOL isBinding;
@property (nonatomic, strong) NSArray           *bindingArray;
@property (readonly) int  DeviceState;            //当前设备状态

+ (id)sharedInstance;
- (void)scan;
- (void)stopScan;
- (void)cancelPeripheralConnection;
- (NSString *)connectedPeripheralManufacturer;
- (void)connectPeripheralWithInfo:(MXBTInfo *)info;
- (void)reDiscovere;
@end

@protocol MXBluetoothManagerDelegate <NSObject>
@optional
- (void)managerStateDidChanged:(CBCentralManagerState)state;
- (void)managerDidConnected:(MXBluetoothManager *)aManager;
- (void)managerDidDisconnect:(MXBluetoothManager *)aManager;
- (void)manager:(MXBluetoothManager *)aManager didUpdateValues:(int)value withPrValue:(int)prValue withBloodValues:(NSArray*)bloodValue withPIValue:(CGFloat)piValue withRespValue:(int)respValue;
- (void)manager:(MXBluetoothManager *)aManager didUpdateSpo2Waves:(NSArray*)waves;
- (void)manager:(MXBluetoothManager *)aManager electricityStatus:(NSInteger)status;
- (void)manager:(MXBluetoothManager *)aManager discoverBTInfo:(NSArray *)btInfos;
- (void)manager:(MXBluetoothManager *)aManager firmwareVersion:(NSString *)version withSubVersion:(NSString*)subVersion;
@end

