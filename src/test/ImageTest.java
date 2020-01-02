package test;

import java.awt.image.BufferedImage;
import java.io.File;

import javax.imageio.ImageIO;

import com.jukka.Rect;

import atlas.PackContent;

public class ImageTest {

	public static void main(String[] args) throws Exception {
		File f = new File("C:\\Users\\yangjin1\\Desktop\\texture\\global\\sheets\\liaoyuan\\liaoyuan_012.png");
		BufferedImage srcImage = ImageIO.read(f);
		Rect rect = PackContent.getOpaqueRegion(srcImage);
		BufferedImage out = srcImage.getSubimage(rect.x, rect.y, rect.width, rect.height);
		ImageIO.write(out, "png", new File("C:\\Users\\yangjin1\\Desktop\\out.png"));
		System.out.println("ok");
	}

}
