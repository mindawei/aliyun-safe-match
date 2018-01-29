package safe.fish;

import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import safe.fish.utils.DataLoader;
import safe.fish.whitelist.ChineseDns;

/**
 * 网页中的关键字检查
 * @author mindw
 */
public class PageKeyChecker {

	/** 关键字映射到主域名 */
	private static final Map<String, String> KEY_TO_DOMAIN = DataLoader.loadKeysMap();

	public static Result check(PageInfo pageInfo) {

		String host = pageInfo.getHost();
		String domain = pageInfo.getDomain();
		Document document = pageInfo.getDocument();
		String htmlContent = document.text();

		// 关键词
		for (String pageKey : KEY_TO_DOMAIN.keySet()) {
			if (htmlContent.contains(pageKey)) {
				String realDomain = KEY_TO_DOMAIN.get(pageKey);
				return domain.equals(realDomain) ? Result.GOOD : Result.FISH;
			}
		}
		
		Result result = Result.UNKNOWN;

		// 标题
		String title = document.title();
		if (title != null && title.length() > 0) {
			result = checkByPageKey(title, domain);
			if (result != Result.UNKNOWN) {
				return result;
			}
		}

		// 版权所有
		String copyright = getCopyright(document).toLowerCase().trim();
		if (copyright.length() > 0) {
			if (!host.startsWith("www.")) {
				result = checkByPageKey(copyright, domain);
				if (result != Result.UNKNOWN) {
					return result;
				}
			}
		}

		// 内容
		String websiteName = getWebsiteName(htmlContent);
		if (websiteName.length() > 0) {
			result = checkByPageKey(websiteName, domain);
			if (result != Result.UNKNOWN) {
				return result;
			}
		}

		return result;
	}

	/**
	 * 按网站名称检测
	 * 
	 * @param chineseName
	 *            网站名称
	 * @param pageInfo
	 *            网页信息
	 * @return 检测结果
	 */
	private static Result checkByPageKey(String pageKey, String domain) {

		String realDomain = ChineseDns.getDomainByChineseName(pageKey);
		if (realDomain == null) {
			return Result.UNKNOWN;
		}

		if (domain.length() <= realDomain.length()) { // 如果域名较短则应该是安全的
			return Result.GOOD;
		} else {
			return domain.endsWith(realDomain) ? Result.GOOD : Result.FISH;
		}
	}

	// Copyright © 2016 qianpinxiu.com 版权所有 苏ICP备16034059号
	// Copyright?2014 蘑菇云? 地址：福建泉州市丰泽区东海大街东海湾中心一号楼1502? 电话：0595-22891399?
	// 经营许可证：闽ICP备15025789号-1
	// Copyright 信用网 All Rights Reserved
	// zhaojingdm.com 版权所有
	// 邮箱：1028249634@qq.com 版权所有：磁县巧莲医院有限公司
	
	private static Pattern pattern_welcome_0 = Pattern.compile("欢迎(来到|光临|访问|浏览)([\u4E00-\u9FA50-9a-zA-Z]+)");
	private static Pattern pattern_welcome_1 = Pattern.compile("([\u4E00-\u9FA50-9a-zA-Z]+)欢迎您");
	private static Pattern pattern_copyright1 = Pattern.compile("版权所有\\s*[：|:]\\s*([\u4E00-\u9FA5\\w\\.@]+)");
	private static Pattern pattern_copyright2 = Pattern.compile("([\u4E00-\u9FA5\\w\\.@]+)\\s*版权所有");

	private static String getCopyright(Document document) {

		for (Element element : document.getAllElements()) {
			if (element.getAllElements().size() > 1) {
				continue;
			}
			String copyright = _getCopyright(element.text());
			if (copyright != null) {
				return copyright;
			}
		}
		return "";
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
		return "";
	}

}
