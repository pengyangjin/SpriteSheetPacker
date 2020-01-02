package test;

import java.io.File;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.List;

import utils.XmlParser;

public class XmlTest {
	public static void main(String[] args) throws Exception {

		File in = new File("C:\\Users\\yangjin1\\Desktop\\out\\");

		for (File f : in.listFiles()) {
			File[] files=	f.listFiles(new FilenameFilter() {
				
				@Override
				public boolean accept(File dir, String name) {
					if(name.toLowerCase().endsWith(".xml"))
					{
						return true;
					}else {
						return false;
					}
					
				}
			});
			
			List<String> list = new ArrayList<String>();
			for(File f1 : files)
			{
				list.addAll(XmlParser.parserSubTexture(f1));
			}
			
			System.out.println( f .getName() +" "+ list.size());
		}

	}
}
