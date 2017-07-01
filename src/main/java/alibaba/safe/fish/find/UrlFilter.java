package alibaba.safe.fish.find;

import java.net.InetAddress;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class UrlFilter {
	
	private static final UrlFilter instance = new UrlFilter();
	
	private UrlFilter(){}
	
	public static UrlFilter getInstance(){
		return instance;
	}
	

	/** 
	 *  是否危险概率较大
	 *  如果危险，则返回负值；越小危险性越大
	 *  如果不清楚，则返回0
	 */
	boolean isFish(String urlAdress){
		
		try {
			
			URL urlObject = new URL(urlAdress);
			
			// url 主要的部分
			String url = urlObject.getHost();
			
			
			// >>> 黑名单 
		
			
			/** url  8 个 */
			if(isUrlEasyMistake(url))
				return true;
			
			// ---------------------
			
			// >>> 白名单
//			String applyName = getApplyName(url);
//			
//			// 申请的部分  第一个>= 4  edu com cn 
//			if (applyName.length() <= 5) {
//				return Integer.MAX_VALUE;			
//			}
//			
			// 中文 
//			if(applyName.contains("china")
//					||applyName.contains("chinese")||applyName.contains("national"))
//				return Integer.MAX_VALUE;
//			
//			// 带有目录或者参数
//			if(urlObject.getFile().length()>0||urlObject.getQuery()!=null){
//				return Integer.MAX_VALUE;
//			}
//			
//			
//			// ---------------------
//			
//			
//			// 灰名单 
//			
//			// 检测‘.’的数目，大于3的话有可能是钓鱼网站  www.baidu.com.cn
//			int pointNum = 0;
//			for(char ch : url.toCharArray())
//				if(ch=='.')
//					pointNum++;
//			if (pointNum > 3)
//				pointNum -= 1;
//			
//			
//			if(applyName.length()>23){
//				safeRank -=1;
//			}
//			
//			// 包含异常符号
//			if(containsAbnomalStr(applyName)){
//				safeRank-=1;
//			}
//			
//								
//			// 一些ISP阻塞了个人用户的80端口（为了防止假设钓鱼网站），一些钓鱼网站就使用了非80端口
//			int port = urlObject.getPort();
//			if(port==-1)
//				port = urlObject.getDefaultPort();
//			if(port!=80){
//				safeRank-=1;
//			}
			// ---------------------
			
			// http://alipay.ksw8.com
			
		} catch(Exception e) {}
		
		return false;
	}
	
	
	/** 获得申请的那级域名 */
	private String getApplyName(String url) {
		
		String name = "null";
		
		String[] parts = url.split("\\.");
		for(int i=2;i<=parts.length;++i){ // lenth -2 开始
			String part = parts[parts.length - i];
			if(part.length()<4){ // com.cn edu.cn
				continue;
			}
			name = part;
			break;
		}
		
		return name;
		
	}

	
//	public static void main(String[] args) {
//		System.out.println(UrlFilter.getInstance().getSafeRank("http://xn--5hq5l637aqgv.com"));
//	}
	
	private final boolean isUrlEasyMistake(String url){
		String[] parts = url.split("\\.");
		for(String part : parts){
			if(isUrlPartEasyMistake(part)){
				return true;
			}
		}
		return false;
	}
	
	/** 容易混淆 的  0 o  1 l*/
	private boolean isUrlPartEasyMistake(String part) {
		if(part.contains("ta0bao")||part.contains("taoba0")||part.contains("ta0ba0")
				||part.contains("a1ipay")
				||part.contains("l0086")||part.contains("1o086")||part.contains("10o86")||part.contains("1oo86")||part.contains("loo86")
				||part.contains("l2306")||part.contains("123o6")||part.contains("l23o6")
				||part.contains("tma1l")||part.contains("tmal1")||part.contains("tma11")
				||part.contains("paypa1")
				){
			return true;
		}
		return false;
	}
	
	
	/** 包含有名的网站 */
	private final boolean containsFamousName(String url) {
		if(url.contains("taobao")
				||url.contains("baidu")
				||url.contains("alipay")
				||url.contains("10086")
				||url.contains("12306")
				||url.contains("weixin")
				||url.contains("qq")
				||url.contains("163")
				||url.contains("icbc")
				||url.contains("tmall")
				||url.contains("cmbchina")
				||url.contains("ccb")
				||url.contains("bankcomm")
				||url.contains("paypal")
				||url.contains("abchina")
				)
			return true;
		return false;
	}

	/**  某一段太长 */
//	private final boolean isPartToLong(String url){
//		String[] segs = url.split("\\.");
//		for(String seg : segs){
//			String[] subSegs = seg.split("-");
//			for(String subSeg : subSegs){
//				if(subSeg.length()>20)
//					return true;
//			}
//		}
//		return false;
//	}
	
	/** 
	 * 是否伪装 
	 * 000593www.5151job.cn
	 * ccc.www.5151job.cn 
	 * 
	 * www.baidu.com.swd33419df26ac945ddb4c6d26856484b5.tg-seo.com
	 */
//	private final boolean isDisguised(String url){
//		String[] segs = url.split("\\.");
//		boolean isDisguised = false;
//		
//		// 只有两段
//		if(segs.length<=2){ //A.B
//			return false;
//		}
//			
//		// www.baidu.com.cn.tg-seo.com
//		if(segs.length>4&&segs[0].equals("www")&&segs[2].equals("com")){
//			return true;
//		}
//		
//		for(int i=segs.length-1;i>=0;i--){
//			if(segs[i].endsWith("www")||segs[i].startsWith("www")){		
//				//  www.1090www.com 倒数第二个
//				if(i==segs.length-2){ 
//					continue;
//				}
//				// 000593www.5151job.cn
//				// ccc.www.5151job.cn 不是 
//				if(segs[i].length()>3){
//					isDisguised = true;
//				}
//			}
//		}
//		return isDisguised;
//	}
	
	
	/** 是否具有异常符号 */
	private final boolean containsAbnomalStr(String applyName) {
		applyName = applyName.toLowerCase();
		for(char ch : applyName.toCharArray()){
			if(ch>='a'&&ch<='z') // 字母
				continue;
			if(ch=='-') // .
				continue;
			if(ch>='0'&&ch<='9') // 数字
				continue;
			return true;
		}
		return false;
	}

//	/** 去掉http:// */
//	private final String reduceHttp(String url){
//		String[] segs = url.split("://");
//		if(segs.length>1){
//			String leftUrl = segs[1];
//			int index = -1;
//			if((index=leftUrl.indexOf('/'))!=-1){
//				leftUrl = leftUrl.substring(0,index);
//			}
//			index = -1;
//			if((index=leftUrl.indexOf('?'))!=-1){
//				leftUrl = leftUrl.substring(0,index);
//			}
//			return leftUrl;
//		}else{
//			return segs[0];
//		}
//	}
	
//	/** 包含多少个指定字符串 */
//	private final int numberOfsubString(String str,String subString){
//		int num = 0;
//		int startIndex = 0;
//		while((startIndex = str.indexOf(subString,startIndex))!=-1){
//			startIndex+=subString.length();
//			num++;
//		}
//		return num;
//	}
	
	
//	public static void main(String[] args) {
//	
//		UrlFilter urlFilter = UrlFilter.getInstance();
//	     System.out.println(urlFilter.reduceHttp("http://www.china-aseanbusiness.org.cn/details.asp?id=6328"));
//		System.out.println(urlFilter.numberOfsubString("http://0007sb.com_www.99sun.com", "com"));
//		System.out.println(urlFilter.getSafeRank("http://bbs.nannanannananna-vip.com"));
//		System.out.println(urlFilter.getSafeRank("http://000593www.5151job.cn"));
//		System.out.println(urlFilter.getSafeRank("http://cn.commercial-telematics-summit.com/"));
//		
//	}
//	
	
	
	
	
//	public static void main(String[] args) throws MalformedURLException {
//		URL url = new URL("http://55fqwiofj.55ioajdoias.pw/detaila/?js001-2016-07-08-165237.shtml");
//		
//		String protocol = url.getProtocol(); 
//		System.out.println(protocol);
//		
//		
//		
//		String host = url.getHost();
//		System.out.println(host);
//		
//		System.out.println(url.getFile());
//		
//		System.out.println(url.getQuery());
//		
//		
//		
//	}
	
}
