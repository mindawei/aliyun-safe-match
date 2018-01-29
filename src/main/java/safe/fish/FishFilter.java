package safe.fish;

import java.util.HashSet;
import java.util.Set;

/**
 * 检测钓鱼网站
 * @author mindw
 */
public class FishFilter {
	
	/** 是否是钓鱼网站 ：判断url是是否正确的本质是，找出该网站真正的url　*/
	public static boolean isFish(String URL,String HTML) {
		
		PageInfo pageInfo = new PageInfo(URL,HTML);
	
		// 根据欢迎来到...、标题 等提取关键词，判断url是否相同
		if(moniWellKnown(pageInfo)) {
			return true;
		}
		
		// 检测中文名字是否包含敏感词
		String chinese = pageInfo.getChineseInHost();	
		if (chinese.contains("欢迎") || chinese.contains("官网") || chinese.contains("官方网站")) {
			if (chinese.contains("jd") || chinese.contains("taobao") || chinese.contains("淘宝")
					|| chinese.contains("中国移动") || chinese.contains("10086") || chinese.contains("天猫")
					|| chinese.contains("银行")) {
				return true;
			}
		}
			
		// 获得主机名
		String host = pageInfo.getHost();
		// cn 需要实名登记; org 不盈利 ; .comj较短的代价大
		if (host.endsWith(".cn") || host.endsWith(".org")
				||(host.endsWith(".com") && pageInfo.getDomain().length() <= 7) ) {
			return false;
		}
		
		// 获得域名
		String domain = pageInfo.getDomain();
		if(domain.length()<=6
				||domain.contains("121mai") // 申请多个域名的网站
				||domain.equals("my3w") // 临时域名 
				||domain.equals("chinaw3") // 临时域名 
				) {
			return true;
		}
		
		Result result = Result.UNKNOWN;
		
		// 根据内容中的url，和主页是否匹配
		result = DomainConflictChecker.check(pageInfo);
		if(result==Result.FISH){
			return true;
		}else if(result==Result.GOOD){
			return false;
		}

		// 根据欢迎来到...、标题 等提取关键词，判断url是否相同
		result = PageKeyChecker.check(pageInfo);
		if(result==Result.FISH){
			return true;
		}else if(result==Result.GOOD){
			return false;
		}
		
		return false;	   
	}
	
	
	private static final Set<String> WELL_KNOWNS = new HashSet<String>();
	static {
		WELL_KNOWNS.add("taobao");
		WELL_KNOWNS.add("tmall");
		WELL_KNOWNS.add("jd");
		WELL_KNOWNS.add("vmall");
		WELL_KNOWNS.add("abchina");
		WELL_KNOWNS.add("10086");
	}

	/** 是否模仿知名网站 */
	public static boolean moniWellKnown(PageInfo pageInfo) {

		String url = pageInfo.getUrl();
		String host = pageInfo.getHost();
		String domain = pageInfo.getDomain();
		String html = pageInfo.getHtml();
		
		// 是否和知名网站相似
		for (String wellKnown : WELL_KNOWNS) {
			if (domain.equals(wellKnown)) {
				return false;
			}
			if (url.contains(wellKnown)) {
				return true;
			}
			for (String domainPart : domain.split("-")) {
				// 是否不易分辨
				if(t(domainPart).equals(t(wellKnown))) {
					return true;
				}
			}
		}
		
		// 黑名单
		if (domain.equals("1212-tmall") || domain.equals("52-taobao") || domain.equals("liuguoping") || domain.equals("l0086-vip") || domain.equals("10086yna")) {
			return true;
		}

		if (host != null && host.endsWith(".pw")) {
			if (html.contains("网址导航") || html.contains("网址大全") || html.contains("上网导航")) {
				return false;
			}
			
			if (html.contains("淘宝网") || html.contains("京东商城") || html.contains("中国移动")) {
				return true;
			}
		}
		
		return false;
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
