//
//  MXSpo2WavesView.m
//  BLT-Spo2Monitor
//
//  Created by luteng on 15/10/21.
//  Copyright © 2015年 luteng. All rights reserved.
//

#import "MXSpo2WavesView.h"
#import "consDefine.h"

#define Waves_y  37

@interface MXSpo2WavesView()

@end

@implementation MXSpo2WavesView
{
    CGPoint drawingPoints[20];
    CGPoint endPoint, endPoint2, endPoint3;
    int currentPoint;
    int count;
    bool bDrawBaseLine;
}

-(void)canDrawBaseLine:(bool)bDraw
{
    bDrawBaseLine = bDraw;
    currentPoint =0;
}


-(void)drawWaves:(NSArray *)waves
{
    NSLog(@"WAVES = %@", waves);
    int Spo2pointCount = (int)waves.count; //常量10
    CGFloat full_height = self.frame.size.height - Waves_y;
    CGFloat full_width = self.frame.size.width;
    long Spo2pointTatal = 180;                 //一屏画6s波形
    CGFloat Spo2proPortion = full_width/Spo2pointTatal; //比例系数
    count = 0;
    int Spo2index=0;
    CGFloat cellH;
    CGFloat vProportion;
    //7导同屏
    cellH = full_height;
    vProportion = (cellH)/200.0;
        for (int i=0; i<Spo2pointCount; i++)
        {
            double EcgPos_x = (currentPoint%Spo2pointTatal)*Spo2proPortion;
            if (i > 0 && EcgPos_x == 0)
            {
                break;
            }
            if (i == 0 && EcgPos_x != 0)
            {
                drawingPoints[0] = endPoint3;
                drawingPoints[1] = endPoint2;
                drawingPoints[2] = endPoint;
                i+=3; Spo2pointCount+=3; count+=3;
            }
            double Spo2value = [waves[Spo2index] integerValue];
//            NSLog(@"spo2Value:::%f",Spo2value);
            
            if (Spo2value==0) {
                Spo2value=1;
            }

            //spo2
            if(Spo2value !=INVAL_VALUE && Spo2value != 0)
            {
                Spo2value= -Spo2value*vProportion + cellH;
            }
            else
            {
                Spo2value =  cellH/2;
            }
            
            drawingPoints[i] = CGPointMake(EcgPos_x, Spo2value);
            currentPoint++;
            count++;
            Spo2index++;
        }
    if (count > 0)
    {
        CGRect rect;
        if(bDrawBaseLine)
            rect = CGRectMake(drawingPoints[1].x, Waves_y, count*Spo2proPortion+10, full_height);
        else
            rect = CGRectMake(0, Waves_y, self.frame.size.width, self.frame.size.height);
        [self setNeedsDisplayInRect:rect];
    }
}

-(void)drawSpo2Waves:(CGContextRef)contex
{
    CGContextSetLineWidth(contex, 1.0);
    if (count == 0)
        return;
    if(bDrawBaseLine)
        CGContextSetStrokeColorWithColor(contex, UUSpo2LabelColor.CGColor);
    else
        CGContextSetStrokeColorWithColor(contex, UUClear.CGColor);
    CGContextAddLines(contex, drawingPoints, count);
    CGContextStrokePath(contex);
    NSLog(@"count:::%d",count);
    endPoint3  =drawingPoints[count-3];
    endPoint2  =drawingPoints[count-2];
    endPoint   =drawingPoints[count-1];
        
}

-(void)drawLabel:(CGContextRef)contex
{
    CGContextSetFillColorWithColor(contex, UUSpo2LabelColor.CGColor);
    float fullHeight = self.frame.size.height;
    UIFont* font = [UIFont systemFontOfSize:14];
    [@"Pleth" drawAtPoint:CGPointMake(11, 13) withFont:font];
}


- (void)drawRect:(CGRect)rect
{
    CGContextRef context1 = UIGraphicsGetCurrentContext();
    [self drawGrid:context1];
    [self drawLabel:context1];
    [self drawSpo2Waves:context1];
}

- (void)drawGrid:(CGContextRef)ctx {
    CGFloat full_height = self.frame.size.height;
    CGFloat full_width = self.frame.size.width;
    CGFloat cell_square_width = 7;
    
    CGContextSetLineWidth(ctx, 0.2);
    CGContextSetStrokeColorWithColor(ctx, [UIColor lightGrayColor].CGColor);
    
    int pos_x = 0;
    int pos_y = Waves_y;
    while (pos_x < full_width) {
        CGContextMoveToPoint(ctx, pos_x, pos_y);
        CGContextAddLineToPoint(ctx, pos_x, full_height);
        pos_x += cell_square_width;
        
        CGContextStrokePath(ctx);
    }
    
    while (pos_y <= full_height) {
        
        CGContextSetLineWidth(ctx, 0.2);
        
        CGContextMoveToPoint(ctx, 1, pos_y);
        CGContextAddLineToPoint(ctx, full_width, pos_y);
        pos_y += cell_square_width;
        
        CGContextStrokePath(ctx);
    }
    
//    CGContextSetLineWidth(ctx, 0.1);
//
//    cell_square_width = cell_square_width / 5;
//    pos_x = 1 + cell_square_width;
//    while (pos_x < full_width) {
//        CGContextMoveToPoint(ctx, pos_x, 1);
//        CGContextAddLineToPoint(ctx, pos_x, full_height);
//        pos_x += cell_square_width;
//
//        CGContextStrokePath(ctx);
//    }
//    pos_y = 1 + cell_square_width;
//    while (pos_y <= full_height) {
//        CGContextMoveToPoint(ctx, 1, pos_y);
//        CGContextAddLineToPoint(ctx, full_width, pos_y);
//        pos_y += cell_square_width;
//
//        CGContextStrokePath(ctx);
//    }
}


@end
