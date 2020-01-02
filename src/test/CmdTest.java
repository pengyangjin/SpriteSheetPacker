package test;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;

public class CmdTest {

	public static void main(String[] args) {
		/*
		 * String line =
		 * "C:\\yttx\\client\\trunk\\main\\LueDi\\builder\\atfTool\\png2atf.exe -i C:\\Users\\yangjin1\\Desktop\\out\\global\\global1.png -o C:\\Users\\yangjin1\\Desktop\\out\\global\\global1.atf -q 0 -n 0,0 -c d -r -4"
		 * ; CommandLine cmdLine = CommandLine.parse(line); DefaultExecutor executor =
		 * new DefaultExecutor(); try { int exitValue = executor.execute(cmdLine); }
		 * catch (ExecuteException e) { e.printStackTrace(); } catch (IOException e) {
		 * e.printStackTrace(); }
		 */
		try {

			String atf = "C:\\Users\\yangjin1\\Desktop\\out\\zhulu\\zhulu.atf";
			String xml = "C:\\Users\\yangjin1\\Desktop\\out\\zhulu\\zhulu.xml";
			
			String atf2 = "C:\\Users\\yangjin1\\Desktop\\out\\zhulu\\zhulu2.atf";
			
			byte[] bytesAtf = FileUtils.readFileToByteArray(new File(atf));
			byte[] bytesXml = FileUtils.readFileToByteArray(new File(xml));
			int len = bytesXml.length;

			ByteArrayOutputStream os = new ByteArrayOutputStream();
			os.write(bytesAtf);
			os.write(bytesXml);
			
			os.write((len & 0xff));

			String PKT = "PKT-sunxinzhe";
			os.write(PKT.getBytes());

			FileUtils.writeByteArrayToFile(new File(atf2), os.toByteArray(), false);
		} catch (IOException e) {
			e.printStackTrace();
		}

		System.out.println("1");

	}

}
