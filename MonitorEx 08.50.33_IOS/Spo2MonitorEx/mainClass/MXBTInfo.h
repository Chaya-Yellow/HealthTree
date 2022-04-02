//
//  MXBTInfo.h
//  BLT-Spo2Monitor
//
//  Created by luteng on 15/10/20.
//  Copyright © 2015年 luteng. All rights reserved.
//

#import <Foundation/Foundation.h>
#import <CoreBluetooth/CoreBluetooth.h>
@interface MXBTInfo:NSObject
@property (nonatomic, strong) CBPeripheral *peripheral;
@property (nonatomic, strong) NSData *manufacturerData;
@end
