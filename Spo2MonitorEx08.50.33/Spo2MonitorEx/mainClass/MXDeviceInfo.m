//
//  MXDeviceInfo.m
//  BLT-Spo2Monitor
//
//  Created by luteng on 15/10/20.
//  Copyright © 2015年 luteng. All rights reserved.
//

#import "MXDeviceInfo.h"

@interface MXDeviceInfo()

@end

@implementation MXDeviceInfo
@synthesize deviceModel = _deviceModel;
@synthesize protocolVersion = _protocolVersion;
@synthesize serialNumber = _serialNumber;
@synthesize nickName = _nickName;
@synthesize firmwareVersion = _firmwareVersion;
@synthesize boundId = _boundId;
@synthesize firmSubVersion = _firmSubVersion;

- (NSString *)deviceModelName
{
    NSString *result = @"";
    NSLog(@"---------------------%ld",(long)_deviceModel);
    if (_deviceModel == 1) {
        result = @"WT1";
    } else if (_deviceModel == 2) {
        result = @"WT2";
    }
    else if(_deviceModel == 48)
    {
        result = @"M70C";
    }
    return result;
}

+ (MXDeviceInfo *)DeviceInfoWithHex:(NSString *)hexStr
{
    NSLog(@"yYYYYYYYYY%@", hexStr);
    if (hexStr == nil || hexStr.length <=0) {
        return nil;
    }
    MXDeviceInfo *deviceInfo = [[MXDeviceInfo alloc] init];
    //00010001 57303737 41303030 30303100
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
    NSString *protocolStr = [hexStr substringWithRange:NSMakeRange(2, 2)];
    NSString *modelStr = [hexStr substringWithRange:NSMakeRange(6, 2)];
    deviceInfo.protocolVersion = [[NSString stringWithFormat:@"%lu",strtoul([protocolStr UTF8String], 0, 16)] intValue];
    deviceInfo.deviceModel = [[NSString stringWithFormat:@"%lu",strtoul([modelStr UTF8String], 0, 16)] intValue];
    deviceInfo.serialNumber = [NSString stringWithString:currentResult];
    return deviceInfo;
}

- (BOOL)isOEMTempDevice
{
    if (self.serialNumber != nil && self.serialNumber.length > 0) {
        if (self.serialNumber.length >= 9) {
            NSLog(@"%@",self.serialNumber);
            NSInteger serialNum = [[self.serialNumber substringFromIndex:4] integerValue];
            if (serialNum >= 60001 && serialNum <= 70000) {
                return YES;
            }
        }
    }
    return NO;
}

@end
