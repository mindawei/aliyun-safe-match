package alibaba.safe.fish.find;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.regex.Pattern;

import sun.font.EAttribute;
import alibaba.safe.fish.find.white.FileUtil;

public class DomainUtil {
	
	/** 常用域名结尾 */
	private static Set<String> domains = new HashSet<String>();
	static{
		domains.add("com");
		domains.add("top");
		domains.add("win");
		domains.add("red");
		domains.add("com");
		domains.add("com");
		domains.add("net");
		domains.add("org");
		domains.add("wang");
		domains.add("gov");
		domains.add("edu");
		domains.add("mil");
		domains.add("biz");
		domains.add("name");
		domains.add("info");
		domains.add("mobi");
		domains.add("pro");
		domains.add("travel");
		domains.add("museum");
		domains.add("int");
		domains.add("aero");
		domains.add("post");
		domains.add("rec");
		domains.add("asia");
		domains.add("au");
		domains.add("ad");
		domains.add("ae");
		domains.add("af");
		domains.add("ag");
		domains.add("ai");
		domains.add("al");
		domains.add("am");
		domains.add("an");
		domains.add("ao");
		domains.add("aa");
		domains.add("ar");
		domains.add("as");
		domains.add("at");
		domains.add("au");
		domains.add("aw");
		domains.add("az");
		domains.add("ba");
		domains.add("bb");
		domains.add("bd");
		domains.add("be");
		domains.add("bf");
		domains.add("bg");
		domains.add("bh");
		domains.add("bi");
		domains.add("bj");
		domains.add("bm");
		domains.add("bn");
		domains.add("bo");
		domains.add("br");
		domains.add("bs");
		domains.add("bt");
		domains.add("bv");
		domains.add("bw");
		domains.add("by");
		domains.add("bz");
		domains.add("ca");
		domains.add("cc");
		domains.add("cf");
		domains.add("cd");
		domains.add("ch");
		domains.add("ci");
		domains.add("ck");
		domains.add("cl");
		domains.add("cm");
		domains.add("cn");
		domains.add("co");
		domains.add("cq");
		domains.add("cr");
		domains.add("cu");
		domains.add("cv");
		domains.add("cx");
		domains.add("cy");
		domains.add("cz");
		domains.add("de");
		domains.add("dj");
		domains.add("dk");
		domains.add("dm");
		domains.add("do");
		domains.add("dz");
		domains.add("ec");
		domains.add("ee");
		domains.add("eg");
		domains.add("eh");
		domains.add("er");
		domains.add("es");
		domains.add("et");
		domains.add("ev");
		domains.add("fi");
		domains.add("fj");
		domains.add("fk");
		domains.add("fm");
		domains.add("fo");
		domains.add("fr");
		domains.add("ga");
		domains.add("gd");
		domains.add("ge");
		domains.add("gf");
		domains.add("gg");
		domains.add("gh");
		domains.add("gi");
		domains.add("gl");
		domains.add("gm");
		domains.add("gn");
		domains.add("gp");
		domains.add("gr");
		domains.add("gs");
		domains.add("gt");
		domains.add("gu");
		domains.add("gw");
		domains.add("gy");
		domains.add("hk");
		domains.add("hm");
		domains.add("hn");
		domains.add("hr");
		domains.add("ht");
		domains.add("hu");
		domains.add("id");
		domains.add("ie");
		domains.add("il");
		domains.add("im");
		domains.add("in");
		domains.add("io");
		domains.add("iq");
		domains.add("ir");
		domains.add("is");
		domains.add("it");
		domains.add("jm");
		domains.add("jo");
		domains.add("jp");
		domains.add("je");
		domains.add("ke");
		domains.add("kg");
		domains.add("kh");
		domains.add("ki");
		domains.add("km");
		domains.add("kn");
		domains.add("kp");
		domains.add("kr");
		domains.add("kw");
		domains.add("ky");
		domains.add("kz");
		domains.add("la");
		domains.add("lb");
		domains.add("lc");
		domains.add("li");
		domains.add("lk");
		domains.add("lr");
		domains.add("ls");
		domains.add("lt");
		domains.add("lu");
		domains.add("lv");
		domains.add("ly");
		domains.add("ma");
		domains.add("mc");
		domains.add("md");
		domains.add("me");
		domains.add("mg");
		domains.add("mh");
		domains.add("mk");
		domains.add("ml");
		domains.add("mm");
		domains.add("mn");
		domains.add("mo");
		domains.add("mp");
		domains.add("mq");
		domains.add("mr");
		domains.add("ms");
		domains.add("mt");
		domains.add("mu");
		domains.add("mv");
		domains.add("mw");
		domains.add("mx");
		domains.add("my");
		domains.add("mz");
		domains.add("na");
		domains.add("nc");
		domains.add("ne");
		domains.add("nf");
		domains.add("ng");
		domains.add("ni");
		domains.add("nl");
		domains.add("no");
		domains.add("np");
		domains.add("nr");
		domains.add("nt");
		domains.add("nu");
		domains.add("nz");
		domains.add("om");
		domains.add("qa");
		domains.add("pa");
		domains.add("pe");
		domains.add("pf");
		domains.add("pg");
		domains.add("ph");
		domains.add("pk");
		domains.add("pl");
		domains.add("pm");
		domains.add("pn");
		domains.add("pr");
		domains.add("pt");
		domains.add("pw");
		domains.add("py");
		domains.add("re");
		domains.add("rs");
		domains.add("ro");
		domains.add("ru");
		domains.add("rw");
		domains.add("sa");
		domains.add("sb");
		domains.add("sc");
		domains.add("sd");
		domains.add("se");
		domains.add("sg");
		domains.add("sh");
		domains.add("si");
		domains.add("sj");
		domains.add("sk");
		domains.add("sl");
		domains.add("sm");
		domains.add("sn");
		domains.add("so");
		domains.add("sr");
		domains.add("st");
		domains.add("sv");
		domains.add("su");
		domains.add("sy");
		domains.add("sz");
		domains.add("sx");
		domains.add("tc");
		domains.add("td");
		domains.add("tf");
		domains.add("tg");
		domains.add("th");
		domains.add("tj");
		domains.add("tk");
		domains.add("tl");
		domains.add("tm");
		domains.add("tn");
		domains.add("to");
		domains.add("tr");
		domains.add("tt");
		domains.add("tv");
		domains.add("tw");
		domains.add("tz");
		domains.add("ua");
		domains.add("ug");
		domains.add("uk");
		domains.add("um");
		domains.add("us");
		domains.add("uy");
		domains.add("uz");
		domains.add("va");
		domains.add("vc");
		domains.add("ve");
		domains.add("vg");
		domains.add("vi");
		domains.add("vn");
		domains.add("vu");
		domains.add("wf");
		domains.add("ws");
		domains.add("ye");
		domains.add("yt");
		domains.add("za");
		domains.add("zm");
		domains.add("zw");
		domains.add("arts");
		domains.add("com");
		domains.add("edu");
		domains.add("firm");
		domains.add("gov");
		domains.add("info");
		domains.add("net");
		domains.add("nom");
		domains.add("org");
		domains.add("rec");
		domains.add("store");
		domains.add("web");		
	}
	
	
	/** 获得域名 */
	public static String getApplyDomainByUrl(String url){
		try {
			String host = new URL(url).getHost();
			String[] hostParts = host.split("\\.");
			for(int i = hostParts.length-1;i>=0;i--){
				String part = hostParts[i];
				if(domains.contains(part))
					continue;
				else
					return part;
			}
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
		return "";
	}
	
	/** 获得域名 */
	public static String getApplyDomainByHost(String host) {
		String[] hostParts = host.split("\\.");
		for (int i = hostParts.length - 1; i >= 0; i--) {
			String part = hostParts[i];
			if (domains.contains(part))
				continue;
			else
				return part;
		}
		return "";
	}
	
	private static Set<String> applyDomains = new HashSet<String>();

	static{
		
		applyDomains.add("taobao");
		applyDomains.add("tmall");
		applyDomains.add("jd");
//		applyDomains.add("vmall");
//		applyDomains.add("abchina");
//		applyDomains.add("10086");		
	}
	
	
	
	public static boolean isFish(String needCheckUrl,String htmlContent){
		
		
		String needCheckDomain = getApplyDomainByUrl(needCheckUrl.toLowerCase());
		
		
//		for(String domain : applyDomains){
//		if(needCheckDomain.equals(domain))
//			return false;
//		for(String needCheckPart : needCheckDomain.split("-")){
//			if(like(needCheckPart, domain)){
//				return true;
//			}
//		}
//	}
		// http://l0086-vip.pw
		// http://www.10086yna.pw		
	
		if(needCheckDomain.equals("1212-tmall")
				||needCheckDomain.equals("52-taobao")
				||needCheckDomain.equals("liuguoping")
				||needCheckDomain.equals("l0086-vip")
				||needCheckDomain.equals("10086yna")
				)
			return true;
		
		
//		String host = Util.getHost(needCheckUrl);
//		if(host!=null && (host.endsWith(".pw"))){
//			
//			if(htmlContent.contains("网址导航")&&htmlContent.contains("网址大全")||htmlContent.contains("上网导航"))
//				return false;
//			if(htmlContent.contains("淘宝网")
//					||htmlContent.contains("京东商城")
//					||htmlContent.contains("中国移动"))
//				return true;
//			
//			return false;
//		}
		
//		
//		for(String domain : applyDomains){
//			if(needCheckUrl.contains(domain)){
//				return true;
//			}
//		}

		return false;
		
	}

	/** 是否不易分辨 */
	private static boolean like(String needCheckDomain, String domain) {
		return t(needCheckDomain).equals(t(domain));
	}
	
	/** 转换 */
	private static String t(String s){
		StringBuilder builder = new StringBuilder();
		for(char ch : s.toCharArray()){
			if(ch=='0'||ch=='o'){
				builder.append('!');
			}else if(ch=='1'||ch=='l'){
				builder.append('@');
			}else{
				builder.append(ch);
			}
			
		}
		return builder.toString();
	}
	
	
//	public static void main(String[] args) {
//		System.out.println(isFish("http://www.11-2staobao",""));
//	}
	
	
//	public static void main(String[] args) throws IOException {
////		BufferedReader reader = new BufferedReader(new FileReader(FileUtil.getBaseFilePath()+"\\顶级域名.csv"));
////		String line =null;
////		while((line= reader.readLine())!=null){
////			String domain = line.split(",")[0];
////			System.out.println("domains.add(\""+domain+"\");");
////			
////		}
//		System.out.println(getApplyDomain("http://cn.com"));
//		
//	}
	
}
