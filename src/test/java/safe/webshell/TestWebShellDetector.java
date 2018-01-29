package safe.webshell;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import org.junit.Assert;
import org.junit.Test;

import safe.webshell.WebShellDetector;

/**
 * 测试  WebShell 检测
 * @author mindw
 */
public class TestWebShellDetector {

	@Test
	public void testDetectGood() throws IOException {
		// 无害内容，检测结果应该是 false
		detect("data\\webshell\\test\\good.txt", false);
	}

	@Test
	public void testDetectWebShell() throws IOException {
		// 有害内容，检测结果应该是 true
		detect("data\\webshell\\test\\webshell.txt", true);
	}

	/**
	 * 测试文件中的代码检测结果是否符合预期
	 * @param filePath 文件地址
	 * @param isWebShell 是否是恶意代码
	 * @throws IOException
	 */
	public void detect(String filePath, boolean isWebShell) throws IOException {
		
		File file = new File(filePath);
		FileReader reader = new FileReader(file);
		BufferedReader bufferedReader = new BufferedReader(reader);
		
		String postData = null;
		int lineNum = 0;
		while ((postData = bufferedReader.readLine()) != null) {
			lineNum++;
			String msg = String.format("error at %s in line %d : the decotor should be %s !", filePath,lineNum,isWebShell);
			Assert.assertEquals(msg,isWebShell, WebShellDetector.isWebShell(postData));		
		}
		bufferedReader.close();
	}
}
