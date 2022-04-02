//
//  MXBloodBarView.m
//  BLT-Spo2Monitor
//
//  Created by luteng on 15/11/5.
//  Copyright © 2015年 luteng. All rights reserved.
//

#import "MXBloodBarView.h"
#import "consDefine.h"

@implementation MXBloodBarView
{
    int myBarValue;
}

-(void)DrawBloodBarValue:(int)Value
{
    myBarValue = Value;
    [self setNeedsDisplay];
}


-(void)drawRect:(CGRect)rect
{
    CGContextRef context1 = UIGraphicsGetCurrentContext();
    CGRect Rc = self.bounds;
    CGRect xrect = CGRectMake(0, 0, CGRectGetWidth(Rc), CGRectGetHeight(Rc));
    [self drawBar:context1 withRect:xrect];
}


-(void)drawBar:(CGContextRef)contex withRect:(CGRect)Rect
{
    ////CGContextSetStrokeColorWithColor(contex, UUBloodBarBackColor.CGColor);
    // NSLog(@"%f, %f, %f, %f", Rect.origin.x, Rect.origin.y, Rect.size.width, Rect.size.height);
    float h = CGRectGetHeight(Rect)/15;
    CGContextSetLineWidth(contex, h-1);
    for(int i=0 ;i<15; i++)
    {
        if(i<myBarValue)
        {
            CGContextSetStrokeColorWithColor(contex, UUBloodBarFrontColor.CGColor);
        }
        else
        {
            CGContextSetStrokeColorWithColor(contex, UUBloodBarBackColor.CGColor);
        }
        CGContextMoveToPoint(contex, Rect.origin.x, Rect.origin.y+CGRectGetHeight(Rect)-i*h-1);
        CGContextAddLineToPoint(contex, Rect.origin.x+CGRectGetWidth(Rect), Rect.origin.y+CGRectGetHeight(Rect)-i*h-1);
        CGContextStrokePath(contex);
    }
}
@end
