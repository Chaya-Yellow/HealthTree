//
//  MXDeviceInfoViewController.h
//  BLT-Spo2Monitor
//
//  Created by luteng on 15/10/20.
//  Copyright © 2015年 luteng. All rights reserved.
//

#import <UIKit/UIKit.h>
@class MXDeviceInfo;
@interface MXDeviceInfoViewController : UIViewController <UITableViewDelegate, UITableViewDataSource>
{
    
}
- (id)initWithDeviceInfo:(MXDeviceInfo *)info;
//- (void)updateNickName:(NSString *)name;
@end
