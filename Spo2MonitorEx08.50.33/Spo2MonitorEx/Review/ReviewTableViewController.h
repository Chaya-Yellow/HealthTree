//
//  ReviewTableViewController.h
//  BLT-Spo2Monitor
//
//  Created by luteng on 15/10/23.
//  Copyright © 2015年 luteng. All rights reserved.
//

#import <UIKit/UIKit.h>

@interface ReviewTableViewController : UITableViewController<UITableViewDelegate, UITableViewDataSource>
@property (nonatomic)int paramID;
-(void)setTrendDataWithArray:(NSArray*)trendData;
@end
