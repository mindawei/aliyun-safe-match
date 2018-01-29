package safe.fish;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import safe.fish.FishFilter;


/**
 * 测试钓鱼网站判断接口
 * @author mindw
 */
public class TestFishFilter {

	public static final String UTF8 = "utf-8";
	public static final String GB2312 = "gb2312";

	private class TestInfo {
		String url;
		String filePath;
		String charsetName;
		boolean isFish;

		public TestInfo(String url, String filePath, String charsetName, boolean isFish) {
			super();
			this.url = url;
			this.filePath = filePath;
			this.charsetName = charsetName;
			this.isFish = isFish;
		}
	}

	private List<TestInfo> testInfos;

	@Before
	public void init() {
		testInfos = new ArrayList<>();
		String format = "data\\fish\\钓鱼网站测试\\%d.html";
		testInfos.add(new TestInfo("http://l0086-vip.pw", String.format(format, 1), GB2312, true));
		testInfos.add(new TestInfo("http://l0086qtt.com", String.format(format, 2), GB2312, false));
		testInfos.add(new TestInfo("http://www.ta0ba0.ren", String.format(format, 3), UTF8, true));
	}

	@Test
	public void testIsFish() throws IOException {
		
		for (TestInfo testInfo : testInfos) {

			File file = new File(testInfo.filePath);
			InputStreamReader reader = new InputStreamReader(new FileInputStream(file), testInfo.charsetName);
			BufferedReader bufferedReader = new BufferedReader(reader);

			StringBuilder builder = new StringBuilder();

			String htmlLine = null;
			while ((htmlLine = bufferedReader.readLine()) != null) {
				builder.append(htmlLine);
				builder.append("\n");
			}
			String htmlContent = builder.toString();

			bufferedReader.close();

			boolean isFish = FishFilter.isFish(testInfo.url, htmlContent);
			Assert.assertEquals(testInfo.isFish, isFish);

		}
	}
	
}
