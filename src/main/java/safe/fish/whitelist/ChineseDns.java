package safe.fish.whitelist;

import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import safe.fish.PageInfo;
import safe.fish.utils.DataLoader;

/**
 * 根据中文查询域名
 * @author mindw
 */
public class ChineseDns {

	// 官网 、 网站 、官方网站 、中国网站 、全球门户网站 、公司、首页
	private static final List<String> SUFFIX_LIST = Arrays.asList("全球门户网站", "中国网站", "门户网站", "官方网站", "官网", "网站", "公司", "首页",
			"公共主页", "主页", "手机银行", "手机网", "手机版", "移动版");
	
	private static final Map<String, String> CHINESE_TO_DOMAIN = new LinkedHashMap<>();

	static {

		Map<String, String> chinese2website = DataLoader.loadAllWhiteList();
		// 放入一些热门的映射
		chinese2website.putAll(DataLoader.loadHotWhiteList());
		
		// 进行筛选，去除加载中等
		for (Map.Entry<String, String> entry : chinese2website.entrySet()) {

			String chinese = entry.getKey();
			String website = entry.getValue();

			chinese = removeSuffix(chinese);

			if (chinese.length() == 0 || 
					CHINESE_TO_DOMAIN.containsKey(chinese) 
					|| chinese.contains("加载中")
					|| chinese.contains("正在加载") 
					|| chinese.contains("�") 
					|| chinese.contains("建设中")
					|| chinese.length() <= 1) {
				continue;
			}

			String domain = PageInfo.getDomainByHost(website);

			if (domain.length() == 0) {
				continue;
			}
		
			CHINESE_TO_DOMAIN.put(chinese, domain);
		}
		
	}

	/**
	 * 根据中文名称查找域名
	 * 
	 * @param chineseName
	 *            中文名称
	 * @return 域名，没有则为空
	 */
	public static String getDomainByChineseName(String chineseName) {
		
		if(chineseName==null) {
			return null;
		}

		// 一些特殊名称
		if (chineseName.equals("中国早教网") || chineseName.equals("美橙互联") || chineseName.equals("建站之星")
				|| chineseName.equals("ecshop") || chineseName.equals("机械科学研究总院") || chineseName.equals("户户通")
				|| chineseName.endsWith("有限公司") || chineseName.endsWith("大学")) {
			return null;
		}

		// 去后缀
		chineseName = removeSuffix(chineseName);
		if (chineseName.length() < 2) {
			return null;
		}

		return CHINESE_TO_DOMAIN.get(chineseName);
	}

	// 去后缀
	private static String removeSuffix(String websiteName) {
		for (String suffix : SUFFIX_LIST) {
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
