package utils;

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.dom4j.Attribute;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

/**
 * @author pengyangjin
 * @version 1.0.0
 * */
public class XmlParser {
	public static List<String> parserSubTexture(File f) {
		List<String> result = new ArrayList<String>();
		SAXReader reader = new SAXReader();
		Document document;
		try {
			document = reader.read(f);
			Element rootElm = document.getRootElement();
			Element TextureAtlas = rootElm.element("TextureAtlas");
			List nodes = TextureAtlas.elements("SubTexture");
			for (Iterator it = nodes.iterator(); it.hasNext();) {
				Element elm = (Element) it.next();
				Attribute attr1 = elm.attribute("name");
				result.add(attr1.getData().toString());
			}
		} catch (DocumentException e) {
			e.printStackTrace();
		}

		return result;

	}
}
