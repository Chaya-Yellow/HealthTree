//
//  ReviewListViewController.h
//  BLT-Spo2Monitor
//
//  Created by luteng on 15/10/28.
//  Copyright © 2015年 luteng. All rights reserved.
//

#import <UIKit/UIKit.h>

@interface ReviewListViewController : UIViewController<UITableViewDataSource, UITableViewDelegate>
/*!
 *    @author luteng, 16-07-21 15:07:26
 *
 *    @brief <#Description#>
 *
 *    @param trendArray <#trendArray description#>
 */
-(void)setTrendDataWithArray:(NSArray*)trendArray;
@end
