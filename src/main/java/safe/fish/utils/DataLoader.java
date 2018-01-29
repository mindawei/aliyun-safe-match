package safe.fish.utils;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;

/**
 * 加载检测钓鱼网站相关的数据
 * 
 * @author mindw
 */
public class DataLoader {

	public static final String FISH_BASE_FILE_PATH = "data\\fish\\";
	
	/** 道路网站名和网址的映射（补充的热门的几个） */
	public static Map<String, String> loadKeysMap() {
		return _loadMap(FISH_BASE_FILE_PATH + "关键词名单.csv");
	}

	/** 道路网站名和网址的映射（来自站长统计） */
	public static Map<String, String> loadAllWhiteList() {
		return _loadMap(FISH_BASE_FILE_PATH + "所有白名单.csv");
	}

	/** 道路网站名和网址的映射（补充的热门的几个） */
	public static Map<String, String> loadHotWhiteList() {
		return _loadMap(FISH_BASE_FILE_PATH + "热门白名单.csv");
	}

	/**
	 * 百度,www.baidu.com 腾讯网,qq.com ...
	 */
	private static Map<String, String> _loadMap(String filePath) {
		Map<String, String> chinese2site = new HashMap<>();
		try {
			FileReader fileReader = new FileReader(filePath);
			CSVParser parser = new CSVParser(fileReader, CSVFormat.DEFAULT);

			parser.forEach(record -> {
				chinese2site.put(record.get(0), record.get(1));
			});

			parser.close();
			fileReader.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return chinese2site;
	}
	
	/** 加载顶级域名(常用域名结尾) */
	public static Set<String> loadDomains() {
		Set<String> domains = new HashSet<>();
		String filePath = FISH_BASE_FILE_PATH + "顶级域名.csv";
		try {
			FileReader fileReader = new FileReader(filePath);
			CSVParser parser = new CSVParser(fileReader, CSVFormat.DEFAULT);

			parser.forEach(record -> {
				domains.add(record.get(0));
			});

			parser.close();
			fileReader.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return domains;
	}

}
