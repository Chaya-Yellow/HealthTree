//
//  ReviewWavesView.h
//  BLT-Spo2Monitor
//
//  Created by luteng on 15/10/23.
//  Copyright © 2015年 luteng. All rights reserved.
//

#import <UIKit/UIKit.h>

@interface ReviewWavesView : UIView
@property (nonatomic)int paramID;
@property (nonatomic, copy)NSArray* paramDataArray;
-(void)initOldFrams;
@end
