package safe.fish.utils;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TitleDivider {
	
	public static void main(String[] args) {
		List<String> ls = new ArrayList<>();
		ls.add("支付宝全国授权商 -灵风易商是支付宝代理商，支付宝POS机技术开发商，最贴心的O2O平台解决方案提供商。");
		ls.add("重庆市那美品牌管理有限公司");
		ls.add("交通银行 - 交银金融网");
		ls.add("一网通主页 -- 招商银行官方网站");
		ls.add("中国工商银行中国网站");
		ls.add("成都POS机办理_成都POS机申请_成都移动POS机_成都银联POS机安装_成都银行POS机-成都东胜付信息技术有限公司");
		ls.add("中日在线,最老牌的日本点卡网站,Skype 阿里通 Q币 游戏点卡 支付宝");
		ls.add("快充网 空中平台 全国话费 Q币 支付宝 财付通 游戏点卡");
		ls.add("支付宝全国授权商 - 旺宝商盟是支付宝代理商，支付宝POS机技术开发商，最贴心的O2O平台解决方案提供商。");
		ls.add("云付宝官网-本赢支付招商中心，POS机招商代理 乐富POS机办理中心 中国银联POS机 ，云南钻玛商贸有限公司");
		ls.add("银行_hao123上网导航");
		ls.add("java匹配中文汉字的正则表达式 - divor - 博客园");
		ls.add("支付宝 - 网上支付 安全快速！");
		ls.add("支付宝pos机-商通道南京唯一的线下支付宝扫码支付平台");
		
		for(String input : ls) {
			getKeyWords(input);
		}
		
	}
	
	public static List<String> getKeyWords(String input){
	
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
		
		System.out.println(input+"\n"+keyWords.toString()+"\n");
		
		return keyWords;
	}
	
	/** 第一段中文 */
	private  static Pattern patternFirstChinese = Pattern.compile("[\u4E00-\u9FA5|[0-9a-zA-Z]]+");
	private  static String getFirstChineseWords(String input){
		Matcher matcher = patternFirstChinese.matcher(input);
		if(matcher.find()){
			input = matcher.group();
		}else{
			input = null;
		}
		return input;
	}
	
	/** 主语 是 */
	private  static Pattern patternShi = Pattern.compile("([\u4E00-\u9FA5|[0-9a-zA-Z]]+)是([\u4E00-\u9FA5|[0-9a-zA-Z]]+)");
	private  static String getWhatIs(String input){
		Matcher matcher = patternShi.matcher(input);
		if(matcher.find()){
			input = matcher.group(1);
			
		}else{
			input = null;
		}
		return input;
	}

}
