package alibaba.safe.fish.find;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import alibaba.safe.fish.find.FishFilter.Type;
import alibaba.safe.fish.find.white.WhiteList;

public class FishFilterByChineseKeyWord {
	
	private static final String EMPTY_STRING = "";
	
	private static Map<String,String> key2domain = new HashMap<>();
	static{
		key2domain.put("浙网文[2013]0268-027号", "taobao");
		key2domain.put("浙B2-20080224", "taobao");
		key2domain.put("Taobao.com 版权所有", "taobao");

		key2domain.put("11000002000088号", "jd");
		key2domain.put("京网文[2014]2148-348号 ", "jd");
		key2domain.put("JD.com 版权所有", "jd");
		
		key2domain.put("京ICP备05002571", "10086");
		key2domain.put("A2.B1.B2-20100001", "10086");
		
		key2domain.put("浙ICP备11016796", "zjstv");
		
		key2domain.put("Copyright © 2012-2016 Tencent. All Rights Reserved", "qq");
		
		key2domain.put("京ICP备13030780号", "ccb");
		key2domain.put("版权所有中国建设银行", "ccb");
		key2domain.put("京公网安备：110102000450", "ccb");
		
		key2domain.put("中国农业银行版权所有", "abchina");
		key2domain.put("京ICP备05049539", "abchina");

		key2domain.put("中国工商银行版权所有", "icbc");
		
		key2domain.put("京ICP备10052455号", "boc");
		key2domain.put("京公网安备110102002036号", "boc");
	}
	
	public static Type filter(Document document, String url) {
		
		// 获得域名
		String host = Util.getHost(url);
		if(host==null)
			return Type.UNKNOWN;
		else
			host = host.toLowerCase();
		
		if(host.endsWith(".cn")
				||host.endsWith(".org"))
			return Type.GOOD;
		
		String needCheckDomain = DomainUtil.getApplyDomainByHost(host);
		if(needCheckDomain.length()==0)
			return Type.GOOD;
		
		if(host.endsWith(".com")&&needCheckDomain.length()<=7)
			return Type.GOOD;
		
		Type type = Type.UNKNOWN;
		
		String htmlContent = document.text();
		
		// 关键词 	25
		for(String key :key2domain.keySet()){
			if(htmlContent.contains(key)){
				if(needCheckDomain.equals(key2domain.get(key))){
					return Type.GOOD;
				}else{
					return Type.FISH;
				}
			}
		}
		
		// 标题  54
		String title = document.title();
		if(title!=null&&title.length()>0){
			type = filterTitle(title, host);
			if(type!=Type.UNKNOWN)
				return type;	
		}
	
	
		// 版权所有 50
		String copyright = getCopyright(document,htmlContent).toLowerCase().trim();
		if(copyright.length()>0){
			if(!host.startsWith("www.")){
				
//			if(pattern_url.matcher(copyright).find()&&
//					(!host.contains("xn--"))){ // url
//				String copyrightDomain = copyright.replaceAll("\\.com$","").replaceAll("^www\\.","");
//				if(host.replaceAll("\\.cn$","").replaceAll("\\.com$","").replaceAll("\\.net$","").endsWith(copyrightDomain)){
//					return Type.GOOD;
//				}else{
//					return Type.FISH;
//				}	
//			}else{
			type = filterWelcome(copyright, host);
			if (type != Type.UNKNOWN)
				return type;	
//			}
			
			}
		}
		
		// 内容  ~50
		String websiteName = getWebsiteName(htmlContent);
		if(websiteName.length()>0){
			type = filterWelcome(websiteName, host);
			if(type!=Type.UNKNOWN)
				return type;
		}
		
		return Type.UNKNOWN;
		
	}
	
//	public static Type filter(String chineseName,String host){
//		
//		// 不考虑网站导航类
//		if (chineseName.contains("网址大全") 
//				|| chineseName.contains("导航")
////				|| chineseName.contains("网址导航")
////				|| chineseName.contains("网站导航")
////				|| chineseName.contains("上网导航")
////				|| chineseName.contains("网页导航")
//				)
//			return Type.UNKNOWN;
//		
//		// 太短的不考虑：两个字
//		if(chineseName.length()<=1) 
//			return Type.UNKNOWN;
//		
//		// 微信代理商太多
//		if(chineseName.equals("微信")||chineseName.equals("论坛")
//				||chineseName.equals("微店")||chineseName.equals("快递查询")
//				||chineseName.equals("结婚网")||chineseName.equals("热点新闻")
//				) 
//			return Type.UNKNOWN;
//		
//		String realHost = DataCenter.getHostByChineseName(removeSuffix(chineseName));
//		if (realHost != null) {
//			return host.endsWith(realHost) ? Type.GOOD : Type.FISH;
//		}else{
//			return Type.UNKNOWN;
//		}
//	}
	
	
	public static Type filterWelcome(String chineseName,String host){	
		
		String _host =  DomainUtil.getApplyDomainByHost(host);
		
		String realHost = WhiteList.getHost(chineseName);
		
		if (realHost != null) {			
			if(_host.length()<=realHost.length()) // 如果域名较短则应该是安全的
				return Type.GOOD;
			else
				return _host.endsWith(realHost) ? Type.GOOD : Type.FISH;
		}else{
			return Type.UNKNOWN;
		}
	}

	public static Type filterTitle(String chineseName,String host){	
		
		String _host =  DomainUtil.getApplyDomainByHost(host);
		
		String realHost = WhiteList.getHost(chineseName);
		if (realHost != null) {			
			
			if(_host.length()<=realHost.length()) // 如果域名较短则应该是安全的
				return Type.GOOD;
			else
				return _host.endsWith(realHost) ? Type.GOOD : Type.FISH;
		}else{
			return Type.UNKNOWN;
		}
	}

	
	
	
//	public static void main(String[] args) {
////		String s = "马兰花开 您好，欢迎来到马兰花开电子商城 [请登录] [免费注册] 会员中心 我的订单 加入收藏 分享给好友 加入收藏 请使用菜单栏或Ctrl+D进行添加！ 确定 分享好友 复制 取消 亲！如果您的好友成功注册为会员，会有积分送给您哦！ 登录提示 您未登录！现在确认要登录吗？ 确定 取消 操作提示 复制成功！ 确定 我的购物车 0 购物车中还没有商品，再去逛逛吧！ 正在为您加载数量！ ¥ 0.00 结算 购物车 共 0 0 件 热门搜索： 有机黄瓜 绢花 生物酶 空气净化 全部商品分类 商城新闻 更多>> 【商城新闻】 福缘地酶系列产品发布会 【商城新闻】 水母网牵手“福缘地”走进万达 【商城新闻】 马兰花开电子商城即将启动 【商城新闻】 “健康传播者”孙革新教授福缘地行 【商城新闻】 健康厨房进万家公益活动系列宣传片—— 【商城新闻】 “福缘地”携手水母网 共同传递人生 今日推荐 热门推荐 换一批 1F健康厨房 健康果蔬 生物酶洗涤用品 张大姐系列食品 生物酶洗涤用品 健康果蔬 张大姐系列食品 生态免洗型健康蔬菜礼盒298元 ¥298.00 生态免洗型健康蔬菜礼盒298元 ¥398.00 生态免洗型健康蔬菜礼盒598元 ¥598.00 2F生态农业 阳台菜园 生物酶系列肥料 生态大棚加工系统 阳台菜园 生物酶系列肥料 生态大棚加工系统 双面梯式无土栽培水培水耕阳台蔬菜种植架 ¥286.00 双面梯式无土栽培水培水耕阳台蔬菜种植水培架 ¥398.00 无土栽培水培水耕阳台蔬菜种植架 ¥398.00 阳台种植立体栽培无土栽培水培水耕阳台蔬菜种植架 ¥398.00 3F健康校园 东大白墨咖啡店 东大白墨咖啡店 美式咖啡 Coffee Americano ¥11.00 白墨特调 Coffee Bailments ¥19.00 卡布奇诺 Cappuccino ¥15.00 拿铁 Coffee Latte ¥16.00 太妃榛享 Taffy Nut Latte ¥17.00 榛果拿铁 Hazelnut Latte ¥17.00 4F环保中心 健康家居 空气净化中心 空气净化中心 健康家居 光触媒绢花玫瑰花，私人订制八十元每延米 ¥80.00 矢车菊光触媒绢花，私人订制八十元每延米 ¥80.00 绣球花光触媒绢花，私人订制八十元每延米 ¥80.00 蝴蝶兰光触媒绢花，私人订制八十元每延米 ¥80.00 欧式田园玫瑰光触媒绢花，私人订制八十元每延米 ¥80.00 欧式田园玫瑰光触媒绢花，私人订制八十元每延米 ¥80.00 绿植光触媒绢花，私人订制八十元每延米 ¥80.00 郁金香光触媒绢花，私人订制八十元每延米 ¥80.00 乳胶漆油漆韩国现代纳米杀菌负氧离子空气净化除甲醛涂料光触媒 ¥588.00 乳胶漆韩国现代纳米杀菌负氧离子空气净化除甲醛 ¥588.00 5F文化中心 瓷文化 茶文化 养生文化 咖啡文化 奶茶文化 茶文化 养生文化 咖啡文化 瓷文化 景德镇高档青花瓷茶具套装 ¥298.00 景德镇陶瓷茶具 ¥298.00 景德镇陶瓷器小花瓶家居装饰客厅摆件婚庆插花瓷瓶台面工艺品摆设 ¥680.00 景德镇陶瓷器小花瓶家居装饰客厅摆件婚庆插花瓷瓶台面工艺品摆设 ¥680.00 景德镇陶瓷器小花瓶家居装饰客厅摆件婚庆插花瓷瓶台面工艺品摆设 ¥680.00 茶宠 陶瓷紫砂 鸟哨茶宠 注水后吹 发出小鸟声 ¥28.00 泗水红砭石养生多功能梳子 ¥880.00 原生态木桶浴专用木桶 ¥80.00 泗水红砭石养生多功能半圆梳 ¥880.00 原生态泡浴系列原木足浴盆 ¥80.00 泗水红砭石养生刮痧板 ¥780.00 泗水红砭石养生刮刀 ¥680.00 泗水红砭石养生刮痧s板 ¥680.00 泗水红砭石养生观音坠 ¥980.00 1F健康厨房 2F生态农业 3F健康校园 4F环保中心 5F文化中心 联系客服 我要反馈 联系客服 测试qq2 测试qq1 千米客服 晒家分享 热门活动 购物指南 购物流程 账户安全 配送方式 支付方式 特色服务 售后服务 关于我们 | 联系我们 | 人才招聘 | 商家入驻 | 广告服务 | 手机千米 | 友情链接 | 销售联盟 | English Site 南京市公安局朝阳分局备案编号110105014669  |  京ICP证070359号  |  互联网药品信息服务资格证编号(京)-非经营性-2011-0034 Copyright © 2004-2014  www.baidu.com 版权所有";
//		System.out.println(getCopyright("版权所有：www.yumingben.com备案号：京ICP备07007526号-3"));
//		System.out.println(pattern_url.matcher("日记大全").find());
//		
////		Matcher matcher = pattern_url.matcher("欢迎来到  www.baidu.com");
////		if(matcher.find()){
////		System.out.println(matcher.group(2));
////		}
//	}
	
	
	public static Pattern pattern_welcome_0 = Pattern
			.compile("欢迎(来到|光临|访问|浏览)([\u4E00-\u9FA50-9a-zA-Z]+)");
	
	// Copyright © 2016 qianpinxiu.com 版权所有 苏ICP备16034059号
	// Copyright?2014 蘑菇云? 地址：福建泉州市丰泽区东海大街东海湾中心一号楼1502? 电话：0595-22891399? 经营许可证：闽ICP备15025789号-1 
	// Copyright 信用网 All Rights Reserved
	
	public static Pattern pattern_welcome_1 = Pattern
			.compile("([\u4E00-\u9FA50-9a-zA-Z]+)欢迎您");

	
	public static Pattern pattern_url = Pattern
			.compile("^([\\w-[\\.]]+)\\.com$");
	
	public static Pattern pattern_copyright1 = Pattern
			.compile("版权所有\\s*[：|:]\\s*([\u4E00-\u9FA5\\w\\.@]+)");
	
	public static Pattern pattern_copyright2 = Pattern
			.compile("([\u4E00-\u9FA5\\w\\.@]+)\\s*版权所有");
	
//	public static void main(String[] args) {
////String s = "马兰花开 您好，欢迎来到马兰花开电子商城 [请登录] [免费注册] 会员中心 我的订单 加入收藏 分享给好友 加入收藏 请使用菜单栏或Ctrl+D进行添加！ 确定 分享好友 复制 取消 亲！如果您的好友成功注册为会员，会有积分送给您哦！ 登录提示 您未登录！现在确认要登录吗？ 确定 取消 操作提示 复制成功！ 确定 我的购物车 0 购物车中还没有商品，再去逛逛吧！ 正在为您加载数量！ ¥ 0.00 结算 购物车 共 0 0 件 热门搜索： 有机黄瓜 绢花 生物酶 空气净化 全部商品分类 商城新闻 更多>> 【商城新闻】 福缘地酶系列产品发布会 【商城新闻】 水母网牵手“福缘地”走进万达 【商城新闻】 马兰花开电子商城即将启动 【商城新闻】 “健康传播者”孙革新教授福缘地行 【商城新闻】 健康厨房进万家公益活动系列宣传片—— 【商城新闻】 “福缘地”携手水母网 共同传递人生 今日推荐 热门推荐 换一批 1F健康厨房 健康果蔬 生物酶洗涤用品 张大姐系列食品 生物酶洗涤用品 健康果蔬 张大姐系列食品 生态免洗型健康蔬菜礼盒298元 ¥298.00 生态免洗型健康蔬菜礼盒298元 ¥398.00 生态免洗型健康蔬菜礼盒598元 ¥598.00 2F生态农业 阳台菜园 生物酶系列肥料 生态大棚加工系统 阳台菜园 生物酶系列肥料 生态大棚加工系统 双面梯式无土栽培水培水耕阳台蔬菜种植架 ¥286.00 双面梯式无土栽培水培水耕阳台蔬菜种植水培架 ¥398.00 无土栽培水培水耕阳台蔬菜种植架 ¥398.00 阳台种植立体栽培无土栽培水培水耕阳台蔬菜种植架 ¥398.00 3F健康校园 东大白墨咖啡店 东大白墨咖啡店 美式咖啡 Coffee Americano ¥11.00 白墨特调 Coffee Bailments ¥19.00 卡布奇诺 Cappuccino ¥15.00 拿铁 Coffee Latte ¥16.00 太妃榛享 Taffy Nut Latte ¥17.00 榛果拿铁 Hazelnut Latte ¥17.00 4F环保中心 健康家居 空气净化中心 空气净化中心 健康家居 光触媒绢花玫瑰花，私人订制八十元每延米 ¥80.00 矢车菊光触媒绢花，私人订制八十元每延米 ¥80.00 绣球花光触媒绢花，私人订制八十元每延米 ¥80.00 蝴蝶兰光触媒绢花，私人订制八十元每延米 ¥80.00 欧式田园玫瑰光触媒绢花，私人订制八十元每延米 ¥80.00 欧式田园玫瑰光触媒绢花，私人订制八十元每延米 ¥80.00 绿植光触媒绢花，私人订制八十元每延米 ¥80.00 郁金香光触媒绢花，私人订制八十元每延米 ¥80.00 乳胶漆油漆韩国现代纳米杀菌负氧离子空气净化除甲醛涂料光触媒 ¥588.00 乳胶漆韩国现代纳米杀菌负氧离子空气净化除甲醛 ¥588.00 5F文化中心 瓷文化 茶文化 养生文化 咖啡文化 奶茶文化 茶文化 养生文化 咖啡文化 瓷文化 景德镇高档青花瓷茶具套装 ¥298.00 景德镇陶瓷茶具 ¥298.00 景德镇陶瓷器小花瓶家居装饰客厅摆件婚庆插花瓷瓶台面工艺品摆设 ¥680.00 景德镇陶瓷器小花瓶家居装饰客厅摆件婚庆插花瓷瓶台面工艺品摆设 ¥680.00 景德镇陶瓷器小花瓶家居装饰客厅摆件婚庆插花瓷瓶台面工艺品摆设 ¥680.00 茶宠 陶瓷紫砂 鸟哨茶宠 注水后吹 发出小鸟声 ¥28.00 泗水红砭石养生多功能梳子 ¥880.00 原生态木桶浴专用木桶 ¥80.00 泗水红砭石养生多功能半圆梳 ¥880.00 原生态泡浴系列原木足浴盆 ¥80.00 泗水红砭石养生刮痧板 ¥780.00 泗水红砭石养生刮刀 ¥680.00 泗水红砭石养生刮痧s板 ¥680.00 泗水红砭石养生观音坠 ¥980.00 1F健康厨房 2F生态农业 3F健康校园 4F环保中心 5F文化中心 联系客服 我要反馈 联系客服 测试qq2 测试qq1 千米客服 晒家分享 热门活动 购物指南 购物流程 账户安全 配送方式 支付方式 特色服务 售后服务 关于我们 | 联系我们 | 人才招聘 | 商家入驻 | 广告服务 | 手机千米 | 友情链接 | 销售联盟 | English Site 南京市公安局朝阳分局备案编号110105014669  |  京ICP证070359号  |  互联网药品信息服务资格证编号(京)-非经营性-2011-0034 Copyright © 2004-2014  www.baidu.com 版权所有";
//		
//		String s = "邮箱：1028249634@qq.com 版权所有 :  磁县巧莲医院有限公司";
//System.out.println(getCopyright(s));
//System.out.println(getCopyright(s).matches("(www\\.|^)[0-9a-zA-Z\\-]+\\.com"));
//
////Matcher matcher = pattern_url.matcher("欢迎来到  www.baidu.com");
////if(matcher.find()){
////System.out.println(matcher.group(2));
////}
//}
//	
	// zhaojingdm.com 版权所有
	// 邮箱：1028249634@qq.com 版权所有：磁县巧莲医院有限公司
	public static String getCopyright(Document document,String input) {
	
		for (Element element : document.getAllElements()) {
			if(element.getAllElements().size()>1)
				continue;
			String copyright = _getCopyright(element.text());
			if(copyright!=null)
				return copyright;
		}
		
//		for (Element element : document.select("em")) {
//			String copyright = _getCopyright(element.text());
//			if(copyright!=null)
//				return copyright;
//		}
//		
//		for (Element element : document.select("p")) {
//			String copyright = _getCopyright(element.text());
//			if(copyright!=null)
//				return copyright;
//		}
//		
//		for (Element element : document.select("a")) {
//			String copyright = _getCopyright(element.text());
//			if(copyright!=null)
//				return copyright;
//		}
//		
//		for (Element element : document.select("div")) {
//			String copyright = _getCopyright(element.text());
//			if(copyright!=null)
//				return copyright;
//		}
		
		
		
		return EMPTY_STRING;
	}
	
	public static String _getCopyright(String input) {
		Matcher matcher = null;
		matcher = pattern_copyright1.matcher(input);
		if (matcher.find()) {
			return matcher.group(1);
		}
		matcher = pattern_copyright2.matcher(input);
		if (matcher.find()) {
			return matcher.group(1);
		}
		return null;
	}
	
	public static String getWebsiteName(String input) {
	
		if (input.contains("欢迎来到") || input.contains("欢迎光临")
				|| input.contains("欢迎访问") || input.contains("欢迎浏览")) {

			Matcher matcher = null;
			matcher = pattern_welcome_0.matcher(input);
			if (matcher.find()) {
				return matcher.group(2);
			}
			
			matcher = pattern_welcome_1.matcher(input);
			if (matcher.find()) {
				return matcher.group(1);
			}

		}

		return EMPTY_STRING;

	}

	
}
