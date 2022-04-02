//
//  SCActivityIndicatorView.m
//  BLT-Spo2Monitor
//
//  Created by luteng on 15/10/20.
//  Copyright © 2015年 luteng. All rights reserved.
//

#import "SCActivityIndicatorView.h"

@interface SCActivityIndicatorView()
@property (nonatomic, weak) NSTimer *timer;
@property (nonatomic) BOOL isAnimation;
@property (nonatomic) NSUInteger currentStep;
@property (nonatomic) NSUInteger steps;
@property (nonatomic) float anglePerStep;
@property (nonatomic) CGSize cornerRadii;
@property (nonatomic) NSUInteger indicatorRadius;
@end

@implementation SCActivityIndicatorView
@synthesize color = _color;
@synthesize hidesWhenStopped = _hidesWhenStopped;
@synthesize stepDuration = _stepDuration;
@synthesize fingerSize = _fingerSize;

- (void)dealloc
{
    if (_timer) {
        [_timer invalidate];
        _timer = nil;
    }
}

- (id)initWithFrame:(CGRect)frame
{
    self = [super initWithFrame:frame];
    if (self) {
        [self initWithStyle:UIActivityIndicatorViewStyleWhite];
    }
    return self;
}

- (id)initWithActivityIndicatorStyle:(UIActivityIndicatorViewStyle)style
{
    self = [self initWithFrame:CGRectZero];
    if (self)
    {
        [self initWithStyle:style];
    }
    return self;
}

- (id)init
{
    return [self initWithActivityIndicatorStyle:UIActivityIndicatorViewStyleWhite];
}

- (id)initWithCoder:(NSCoder *)aDecoder
{
    return [self initWithActivityIndicatorStyle:UIActivityIndicatorViewStyleWhite];
}

- (void)initWithStyle:(UIActivityIndicatorViewStyle)activityIndicatorViewStyle
{
    self.backgroundColor = [UIColor clearColor];
    self.steps = 8;
    self.stepDuration = 0.1;
    self.cornerRadii = CGSizeMake(1, 1);
    
    switch (activityIndicatorViewStyle) {
        case UIActivityIndicatorViewStyleGray:
            self.color = [UIColor darkGrayColor];
            self.fingerSize = CGSizeMake(2, 4);
            self.indicatorRadius = 3;
            break;
        case UIActivityIndicatorViewStyleWhite:
            self.color = [UIColor grayColor];
            self.fingerSize = CGSizeMake(2, 4);
            self.indicatorRadius = 3;
            break;
        case UIActivityIndicatorViewStyleWhiteLarge:
            self.color = [UIColor whiteColor];
            self.fingerSize = CGSizeMake(3, 9);
            self.indicatorRadius = 8;
            break;
        default:
            break;
    }
    _isAnimation = NO;
    if (_hidesWhenStopped) {
        self.hidden = YES;
    }
}

// Only override drawRect: if you perform custom drawing.
// An empty implementation adversely affects performance during animation.
- (void)drawRect:(CGRect)rect
{
    CGContextRef context = UIGraphicsGetCurrentContext();
    
    CGRect finRect = CGRectMake(self.bounds.size.width/2 - _fingerSize.width/2, 0,
                                _fingerSize.width, _fingerSize.height);
    CGPathRef bezierPath = [self finPathWithRect:finRect];
    
    for (int i = 0; i < _steps; i++)
    {
        [[self colorForStep:_currentStep+i*-1] set];
        
        CGContextBeginPath(context);
        CGContextAddPath(context, bezierPath);
        CGContextClosePath(context);
        CGContextFillPath(context);
        
        CGContextTranslateCTM(context, self.bounds.size.width / 2, self.bounds.size.height / 2);
        CGContextRotateCTM(context, _anglePerStep);
        CGContextTranslateCTM(context, -(self.bounds.size.width / 2), -(self.bounds.size.height / 2));
    }
}

#pragma mark - Private Methods

- (void)repeatAnimation
{
    _currentStep++;
    [self setNeedsDisplay];
}

- (UIColor*)colorForStep:(NSUInteger)stepIndex
{
    CGFloat alpha = 1.0 - (stepIndex % _steps) * (1.0 / _steps);
    
    return [UIColor colorWithCGColor:CGColorCreateCopyWithAlpha(_color.CGColor, alpha)];
}

- (CGPathRef)finPathWithRect:(CGRect)rect
{
    UIBezierPath *bezierPath = [UIBezierPath bezierPathWithRoundedRect:rect
                                                     byRoundingCorners:UIRectCornerAllCorners
                                                           cornerRadii:_cornerRadii];
    CGPathRef path = CGPathCreateCopy([bezierPath CGPath]);
    return path;
}

#pragma mark - Public Methods

- (void)startAnimating
{
    if (_isAnimation) {
        return;
    }
    _currentStep = 0;
    _timer = [NSTimer scheduledTimerWithTimeInterval:_stepDuration target:self selector:@selector(repeatAnimation) userInfo:nil repeats:YES];
    _isAnimation = YES;
    if (_hidesWhenStopped) {
        self.hidden = NO;
    }
    
}

- (void)stopAnimating
{
    
    if (_timer) {
        [_timer invalidate];
        _timer = nil;
    }
    
    _isAnimation = NO;
    if (_hidesWhenStopped) {
        self.hidden = YES;
    }
}

- (BOOL)isAnimating
{
    return _isAnimation;
}

#pragma mark - Set Methods

- (void)setSteps:(NSUInteger)s
{
    _anglePerStep = (360/s) * M_PI / 180;
    _steps = s;
    [self setNeedsDisplay];
}

- (void)setFingerSize:(CGSize)size
{
    _fingerSize = size;
    [self setNeedsDisplay];
}

- (void)setIndicatorRadius:(NSUInteger)indicatorRadius
{
    _indicatorRadius = indicatorRadius;
    self.frame = CGRectMake(self.frame.origin.x, self.frame.origin.y,
                            _indicatorRadius*2 + _fingerSize.height*2,
                            _indicatorRadius*2 + _fingerSize.height*2);
    [self setNeedsDisplay];
}

@end
