package utils;

import java.io.IOException;

import org.apache.commons.exec.CommandLine;
import org.apache.commons.exec.DefaultExecutor;
import org.apache.commons.exec.ExecuteException;

/**
 * @author pengyangjin
 * @version 1.0.0
 * */
public class AtfCreator {
	
//	public static final String png2atf="C:\\yttx\\client\\trunk\\main\\LueDi\\builder\\atfTool\\png2atf.exe";
	public static boolean generate(String png2atf,String in,String out)
	{
		String line = png2atf+" -i "+in+" -o "+out+" -q 0 -n 0,0 -c d -r -4";
		CommandLine cmdLine = CommandLine.parse(line);
		DefaultExecutor executor = new DefaultExecutor();
		try {
			int exitValue = executor.execute(cmdLine);
		} catch (ExecuteException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return true;
	}

}
