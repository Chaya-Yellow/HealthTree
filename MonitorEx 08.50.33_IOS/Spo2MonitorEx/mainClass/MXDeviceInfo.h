//
//  MXDeviceInfo.h
//  BLT-Spo2Monitor
//
//  Created by luteng on 15/10/20.
//  Copyright © 2015年 luteng. All rights reserved.
//

#import <Foundation/Foundation.h>

@interface MXDeviceInfo : NSObject
@property (nonatomic, assign) NSInteger boundId;
@property (nonatomic, assign) NSInteger deviceModel;
@property (nonatomic, assign) NSInteger protocolVersion;
@property (nonatomic, readonly) NSString *deviceModelName;//型号
@property (nonatomic, strong) NSString *serialNumber;
@property (nonatomic, strong) NSString *nickName;//备注
@property (nonatomic, strong) NSString *firmwareVersion;
@property (nonatomic, strong) NSString *firmSubVersion;

+ (MXDeviceInfo *)DeviceInfoWithHex:(NSString *)hexStr;

- (BOOL)isOEMTempDevice;

@end
