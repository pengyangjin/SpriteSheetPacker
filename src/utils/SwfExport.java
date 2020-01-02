package utils;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.jpexs.decompiler.flash.SWF;
import com.jpexs.decompiler.flash.exporters.commonshape.Matrix;
import com.jpexs.decompiler.flash.helpers.ImageHelper;
import com.jpexs.decompiler.flash.tags.DefineSpriteTag;
import com.jpexs.decompiler.flash.tags.Tag;
import com.jpexs.decompiler.flash.tags.base.CharacterTag;
import com.jpexs.decompiler.flash.tags.enums.ImageFormat;
import com.jpexs.decompiler.flash.timeline.Frame;
import com.jpexs.decompiler.flash.timeline.Timeline;
import com.jpexs.decompiler.flash.timeline.Timelined;
import com.jpexs.helpers.Helper;
import com.jpexs.helpers.Path;

import atlas.PackContent;

/**
 * @author pengyangjin
 * @version 1.0.0
 * */
public class SwfExport {
	public static List<PackContent> exportFramesFromFile(File f) {
		List<PackContent> result = new ArrayList<>();
		PackContent imageData;

		FileInputStream fis;

		try {
			fis = new FileInputStream(f);
			SWF swf = new SWF(fis, true);
			for (Tag tag : swf.getTags()) {
				if (tag instanceof DefineSpriteTag) {

					if (((DefineSpriteTag) tag).getClassName() == null) {
						continue;
					}

		//			System.out.println(((DefineSpriteTag) tag).getClassName());

					int containerId = ((DefineSpriteTag) tag).getCharacterId();

					if (swf.getTags().isEmpty()) {
						return result;
					}

					Timeline tim0;
					String cName = "";
					if (containerId == 0) {
						tim0 = swf.getTimeline();
					} else {
						tim0 = ((Timelined) swf.getCharacter(containerId)).getTimeline();
						cName = Helper.makeFileName(swf.getCharacter(containerId).getClassName()) + "_";
					}

					final Timeline tim = tim0;
					
					//因为Flash坑爹的机制 如果子 元件的帧数比主元件的帧多的话用这个库是不能把动画完整的复制下来 所以需要把主元件的时间轴插入帧 使主元件和子元件的帧数一致
				
			//		System.out.println("-->"+tim.getFrameCount());
					
					int cc=calcMaxFrameCount(swf,containerId);
					
			//		System.out.println(containerId + " "+cc);
					Frame oldframe = tim.getFrame(0);
					Frame newFrame;
					for(int ii=tim.getFrameCount();ii<cc;ii++) {
						newFrame=new Frame(oldframe,ii);
						tim.addFrame(newFrame);
					}
					
					
					List<Integer> frames = new ArrayList<>();
					for (Frame frame : tim.getFrames()) {
						frames.add(frame.frame);
					}

					final List<Integer> fframes = frames;

					Color backgroundColor = new Color(0, 0, 0, 0);

					final Color fbackgroundColor = backgroundColor;
					final Iterator<BufferedImage> frameImages = new Iterator<BufferedImage>() {
						private int pos = 0;

						@Override
						public boolean hasNext() {
							return fframes.size() > pos;
						}

						@Override
						public void remove() {
							throw new UnsupportedOperationException();
						}

						@Override
						public BufferedImage next() {
							if (!hasNext()) {
								return null;
							}

							// Tag parentTag = tim.getParentTag();
							// String tagName = parentTag == null ? "" : parentTag.getName();

							int fframe = fframes.get(pos++);
							BufferedImage result = SWF.frameToImageGet(tim, fframe, fframe, null, 0, tim.displayRect,
									new Matrix(), null, fbackgroundColor, 1.0).getBufferedImage();
							return result;
						}
					};

					for (int i = 0; frameImages.hasNext(); i++) {
						final int fi = i;
						String fname = cName + String.format("%05d", fframes.get(fi));
						BufferedImage srcImage = frameImages.next();
						imageData = PackContent.create(fname, srcImage);
						
					//	imageData =new SubTexture(fname, srcImage.getWidth(), srcImage.getHeight(), 0, 0, srcImage.getWidth(), srcImage.getHeight(), srcImage);
						result.add(imageData);
					}

				}
			}
		} catch (Exception ex) {
			System.out.println(ex.toString());
		}

		return result;
	}

	private static int calcMaxFrameCount(SWF swf, int characterId) {
		int maxFrameCount=0;
		CharacterTag ctag;
		Timeline tim0;
		int cId=characterId;
		
		ctag = swf.getCharacter(cId);
		if (ctag.getTagName().equals("DefineSprite")) {
			tim0 = ((Timelined) swf.getCharacter(cId)).getTimeline();
			maxFrameCount = Math.max(maxFrameCount, tim0.getFrameCount());
		}else {
			return maxFrameCount;
		}

		List<Integer> a = findDependentCharacters(swf, characterId);

		while (a.size() > 0) {
			cId = a.remove(0);
			ctag = swf.getCharacter(cId);
			if (ctag!=null &&  ctag.getTagName().equals("DefineSprite")) {
				tim0 = ((Timelined) swf.getCharacter(cId)).getTimeline();
				maxFrameCount = Math.max(maxFrameCount, tim0.getFrameCount());
				List<Integer> a1 = findDependentCharacters(swf, cId);
				
				for(Integer i2 : a1 )
				{
					if(!a.contains(i2))
					{
						a.add(i2);
					}
				}
			}
		}
		return maxFrameCount;
		
	}

	private static List<Integer> findDependentCharacters(SWF swf, int characterId) {
		List<Integer> s = new ArrayList<>();

		Map<Integer, Set<Integer>> set = swf.getDependentCharacters();
		for (Map.Entry<Integer, Set<Integer>> a : set.entrySet()) {
			for (Integer i : a.getValue()) {
				if (i.equals(characterId)) {
					// System.out.println(a.getKey());
					s.add(a.getKey());
				}
			}
		}
		return s;
	}
	
	@SuppressWarnings("unused")
	private static boolean exportFrames(String outdir, final SWF swf, int containerId) {
		if (swf.getTags().isEmpty()) {
			return false;
		}

		Timeline tim0;
		String cName = "";
		if (containerId == 0) {
			tim0 = swf.getTimeline();
		} else {
			tim0 = ((Timelined) swf.getCharacter(containerId)).getTimeline();
			cName = Helper.makeFileName(swf.getCharacter(containerId).getClassName()) + "_";
		}

		final Timeline tim = tim0;

		List<Integer> frames = new ArrayList<>();
		for (Frame frame : tim.getFrames()) {
			frames.add(frame.frame);
		}

		final File foutdir = new File(outdir);
		try {
			Path.createDirectorySafe(foutdir);
		} catch (IOException e) {
			e.printStackTrace();
		}

		final List<Integer> fframes = frames;

		Color backgroundColor = new Color(0, 0, 0, 0);

		final Color fbackgroundColor = backgroundColor;
		final Iterator<BufferedImage> frameImages = new Iterator<BufferedImage>() {
			private int pos = 0;

			@Override
			public boolean hasNext() {
				return fframes.size() > pos;
			}

			@Override
			public void remove() {
				throw new UnsupportedOperationException();
			}

			@Override
			public BufferedImage next() {
				if (!hasNext()) {
					return null;
				}

				// Tag parentTag = tim.getParentTag();
				// String tagName = parentTag == null ? "" : parentTag.getName();

				int fframe = fframes.get(pos++);
				BufferedImage result = SWF.frameToImageGet(tim, fframe, fframe, null, 0, tim.displayRect, new Matrix(),
						null, fbackgroundColor, 1.0).getBufferedImage();
				return result;
			}
		};

		for (int i = 0; frameImages.hasNext(); i++) {
			final int fi = i;
			File file = new File(foutdir + File.separator + cName + String.format("%05d", fframes.get(fi)) + ".png");
			try {
				ImageHelper.write(frameImages.next(), ImageFormat.PNG, file);
			} catch (IOException e) {
				e.printStackTrace();
			}

		}

		return true;
	}
}
