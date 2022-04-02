//
//  familyListViewController.h
//  BLT-Spo2Monitor
//
//  Created by luteng on 16/7/5.
//  Copyright © 2016年 luteng. All rights reserved.
//

#import <UIKit/UIKit.h>

@interface familyListViewController : UIViewController<UITableViewDataSource, UITableViewDelegate>
{
    NSInteger deleteId;
    BOOL _reloading;
}
- (void)reloadUserData;
@property (nonatomic, assign) BOOL isAskDoctor;
@end
