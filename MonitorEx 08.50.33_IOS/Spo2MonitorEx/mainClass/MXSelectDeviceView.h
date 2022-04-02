//
//  MXSelectDeviceView.h
//  BLT-Spo2Monitor
//
//  Created by luteng on 15/10/20.
//  Copyright © 2015年 luteng. All rights reserved.
//

#import <UIKit/UIKit.h>

@protocol MXSelectDeviceViewDelegate;
@interface MXSelectDeviceView : UIView
@property (nonatomic, strong) NSArray *selectDevices;
@property (nonatomic, assign) id <MXSelectDeviceViewDelegate> delegate;
@end

@protocol MXSelectDeviceViewDelegate  <NSObject>

- (void)deviceViewCancelDidSelected:(MXSelectDeviceView *)view;

@end
