package atlas;

import java.awt.Image;
import java.awt.image.BufferedImage;

import com.jukka.Rect;

/**
 * @author pengyangjin
 * @version 1.0.0
 * */
public class PackContent {

	public String name;

	public int x;
	public int y;

	public int width;
	public int height;

	public int pivotX;
	public int pivotY;

	public int frameX;
	public int frameY;
	
	public int frameWidth;
	public int frameHeight;

	public Image image;

	public PackContent(String name, int width, int height, int frameX, int frameY, int frameWidth, int frameHeight,
			Image image) {
		this.name = name;
		this.x = 0;
		this.y = 0;
		this.width = width;
		this.height = height;
		
		this.frameX=frameX;
		this.frameY=frameY;
		this.frameWidth=frameWidth;
		this.frameHeight=frameHeight;
		this.image = image;
		
		
	//	System.out.println(frameWidth + " " + frameHeight   + "  ") ;
	}
	
	public static PackContent create(String name,BufferedImage srcImage)
	{
		PackContent imageData = null;
		Rect srcImageFrameRect = getOpaqueRegion(srcImage);
		
		int srcImageWidth = srcImage.getWidth();
		int srcImageHeight = srcImage.getHeight();
	
		BufferedImage subImage ;
		try {
		 subImage = srcImage.getSubimage(srcImageFrameRect.x, srcImageFrameRect.y,
				srcImageFrameRect.width, srcImageFrameRect.height);
			
			imageData = new PackContent(name, srcImageWidth, srcImageHeight, srcImageFrameRect.x,
					srcImageFrameRect.y, srcImageFrameRect.width, srcImageFrameRect.height, subImage);
		}catch(Exception ex)
		{
			System.out.println(ex.toString());
		}
	
		return imageData;
	}
	
	public static Rect getOpaqueRegion(BufferedImage srcImage)
	{
		int x1, y1, x2, y2;// 左上角，右下角
		
		int w = srcImage.getWidth();
		int h = srcImage.getHeight();
		x1 = w;
		y1 = h;
		x2 = 0;
		y2 = 0;

		for (int i = 0; i < w; i++) {
			for (int j = 0; j < h; j++) {
				int argb=srcImage.getRGB(i, j);
				//int trans = ;
				int a = (argb >> 24)&0xFF;
				
				if(a != 0 )
				{
					if(i<x1) x1=i;
					if(j<y1) y1=j;
					if(i>x2) x2=i;
					if(j>y2) y2=j;
				}
			}
		}
		
		//如果是一张全透明的图片 那么就手动设置成像素为1的图片
		if(x2 ==0 || y2 == 0 )
		{
			x1=x2=y1=y2=0;
		}
		
		return new Rect(x1, y1, x2-x1+1, y2-y1+1);
	}
}
