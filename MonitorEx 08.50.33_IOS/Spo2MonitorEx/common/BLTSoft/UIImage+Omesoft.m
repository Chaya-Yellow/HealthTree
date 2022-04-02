//
//  UIImage+Omesoft.m
//  
//
//  Created by sincan on 12-9-5.
//  Copyright (c) 2012年 Omesoft. All rights reserved.
//

#import "UIImage+Omesoft.h"
#import "OMESoft.h"

@implementation UIImage (Omesoft)
static void addRoundedRectToPath(CGContextRef context, CGRect rect, float ovalWidth,
                                 float ovalHeight)
{
    float fw, fh;
    if (ovalWidth == 0 || ovalHeight == 0) {
        CGContextAddRect(context, rect);
        return;
    }
    
    CGContextSaveGState(context);
    CGContextTranslateCTM(context, CGRectGetMinX(rect), CGRectGetMinY(rect));
    CGContextScaleCTM(context, ovalWidth, ovalHeight);
    fw = CGRectGetWidth(rect) / ovalWidth;
    fh = CGRectGetHeight(rect) / ovalHeight;
    
    CGContextMoveToPoint(context, fw, fh/2);  // Start at lower right corner
    CGContextAddArcToPoint(context, fw, fh, fw/2, fh, 1);  // Top right corner
    CGContextAddArcToPoint(context, 0, fh, 0, fh/2, 1); // Top left corner
    CGContextAddArcToPoint(context, 0, 0, fw/2, 0, 1); // Lower left corner
    CGContextAddArcToPoint(context, fw, 0, fw, fh/2, 1); // Back to lower right
    
    CGContextClosePath(context);
    CGContextRestoreGState(context);
}


+ (id) createRoundedRectImage:(UIImage*)image size:(CGSize)size
{
    // the size of CGContextRef
    int w = size.width;
    int h = size.height;
    
    UIImage *img = image;
    CGColorSpaceRef colorSpace = CGColorSpaceCreateDeviceRGB();
    CGContextRef context = CGBitmapContextCreate(NULL, w, h, 8, 4 * w, colorSpace, (CGBitmapInfo)(CGBitmapInfo)kCGImageAlphaPremultipliedLast);
    CGRect rect = CGRectMake(0, 0, w, h);
    
    CGContextBeginPath(context);
    addRoundedRectToPath(context, rect, 10, 10);
    CGContextClosePath(context);
    CGContextClip(context);
    CGContextDrawImage(context, CGRectMake(0, 0, w, h), img.CGImage);
    CGImageRef imageMasked = CGBitmapContextCreateImage(context);
    UIImage *resultImage = [UIImage imageWithCGImage:imageMasked];
    CGImageRelease(imageMasked);
    CGContextRelease(context);
    CGColorSpaceRelease(colorSpace);
    return resultImage;
}

+ (UIImage *)createRoundImageWithColor:(UIColor *)color
{
    CGRect rect;
    if (UIGraphicsBeginImageContextWithOptions != NULL) {
        rect = CGRectMake(0.0f, 0.0f, 32, 32);
    }else{
        rect = CGRectMake(0.0f, 0.0f, 16, 16);
    }
    
    UIGraphicsBeginImageContext(rect.size);
    CGContextRef context = UIGraphicsGetCurrentContext();
    CGContextSetFillColorWithColor(context, [color CGColor]);
    //    CGContextFillRect(context, rect);
    CGContextFillEllipseInRect(context, rect);
    UIImage *theImage = UIGraphicsGetImageFromCurrentImageContext();
    UIGraphicsEndImageContext();
    return theImage;
}

+ (UIImage *)createImageWithColor:(UIColor *)color
{
    CGRect rect;
    if (UIGraphicsBeginImageContextWithOptions != NULL) {
        rect = CGRectMake(0.0f, 0.0f, 32, 32);
    }else{
        rect = CGRectMake(0.0f, 0.0f, 16, 16);
    }
//    rect = CGRectMake(0.0f, 0.0f, 320, 64);
    UIGraphicsBeginImageContext(rect.size);
    CGContextRef context = UIGraphicsGetCurrentContext();
    CGContextSetFillColorWithColor(context, [color CGColor]);
    //    CGContextFillRect(context, rect);
    CGContextFillRect(context, rect);
    UIImage *theImage = UIGraphicsGetImageFromCurrentImageContext();
    UIGraphicsEndImageContext();
    return theImage;
}

+ (UIImage *)gradientImageWithStartColor:(UIColor *)sColor endColor:(UIColor *)eColor height:(float)height
{
    CGRect rect = CGRectMake(0, 0, height, height);
    
    UIGraphicsBeginImageContext(rect.size);
    CGContextRef context = UIGraphicsGetCurrentContext();
    UIColor *colors[2] = {sColor,eColor};
    CGColorSpaceRef rgb = CGColorSpaceCreateDeviceRGB();
    CGFloat colorComponents [8];
    for (int i = 0; i < 2; i++) {
        UIColor *color = colors[i];
        CGColorRef colorRef = color.CGColor;
        const CGFloat *components = CGColorGetComponents(colorRef);
        for (int j = 0; j < 4; j++) {
            colorComponents[i * 4 + j] = components[j];
        }
    }
    CGGradientRef gradient = CGGradientCreateWithColorComponents(rgb, colorComponents, NULL, sizeof(colorComponents)/(sizeof(colorComponents[0])*4));
    CGPoint startY = CGPointMake(rect.origin.x, rect.origin.y);
    CGPoint endY = CGPointMake(rect.origin.x, rect.origin.y + rect.size.height);
    CGContextDrawLinearGradient(context, gradient, startY, endY, 0);
    CGColorSpaceRelease(rgb);
    CGGradientRelease(gradient);
    
    UIImage *theImage = UIGraphicsGetImageFromCurrentImageContext();
    UIGraphicsEndImageContext();
    return theImage;
}


+(UIImage*)addHalfOfBlackMaskWithImage:(UIImage*)startImage{
    // Create the proper sized rect
    CGRect imageRect = CGRectMake(0, 0, CGImageGetWidth(startImage.CGImage), CGImageGetHeight(startImage.CGImage));
    
    // Create a new bitmap context
    CGContextRef context = CGBitmapContextCreate(NULL, imageRect.size.width, imageRect.size.height, 8, 0, CGImageGetColorSpace(startImage.CGImage), (CGBitmapInfo)(CGBitmapInfo)kCGImageAlphaPremultipliedLast);

//    CGContextSetRGBFillColor(context, 0, 0, 0, 0.5);
//    CGContextFillRect(context, imageRect);
    CGContextDrawImage(context, imageRect, startImage.CGImage);
    // Use the passed in image as a clipping mask
    CGContextClipToMask(context, imageRect, startImage.CGImage);
    // Set the fill color to black: R:0 G:0 B:0 alpha:1
    CGContextSetRGBFillColor(context, 0, 0, 0, 0.5);
    // Fill with black
    CGContextFillRect(context, imageRect);
    
    // Generate a new image
    CGImageRef newCGImage = CGBitmapContextCreateImage(context);
    UIImage* newImage = [UIImage imageWithCGImage:newCGImage scale:startImage.scale orientation:startImage.imageOrientation];
    
    // Cleanup
    CGContextRelease(context);
    CGImageRelease(newCGImage);
    return newImage;
}

+ (UIImage *)addHalfOfBlackMaskWithImageName:(NSString *)imageName
{
    return [self addHalfOfBlackMaskWithImage:[self imageNamed:imageName]];
}

+ (UIImage *)scaleImage:(UIImage *)img ToSize:(CGSize)size{
    UIGraphicsBeginImageContext(size);
    // 绘制改变大小的图片
    [img drawInRect:CGRectMake(0, 0, size.width, size.height)];
    // 从当前context中创建一个改变大小后的图片
    UIImage* scaledImage = UIGraphicsGetImageFromCurrentImageContext();
    UIGraphicsEndImageContext();
    return scaledImage;
}

+ (UIImage *)barButtonItemImageWithIcon:(UIImage *)iconImage title:(NSString *)title
{
    CGRect imageRect;
    UIFont *font;
    if (UIGraphicsBeginImageContextWithOptions != NULL) {
        imageRect = CGRectMake(0.0f, 0.0f, 36, 36);
        font = [UIFont systemFontOfSize:18.0f];
    }else{
        imageRect = CGRectMake(0.0f, 0.0f, 28, 28);
        font = [UIFont systemFontOfSize:12.0f];
    }
    
    CGSize titleSize = [title sizeWithFont:font constrainedToSize:CGSizeMake(100, 15)];
    CGRect rect = CGRectMake(0, 0, imageRect.size.width + 5 + titleSize.width, imageRect.size.height);
    
    
    UIGraphicsBeginImageContext(rect.size);
    CGContextRef context = UIGraphicsGetCurrentContext();
    CGContextSetAllowsAntialiasing(context, TRUE);
    CGContextSetFillColorWithColor(context, [[UIColor whiteColor] CGColor]);
    //    CGContextFillRect(context, rect);
    [title drawAtPoint:CGPointMake(imageRect.size.width + 5, (imageRect.size.height - titleSize.height) / 2) forWidth:titleSize.width withFont:font lineBreakMode:NSLineBreakByWordWrapping];
    CGContextSaveGState(context);
    CGContextTranslateCTM(context, 0, rect.size.height);
    CGContextScaleCTM(context, 1.0, -1.0);
    CGContextDrawImage(context, imageRect, iconImage.CGImage);
    CGContextRestoreGState(context);
//    CGContextFillRect(context, rect);
    UIImage *theImage = UIGraphicsGetImageFromCurrentImageContext();
    UIGraphicsEndImageContext();
    return theImage;
    
    
    
    
//    // Create a new bitmap context
//    CGContextRef context = CGBitmapContextCreate(NULL, imageRect.size.width + 5 + titleSize.width, imageRect.size.height, 8, 0, CGImageGetColorSpace(iconImage.CGImage), (CGBitmapInfo)kCGImageAlphaPremultipliedLast);
//    
//    //    CGContextSetRGBFillColor(context, 0, 0, 0, 0.5);
//    //    CGContextFillRect(context, imageRect);
//    
//    // Use the passed in image as a clipping mask
//    [[UIColor whiteColor] set];
//    const char* p = [title cStringUsingEncoding:NSASCIIStringEncoding];
//    
//    CGContextSelectFont(context, "Helvetica", 12.0f, kCGEncodingMacRoman);
////
//    CGContextShowTextAtPoint(context, imageRect.size.width + 5,0, p, strlen(p));
//    CGContextSetTextDrawingMode(context, kCGTextFill);
////    CGContextClipToMask(context, imageRect, startImage.CGImage);
////    // Set the fill color to black: R:0 G:0 B:0 alpha:1
////    CGContextSetRGBFillColor(context, 0, 0, 0, 0.5);
////    // Fill with black
////    CGContextFillRect(context, imageRect);
//    
//    // Generate a new image
//    CGImageRef newCGImage = CGBitmapContextCreateImage(context);
//    UIImage* newImage = [UIImage imageWithCGImage:newCGImage scale:iconImage.scale orientation:iconImage.imageOrientation];
//    
//    // Cleanup
//    CGContextRelease(context);
//    CGImageRelease(newCGImage);
//    return newImage;
}

- (UIImage*)addMaskImage:(UIImage*)startImage
{
    CGRect imageRect = CGRectMake(0, 0, CGImageGetWidth(self.CGImage), CGImageGetHeight(self.CGImage));
    // Create a new bitmap context
    CGContextRef context = CGBitmapContextCreate(NULL, imageRect.size.width, imageRect.size.height, 8, 0, CGImageGetColorSpace(self.CGImage), (CGBitmapInfo)kCGImageAlphaPremultipliedLast);
    CGContextClipToMask(context, imageRect, startImage.CGImage);
    CGContextDrawImage(context, imageRect, self.CGImage);

    // Generate a new image
    CGImageRef newCGImage = CGBitmapContextCreateImage(context);
    UIImage* newImage = [UIImage imageWithCGImage:newCGImage scale:startImage.scale orientation:startImage.imageOrientation];
    
    // Cleanup
    CGContextRelease(context);
    CGImageRelease(newCGImage);
    return newImage;
}


+ (UIImage *)roundImageWithDiameter:(float)px
{
    CGRect imageRect = CGRectMake(0, 0, px, px);

    UIGraphicsBeginImageContext(imageRect.size);
    CGContextRef context = UIGraphicsGetCurrentContext();
    [[UIColor whiteColor] set];
    //    CGContextFillRect(context, rect);
   CGContextFillEllipseInRect(context, imageRect);
    UIImage *theImage = UIGraphicsGetImageFromCurrentImageContext();
    UIGraphicsEndImageContext();
    return theImage;
}

- (UIImage *)roundImage
{
    float px = MIN(CGImageGetWidth(self.CGImage), CGImageGetHeight(self.CGImage));
    CGRect imageRect = CGRectMake(0, 0, px, px);
     CGContextRef context = CGBitmapContextCreate(NULL, imageRect.size.width, imageRect.size.height, 8, 0, CGImageGetColorSpace(self.CGImage), (CGBitmapInfo)kCGImageAlphaPremultipliedLast);
    CGContextSetFillColorWithColor(context, [UIColor whiteColor].CGColor);
    CGContextSetStrokeColorWithColor(context, [UIColor whiteColor].CGColor);
    CGContextAddEllipseInRect(context, imageRect);
    CGContextClip(context);
    CGContextDrawImage(context, imageRect, self.CGImage);

    
    CGImageRef newCGImage = CGBitmapContextCreateImage(context);
    UIImage* newImage = [UIImage imageWithCGImage:newCGImage scale:self.scale orientation:self.imageOrientation];
    CGContextRelease(context);
     CGImageRelease(newCGImage);
    return newImage;
}

- (UIImage *)addRoundBoardWithColor:(UIColor*)color pxWidth:(float)pxWidth
{

    float px = MIN(CGImageGetWidth(self.CGImage), CGImageGetHeight(self.CGImage));
    CGRect imageRect = CGRectMake(0, 0, px, px);
    CGRect boardRect = CGRectMake(pxWidth/2.0, pxWidth/2.0, px-pxWidth, px-pxWidth);
    
    UIGraphicsBeginImageContext(imageRect.size);
    CGContextRef context = UIGraphicsGetCurrentContext();
    CGContextDrawImage(context, imageRect, self.CGImage);
    [color set];
    CGContextSetLineWidth(context, pxWidth);
    CGContextStrokeEllipseInRect(context, boardRect);

    
    UIImage *theImage = UIGraphicsGetImageFromCurrentImageContext();
    UIGraphicsEndImageContext();
    return theImage;

}

- (UIImage*)addRound
{
    float px = CGImageGetWidth(self.CGImage) > CGImageGetHeight(self.CGImage) ? CGImageGetHeight(self.CGImage) : CGImageGetWidth(self.CGImage) ;
    CGRect imageRect = CGRectMake(0, 0, px,px);
    // Create a new bitmap context
    CGContextRef context = CGBitmapContextCreate(NULL, imageRect.size.width, imageRect.size.height, 8, 0, CGImageGetColorSpace(self.CGImage), (CGBitmapInfo)kCGImageAlphaPremultipliedLast);
     [[UIColor blackColor] set];
    CGContextFillEllipseInRect(context, imageRect);
    CGContextClipToMask(context, imageRect, UIGraphicsGetImageFromCurrentImageContext().CGImage);
//    CGContextDrawImage(context, imageRect, self.CGImage);
    
    // Generate a new image
    CGImageRef newCGImage = CGBitmapContextCreateImage(context);
    UIImage* newImage = [UIImage imageWithCGImage:newCGImage scale:self.scale orientation:self.imageOrientation];
    
    // Cleanup
    CGContextRelease(context);
    CGImageRelease(newCGImage);
    return newImage;
}

- (UIImage *)addImgeToUpper:(UIImage *)uperImage
{
    CGRect imageRect = CGRectMake(0, 0, CGImageGetWidth(self.CGImage), CGImageGetHeight(self.CGImage));
    // Create a new bitmap context
    CGContextRef context = CGBitmapContextCreate(NULL, imageRect.size.width, imageRect.size.height, 8, 0, CGImageGetColorSpace(self.CGImage), (CGBitmapInfo)kCGImageAlphaPremultipliedLast);
    CGContextDrawImage(context, CGRectMake(0, 0, CGImageGetWidth(self.CGImage), CGImageGetHeight(self.CGImage)), self.CGImage);
//    [uperImage drawInRect:CGRectMake(0, 0, CGImageGetWidth(uperImage.CGImage), CGImageGetHeight(uperImage.CGImage))];
//    CGContextTranslateCTM(context, 0, CGImageGetHeight(self.CGImage));
//    CGContextScaleCTM(context, 1.0, -1.0);
    CGContextDrawImage(context, imageRect, uperImage.CGImage);
    // Generate a new image
    CGImageRef newCGImage = CGBitmapContextCreateImage(context);
    UIImage* newImage = [UIImage imageWithCGImage:newCGImage scale:uperImage.scale orientation:uperImage.imageOrientation];
    
    // Cleanup
    CGContextRelease(context);
    CGImageRelease(newCGImage);
    return newImage;
}

- (UIImage *)addImagToFrameImage:(UIImage *)img rect:(CGRect)rect
{
//    float scale = [[UIScreen mainScreen] scale];
//    CGRect drwaRect = CGRectMake(0, 0, rect.size.width*scale, rect.size.height*scale);

    CGRect imageRect = CGRectMake(0, 0, CGImageGetWidth(self.CGImage), CGImageGetHeight(self.CGImage));
//    NSLog(@"%f..%f",imageRect.size.width,imageRect.size.height);
//    033358
    // Create a new bitmap context
    CGContextRef context = CGBitmapContextCreate(NULL, imageRect.size.width, imageRect.size.height, 8, 0, CGImageGetColorSpace(self.CGImage), (CGBitmapInfo)kCGImageAlphaPremultipliedLast);
    
    
    CGContextDrawImage(context, imageRect, self.CGImage);
    //    [uperImage drawInRect:CGRectMake(0, 0, CGImageGetWidth(uperImage.CGImage), CGImageGetHeight(uperImage.CGImage))];
    //    CGContextTranslateCTM(context, 0, CGImageGetHeight(self.CGImage));
    //    CGContextScaleCTM(context, 1.0, -1.0);
    CGContextDrawImage(context, imageRect, img.CGImage);
    // Generate a new image
    CGImageRef newCGImage = CGBitmapContextCreateImage(context);
    UIImage* newImage = [UIImage imageWithCGImage:newCGImage scale:img.scale orientation:img.imageOrientation];
    
    // Cleanup
    CGContextRelease(context);
    CGImageRelease(newCGImage);
    return newImage;

}

- (UIImage*) fillColor:(UIColor *)fillColor backgroundColor:(UIColor*)bgColor
{
    // Create the proper sized rect
    CGRect imageRect = CGRectMake(0, 0, CGImageGetWidth(self.CGImage), CGImageGetHeight(self.CGImage));
    
    // Create a new bitmap context
    CGContextRef context = CGBitmapContextCreate(NULL, imageRect.size.width, imageRect.size.height, 8, 0, CGImageGetColorSpace(self.CGImage), (CGBitmapInfo)kCGImageAlphaPremultipliedLast);
    
//    CGContextSetRGBFillColor(context, 1, 1, 1, 0);
    CGContextSetFillColorWithColor(context, bgColor.CGColor);
    CGContextFillRect(context, imageRect);
    
    // Use the passed in image as a clipping mask
    CGContextClipToMask(context, imageRect, self.CGImage);
    // Set the fill color to black: R:0 G:0 B:0 alpha:1
//    CGContextSetRGBFillColor(context, 1, 1, 1, 1);
    CGContextSetFillColorWithColor(context, fillColor.CGColor);

    // Fill with black
    CGContextFillRect(context, imageRect);
    
    // Generate a new image
    CGImageRef newCGImage = CGBitmapContextCreateImage(context);
    UIImage* newImage = [UIImage imageWithCGImage:newCGImage scale:self.scale orientation:self.imageOrientation];
    
    // Cleanup
    CGContextRelease(context);
    CGImageRelease(newCGImage);
    
    return newImage;
}


- (UIColor *)colorAtPixel:(CGPoint)point{
    // Cancel if point is outside image coordinates
    if (!CGRectContainsPoint(CGRectMake(0.0f, 0.0f, CGImageGetWidth(self.CGImage), CGImageGetHeight(self.CGImage)), point)) {
        return nil;
    }
    
    
    // Create a 1x1 pixel byte array and bitmap context to draw the pixel into.
    // Reference: http://stackoverflow.com/questions/1042830/retrieving-a-pixel-alpha-value-for-a-uiimage
    NSInteger pointX = trunc(point.x);
    NSInteger pointY = trunc(point.y);
    CGImageRef cgImage = self.CGImage;
    NSUInteger width = CGImageGetWidth(self.CGImage);
    NSUInteger height = CGImageGetHeight(self.CGImage);
    CGColorSpaceRef colorSpace = CGColorSpaceCreateDeviceRGB();
    int bytesPerPixel = 4;
    int bytesPerRow = bytesPerPixel * 1;
    NSUInteger bitsPerComponent = 8;
    unsigned char pixelData[4] = { 0, 0, 0, 0 };
    CGContextRef context = CGBitmapContextCreate(pixelData,
                                                 1,
                                                 1,
                                                 bitsPerComponent,
                                                 bytesPerRow,
                                                 colorSpace,
                                                 (CGBitmapInfo)kCGImageAlphaPremultipliedLast | kCGBitmapByteOrder32Big);
    CGColorSpaceRelease(colorSpace);
    CGContextSetBlendMode(context, kCGBlendModeCopy);
    
    // Draw the pixel we are interested in onto the bitmap context
    CGContextTranslateCTM(context, -pointX, pointY-(CGFloat)height);
    CGContextDrawImage(context, CGRectMake(0.0f, 0.0f, (CGFloat)width, (CGFloat)height), cgImage);
    CGContextRelease(context);
    
    // Convert color values [0..255] to floats [0.0..1.0]
    CGFloat red   = (CGFloat)pixelData[0] / 255.0f;
    CGFloat green = (CGFloat)pixelData[1] / 255.0f;
    CGFloat blue  = (CGFloat)pixelData[2] / 255.0f;
    CGFloat alpha = (CGFloat)pixelData[3] / 255.0f;
    return [UIColor colorWithRed:red green:green blue:blue alpha:alpha];
}
@end
