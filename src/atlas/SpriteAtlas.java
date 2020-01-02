package atlas;

import java.util.List;
import java.util.Vector;

import com.jukka.MaxRectsBinPack;
import com.jukka.Rect;
import com.jukka.RectSize;

/**
 * @author pengyangjin
 * @version 1.0.0
 * */
public class SpriteAtlas {
	private MaxRectsBinPack packer;
	public Vector<PackContent> items;
	private int gap = 0;

	public SpriteAtlas() {
		this.items = new Vector<>();
		this.packer = new MaxRectsBinPack(2048, 2048, false);
	}

	public boolean addImage(PackContent item) {
		Rect rect = packer.Insert(item.frameWidth + gap * 2, item.frameHeight + gap * 2,
				MaxRectsBinPack.FreeRectChoiceHeuristic.RectBestSquareFit);

		if (rect.width == 0) {
			return false;
		} else {
			item.x = rect.x;
			item.y = rect.y;

			items.add(item);
			return true;
		}
	}

	public void addImages(List<PackContent> items) {
		Vector<RectSize> rects = new Vector<>();
		for (PackContent info : items) {
			rects.add(new RectSize(info.frameWidth, info.frameHeight));
			System.out.println(info.frameWidth + " " + info.frameHeight);
		}

		Vector<Rect> dsts = new Vector<>();
		packer.Insert(rects, dsts, MaxRectsBinPack.FreeRectChoiceHeuristic.RectBestSquareFit);

		for (Rect dst : dsts) {
			System.out.println(dst.toString());
		}

		for (int i = 0; i < dsts.size(); i++) {
			items.get(i).x = dsts.get(i).x;
			items.get(i).y = dsts.get(i).y;
		}

		this.items.clear();
		this.items.addAll(items);
	}

	public int getQuadSize() {
		int w = 0;
		int h = 0;
		for (PackContent rect : items) {
			if (w < rect.x + rect.frameWidth) {
				w = rect.x + rect.frameWidth;
			}

			if (h < rect.y + rect.frameHeight) {
				h = rect.y + rect.frameHeight;
			}
		}

		int cavasW = powerOfTwo(w);
		int cavasH = powerOfTwo(h);
		int quadSize = Math.max(cavasW, cavasH);

		return quadSize;

	}

	private int powerOfTwo(int number) {
		if (number > 0 && (number & (number - 1)) == 0) {
			return number;
		} else {
			int result = 1;
			while (result < number) {
				result <<= 1;
			}
			return result;
		}
	}

}
