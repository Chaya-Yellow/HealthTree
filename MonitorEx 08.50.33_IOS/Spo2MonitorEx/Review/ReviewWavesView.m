//
//  ReviewWavesView.m
//  BLT-Spo2Monitor
//
//  Created by luteng on 15/10/23.
//  Copyright © 2015年 luteng. All rights reserved.
//

#import "ReviewWavesView.h"
#import "ReviewDataObj.h"
#import "consDefine.h"

#define LABEL_W 30
#define LABEL_H 10
@implementation ReviewWavesView
{
    CGRect oldFrame;    //保存图片原来的大小
    CGRect largeFrame;  //确定图片放大最大的程度
}
- (void)drawRect:(CGRect)rect
{
    CGContextRef context1 = UIGraphicsGetCurrentContext();
    //
    [self drawCoordinate:context1];
    [self drawHLines:context1];
}

-(void)initOldFrams
{
    //oldFrame = self.frame;
    //largeFrame = CGRectMake(0 - self.frame.size.width, 0 - self.frame.size.height, 3 * oldFrame.size.width, 3 * oldFrame.size.height);
}

-(void)layoutSubviews
{
    //UIPinchGestureRecognizer* pinch = [[UIPinchGestureRecognizer alloc]initWithTarget:self action:@selector(pinchProc:)];
    //[self addGestureRecognizer:pinch];
    //UIPanGestureRecognizer* panch = [[UIPanGestureRecognizer alloc]initWithTarget:self action:@selector(panchProc:)];
    //[self addGestureRecognizer:panch];
}

-(void)panchProc:(UIPanGestureRecognizer*)panch
{
    /*
    CGPoint translation = [panch translationInView:self];
    panch.view.center = CGPointMake(panch.view.center.x + translation.x,
                                         panch.view.center.y + translation.y);
    [panch setTranslation:CGPointZero inView:self];
     */
}

-(void)pinchProc:(UIPinchGestureRecognizer*)pinch
{
    if (pinch.view.frame.size.width > 3*oldFrame.size.width) {
        return;
    }
    if(pinch.view.frame.size.width < oldFrame.size.width/2)
    {
        return;
    }
    pinch.view.transform = CGAffineTransformScale(pinch.view.transform, pinch.scale, pinch.scale);
    pinch.scale = 1;
}

//坐标系
-(void)drawCoordinate:(CGContextRef)context
{
    CGRect rect = CGRectMake(SPACING_W+LABEL_W, SPACING_W+LABEL_H, CGRectGetWidth(self.bounds)-2*SPACING_W-LABEL_W,CGRectGetHeight(self.bounds)-2*SPACING_W-LABEL_H);
    CGPoint originPoint = rect.origin;
    CGContextSetLineWidth(context, 1);
    CGContextSetStrokeColorWithColor(context, UUDeepGrey.CGColor);
    CGContextMoveToPoint(context, originPoint.x, originPoint.y-LABEL_H);
    CGContextAddLineToPoint(context, originPoint.x, originPoint.y+CGRectGetHeight(rect));
    CGContextAddLineToPoint(context, originPoint.x+CGRectGetWidth(rect), originPoint.y+CGRectGetHeight(rect));
    CGContextStrokePath(context);
}
//横线
-(void)drawHLines:(CGContextRef)context
{
    CGRect rect = CGRectMake(SPACING_W+LABEL_W, SPACING_W+LABEL_H, CGRectGetWidth(self.bounds)-2*(SPACING_W+LABEL_W),CGRectGetHeight(self.bounds)-2*SPACING_W-LABEL_H);
    CGPoint originPoint = rect.origin;
    CGContextSetStrokeColorWithColor(context, UUGrey.CGColor);
    CGContextSetLineWidth(context, 0.3);
    float h;
    float w = CGRectGetWidth(rect);
    CGFloat lengths[] = {3,1};
    CGContextSetLineDash(context, 0, lengths,1);
    //NSLog(@"%i", _paramID);
    switch (_paramID) {
        case 0:
        {
            h = CGRectGetHeight(rect)/10;
            //初始化坐标系
            [self initChart:context withOriginPoint:originPoint withCellH:h withLineNu:10 withLineWidth:w widthSpace:2 withMinValue:80];
            CGContextStrokePath(context);
            CGFloat lengths[] = {0,0};
            CGContextSetLineDash(context, 0, lengths,0);
            CGContextSetStrokeColorWithColor(context, UUSpo2LabelColor.CGColor);
            CGContextSetLineWidth(context, 0.8);
            NSArray* arry = [self getParamDataByMoudleID:_paramID withParamID:0];
            //NSLog(@"arry******************* = %@", arry);
            //画spo2数据线
            [self drawDataLines:context withOrigin:originPoint withWidth:w withHeight:CGRectGetHeight(rect) withDataArray:arry withMaxValue:100 withMinValue:80 withParamID:0];
            CGContextStrokePath(context);
        }
            break;
        case 1:
        {
            h = CGRectGetHeight(rect)/13;
            [self initChart:context withOriginPoint:originPoint withCellH:h withLineNu:13 withLineWidth:w widthSpace:20 withMinValue:0];
            CGContextStrokePath(context);
            CGFloat lengths[] = {0,0};
            CGContextSetLineDash(context, 0, lengths,0);
            CGContextSetStrokeColorWithColor(context, UUPrLabelColor.CGColor);
            CGContextSetLineWidth(context, 0.8);
            NSArray* arry = [self getParamDataByMoudleID:_paramID withParamID:0];
            //画pr数据线
            [self drawDataLines:context withOrigin:originPoint withWidth:w withHeight:CGRectGetHeight(rect) withDataArray:arry withMaxValue:260 withMinValue:0 withParamID:1];
            CGContextStrokePath(context);
            CGContextSetStrokeColorWithColor(context, UUTwitterColor.CGColor);
            arry = [self getParamDataByMoudleID:_paramID withParamID:1];
        }
        break;
        default:
            break;
    }
    CGContextStrokePath(context);
}

-(NSMutableArray*)getParamDataByMoudleID:(int)moudleID withParamID:(int)paramID
{
    NSMutableArray* array = [NSMutableArray array];
    if(_paramDataArray == nil || _paramDataArray.count==0)
        return nil;
    else
    {
        switch (moudleID) {
            case 0:
                for(int i=0; i<_paramDataArray.count; i++)
                {
                    NSDictionary* dic = _paramDataArray[i];
                    int value = (int)[[dic objectForKey:@"SPO2"] integerValue];
                    [array addObject:@(value)];
                }
                break;
            case 1:
                for(int i=0; i<_paramDataArray.count; i++)
                {
                    NSDictionary* Dic  = _paramDataArray[i];
                    int value =(int)[[Dic objectForKey:@"PR"] integerValue];
                    [array addObject:@((int)value) ];
                }
                break;
                default:
                break;
        }
    }
    return  array;
}

-(void)initChart:(CGContextRef)context withOriginPoint:(CGPoint)origin withCellH:(float)height withLineNu:(int)lineNo withLineWidth:(float)width widthSpace:(float)space withMinValue:(float)minValue
{
    for(int i=0; i<lineNo; i++)
    {
        CGContextMoveToPoint(context, origin.x, origin.y+i*height);
        CGContextAddLineToPoint(context, origin.x+width, origin.y+i*height);
        UILabel* label = [[UILabel alloc]initWithFrame:CGRectMake(SPACING_W, origin.y+i*height-height/2, LABEL_W, height)];
        label.textColor = UUGrey;
        label.textAlignment = NSTextAlignmentCenter;
        label.font =[UIFont systemFontOfSize:10];
        label.text =[NSString stringWithFormat:@"%.f",space*(lineNo+1)-space*(i+1)+minValue];
        [self addSubview:label];
    }
}

-(void)drawDataLines:(CGContextRef)context withOrigin:(CGPoint)origin withWidth:(float)width withHeight:(float)height withDataArray:(NSArray*)array withMaxValue:(float)maxValue withMinValue:(float)minValue withParamID:(int)paramID
{
    if(array.count ==0 || array ==nil || context==nil)
        return;
    bool bDraw = false;
    float x_proportion = width/(array.count-1);         //横向比例系数
    float y_proportion = height/(maxValue-minValue);           //纵向比例系数
    float firstPoint_y = INVAL_VALUE;
    CGPoint prePoint;
    int firstPox = 0;
    //=[array[0] floatValue]-minValue;       //首点
    for(int i=0; i<array.count; i++)
    {
        if(paramID ==0)
        {
            if([array[i] floatValue] != INVAL_VALUE && [array[i] floatValue] != 127)
            {
                bDraw = TRUE;
                firstPox = i;
                firstPoint_y = [array[i] floatValue]-minValue;       //首点
                break;
            }
        }
        else if(paramID ==1)
        {
            if([array[i] floatValue] != INVAL_VALUE && [array[i] floatValue] != 255)
            {
                firstPox = i;
                bDraw = TRUE;
                firstPoint_y = [array[i] floatValue]-minValue;       //首点
                break;
            }
        }
    }
    if(bDraw == false)
        return;
    firstPoint_y = firstPoint_y * y_proportion;
    firstPoint_y = height - firstPoint_y+origin.y;
    prePoint.x = origin.x+firstPox*x_proportion;
    prePoint.y = firstPoint_y;
    CGContextMoveToPoint(context, prePoint.x, prePoint.y);
    //NSLog(@"-------arrarcount = %li, x_por = %f, y_por = %f", array.count, x_proportion, y_proportion);
    for(int i=firstPox; i<array.count; i++)
    {
        float value = [array[i] floatValue];
        if(paramID ==0)//spo2
        {
            if(value != INVAL_VALUE && value != 127)
            {
                value -= minValue;
                value= value * y_proportion;
                value = height - value+origin.y;
                CGContextMoveToPoint(context, prePoint.x, prePoint.y);
                CGContextAddLineToPoint(context, origin.x+i*x_proportion, value);
                prePoint.x = origin.x+i*x_proportion;
                prePoint.y = value;
            }else{
                prePoint.x = origin.x+i*x_proportion;
               //CGContextMoveToPoint(context, origin.x+i*x_proportion,value);
            }
        }
        else if(paramID ==1)
        {
            if(value != INVAL_VALUE && value != 255)
            {
                value -= minValue;
                value= value * y_proportion;
                value = height - value+origin.y;
                CGContextMoveToPoint(context, prePoint.x, prePoint.y);
                CGContextAddLineToPoint(context, origin.x+i*x_proportion, value);
                prePoint.x = origin.x+i*x_proportion;
                prePoint.y = value;
            }
            else
            {
                prePoint.x = origin.x+i*x_proportion;
            }
        }
    }
}
@end
