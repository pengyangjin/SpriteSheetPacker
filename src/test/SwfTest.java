package test;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.List;

import com.jpexs.decompiler.flash.helpers.ImageHelper;
import com.jpexs.decompiler.flash.tags.enums.ImageFormat;

import atlas.PackContent;
import utils.SwfExport;

public class SwfTest {

	public static void main(String[] args) throws Exception {
		/*
		File f = new File("C:\\Users\\yangjin1\\Desktop\\texture\\global\\tutorial\\sheets\\arrow.swf");
		String outdir = "C:\\Users\\yangjin1\\Desktop\\123";

		FileInputStream fis;

		fis = new FileInputStream(f);
		SWF swf = new SWF(fis, true);

		for (Tag tag : swf.getTags()) {
			if (tag instanceof DefineSpriteTag) {
				String cName = ((DefineSpriteTag) tag).getClassName();
				System.out.println(cName);
			}
		}
		*/
		
		File f = new File("C:\\Users\\yangjin1\\Desktop\\texture\\world\\sheets\\zh_CN\\reset.swf");
		String outdir = "C:\\Users\\yangjin1\\Desktop\\123";
		
		List<PackContent> listFiles=SwfExport.exportFramesFromFile(f);
		for (PackContent imageFile : listFiles) {
			File file = new File(outdir + File.separator + imageFile.name + ".png");
			try {
				ImageHelper.write((BufferedImage) imageFile.image, ImageFormat.PNG, file);
			} catch (IOException e) {
				e.printStackTrace();
			}

		}
	}

}
