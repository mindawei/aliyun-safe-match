package alibaba.safe.fish.find;

import java.net.InetAddress;
import java.net.URL;
import java.net.UnknownHostException;
import java.util.LinkedList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Util {
	
	static List<String> getKeyWords(String input){
		
		//System.out.println(getFirstChineseWords(input));
		
		List<String> keyWords = new LinkedList<String>();
		
		String[] sentences = input.split("-|_");
		
		for (String sentence : sentences) {
			
			String keyWord = null;

			keyWord = getWhatIs(sentence);
			if (keyWord != null){
				keyWords.add(keyWord);
				continue;
			}
			
			keyWord = getFirstChineseWords(sentence);
			if (keyWord != null)
				keyWords.add(keyWord);
			
		}
		
		//System.out.println(input+"\n"+keyWords.toString()+"\n");
		
		return keyWords;
	}
	
	/** 第一段中文 */
	static Pattern patternFirstChinese = Pattern.compile("[\u4E00-\u9FA5|[0-9a-zA-Z]]+");
	static String getFirstChineseWords(String input){
		Matcher matcher = patternFirstChinese.matcher(input);
		if(matcher.find()){
			input = matcher.group();
		}else{
			input = null;
		}
		return input;
	}
	
	
	
	/** 主语 是 */
	static Pattern patternShi = Pattern.compile("([\u4E00-\u9FA5|[0-9a-zA-Z]]+)是([\u4E00-\u9FA5|[0-9a-zA-Z]]+)");
	static String getWhatIs(String input){
		Matcher matcher = patternShi.matcher(input);
		if(matcher.find()){
			input = matcher.group(1);
			
		}else{
			input = null;
		}
		return input;
	}

//	public static void main(String[] args) {
//		String input;
//		input = "支付宝全国授权商 -灵风易商是支付宝代理商，支付宝POS机技术开发商，最贴心的O2O平台解决方案提供商。";;
//		getKeyWords(input);
//		
//		input="重庆市那美品牌管理有限公司";
//		getKeyWords(input);
//		
//		input = "交通银行 - 交银金融网";
//		getKeyWords(input);
//		
//		input = "一网通主页 -- 招商银行官方网站";
//		getKeyWords(input);
//		
//		input = "中国工商银行中国网站";
//		getKeyWords(input);
//		
//		input = "成都POS机办理_成都POS机申请_成都移动POS机_成都银联POS机安装_成都银行POS机-成都东胜付信息技术有限公司";
//		getKeyWords(input);
//		
//		input = "中日在线,最老牌的日本点卡网站,Skype 阿里通 Q币 游戏点卡 支付宝";
//		getKeyWords(input);
//		
//		input = "快充网 空中平台 全国话费 Q币 支付宝 财付通 游戏点卡";
//		getKeyWords(input);
//		
//		input = "支付宝全国授权商 - 旺宝商盟是支付宝代理商，支付宝POS机技术开发商，最贴心的O2O平台解决方案提供商。";
//		getKeyWords(input);
//		
//		input = "云付宝官网-本赢支付招商中心，POS机招商代理 乐富POS机办理中心 中国银联POS机 ，云南钻玛商贸有限公司";
//		getKeyWords(input);
//		
//		input = "银行_hao123上网导航";
//		getKeyWords(input);
//		
//		input = "java匹配中文汉字的正则表达式 - divor - 博客园";
//		getKeyWords(input);
//		
//		input = "支付宝 - 网上支付 安全快速！";
//		getKeyWords(input);
//		
//		input = "支付宝pos机-商通道南京唯一的线下支付宝扫码支付平台";
//		getKeyWords(input);
//		
//	}
	
	// www.waan-tech.com.cn
	
	static Pattern patternSimpleDomain = Pattern.compile("([\\-0-9a-zA-Z]+\\.)*([\\-0-9a-zA-Z]+\\.[a-zA-Z]{0,5}(\\.cn|\\.hk|$))");
	
	//static Pattern patternSimpleDomainNumber = Pattern.compile("^[\\.|[0-9]]+$");
	
	
	// >> 获得域名
//	public static String getSimpleDomainInUrl(String urlStr) {
//
//		String simpleDomain = null;
//		try {
//
//			if (urlStr.startsWith("www."))
//				urlStr = "http://" + urlStr;
//			
//			if(urlStr.startsWith("http://http://")){
//				urlStr = "http://"+urlStr.replaceAll("http://", "");
//			}
//			
//			String host = new URL(urlStr).getHost();
//			
//			if (host.endsWith(".com.cn") || host.endsWith(".com.hk")) {
//				String[] parts = host.split("\\.");
//				int len = parts.length;
//				simpleDomain = parts[len - 3] + "." + parts[len - 2] + "."
//						+ parts[len - 1];
//			} else {
//				Matcher matcher = patternSimpleDomain.matcher(host);
//				if (matcher.find()) {
//					simpleDomain = matcher.group(2);
//				} else {
//					simpleDomain = host;
//				}
//			}
//		} catch (Exception e) {
//		}
//
//		// System.out.println(simpleDomain);
//		return simpleDomain;
//	}
	
//	public static void main(String[] args) {
//		System.out.println(getSimpleDomainInUrl("http://http://http://www.zlms.org/index.html"));
//	}
	
	// >> 获得域名
		public static String getHost(String urlStr) {
			try {
				String host = new URL(urlStr).getHost();
				return host;
			} catch (Exception e) {
			}
			return null;
		}
		
//		public static void main(String[] args) {
//			System.out.println(getHost("http://localhos22t"));
//		}
//	public static void main(String[] args) {
//		
//		getSimpleDomainInUrl("http://hejing.junfix.com");
//		
//		getSimpleDomainInUrl("http://njyasa.cn");
//		
//		getSimpleDomainInUrl("http://qianlintouzi.com:8080");
//	
//		getSimpleDomainInUrl("http://www.tavtat.trade/login/buy/pay.php?url=xh_4csculswlg6u-ywc6ouhm8wuqwdmhzf7kqno&payword=199718");
//		
//		getSimpleDomainInUrl("http://www.waan-tech.com");
//		
//		getSimpleDomainInUrl("http://www.waan-tech.com.cn");
//		
//		getSimpleDomainInUrl("http://www.google.com.hk");
//		
//		getSimpleDomainInUrl("http://127.0.1.253/search.asp");
//		
//		getSimpleDomainInUrl("http://translate.google.cn/translate");
//		
//	}
	
	
	public static String getIP(String name){
		try {
			 return InetAddress.getByName(name).getHostAddress().toString();

		} catch (UnknownHostException e) {
	
			System.out.println("获取失败");
		}		
		return "null";
	}
//	public static void main(String[] args) {
//	System.out.println(getIP("baidu.com.cn"));
//}


	/** 
	 * 获得网站的名字
	 * 欢迎来到京东商城！ -> 京东商城
	 */
	
//	public static void main(String[] args) {
//		
//		// 官网 、 网站 、官方网站  、中国网站 、全球门户网站 
//		System.out.println(getWebsiteName("欢迎来到163官网！"));
//		System.out.println(getWebsiteName("欢迎来到163网站！"));
//		System.out.println(getWebsiteName("欢迎来到163官方网站！"));
//		System.out.println(getWebsiteName("欢迎来到163中国网站！"));
//		System.out.println(getWebsiteName("欢迎来到163全球门户网站！"));
//		System.out.println(getWebsiteName("欢迎来到163公司！"));
//		System.out.println(getWebsiteName("欢迎来到163首页！"));
//		
//		System.out.println(getWebsiteName("欢迎来到QQ官方网站！"));
//		System.out.println(getWebsiteName("欢迎来到京东商城官网！"));
//		System.out.println(getWebsiteName("欢迎来到京东商城！"));
//
//		System.out.println(getWebsiteName("淘宝网欢迎您！"));
//		System.out.println(getWebsiteName("淘宝网官网欢迎您！"));
//		System.out.println(getWebsiteName("1主页欢迎您！"));
//		
//	}
	
	

	

	
}
