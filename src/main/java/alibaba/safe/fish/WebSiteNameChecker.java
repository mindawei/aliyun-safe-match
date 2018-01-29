package alibaba.safe.fish;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import alibaba.safe.fish.FishFilter.Type;
import alibaba.safe.fish.WebPage.PageInfo;
import alibaba.safe.fish.whitelist.ChineseDns;

public class WebSiteNameChecker {

	private static final String EMPTY_STRING = "";

	private static Map<String, String> key2domain = new HashMap<>();
	static {
		key2domain.put("浙网文[2013]0268-027号", "taobao");
		key2domain.put("浙B2-20080224", "taobao");
		key2domain.put("Taobao.com 版权所有", "taobao");

		key2domain.put("11000002000088号", "jd");
		key2domain.put("京网文[2014]2148-348号 ", "jd");
		key2domain.put("JD.com 版权所有", "jd");

		key2domain.put("京ICP备05002571", "10086");
		key2domain.put("A2.B1.B2-20100001", "10086");

		key2domain.put("浙ICP备11016796", "zjstv");

		key2domain.put("Copyright © 2012-2016 Tencent. All Rights Reserved", "qq");

		key2domain.put("京ICP备13030780号", "ccb");
		key2domain.put("版权所有中国建设银行", "ccb");
		key2domain.put("京公网安备：110102000450", "ccb");

		key2domain.put("中国农业银行版权所有", "abchina");
		key2domain.put("京ICP备05049539", "abchina");

		key2domain.put("中国工商银行版权所有", "icbc");

		key2domain.put("京ICP备10052455号", "boc");
		key2domain.put("京公网安备110102002036号", "boc");
	}

	public static Type filter(PageInfo pageInfo) {

		// 获得域名
		String host = pageInfo.getHost();

		Type type = Type.UNKNOWN;

		String htmlContent = pageInfo.getDocument().text();

		// 关键词
		for (String key : key2domain.keySet()) {
			if (htmlContent.contains(key)) {
				if (pageInfo.getDomain().equals(key2domain.get(key))) {
					return Type.GOOD;
				} else {
					return Type.FISH;
				}
			}
		}

		// 标题
		String title = pageInfo.getDocument().title();
		if (title != null && title.length() > 0) {
			type = checkByWebSiteName(title, pageInfo);
			if (type != Type.UNKNOWN) {
				return type;
			}
		}

		// 版权所有
		String copyright = getCopyright(pageInfo.getDocument(), htmlContent).toLowerCase().trim();
		if (copyright.length() > 0) {
			if (!host.startsWith("www.")) {
				type = checkByWebSiteName(copyright, pageInfo);
				if (type != Type.UNKNOWN) {
					return type;
				}
			}
		}

		// 内容 
		String websiteName = getWebsiteName(htmlContent);
		if (websiteName.length() > 0) {
			type = checkByWebSiteName(websiteName, pageInfo);
			if (type != Type.UNKNOWN) {
				return type;
			}
		}

		return Type.UNKNOWN;
	}

	/**
	 * 按网站名称检测
	 * @param chineseName 网站名称
	 * @param pageInfo 网页信息
	 * @return 检测结果
	 */
	private static Type checkByWebSiteName(String chineseName, PageInfo pageInfo) {

		String domain = pageInfo.getDomain();

		String realHost = ChineseDns.getDomainByChineseName(chineseName);

		if (realHost != null) {
			if (domain.length() <= realHost.length()) { // 如果域名较短则应该是安全的
				return Type.GOOD;
			}else {
				return domain.endsWith(realHost) ? Type.GOOD : Type.FISH;
			}
		} else {
			return Type.UNKNOWN;
		}
	}

	private static Pattern pattern_welcome_0 = Pattern.compile("欢迎(来到|光临|访问|浏览)([\u4E00-\u9FA50-9a-zA-Z]+)");

	// Copyright © 2016 qianpinxiu.com 版权所有 苏ICP备16034059号
	// Copyright?2014 蘑菇云? 地址：福建泉州市丰泽区东海大街东海湾中心一号楼1502? 电话：0595-22891399?
	// 经营许可证：闽ICP备15025789号-1
	// Copyright 信用网 All Rights Reserved

	private static Pattern pattern_welcome_1 = Pattern.compile("([\u4E00-\u9FA50-9a-zA-Z]+)欢迎您");

	private static Pattern pattern_copyright1 = Pattern.compile("版权所有\\s*[：|:]\\s*([\u4E00-\u9FA5\\w\\.@]+)");

	private static Pattern pattern_copyright2 = Pattern.compile("([\u4E00-\u9FA5\\w\\.@]+)\\s*版权所有");

	// zhaojingdm.com 版权所有
	// 邮箱：1028249634@qq.com 版权所有：磁县巧莲医院有限公司
	private static String getCopyright(Document document, String input) {

		for (Element element : document.getAllElements()) {
			if (element.getAllElements().size() > 1)
				continue;
			String copyright = _getCopyright(element.text());
			if (copyright != null)
				return copyright;
		}
		return EMPTY_STRING;
	}

	private static String _getCopyright(String input) {
		Matcher matcher = null;
		matcher = pattern_copyright1.matcher(input);
		if (matcher.find()) {
			return matcher.group(1);
		}
		matcher = pattern_copyright2.matcher(input);
		if (matcher.find()) {
			return matcher.group(1);
		}
		return null;
	}

	private static String getWebsiteName(String input) {

		if (input.contains("欢迎来到") || input.contains("欢迎光临") || input.contains("欢迎访问") || input.contains("欢迎浏览")) {

			Matcher matcher = null;
			matcher = pattern_welcome_0.matcher(input);
			if (matcher.find()) {
				return matcher.group(2);
			}

			matcher = pattern_welcome_1.matcher(input);
			if (matcher.find()) {
				return matcher.group(1);
			}

		}

		return EMPTY_STRING;

	}

}
