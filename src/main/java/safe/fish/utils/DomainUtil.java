package safe.fish.utils;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashSet;
import java.util.Set;

import safe.fish.PageInfo;

/**
 * 主要根据域名判断是否有问题
 * 
 * @author mindw
 */
public class DomainUtil {

	/** 常用域名结尾 */
	public static Set<String> domains = DataLoader.loadDomains();

//	/** 根据网址获得域名：www.baidu.com -> baidu */
//	public static String getDomainByWebSite(String webSite) {
//		String[] hostParts = webSite.split("\\.");
//		for (int i = hostParts.length - 1; i >= 0; i--) {
//			String part = hostParts[i];
//			if (!domains.contains(part)) {
//				return part;
//			}
//		}
//		return "";
//	}

	/** 获得域名 */
	public static String getApplyDomainByUrl(String url) {
		try {
			String host = new URL(url).getHost();
			String[] hostParts = host.split("\\.");
			for (int i = hostParts.length - 1; i >= 0; i--) {
				String part = hostParts[i];
				if (domains.contains(part))
					continue;
				else
					return part;
			}
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
		return "";
	}

	private static Set<String> wellKnownDomains = new HashSet<String>();
	static {
		wellKnownDomains.add("taobao");
		wellKnownDomains.add("tmall");
		wellKnownDomains.add("jd");
		wellKnownDomains.add("vmall");
		wellKnownDomains.add("abchina");
		wellKnownDomains.add("10086");
	}

	public static boolean isFish(PageInfo pageInfo) {

		// 是否和知名网站相似
		for (String wellKnownDomain : wellKnownDomains) {
			if (pageInfo.getDomain().equals(wellKnownDomain)) {
				return false;
			}
			if (pageInfo.getUrl().contains(wellKnownDomain)) {
				return true;
			}
			for (String needCheckPart : pageInfo.getDomain().split("-")) {
				if (like(needCheckPart, wellKnownDomain)) {
					return true;
				}
			}
		}

		// http://l0086-vip.pw
		// http://www.10086yna.pw

		// 黑名单
		if (pageInfo.getDomain().equals("1212-tmall") 
				|| pageInfo.getDomain().equals("52-taobao")
				|| pageInfo.getDomain().equals("liuguoping") 
				|| pageInfo.getDomain().equals("l0086-vip")
				|| pageInfo.getDomain().equals("10086yna"))
			return true;

		if (pageInfo.getHost() != null && pageInfo.getHost().endsWith(".pw")) {

			if (pageInfo.getHtml().contains("网址导航") 
					&& pageInfo.getHtml().contains("网址大全")
					|| pageInfo.getHtml().contains("上网导航")) {
				return false;
			}
			
			if (pageInfo.getHtml().contains("淘宝网") 
					|| pageInfo.getHtml().contains("京东商城")
					|| pageInfo.getHtml().contains("中国移动")) {
				return true;
			}
		}

		return false;
	}

	/** 是否不易分辨 */
	private static boolean like(String needCheckDomain, String domain) {
		return t(needCheckDomain).equals(t(domain));
	}

	/** 转换 */
	private static String t(String s) {
		StringBuilder builder = new StringBuilder();
		for (char ch : s.toCharArray()) {
			if (ch == '0' || ch == 'o') {
				builder.append('!');
			} else if (ch == '1' || ch == 'l') {
				builder.append('@');
			} else {
				builder.append(ch);
			}
		}
		return builder.toString();
	}
}
