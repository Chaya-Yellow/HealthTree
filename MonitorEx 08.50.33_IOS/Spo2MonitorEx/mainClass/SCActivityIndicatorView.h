//
//  SCActivityIndicatorView.h
//  BLT-Spo2Monitor
//
//  Created by luteng on 15/10/20.
//  Copyright © 2015年 luteng. All rights reserved.
//

#import <UIKit/UIKit.h>

@interface SCActivityIndicatorView : UIView
@property (nonatomic, strong) UIColor *color;
@property (nonatomic) BOOL hidesWhenStopped;
@property (nonatomic) float stepDuration;//转动时间 默认0.1
@property (nonatomic) CGSize fingerSize;//指针大小

- (id)initWithActivityIndicatorStyle:(UIActivityIndicatorViewStyle)style;

- (void)startAnimating;
- (void)stopAnimating;
- (BOOL)isAnimating;
@end
