package safe.webshell;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import org.junit.Test;

import safe.webshell.TextChecker;

public class TestTextChecker {

	@Test
	public void testIsExecutable() throws IOException {

		File file = new File("data\\webshell\\train\\webshell.txt");
		FileReader reader = new FileReader(file);
		BufferedReader bufferedReader = new BufferedReader(reader);
		
		String line = null;
		int successNum = 0;
		int failNum = 0;
		while ((line = bufferedReader.readLine()) != null) {
			if (TextChecker.isExecutable(line)) {
				successNum++;
			}else {
				failNum++;
				System.out.println(line);
			}
		}
		bufferedReader.close();
		
		int totalNum = successNum + failNum;
		String msg = String.format("总共 %d 条，检测出 %d 条，未检测出 %d 条！", totalNum,successNum,failNum);
		
		System.out.println(msg);
		if(failNum>0) {
			System.out.println("还需继续努力~");
		}
	}
}
