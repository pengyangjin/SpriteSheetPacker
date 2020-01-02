package com.itlwc.ant.tools.extend;
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
import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Task;

import atlas.PackContent;
import atlas.SpriteAtlas;
import utils.SwfExport;

/**
 * @author pengyangjin
 * @version 1.0.0
 * */
public class SpriteSheetPackerTask extends Task {

	private String source;
	public String getSource() {
		return source;
	}

	public void setSource(String source) {
		this.source = source;
	}

	public String getDist() {
		return dist;
	}

	public void setDist(String dist) {
		this.dist = dist;
	}

	private String dist;

	@Override
	public void execute() throws BuildException {
		super.execute();
		try {
		//	File dir = new File(source);
		//	for (File file : dir.listFiles()) {
				File file=new File(source);
				
				if (!file.exists())
					throw new Exception("source is not exists.");

				if (!file.isDirectory())
					throw new Exception("source is not directory.");

				System.out.println(file.getName());

				Collection<File> files = FileUtils.listFiles(file, new String[] { "png", "jpg", "swf" }, true);

				List<SpriteAtlas> sheets = new ArrayList<>();
				List<PackContent> imageList = new ArrayList<>();
				PackContent imageData;
				for (File f : files) {
					if (f.getPath().contains("en_US") || f.getPath().contains("ja_JP") || f.getPath().contains("ko_KR")
							|| f.getPath().contains("my_ZG") || f.getPath().contains("th_TH")
							|| f.getPath().contains("vi_VN") || f.getPath().contains("zh_TW"))
						continue;

					if (f.getName().toLowerCase().contains(".swf")) {
						List<PackContent> ss = SwfExport.exportFramesFromFile(f);
						imageList.addAll(ss);
					} else {

						BufferedImage srcImage = ImageIO.read(f);
						String fname = f.getName();
						String bname = fname.substring(0, fname.lastIndexOf("."));
						imageData = PackContent.create(bname, srcImage);
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

				int index = 0;
				for (SpriteAtlas sheet : sheets) {

					BufferedImage bufferedImage = new BufferedImage(sheet.getQuadSize(), sheet.getQuadSize(),
							BufferedImage.TYPE_INT_ARGB);
					Graphics graphics = bufferedImage.getGraphics();

					StringBuilder sTextureAtlas = new StringBuilder();

					for (PackContent image : sheet.items) {
						graphics.drawImage(image.image, image.x, image.y, image.frameWidth, image.frameHeight, null);

						String sSubTexture = "<SubTexture name=\"" + image.name + "\" x=\"" + image.x + "\" y=\""
								+ image.y + "\" width=\"" + image.width + "\" height=\"" + image.height + "\" pivotX=\""
								+ image.pivotX + "\" pivotY=\"" + image.pivotX + "\"/>";

						sTextureAtlas.append(sSubTexture);
						sTextureAtlas.append("\r\n");
					}

					File outDir = new File(dist + "/" + file.getName());
					if (!outDir.exists()) {
						outDir.mkdirs();
					}

					String ss;
					if (index == 0) {
						ss = "";
					} else {
						ss = "" + index;
					}

					// 生成图片
					ImageIO.write(bufferedImage, "png",
							new File(dist + "/" + file.getName() + "\\" + file.getName() + "" + ss + ".png"));

					StringBuilder sSheetPacket = new StringBuilder();

					String s = "<SheetPacket imagePath=\"" + file.getName() + "" + ss + ".png"
							+ "\" scaleFactor=\"1\">";
					sSheetPacket.append(s);
					sSheetPacket.append("\r\n");

					s = "<TextureAtlas imagePath=\"" + file.getName() + "" + ss + ".png" + "\" scaleFactor=\"1\">";
					sSheetPacket.append(s);
					sSheetPacket.append("\r\n");

					sSheetPacket.append(sTextureAtlas.toString());

					sSheetPacket.append("</TextureAtlas>");
					sSheetPacket.append("\r\n");
					sSheetPacket.append("</SheetPacket>");

					// 生成配置文件

					FileUtils.write(new File(dist + "/" + file.getName() + "\\" + file.getName() + "" + ss + ".xml"),
							sSheetPacket.toString(), "utf-8", false);

					// 进行atf生成
				//	String inPng = dist + "/" + file.getName() + "\\" + file.getName() + "" + ss + ".png";
				//	String inXmlStr = sSheetPacket.toString();
				//	String out = dist + "/" + file.getName() + "\\" + file.getName() + "" + ss + ".atf";
				//	 AtfUtils.generate(png2atf,inPng, out);

					// 写入配置信息
					index++;
				}
		//	}
		} catch (Exception e) {
			System.out.println(e.toString());
		}
	}
}
