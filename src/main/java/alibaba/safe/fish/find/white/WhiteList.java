package alibaba.safe.fish.find.white;

import java.util.Arrays;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import alibaba.safe.fish.find.DomainUtil;

public class WhiteList {

	static final Map<String, String> m = new LinkedHashMap<>();

	static final Set<String> applyDomains = new HashSet<>();

	// 官网 、 网站 、官方网站 、中国网站 、全球门户网站 、公司、首页
	private static List<String> lsSuffix = Arrays.asList("全球门户网站", "中国网站", "门户网站", "官方网站", "官网", "网站", "公司", "首页",
			"公共主页", "主页", "手机银行", "手机网", "手机版", "移动版");

	static {

		Map<String, String> chinese2site = WhiteListLoader.loadAll();

		// 进行筛选，去除加载中等
		for (Map.Entry<String, String> entry : chinese2site.entrySet()) {

			String chinese = entry.getKey();
			String url = entry.getValue();

			chinese = removeSuffix(chinese);

			if (chinese.length() == 0 || m.containsKey(chinese) || chinese.contains("加载中") || chinese.contains("正在加载")
					|| chinese.contains("�") || chinese.contains("建设中") || chinese.length() <= 1)
				continue;

			String applyDomain = DomainUtil.getApplyDomainByHost(url);

			if (applyDomain.length() == 0)
				continue;
			m.put(chinese, applyDomain);
		}

		// 放入一些热门的映射
		m.putAll(WhiteListLoader.loadHot());
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
}
