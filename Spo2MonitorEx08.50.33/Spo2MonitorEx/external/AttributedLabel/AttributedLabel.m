//
//  AttributedLabel.m
//  AttributedStringTest
//
//  Created by sun huayu on 13-2-19.
//  Copyright (c) 2013年 sun huayu. All rights reserved.
//

#import "AttributedLabel.h"

@interface AttributedLabel(){

}
@property (nonatomic, retain) NSMutableAttributedString  *attString;
@property (nonatomic, retain) CATextLayer *textLayer;
@end

@implementation AttributedLabel

- (id)initWithFrame:(CGRect)frame
{
    self = [super initWithFrame:frame];
    if (self) {
        // Initialization code
        self.textLayer = [CATextLayer layer];
        _textLayer.string = _attString;
        _textLayer.frame = CGRectMake(0, 0, self.frame.size.width, self.frame.size.height);
        _textLayer.contentsScale = 2;
        _textLayer.alignmentMode = kCAAlignmentCenter;
        [self.layer addSublayer:_textLayer];
    }
    return self;
}

-(void)dealloc
{
    _attString = nil;
    _textLayer = nil;
}

- (void)drawRect:(CGRect)rect{
    _textLayer.string = _attString;
}

- (void)setText:(NSString *)text{
    [super setText:text];
    if (text == nil) {
        self.attString = nil;
    }else{
        self.attString = [[NSMutableAttributedString alloc] initWithString:text];
    }
}

// 设置某段字的颜色
- (void)setColor:(UIColor *)color fromIndex:(NSInteger)location length:(NSInteger)length{
    if (location < 0||location>self.text.length-1||length+location>self.text.length) {
        return;
    }
    [_attString addAttribute:(NSString *)kCTForegroundColorAttributeName
                        value:(id)color.CGColor
                        range:NSMakeRange(location, length)];
}

// 设置某段字的字体
- (void)setFont:(UIFont *)font fromIndex:(NSInteger)location length:(NSInteger)length{
    if (location < 0||location>self.text.length-1||length+location>self.text.length) {
        return;
    }
    CTFontRef fontRef = CTFontCreateWithName((__bridge CFStringRef)font.fontName,
                                             font.pointSize,
                                             NULL);
    [_attString addAttribute:(NSString *)kCTFontAttributeName
                        value:(__bridge id)fontRef
                        range:NSMakeRange(location, length)];
    CFRelease(fontRef);
    
}

// 设置某段字的风格
- (void)setStyle:(CTUnderlineStyle)style fromIndex:(NSInteger)location length:(NSInteger)length{
    if (location < 0||location>self.text.length-1||length+location>self.text.length) {
        return;
    }
    [_attString addAttribute:(NSString *)kCTUnderlineStyleAttributeName
                        value:(id)[NSNumber numberWithInt:style]
                        range:NSMakeRange(location, length)];
}

/*
// Only override drawRect: if you perform custom drawing.
// An empty implementation adversely affects performance during animation.
- (void)drawRect:(CGRect)rect
{
    // Drawing code
}
*/

-(void)setAttString:(NSMutableAttributedString *)att
{
    _attString = att;
    [self setNeedsDisplay];
}

- (void)setTextAlignment:(NSTextAlignment)textAlignment
{
    switch (textAlignment) {
        case NSTextAlignmentCenter:
            _textLayer.alignmentMode = kCAAlignmentCenter;
            break;
        case NSTextAlignmentLeft:
            _textLayer.alignmentMode = kCAAlignmentLeft;
            break;
        case NSTextAlignmentJustified:
            _textLayer.alignmentMode = kCAAlignmentJustified;
            break;
        case NSTextAlignmentNatural:
            _textLayer.alignmentMode = kCAAlignmentNatural;
            break;
        case NSTextAlignmentRight:
            _textLayer.alignmentMode = kCAAlignmentRight;
            break;
        default:
            break;
    }
    [super setTextAlignment:textAlignment];
}
@end
