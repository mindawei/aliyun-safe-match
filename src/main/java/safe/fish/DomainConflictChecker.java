package safe.fish;

import java.net.URL;
import java.util.HashSet;
import java.util.Set;

import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

/**
 * 从网页本身中找出域名
 * 
 * @author mindw
 */
public class DomainConflictChecker {

	private static final Set<String> SELF_KEYS = new HashSet<String>();
	static {
		SELF_KEYS.add("首页");
		SELF_KEYS.add("网站首页");
		SELF_KEYS.add("主页");
		SELF_KEYS.add("登录");
		SELF_KEYS.add("注册");
		SELF_KEYS.add("关于我们");
		SELF_KEYS.add("关于");
	}

	public static Result check(PageInfo pageInfo) {

		String host = pageInfo.getHost();
		if (host.matches("\\d+\\.\\d+\\.\\d+\\.\\d+") // 124.0.0.1 不考虑
				|| host.contains("xn--")// 中文不考虑
				|| host.endsWith(".com.cn") || host.endsWith(".com") || host.endsWith(".net")) {
			return Result.UNKNOWN;
		}

		String domain = pageInfo.getDomain();
		Elements elements = pageInfo.getDocument().select("a[href]");
		for (Element element : elements) {

			String href = element.attr("href");
			String text = element.text().replaceAll("(\\s)+", "");

			// qq 聊天
			if (href.contains(".qq.com") || href.contains(".baidu.com")) {
				continue;
			}

			if (SELF_KEYS.contains(text)) {

				if (!href.startsWith("http")) { // 当前目录一类
					return Result.UNKNOWN;
				}

				String hrefHost = null;

				try {
					
					hrefHost = new URL(href).getHost();
					
					// 首页指向ip地址威胁不大，而且容易暴露，一般是企业自己知道的
					if (hrefHost != null && hrefHost.matches("\\d+\\.\\d+\\.\\d+\\.\\d+")
							|| hrefHost.equals("localhost") || hrefHost.endsWith("wezhan.cn")) { // 微站
						continue;
					}
					
					// 获得域名
					String hrefDomain = PageInfo.getDomainByHost(hrefHost);
					if (hrefDomain != null && hrefDomain.length() > 0) {
						// url 更难申请，或者很短
						if (domain.length() <= hrefDomain.length()) {
							return Result.UNKNOWN;
						} else if (domain.equals(hrefDomain.toLowerCase())) {
							return Result.GOOD;
						} else {
							return Result.FISH;
						}
					}
					
				} catch (Exception e) {}
			}
		}

		return Result.UNKNOWN;
	}
}
