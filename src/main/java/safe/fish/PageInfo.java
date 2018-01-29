package safe.fish;

import java.net.URL;
import java.nio.charset.Charset;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import safe.fish.utils.DataLoader;
import safe.fish.utils.XnTool;

/**
 * 页面处理信息
 * @author mindw
 */
/**
 * @author mindw
 *
 */
public class PageInfo {
	
	/** 网址：小写 */
	private String url;
	
	/** 网页的主机，可能是域名也可能使 IP*/
	private String host;
	
	/** 主体域名 */
	private String domain;
	
	/** 有些网址中带中文 */
	private String chineseInHost;
	
	/** 网页内容 */
	private String html;
	
	/** 网页解析对象 */
	private Document document;
	
	/** 字符集 */
	private String charset;
	
	/** title 标签 */
	private String title = "";
	
	/** meatInfo */
	private String metaInfo = "";
	
	/** a 有效链接数 */
    private int validLink = 0;
    
    /** a 无效链接数 */
    private  int inValidLink = 0;
	
	public PageInfo(String URL,String HTML) {
		this.url = URL.toLowerCase(); 
		this.host = getHost(url);
		this.domain = getDomainByHost(host);
		this.chineseInHost = getChineseInHost(host).toLowerCase();	
		this.html = HTML;
		this.charset= getCharset(HTML);
		this.document = Jsoup.parse(html, charset);  
		parseHtml();
		
		System.out.println(this);
	}

	private void parseHtml() {
		
		// 获得标题
		Elements elements = document.select("title");  
	    for (Element element : elements) {  
	    	if(title==null)
	    		title = element.text().trim();
	    	else
	    		title += " "+element.text().trim();
	    }  
	  	    
	    // 获得描述
	    // <meta/> : keywords,description,copyright
        // content of META name = “keywords”/“Description”/“copyright”,
        // content of META http-Equiv = “keywords”/“Description”/“copyright”
        elements = document.select("meta");  
        for (Element element : elements) {
        	String key = "";
        	if(element.hasAttr("http-equiv")){
        		key = element.attr("http-equiv");
        	}else if(element.hasAttr("name")){
        		key = element.attr("name");
        	}
       	
        	if(key.equalsIgnoreCase("keywords")
        			||key.equalsIgnoreCase("description")
        			||key.equalsIgnoreCase("copyright")){
        		if(element.hasAttr("content")){
        			if(metaInfo==null)
        				metaInfo = element.attr("content").trim();
        			else
        				metaInfo += element.attr("content").trim();
        		}
			}
		}
		
        // href of A
        elements = document.select("a");
		for (Element element : elements) {
			if (element.hasAttr("href")) {
				String href = (element.attr("href"));
				href = href.trim();
				if(href.length()==0||href.equals("#")||href.equals("?")||href.contains("void(0)")) {
					inValidLink++;
				}else {
					validLink++;
				}
			}
		
		}	
	}

	public String getUrl() {
		return url;
	}

	public String getHtml() {
		return html;
	}

	public String getHost() {
		return host;
	}
	
	public String getDomain() {
		return domain;
	}

	public String getChineseInHost() {
		return chineseInHost;
	}

	public String getCharset() {
		return charset;
	}

	public String getTitle() {
		return title;
	}

	public String getMetaInfo() {
		return metaInfo;
	}
	
	public int getValidLink() {
		return validLink;
	}
	
	public int getInValidLink() {
		return inValidLink;
	}
	
	public Document getDocument() {
		return document;
	}
	
	@Override
	public String toString() {
		return "PageInfo [url=" + url + ", host=" + host + ", domain=" + domain + ", chineseInHost=" + chineseInHost
				+ ", charset=" + charset + ", title=" + title + ", metaInfo=" + metaInfo + ", validLink=" + validLink
				+ ", inValidLink=" + inValidLink + "]";
	}

	/// >> 解析函数

	/** 获得网站主机部分 */
	private static String getHost(String urlStr) {
		try {
			String host = new URL(urlStr).getHost();
			return host;
		} catch (Exception e) {
		}
		return "";
	}
	
	private static Set<String> domains = DataLoader.loadDomains();
	
	/** 获得主体域名部分， www.baidu.com -> baidu */
	public static String getDomainByHost(String host) {
		if (host == null || host.length() == 0) {
			return "";
		}
		String[] parts = host.split("\\.");
		for (int i = parts.length - 1; i >= 0; i--) {
			if (!domains.contains(parts[i])) {
				return parts[i];
			}
		}
		return "";
	}

	/** 网址中的中文 xn-- */
	public static String getChineseInHost(String host){
		if (host == null || host.length() == 0) {
			return "";
		}
		try {
			String[] parts = host.split("\\.");
			for(String part : parts){
				if(part.startsWith("xn--")){
					part = part.substring(4);
					return XnTool.decode(part);
				}
			}
		} catch (Exception e) {
	
		}
		return "";
	}
	
	/** 获得默认的字符串集  */
    public static String getCharset(String htmlContent) {  
        String defaultCharset = "utf-8";  
        Pattern p = Pattern.compile("(?<=charset=)(.+)(?=\")");  
        Matcher m = p.matcher(htmlContent);  
        if (m.find()){
        	String charset = m.group();
        	try{
        		if(Charset.isSupported(charset))
        			return charset;
        	}catch(Exception e){ 
        		// 忽略非法字符串集 
        	}
        }
        return defaultCharset;  
    }  
    
//	private static String getIP(String name){
//	try {
//		 return InetAddress.getByName(name).getHostAddress().toString();
//
//	} catch (UnknownHostException e) {
//
//		System.out.println("获取失败");
//	}		
//	return "null";
//}
		
}
