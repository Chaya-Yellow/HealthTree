//
//  MXAlertView.h
//  MarkMan
//
//  Created by OMESOFT-112 on 14-2-12.
//  Copyright (c) 2014年 omesoft. All rights reserved.
//

#import <UIKit/UIKit.h>

typedef enum {
    AlertViewStyleNormal,
    AlertViewStyleInput
}AlertViewStyle;

@protocol MXAlertViewDelegate;
@interface MXAlertView : UIView{
    id <MXAlertViewDelegate> delegate;
    AlertViewStyle style;
}
@property (assign, nonatomic) id <MXAlertViewDelegate> delegate;
@property (assign, nonatomic) AlertViewStyle alertSytle;
@property (nonatomic,copy)   NSString *placeholder;

- (id)initWithTitle:(NSString *)title message:(NSString *)message delegate:(id)dele cancelButtonTitle:(NSString *)cancelButtonTitle otherButtonTitles:(NSString *)otherButtonTitles upperView:(UIView *)view;
- (void)show;
- (void)setTitleColor:(UIColor *)color;
@end

@protocol MXAlertViewDelegate <NSObject>

@optional

- (void)alertView:(MXAlertView *)view buttonDidSelected:(NSInteger)tag;
- (void)alertView:(MXAlertView *)view inputText:(NSString *)text;


@end
