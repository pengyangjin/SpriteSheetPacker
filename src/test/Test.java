package test;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import javax.imageio.ImageIO;

import org.apache.commons.io.FileUtils;

import atlas.PackContent;
import atlas.SpriteAtlas;
import utils.SwfExport;

public class Test {

	public static void main(String[] args) throws Exception {
		
		List<String> s=new ArrayList<>();
		
		
		File file = new File("C:\\Users\\yangjin1\\Desktop\\texture\\world");
		Collection<File> files = FileUtils.listFiles(file, new String[] { "png", "jpg", "swf" }, true);
		List<SpriteAtlas> sheets = new ArrayList<>();
		List<PackContent> imageList = new ArrayList<>();
		PackContent imageData;
		for (File f : files) {
			if (f.getPath().contains("en_US") || f.getPath().contains("ja_JP") || f.getPath().contains("ko_KR")
					|| f.getPath().contains("my_ZG") || f.getPath().contains("th_TH") || f.getPath().contains("vi_VN")
					|| f.getPath().contains("zh_TW"))
				continue;

			if (f.getName().toLowerCase().contains(".swf")) {
				List<PackContent> ss = SwfExport.exportFramesFromFile(f);
				imageList.addAll(ss);
			} else {
				BufferedImage srcImage = ImageIO.read(f);
				String fname= f.getName();
				String  bname=fname.substring(0,fname.lastIndexOf("."));
				imageData =PackContent.create(bname, srcImage);
				imageList.add(imageData);
			}
		}

		Collections.sort(imageList, new Comparator<PackContent>() {
			@Override
			public int compare(PackContent o1, PackContent o2) {
				if (o2.frameWidth > o1.frameWidth) {
					return 1;
				} else if (o2.frameWidth < o1.frameWidth) {
					return -1;
				} else {
					return 0;
				}
			}
		});

		SpriteAtlas pack = new SpriteAtlas();
		sheets.add(pack);

		while (imageList.size() > 0) {
			PackContent img = imageList.remove(0);
			if (!pack.addImage(img)) {
				pack = new SpriteAtlas();
				sheets.add(pack);
				pack.addImage(img);
			}
		}

		int count =0;
		int index = 0;
		for (SpriteAtlas sheet : sheets) {

			BufferedImage bufferedImage = new BufferedImage(sheet.getQuadSize(), sheet.getQuadSize(),
					BufferedImage.TYPE_INT_ARGB);
			Graphics graphics = bufferedImage.getGraphics();

			for (PackContent image : sheet.items) {
				count++;
				s.add(image.name);
				graphics.drawImage(image.image, image.x, image.y, image.frameWidth, image.frameHeight, null);
			}

			File outDir = new File("C:\\Users\\yangjin1\\Desktop\\out2\\" + file.getName());
			if (!outDir.exists()) {
				outDir.mkdirs();
			}

			String ss;
			if (index == 0) {
				ss = "";
			} else {
				ss = "" + index;
			}

			ImageIO.write(bufferedImage, "png", new File("C:\\Users\\yangjin1\\Desktop\\out2\\" + file.getName() + "\\"
					+ file.getName() + "" + ss + ".png"));

			index++;

		}
		
		
		Collections.sort(s,String.CASE_INSENSITIVE_ORDER);
		
		
		for(String ss : s)
		{
			System.out.println(ss);
		}
	
		
		
	}

}
