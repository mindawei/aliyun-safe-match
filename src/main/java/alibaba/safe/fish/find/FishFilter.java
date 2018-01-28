package alibaba.safe.fish.find;

import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;


public class FishFilter {
	
	
	
	
//	static Pattern p = Pattern.compile(("(^|([^\u4E00-\u9FA5]+))"+"支付宝"+"($|([^\u4E00-\u9FA5+]))"));
//	public static void main(String[] args) {
//		Matcher matcher = p.matcher("寄无忧|Q币提现|Q币换钱|回收Q币|Q币换点卡|Q币收购|Q币提现财付通|Q币提支付宝|Q币提现财付通");
//		System.out.println(matcher.find());
//	}
//	


	// 获得标题
	String getTitle(String htmlContent){
		String charset = getCharset(htmlContent);	
		Document document = Jsoup.parse(htmlContent, charset);  
		
		String title = "";
		Elements elements = document.select("title");  
	    for (Element element : elements) {  
	    	title += " "+element.text().trim();
	    }  
	    return title;
	}
	
	double getEmptyAttrElementNumRate(Document document){
		int allElementNum = 1;
		int emptyAttrElementNum = 0;
		List<String> attrNames = Arrays.asList("id","style","href","src","action");
		for(Element element : document.getAllElements()){
			
			allElementNum++;
			for(String attrName : attrNames){
				if(element.hasAttr(attrName)){
					String elementAttrValue = element.attr(attrName).trim();
					if(elementAttrValue.length()==0){
						emptyAttrElementNum++;
						break;
					}
				}
			}
		}
		
		//System.out.println(emptyAttrElementNum);
		return emptyAttrElementNum*1.0/allElementNum;
	}
	
	public enum Type{
		FISH,
		UNKNOWN,
		GOOD
	}
	
	/** 是否是钓鱼网站 ：判断url是是否正确的本质是，找出该网站真正的url　*/
	public boolean isFish(String url,String htmlContent) {
		

		// 根据欢迎来到...、标题 等提取关键词，判断url是否相同
		if(DomainUtil.isFish(url,htmlContent))
			return true;
		
//		// 检测中文名字是否包含敏感词
		String chinese = CharsetTool.getChineseInUrl(url).toLowerCase();	
		// find num:10
		if(chinese.contains("欢迎")||chinese.contains("官网")||chinese.contains("官方网站")){
			if(chinese.contains("jd")
				||chinese.contains("taobao")
				||chinese.contains("淘宝")
				||chinese.contains("中国移动")
				||chinese.contains("10086")
				||chinese.contains("天猫")
				||chinese.contains("银行")
				){
			return true;
			}
		}
//		
//		if(UrlFilter.getInstance().isFish(url))
//			return true;
		
		// 去掉注释，防止误判
//		htmlContent = ZhushiUtil.removeZhushi(htmlContent);	
		// 去空格
		//htmlContent = htmlContent.replaceAll("&nbsp;","");
	
//		if(!_isSensitive(htmlContent)){
//			return false;
//		}
		
		// 解析
		Document document = Jsoup.parse(htmlContent, getCharset(htmlContent)); 
		
		// 获得域名
		String host = Util.getHost(url);
		if (host == null)
			return false;
		else
			host = host.toLowerCase();

		if (host.endsWith(".cn") || host.endsWith(".org"))
			return false;

		String needCheckDomain = DomainUtil.getApplyDomainByHost(host);
		if (needCheckDomain.length() == 0)
			return false;

		if (host.endsWith(".com") && needCheckDomain.length() <= 7)
			return false;
	
		
		// 根据内容中的url，和主页是否匹配
		Type type = FishFilterByUrl.filter(document,url);
		if(type==Type.FISH){
			return true;
		}else if(type==Type.GOOD){
			return false;
		}
	

		// 根据欢迎来到...、标题 等提取关键词，判断url是否相同
		type = FishFilterByChineseKeyWord.filter(document,url);
		if(type==Type.FISH){
			return true;
		}else if(type==Type.GOOD){
			return false;
		}
		
		return false;
		
//		String defaulSimpleDomain = Util.getSimpleDomainInUrl(url);
//		
//     
//		
//		// <url,text> in post form 
//		Map<String,String> url2text =new HashMap<>();
//		

//      
//	  //System.out.println(url2text);
//	    
//      for(Map.Entry<String,String> entry : url2text.entrySet()){
//    	  
//    	  String submitUrl = entry.getKey();
//    	  String text = entry.getValue();
//    	  
//    	  boolean isSensitive = false;
//    	  for(String key : keysInPays){
//    		  if(text.contains(key)){
//    			  isSensitive = true;
//    			  break;
//    		  }
//    	  }
//    	  if(!isSensitive){
//    		  continue;
//    	  }
//    	  
//    	  if(submitUrl.startsWith("https")||submitUrl.startsWith("/")||submitUrl.startsWith("#")||submitUrl.startsWith("?"))
//    		  continue;
//    	  
//    	  String submitSimpleDomain = Util.getSimpleDomainInUrl(submitUrl);
//    	  if(defaulSimpleDomain==null||defaulSimpleDomain.length()==0
//    			  ||submitSimpleDomain==null||submitSimpleDomain.length()==0
// 
//    			  ||submitSimpleDomain.equals("163.com")
//    			  ||submitSimpleDomain.equals("qq.com")
//    			  ||submitSimpleDomain.equals("weixin.com")
//    			  ||submitSimpleDomain.equals("sinaapp.com") // 新浪云
//    			  ||submitSimpleDomain.equals("alimama.com")
//    			  ||submitSimpleDomain.equals("chinaz.com")
//    			
//    			  ||submitSimpleDomain.equals("google.com.hk")
//    			  ||submitSimpleDomain.equals("google.com") 
//    			  ||submitSimpleDomain.equals("translate.google.cn")
//    			  ||submitSimpleDomain.equals("yahoo.com")
//    		
//    			  ||submitSimpleDomain.equals("yto.net.cn") // 圆通
//    			  ||submitSimpleDomain.equals("geometry.net")
//    			  ||submitSimpleDomain.equals("global-online-store.com")
//    			  
//    			  ||submitSimpleDomain.equals("tongling.so")
//    			  ||submitSimpleDomain.equals("fczzw.com")
//    			  ||submitSimpleDomain.equals("mulinxinli.com")
//    			  ||submitSimpleDomain.equals("sanyashd.com")
//    			  ||submitSimpleDomain.equals("cn99.com")
//    			  
//    			// zoossoft.com
//    			
//    			  ){
//    		  continue;
//    	  }else{
//    		  if(!defaulSimpleDomain.equalsIgnoreCase(submitSimpleDomain)){
//    			  return Integer.MIN_VALUE;
//    		  }
//    	  }
//      }
//      	    	
	   
	}
	

	/** 内容是否敏感 */
	private boolean isSensitive(Document document) {
		Elements elements = document.select("title");  
	    for (Element element : elements) {  
	    	String title = element.text().trim();
	    	if(title.length()>0){
	    		if(_isSensitive(title))
	    			return true;
	    	}
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
        			String info = element.attr("content").trim();
        			if(_isSensitive(info))
        				return true;
        		}
			}
		}
		
		
		return false;
	}
	
	List<String> keyWords = Arrays.asList(
			"银行",
			"银行卡",
			"身份证",
			"证件号",
			"账号",
			"邮箱",
			"手机号",
			"电话",
			"登录",
			"忘记密码",
			"中奖",
			"商城",
			"支付",
			"购买",
			"购物车",
			"优惠",
			"团购",
			"¥",
			"中国移动",
			"中国电信",
			"中国好声音",
			"爸爸去哪儿",
			"非常6+1"
			
			); 
	
	private boolean _isSensitive(String str){
		for(String keyWord :keyWords){
			if(str.contains(keyWord))
				return true;
		}
		return false;
	}

	/** 选择某些元素的某些属性 */
	private static List<String> select(Document document, List<String> elementNames,
			List<String> attrNames) {
		List<String> values = new LinkedList<String>();
		for (String elementName : elementNames) {
			Elements elements = document.select(elementName);
			for (Element element : elements) {
				for (String attrName : attrNames) {
					if (element.hasAttr(attrName)) {
						values.add(element.attr(attrName));
					}
				}
			}
		}
		return values;
	}

	private boolean isAbnormal(String url, List<String> urlsInTitle) {

		if (urlsInTitle.size() != 1)
			return false;

		url = url.toLowerCase();

		for (String urlInTitle : urlsInTitle) {
			
			urlInTitle = urlInTitle.toLowerCase();

			String[] parts = urlInTitle.split("\\.");
			if (parts.length > 2) {
				urlInTitle = parts[parts.length - 2] + "."
						+ parts[parts.length - 1];
			}

			if (url.contains(urlInTitle)) {
				return false;
			}

		}
		return true;
	}

	
	
//	public static void main(String[] args) {
//		System.out.println(getInstance().isAbnormal("http://025fafa.com/", getInstance().tryToGetUrl("南京法泽律师网★139139 28820★陈桂昌律师★1707-1525355★025falv.com 025fa.com 025fafa.com南京法泽律师网★13913928820★陈桂昌律师★17071525355★-是南京地区的专业解决婚姻家庭、离婚纠纷、房产纠纷、遗产继承、刑事辩护、民事诉讼、经济仲裁等法律问题的专业咨询网站,是南京律师服务所、宁峰事务所中最具影响力和专业性的权威的咨询网站。")));
//		System.out.println(getInstance().tryToGetUrl("phpMyAdmin 2.9.1.cn"));
//		System.out.println(getInstance().tryToGetUrl("南京法泽律师网★139139 28820★陈桂昌律师★1707-1525355★025falv.com 025fa.com 025fafa.com南京法泽律师网★13913928820★陈桂昌律师★17071525355★-是南京地区的专业解决婚姻家庭、离婚纠纷、房产纠纷、遗产继承、刑事辩护、民事诉讼、经济仲裁等法律问题的专业咨询网站,是南京律师服务所、宁峰事务所中最具影响力和专业性的权威的咨询网站。"));
//		System.out.println(getInstance().tryToGetUrl("金多宝===2289.us--"));
//		System.out.println(getInstance().tryToGetUrl("新青年，新梦想！10期永新年薪100万！7期黄源年薪80万！19位年薪超40万！6位技术总监！新青年X训练营,.NET架构，Java架构，NoSQL高,.NET开发,Java开发,Android开发，IOS，Node.JS,MVC,Web API,SOA,WCF,微服务,MongoDB,Redis--工资最高！学费最低！：《高并发电商网站架构设计》《微软C#7.0程序语言设计》《.NET企业开发实战课程》、《Android移动app开发实战》《IOS移动app开发实战》《ASP.NET MVC4 Web开发实战》《WCF分布式开发与SOA架构设计课程2016》《SQL Server 2016 数据库设计与开发》《UML设计语言》《MongoDB NoSQL数据库开发实战》《分布式缓存Redis高并发》《微软.NET企业架构设计》、《ASP.NET MVC 5 Web开发实战》、《ASP.NET MVC6  Web开发实战》《Office2013：Excel、PPT、Word办公软件课程》架构师、开发经理必备技术！"));
//		System.out.println(getInstance().tryToGetUrl("白沟知道网_接加工 放加工 招聘求职 物流专线 箱包配件 买卖市场_http://www.bgzhidao.com"));
//		System.out.println(getInstance().tryToGetUrl("长微博_changweibo.com_长微博工具_长微博生成器_长颈鹿长微博"));
//	
//		System.out.println(getInstance().tryToGetUrl("PHP探针-UPUPW绿色服务器平台KANGLE专用版"));
//		
//	}
	
	/** 
	 *  是否危险概率较大
	 *  如果安全,则返回正值，越大越安全；
	 *  如果危险，则返回负值；越小危险性越大;
	 */
	int getSafeRank2(String url,String htmlContent) {
		
		int safeRank = 0;
		
		String charset = getCharset(htmlContent);	
		Document document = Jsoup.parse(htmlContent, charset);  
		
		StringBuilder descriptionBuilder = new StringBuilder();
		
		// 获得标题
		String title = null;
		Elements elements = document.select("title");  
	    for (Element element : elements) {  
	    	if(title==null)
	    		title = element.text().trim();
	    	else
	    		title += " "+element.text().trim();
	    }  
	    
	    if(title==null) // 没有title
	    	safeRank-=0;
	    else if(title.length()==0) // 有title，但为空
	    	safeRank-=1;
	    else // 有title，不为空
	    	descriptionBuilder.append(title);
	    
	    
	    // 获得描述
	    // <meta/> : keywords,description,copyright
        // content of META name = “keywords”/“Description”/“copyright”,
        // content of META http-Equiv = “keywords”/“Description”/“copyright”
	    String metaInfo = null;
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
		
        if(metaInfo==null) // 没有metaInfo
	    	safeRank-=0;
	    else if(metaInfo.length()==0) // 有metaInfo，但为空
	    	safeRank-=1;
	    else // 有metaInfo，不为空
	    	descriptionBuilder.append(metaInfo);
        
        String description = descriptionBuilder.toString();
        
        // body text
        StringBuilder bodyTextBuilder = new StringBuilder();	
        elements = document.select("body");  
        for (Element element : elements) {
        	bodyTextBuilder.append(element.text());
        }
        String bodyText = bodyTextBuilder.toString();
        
        
        // href of A
        int hrefNumNormal = 0;
        int hrefNumAbnomal = 0;
        elements = document.select("a");
		for (Element element : elements) {
			if (element.hasAttr("href")) {
				String href = (element.attr("href"));
				href = href.trim();
				if(href.length()==0||href.equals("#")||href.equals("?")||href.contains("void(0)"))
					hrefNumAbnomal++;
				else
					hrefNumNormal++;
			}
		
		}		
		
		int hrefNumTotal = hrefNumAbnomal + hrefNumNormal;
		
		int reduceRank = 0;
		if(hrefNumTotal>0)
			reduceRank = (int)(hrefNumAbnomal * 1.0 / hrefNumTotal*100);
		
		
		if(reduceRank>5)
			reduceRank = 5; 
		safeRank -= reduceRank;
		
	
		if(bodyText.contains("ICP")){
			safeRank += 3;
		}
		
		// 导航
		boolean isNavigate = false;
		if (description.contains("网址大全") || description.contains("上网导航")
				|| description.contains("上网导航") || description.contains("网址导航")) {
			if (hrefNumNormal > 50)
				isNavigate = true;
		} else if (description.contains("网址") || description.contains("导航")) {
			if (hrefNumNormal > 100)
				isNavigate = true;
		}
		if(isNavigate)
			return safeRank;
		
		// 包含关键词
//		for (String key : keysInPays) {
//			if (description.contains(key)) {
//				safeRank-=2;
//			}else if (bodyText.contains(key)) {
//				safeRank-=1;
//			}
//		}

		return safeRank;
		
	}
	
	
	private static List<String> tryToGetUrl(String str){
		
		List<String> urls = new ArrayList<String>();
		// 防止类似  Powered by Y.Z.W
		String strLowerCase = str.toLowerCase();
		if(strLowerCase.contains("powered by")
				|| strLowerCase.contains("power by")
				|| strLowerCase.contains("来源于")
				|| strLowerCase.contains("建站")
				|| str.length()>30 ){ // 太长也不考虑了
			return urls;
		}
		
		Pattern p = Pattern.compile("([a-zA-Z0-9]\\w*)(\\.\\w+)*(\\.)(com|cn)",Pattern.CASE_INSENSITIVE );   
        Matcher m = p.matcher(str);    
        while(m.find()){
        	String url = m.group();
        	if(url.contains("52jscn.com"))
        		continue;
        	urls.add(url);
        }
        return urls;
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
    
}
