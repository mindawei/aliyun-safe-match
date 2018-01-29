package alibaba.safe.fish;

import java.util.HashSet;
import java.util.Set;

import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import alibaba.safe.fish.WebPage.PageInfo;
import alibaba.safe.fish.utils.DomainUtil;
import alibaba.safe.fish.utils.Util;

public class FishFilter {
	
	// 通过标题、空白值比例（"id","style","href","src","action"）等判断
	
	public enum Type{
		FISH,
		UNKNOWN,
		GOOD
	}
	
	/** 是否是钓鱼网站 ：判断url是是否正确的本质是，找出该网站真正的url　*/
	public boolean isFish(String url,String htmlContent) {
		
		PageInfo pageInfo = new PageInfo(url,htmlContent);
	
		// 根据欢迎来到...、标题 等提取关键词，判断url是否相同
		if(DomainUtil.isFish(pageInfo)) {
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
			
		// 获得域名
		String host = pageInfo.getHost();
		// cn 需要实名登记，org 不盈利
		if (host.endsWith(".cn") || host.endsWith(".org")) {
			return false;
		}

		if (pageInfo.getDomain().length() == 0) {
			return false;
		}
		
		if (host.endsWith(".com") && pageInfo.getDomain().length() <= 7) {
			return false;	
		}
		
		// 根据内容中的url，和主页是否匹配
		Type type = filter(pageInfo);
		if(type==Type.FISH){
			return true;
		}else if(type==Type.GOOD){
			return false;
		}
	

		// 根据欢迎来到...、标题 等提取关键词，判断url是否相同
		type = WebSiteNameChecker.filter(pageInfo);
		if(type==Type.FISH){
			return true;
		}else if(type==Type.GOOD){
			return false;
		}
		
		return false;	   
	}


	/// >> 从网页本身中找出域名 
	public static Set<String> keys = new HashSet<String>();
	static {
		keys.add("首页");
//		keys.add("网站首页");
//		keys.add("主页");
//		keys.add("登录");
//		keys.add("注册");
//		keys.add("关于我们");
//		keys.add("关于");
	}
	
	public static Type filter(PageInfo pageInfo) {
	
		String host = pageInfo.getHost();
		if(host==null // 无法解析
				||host.matches("\\d+\\.\\d+\\.\\d+\\.\\d+") // 124.0.0.1 不考虑
				||host.contains("xn--")// 中文不考虑
				||host.endsWith(".com.cn")
				||host.endsWith(".com")
				||host.endsWith(".net")) 
			return Type.UNKNOWN;
		
		String applyDomain = pageInfo.getDomain();
		if(applyDomain.length()<=6
				||applyDomain.contains("121mai") // 申请多个域名的网站
				||applyDomain.equals("my3w") // 临时域名 
				||applyDomain.equals("chinaw3") // 临时域名 
				) {
			return Type.GOOD;
		}
		
		Elements elements = pageInfo.getDocument().select("a[href]");
		for (Element element : elements) {
			
			String href = element.attr("href");
			String text = element.text().replaceAll("(\\s)+", "");
			
			// qq 聊天
			if(href.contains(".qq.com")
					||href.contains(".baidu.com"))
				continue;
			
			if (keys.contains(text)) {
				
				if(!href.startsWith("http")) { // 当前目录一类
					return Type.UNKNOWN; 
				}
				
				String hrefHost = Util.getHost(href);
				if (hrefHost != null) { // 首页指向ip地址威胁不大，而且容易暴露，一般是企业自己知道的
					if (hrefHost.matches("\\d+\\.\\d+\\.\\d+\\.\\d+")
							|| hrefHost.equals("localhost")
							|| hrefHost.endsWith("wezhan.cn")) { // 微站
						continue;
					}
				}

				// 获得域名
				String hrefDomain = DomainUtil.getApplyDomainByUrl(href);
				if(hrefDomain.length()==0)
					continue;
				
				if (hrefDomain != null) {
					// url 更难申请，或者很短
					if(applyDomain.length()<=hrefDomain.length())
						return Type.UNKNOWN;
								
					if (applyDomain.equals(hrefDomain.toLowerCase()))
						return Type.GOOD;
					else{
						return Type.FISH;	
					}
				}

			}
		}
		
		return Type.UNKNOWN;
	}
}
