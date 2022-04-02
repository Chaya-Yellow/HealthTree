//
//  UIImage+Omesoft.h
//  
//
//  Created by sincan on 12-9-5.
//  Copyright (c) 2012年 Omesoft. All rights reserved.
//

#import <UIKit/UIKit.h>


@interface UIImage (Omesoft)
+ (id) createRoundedRectImage:(UIImage*)image size:(CGSize)size;
+ (UIImage *)createRoundImageWithColor:(UIColor *)color;
+ (UIImage *)createImageWithColor:(UIColor *)color;
+ (UIImage *)gradientImageWithStartColor:(UIColor *)sColor endColor:(UIColor *)eColor height:(float)height;
+ (UIImage *)addHalfOfBlackMaskWithImageName:(NSString *)imageName;
+ (UIImage*)addHalfOfBlackMaskWithImage:(UIImage*)startImage;
+ (UIImage *)scaleImage:(UIImage *)img ToSize:(CGSize)size;
+ (UIImage *)barButtonItemImageWithIcon:(UIImage *)iconImage title:(NSString *)title;
- (UIImage*)addMaskImage:(UIImage*)startImage;
- (UIImage *)addImgeToUpper:(UIImage *)uperImage;
- (UIImage *)addImagToFrameImage:(UIImage *)img rect:(CGRect)rect;
- (UIImage*)fillColor:(UIColor *)fillColor backgroundColor:(UIColor*)bgColor;
- (UIColor *)colorAtPixel:(CGPoint)point;
+ (UIImage *)roundImageWithDiameter:(float)px;
- (UIImage*)addRound;
- (UIImage *)roundImage;
- (UIImage *)addRoundBoardWithColor:(UIColor*)color pxWidth:(float)pxWidth;
@end
