package safe.fish.utils;

import org.junit.Assert;
import org.junit.Test;

import safe.fish.PageInfo;
import safe.fish.utils.XnTool;

/**
 * 测试网址中 xn-- 解析
 * @author mindw
 */
public class TestXnTool {

	@Test
	public void testEnCodeAndDecode() {
		try {
			String str = "这是个例子";
			String strChinese = XnTool.decode(XnTool.encode(str));
			Assert.assertEquals(str, strChinese);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Test
	public void testGetChineseInUrl() {
		Assert.assertEquals("京东1",new PageInfo("http://www.xn--1-lp6axr.lnhzlaw.com/?product-115.html?...ka&mp_sourceid=0.1.1","").getChineseInHost());
		Assert.assertEquals("jd欢迎您", new PageInfo("http://www.xn--jd-9j4d569afv8b.lovenancheng.com","").getChineseInHost());
		Assert.assertEquals("京东111", new PageInfo("http://xn--111-w48dtz.coinforpay.com","").getChineseInHost());
	}
}
