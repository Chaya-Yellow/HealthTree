//
//  MXAlertView.m
//  MarkMan
//
//  Created by OMESOFT-112 on 14-2-12.
//  Copyright (c) 2014年 omesoft. All rights reserved.
//

#import "MXAlertView.h"

#import "OMESoft.h"
#import <QuartzCore/QuartzCore.h>

@interface MXAlertView ()
@property (retain, nonatomic) UILabel *messageLabel;
@property (retain, nonatomic) UITextField *textField;
@property (retain, nonatomic) UIView *contextView;
@property (retain, nonatomic) UILabel *titleLabel;
@end

@implementation MXAlertView
@synthesize delegate = _delegate;
@synthesize alertSytle = _alertSytle;

- (void)dealloc
{
    _messageLabel = nil;
    _textField = nil;
    _contextView = nil;
    _titleLabel = nil;
}

- (id)initWithTitle:(NSString *)title message:(NSString *)message delegate:(id)dele cancelButtonTitle:(NSString *)cancelButtonTitle otherButtonTitles:(NSString *)otherButtonTitles upperView:(UIView *)view
{
    self = [super init];
    if (self) {
        self.frame = view.bounds;
        self.backgroundColor = [UIColor colorWithWhite:0 alpha:0.3];
        UIImage *bgImage = [UIImage imageNamed:@"bg_symptom_warn.png"];
        self.contextView = [[UIView alloc] initWithFrame:CGRectMake(0, 0, bgImage.size.width,bgImage.size.height)];
        _contextView.backgroundColor = [UIColor clearColor];
        _contextView.center = self.center;
        
        UIImageView *bgImageView = [[UIImageView alloc] initWithFrame:CGRectMake(0, 0, bgImage.size.width, bgImage.size.height)];
        bgImageView.image = bgImage;
        [_contextView addSubview:bgImageView];
        
        self.alertSytle = AlertViewStyleNormal;
        
        self.titleLabel = [[UILabel alloc] initWithFrame:CGRectMake(bgImageView.frame.origin.x, bgImageView.frame.origin.y+16, bgImageView.frame.size.width, 20)];
        _titleLabel.text = title;
        _titleLabel.backgroundColor = [UIColor clearColor];
        _titleLabel.textAlignment = IOS6 ? NSTextAlignmentCenter : NSTextAlignmentCenter;
        _titleLabel.textColor = [UIColor colorWithHexString:@"#a3a3a3"];
        _titleLabel.font = [UIFont boldSystemFontOfSize:18.0];
        [_contextView addSubview:_titleLabel];
        
        self.messageLabel = [[UILabel alloc] initWithFrame:CGRectMake(_titleLabel.frame.origin.x + 2, _titleLabel.frame.size.height+_titleLabel.frame.origin.y+10, _titleLabel.frame.size.width - 4, 45)];
        _messageLabel.text = message;
        _messageLabel.numberOfLines = 0;
        _messageLabel.textColor = [UIColor colorWithHexString:@"#373737"];
        _messageLabel.textAlignment = IOS6 ? NSTextAlignmentCenter : NSTextAlignmentCenter;
        _messageLabel.font = [UIFont systemFontOfSize:14.0f];
        _messageLabel.backgroundColor = [UIColor clearColor];
        [_contextView addSubview:_messageLabel];
        
        self.textField = [[UITextField alloc] initWithFrame:CGRectMake(0, 0, 170, 30)];
        _textField.borderStyle = UITextBorderStyleRoundedRect;
        _textField.font = [UIFont systemFontOfSize:15.0f];
        _textField.contentVerticalAlignment = UIControlContentVerticalAlignmentCenter;
        _textField.center = CGPointMake(_messageLabel.center.x, _messageLabel.center.y+5);
        [_contextView addSubview:self.textField];
        
        UIImage *cancelImage = [UIImage imageNamed:@"btn_symptom_warncancel.png"];
        UIButton *cancelButton = [UIButton buttonWithType:UIButtonTypeCustom];
        [cancelButton setTitleColor:[UIColor colorWithHexString:@"#4e4e4e"] forState:UIControlStateNormal];
        cancelButton.titleLabel.font = [UIFont boldSystemFontOfSize:16.0f];
        cancelButton.frame = CGRectMake(_messageLabel.frame.origin.x+20, bgImageView.frame.origin.y+bgImageView.frame.size.height-cancelImage.size.height-16, cancelImage.size.width, cancelImage.size.height);
        [cancelButton setBackgroundImage:cancelImage forState:UIControlStateNormal];
        [cancelButton setTitle:cancelButtonTitle forState:UIControlStateNormal];
        [cancelButton addTarget:self action:@selector(buttonPressed:) forControlEvents:UIControlEventTouchUpInside];
        cancelButton.tag = 0;
        [_contextView addSubview:cancelButton];
        
        if (otherButtonTitles == nil) {
            cancelButton.center = CGPointMake(_contextView.frame.size.width / 2, cancelButton.center.y);
            [cancelButton setBackgroundImage:[UIImage imageNamed:@"btn_symptom_warncomf.png"] forState:UIControlStateNormal];
        } else {
            UIImage *comfirmImage = [UIImage imageNamed:@"btn_symptom_warncomf.png"];
            UIButton *comfirmButton = [UIButton buttonWithType:UIButtonTypeCustom];
            [comfirmButton setTitleColor:[UIColor colorWithHexString:@"#f6f6f6"] forState:UIControlStateNormal];
            comfirmButton.titleLabel.font = [UIFont boldSystemFontOfSize:16.0f];
            comfirmButton.frame = CGRectMake(bgImageView.frame.origin.x+bgImageView.frame.size.width-comfirmImage.size.width-20, cancelButton.frame.origin.y, comfirmImage.size.width, comfirmImage.size.height);
            [comfirmButton setBackgroundImage:comfirmImage forState:UIControlStateNormal];
            [comfirmButton setTitle:otherButtonTitles forState:UIControlStateNormal];
            [comfirmButton addTarget:self action:@selector(buttonPressed:) forControlEvents:UIControlEventTouchUpInside];
            comfirmButton.tag = 1;
            [_contextView addSubview:comfirmButton];
        }
        
        
        [self setDelegate:dele];
        [view addSubview:self];
        self.alpha = 0;
        [self addSubview:_contextView];
    }
    return self;
}

#pragma mark -  set & get methods

- (void)setPlaceholder:(NSString *)holder
{
    self.textField.placeholder = holder;
}

- (NSString *)placeholder
{
    return self.textField.placeholder;
}

- (void)setAlertSytle:(AlertViewStyle)s
{
    if (self.alertSytle == s) {
        return;
    }
    _alertSytle = s;
    [self setNeedsDisplay];
}

- (void)setTitleColor:(UIColor *)color
{
    if (color == nil) {
        return;
    }
    self.titleLabel.textColor = color;
}

#pragma mark - button events
- (void)buttonPressed:(id)sender
{
    UIButton *pressedButton = (UIButton *)sender;
    
    if ([_delegate respondsToSelector:@selector(alertView:buttonDidSelected:)]) {
        [_delegate alertView:self buttonDidSelected:pressedButton.tag];
    }
    if (pressedButton.tag == 1) {
        if (self.alertSytle == AlertViewStyleInput) {
            [self.textField resignFirstResponder];
            if ([_delegate respondsToSelector:@selector(alertView:inputText:)]) {
                [_delegate alertView:self inputText:self.textField.text];
            }
        }
    }
    
    [UIView animateWithDuration:0.35 animations:^{
        self.alpha = 0;
    } completion:^(BOOL finished) {
        if (finished) {
            [self removeFromSuperview];
        }
    }];
}

- (void)show
{
    self.alpha = 1;
    if (self.alertSytle == AlertViewStyleInput) {
        [self.textField becomeFirstResponder];
    }
}

- (void)drawRect:(CGRect)rect
{
    if (self.alertSytle == AlertViewStyleNormal) {
        self.textField.hidden = YES;
        self.messageLabel.hidden = NO;
        self.contextView.center = self.center;
    } else {
        self.textField.hidden = NO;
        self.messageLabel.hidden = YES;
        self.contextView.center = CGPointMake(self.center.x, (self.frame.size.height - 216) / 2);
    }
}

/*
// Only override drawRect: if you perform custom drawing.
// An empty implementation adversely affects performance during animation.
- (void)drawRect:(CGRect)rect
{
    // Drawing code
}
*/

@end
