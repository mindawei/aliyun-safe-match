package alibaba.safe.fish.find.white;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import alibaba.safe.fish.find.DomainUtil;

public class Whites {
	
	static final Map<String,String> m = new LinkedHashMap<>();
	
	static final Set<String> applyDomains = new HashSet<>();
	
	// 官网 、 网站 、官方网站 、中国网站 、全球门户网站 、公司、首页
		private static List<String> lsSuffix;
	
	static{

		 lsSuffix = Arrays.asList(
				 "全球门户网站",
				 	"中国网站", 
				 	"门户网站",
					"官方网站", 
					"官网", 
					"网站", 
					"公司", 
					"首页",
					"公共主页",
					"主页",
					"手机银行",
					"手机网",
					"手机版",
					"移动版");		
		 
		filter(White0.m);
		filter(White00.m);
		filter(White01.m);
//		filter(White02.m);
//		filter(White03.m);
//		filter(White04.m);
//		filter(White05.m);
//		filter(White06.m);
//		filter(White07.m);
//		filter(White08.m);
//		filter(White09.m);
//		filter(White10.m);
//		filter(White11.m);
//		filter(White12.m);
//		filter(White13.m);
//		filter(White14.m);
//		filter(White15.m);
//		filter(White16.m);
//		filter(White17.m);
		
		m.put("新浪", "sina");
		m.put("新浪网", "sina");
		m.put("sina", "sina");
		m.put("sina.com.cn", "sina");
		m.put("新浪首页", "sina");
		
		m.put("淘宝网 - 淘！我喜欢", "taobao");
		m.put("【聚划算】无所不能聚", "taobao");
		m.put("天猫超市-上天猫，就够了", "tmall");
		
		m.put("欢迎访问中国建设银行网站", "ccb");
		m.put("建行官网", "ccb");
		m.put("建设银行官方网站", "ccb");
		m.put("建行", "ccb");
		m.put("中国建设银行", "ccb");
	
		m.put("中国工商银行中国网站", "icbc");
		m.put("ICBC首页", "icbc");
		m.put("中国工商银行", "icbc");
		m.put("中国工商银行官方网站", "icbc");
		m.put("工商银行", "icbc");
		m.put("工商银行官方网站", "icbc");
		
		m.put("中国移动官方网站", "10086");
		m.put("中国移动", "10086");
		m.put("中国移动通信", "10086");
		m.put("中国移动首页", "10086");
		m.put("中移动", "10086");
		m.put("中国移动网上商城", "10086");
		m.put("网上营业厅_中国移动通信", "10086");
		m.put("10086网上营业厅", "10086");
		m.put("10086", "10086");
		
		m.put("蓝天下_浙江卫视官方网站", "zjstv");
		m.put("浙江卫视", "zjstv");
		m.put("浙江卫视官方网站", "zjstv");
		m.put("浙江电视台", "zjstv");
		m.put("浙江卫视视频", "zjstv");
		m.put("中国好声音", "zjstv");
		m.put("奔跑吧兄弟", "zjstv");
		m.put("奔跑吧兄弟（runningman）", "zjstv");
		
		m.put("腾讯首页", "qq");
		m.put("腾讯网", "qq");
		m.put("腾讯", "qq");
		
		m.put("中国农业银行", "abchina");
		m.put("中国农业银行官网", "abchina");
		m.put("中国农业银行官方网站", "abchina");
		m.put("农业银行官网", "abchina");
		m.put("农业银行官方网站", "abchina");
		
	}
	
	public static String getHost(String chineseName) {
		String rmvChineseName = removeSuffix(chineseName);
		
		if(chineseName.equals("中国早教网")
				||chineseName.equals("美橙互联")
				||chineseName.equals("建站之星")
				||chineseName.equals("ecshop")
				||chineseName.equals("机械科学研究总院")
				||chineseName.equals("户户通")
				||chineseName.endsWith("有限公司")
				||chineseName.endsWith("大学")
				)
			return null;
		
		if(rmvChineseName.length()<2)
			return null;
		else
			return m.get(rmvChineseName);
	}
	
//	public static void main(String[] args) {
//		System.out.println(m.size());
//		
//		for(String s :applyDomains){
//			System.out.println("applyDomains.add(\""+s+"\");");
//		}
//		
//	}
	 
	// 45254 -> 44289 -> 20189 -> 6847 -> 3450 -> 1500
	private static void filter(Map<String, String> inM){
		
		for(Map.Entry<String, String> entry: inM.entrySet()){
			
			String chinese = entry.getKey();
			String url = entry.getValue();
			
			chinese = removeSuffix(chinese);
	
			if(chinese.length()==0
					||m.containsKey(chinese)
					||chinese.contains("加载中")
					||chinese.contains("正在加载")
					||chinese.contains("�")
					||chinese.contains("建设中")
					||chinese.length()<=1)
				continue;
			
			String applyDomain = DomainUtil.getApplyDomainByHost(url);

			if(applyDomain.length()==0)
				continue;
			
//			if(chinese.contains("商城")){
//				System.out.println(chinese+" "+applyDomain);
//				applyDomains.add(applyDomain);
//			}
			m.put(chinese,applyDomain );
	
			
//			String[] parts = url.split("\\.") ;
//			String reduceUrl = "";
//			for(int i=1;i<=parts.length;++i){
//				if(i>3)
//					break;
//				String part = parts[parts.length-i];
//				if(i>2&&part.length()>4)
//					break;
//				if(i==1)
//					reduceUrl=part;
//				else
//					reduceUrl=part+"."+reduceUrl;
//			}
			
//			String removedChinese = Util.removeLastPart(chinese);
//			if(removedChinese.length()<chinese.length()){
//				System.out.println(chinese+" "+removedChinese+" "+url);
//			}

//			chinese = FishFilterByChineseKeyWord.removeSuffix(chinese);
			
//			if(chinese.length()<=8&&chinese.contains("交易")){
//			_m.put(chinese, url);
//			System.out.println(String.format("dns.put(\"%s\", \"%s\");", chinese,url));
//		}
//			
			
			
//			if(chinese.length()<=6&&chinese.endsWith("银行")){
//				_m.put(chinese, url);
//				System.out.println(String.format("dns.put(\"%s\", \"%s\");", chinese,url));
//			}
			
//			if(chinese.length()==4&&chinese.endsWith("商城")){
//			_m.put(chinese, url);
//			System.out.println(String.format("dns.put(\"%s\", \"%s\");", chinese,url));
//		}
		
//			if(chinese.length()==3&&chinese.endsWith("贷")){
//			_m.put(chinese, url);
//			System.out.println(String.format("dns.put(\"%s\", \"%s\");", chinese,url));
//		}			
			
//			if(chinese.length()==4&&chinese.endsWith("证券")){
//			_m.put(chinese, url);
//			System.out.println(String.format("dns.put(\"%s\", \"%s\");", chinese,url));
//		}			
			
			
//			if(chinese.length()==3&&chinese.endsWith("街")){
//			_m.put(chinese, url);
//			System.out.println(String.format("dns.put(\"%s\", \"%s\");", chinese,url));
//		}
			

//			if(chinese.length()<=4&&chinese.endsWith("淘")){
//			_m.put(chinese, url);
//			System.out.println(String.format("dns.put(\"%s\", \"%s\");", chinese,url));
//		}
			
			
//			if(chinese.length()<=4&&chinese.contains("品")){
//			_m.put(chinese, url);
//			System.out.println(String.format("dns.put(\"%s\", \"%s\");", chinese,url));
//		}			
			
//			if(chinese.length()==4&&chinese.endsWith("电器")){
//			_m.put(chinese, url);
//			System.out.println(String.format("dns.put(\"%s\", \"%s\");", chinese,url));
//		}	
			
			
//			if(chinese.length()==3&&chinese.endsWith("网")&&url.length()<=10){
//			_m.put(chinese, url);
//			System.out.println(String.format("dns.put(\"%s\", \"%s\");", chinese,url));
//		}
		
//			if(chinese.length()<=4&&chinese.endsWith("购")){
//				_m.put(chinese, url);
//				System.out.println(String.format("dns.put(\"%s\", \"%s\");", chinese,url));
//			}
					
//			if(chinese.length()==3&&chinese.endsWith("买")){
//			_m.put(chinese, url);
//			System.out.println(String.format("dns.put(\"%s\", \"%s\");", chinese,url));
//		}
			
			
			
//			if(chinese.length()==3&&chinese.contains("钱")){
//			_m.put(chinese, url);
//			System.out.println(String.format("dns.put(\"%s\", \"%s\");", chinese,url));
//		}
			
			
//			if(chinese.length()==3&&chinese.endsWith("宝")){
//			_m.put(chinese, url);
//			System.out.println(String.format("dns.put(\"%s\", \"%s\");", chinese,url));
//		}
		
		
		
//			if(chinese.length()==4&&chinese.contains("支付")){
//			_m.put(chinese, url);
//			System.out.println(String.format("dns.put(\"%s\", \"%s\");", chinese,url));
//		}
			
//			if(chinese.length()==4&&chinese.endsWith("快递")){
//				_m.put(chinese, url);
//				System.out.println(String.format("dns.put(\"%s\", \"%s\");", chinese,url));
//			}
			
//			if(chinese.length()<=5&&chinese.endsWith("中国")){
//				_m.put(chinese, url);
//				System.out.println(String.format("dns.put(\"%s\", \"%s\");", chinese,url));
//			}
			
//			if(chinese.length()<=5&&chinese.endsWith("旅行社")){
//				_m.put(chinese, url);
//				System.out.println(String.format("dns.put(\"%s\", \"%s\");", chinese,url));
//			}
			
//			if(chinese.length()<=6&&chinese.endsWith("集团")){
//				_m.put(chinese, url);
//				System.out.println(String.format("dns.put(\"%s\", \"%s\");", chinese,url));
//			}

			
//			if(chinese.length()<=8&&chinese.contains("交易")){
//			_m.put(chinese, url);
//			System.out.println(String.format("dns.put(\"%s\", \"%s\");", chinese,url));
//		}
			
			//System.out.println(chinese+" "+url+" "+reduceUrl);
//			if(_m.size()==1500)
//				break;
			
			
			
		}
	}
	
	

	// 去后缀
	public static String removeSuffix(String websiteName) {
		for (String suffix : lsSuffix) {
			if (websiteName.endsWith(suffix)) {
				int index = websiteName.lastIndexOf(suffix);
				if (index != -1) {
					return websiteName.substring(0, index);
				}
			}
		}
		return websiteName;
	}


}
