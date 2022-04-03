

#import "OMEImageView.h"
#import "EGOImageLoader.h"
#import "OMESoft.h"
#import "Constants.h"
@implementation OMEImageView
@synthesize imageName = _imageName, placeholderImage;
@synthesize memberId = _memberId;
@synthesize currentImageURL = _currentImageURL;

- (id)initWithPlaceholderImage:(UIImage*)anImage {
	if((self = [super initWithImage:anImage])) {
		self.placeholderImage = anImage;
	}
	return self;
}

#pragma mark - Private Methods
- (UIImage *)getFrameToImage:(UIImage *)image
{
    if (image) {
        return [self addRoundBoardWithImage:image];
    }
    return image;

}

- (UIImage *)addRoundBoardWithImage:(UIImage *)image
{
    //    UIImage *image = [aImage roundImage];
    float scale = [[UIScreen mainScreen] scale];
    float pxWidth = 1.0 * scale;
    float px = MIN(CGImageGetWidth(image.CGImage), CGImageGetHeight(image.CGImage));
    CGRect imageRect = CGRectMake(0, 0, px, px);
    CGRect boardRect = CGRectMake(pxWidth/2.0, pxWidth/2.0, px-pxWidth, px-pxWidth);
    CGContextRef context = CGBitmapContextCreate(NULL, imageRect.size.width, imageRect.size.height, 8, 0, CGImageGetColorSpace(image.CGImage), (CGBitmapInfo)kCGImageAlphaPremultipliedLast);
    
    CGContextSaveGState(context);
    CGContextAddEllipseInRect(context, boardRect);
    CGContextClip(context);
    CGContextDrawImage(context, CGRectMake(-(CGImageGetWidth(image.CGImage) - px) / 2.0, - (CGImageGetHeight(image.CGImage) - px) / 2.0, CGImageGetWidth(image.CGImage), CGImageGetHeight(image.CGImage)), image.CGImage);
    CGContextRestoreGState(context);
    
    CGContextSaveGState(context);
    
    CGContextSetStrokeColorWithColor(context, [UIColor whiteColor].CGColor);
    CGContextSetFillColorWithColor(context, [UIColor whiteColor].CGColor);
    CGContextSetLineWidth(context, pxWidth);
    CGContextStrokeEllipseInRect(context, boardRect);
    CGContextRestoreGState(context);
    
    //    UIImage *theImage = UIGraphicsGetImageFromCurrentImageContext();
    CGImageRef newCGImage = CGBitmapContextCreateImage(context);
    UIImage* newImage = [UIImage imageWithCGImage:newCGImage scale:image.scale orientation:image.imageOrientation];
    CGImageRelease(newCGImage);
    
    CGContextRelease(context);
    //     UIGraphicsEndImageContext();
    return newImage;
    
}

#pragma mark - Set Methods
- (void)setImageName:(NSString *)name
{
//    NSLog(@"name = %@", name);
    _imageName = name;
    if(_imageName)
    {
        if(_imageName.length >60)
        {
            NSRange pos = [_imageName rangeOfString:@"/" options:NSBackwardsSearch];
            NSString* imageName = [_imageName substringFromIndex:pos.location+1];
            _imageName = imageName;
        }
    }
//    NSLog(@".................>>>>>>>>>>>>>>>>>>>>>>>imagename = %@, imageID=%d", _imageName, _memberId);
    if ( _imageName.length != 0 && _memberId > 0) {
        //NSLog(@"----------------------------line=%i", __LINE__);
        [self cancelImageLoad];
    }
    [self beginLoadImage];
}

- (void)setMemberId:(NSUInteger)member
{
    _memberId = member;
    if ( _imageName.length != 0 && _memberId > 0) {
        [self cancelImageLoad];
    }
    [self beginLoadImage];
}


- (void)setCurrentImageURL:(NSURL *)url
{
    _currentImageURL = url;
    [self beginLoadImage];
}
- (void)beginLoadImage
{
    //_imageName = @"641810_20160108113150_66839.jpg";
//    NSLog(@"url=%@",_currentImageURL);
    if (_currentImageURL) {
        [self setImageURL:_currentImageURL];
        return;
    }
    if ((_imageName == nil || _imageName.length == 0) || (_memberId <= 0)) {
        self.image = self.placeholderImage;
        return;
    }
    NSString *imagePath = [imageDirectory() stringByAppendingPathComponent:_imageName];
    NSFileManager *fileManager = [NSFileManager defaultManager];
    BOOL isExist = [fileManager fileExistsAtPath:imagePath];
//    NSLog(@"currentImageURL=%@, imageName =%@, imagePath=%@, isExist=%i",_currentImageURL, _imageName, imagePath,isExist);
    if (isExist) {
        NSData *imageData = [NSData dataWithContentsOfFile:imagePath];
        self.image = [self getFrameToImage:[UIImage imageWithData:imageData]];
    } else {
        if (_memberId) {
            NSString *serverPath = imageServerPath();
            NSURL *imageNameUrl = [NSURL URLWithString:[serverPath stringByAppendingString:[NSString stringWithFormat:@"/%@",_imageName]]];
//            NSLog(@"imageURL = %@", imageNameUrl);
            [self setImageURL:imageNameUrl];
        }
    }
}

- (void)setImageURL:(NSURL *)aURL {
//    NSLog(@"imageURL = %@, aURl = %@", imageURL,aURL);
	if(imageURL) {
		[[EGOImageLoader sharedImageLoader] removeObserver:self forURL:imageURL];
		imageURL = nil;
	}
	
	if(!aURL) {
        self.image = self.placeholderImage;
		imageURL = nil;
		return;
	} else {
		imageURL = aURL;
	}
    
	[[EGOImageLoader sharedImageLoader] removeObserver:self];
	UIImage* anImage = [[EGOImageLoader sharedImageLoader] imageForURL:aURL shouldLoadWithObserver:self];
//    NSLog(@"--------");
	if(anImage) {
//        NSLog(@"+++");
        self.image = [self getFrameToImage:anImage];
		// trigger the delegate callback if the image was found in the cache
	} else {
//        NSLog(@"---");
        self.image = self.placeholderImage;
	}
}

- (void)setPlaceholderImage:(UIImage *)image
{
    placeholderImage = image;
    self.image = placeholderImage;
}
- (void)setImage:(UIImage *)i
{
    if (i == nil) {
        [super setImage:nil];
        return;
    }
    [super setImage:i];
}

#pragma mark -
#pragma mark Image loading

- (void)cancelImageLoad {
	[[EGOImageLoader sharedImageLoader] cancelLoadForURL:imageURL];
	[[EGOImageLoader sharedImageLoader] removeObserver:self forURL:imageURL];
}

- (void)imageLoaderDidLoad:(NSNotification*)notification {
	if(![[[notification userInfo] objectForKey:@"imageURL"] isEqual:imageURL]) return;
    
	UIImage* anImage = [[notification userInfo] objectForKey:@"image"];
    self.image = [self getFrameToImage:anImage];
	[self setNeedsDisplay];
	
}

- (void)imageLoaderDidFailToLoad:(NSNotification*)notification {
	if(![[[notification userInfo] objectForKey:@"imageURL"] isEqual:imageURL]) return;
	
}

#pragma mark -
- (void)dealloc {
    [self cancelImageLoad];
	[[EGOImageLoader sharedImageLoader] removeObserver:self];
	self.imageURL = nil;
	self.placeholderImage = nil;
}



@end
