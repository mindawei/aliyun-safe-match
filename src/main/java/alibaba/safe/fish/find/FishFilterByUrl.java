package alibaba.safe.fish.find;

import java.util.HashSet;
import java.util.Set;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import alibaba.safe.fish.find.FishFilter.Type;

/** 从网页本身中找出域名 */
public class FishFilterByUrl {

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

	public static Type filter(Document document,String url) {
		
		String host = Util.getHost(url);
		if(host==null // 无法解析
				||host.matches("\\d+\\.\\d+\\.\\d+\\.\\d+") // 124.0.0.1 不考虑
				||host.contains("xn--")// 中文不考虑
				||host.endsWith(".com.cn")
				||host.endsWith(".com")
				||host.endsWith(".net")) 
			return Type.UNKNOWN;
		
		host = host.toLowerCase();
	
		String applyDomain = DomainUtil.getApplyDomainByUrl(url);
		if(applyDomain.length()<=6
				||applyDomain.contains("121mai") // 申请多个域名的网站
				||applyDomain.equals("my3w") // 临时域名 
				||applyDomain.equals("chinaw3") // 临时域名 
				)
			return Type.GOOD;
		else
			applyDomain = applyDomain.toLowerCase();
		
		
		
		Elements elements = document.select("a[href]");
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

//	// http://www.yntaa.com/
//	// http://www.yntaa.cn/
//	private static boolean isEqual(String host,String domain){
//		String _host =  host.replaceAll("\\.cn$","").replaceAll("\\.com$","").replaceAll("\\.net$","");
//		String _domain = domain.replaceAll("\\.cn$","").replaceAll("\\.com$","").replaceAll("\\.net$","");
////		System.out.println(_host);
////		System.out.println(_domain);
//		return _host.endsWith(_domain);
//		
//	}
	
//	public static void main(String[] args) {
//		System.out.println(isEqual("http://www.yntaa.com.cn", "http://www.yntaa.cn"));
//	}
	
}
