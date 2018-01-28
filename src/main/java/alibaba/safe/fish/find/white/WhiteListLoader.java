package alibaba.safe.fish.find.white;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;

/**
 * 加载白名单
 * 
 * @author mindw
 */
public class WhiteListLoader {

	public static Map<String,String> loadAll(){
		return _load("data\\chinese2site.csv");
	}
	
	public static Map<String,String> loadHot(){
		return _load("data\\\\chinese2site-hot.csv");
	}
	
	/**
	 * 百度,www.baidu.com
	 * 腾讯网,qq.com
	 * ...
	 */
	private static Map<String,String> _load(String filePath){
		Map<String,String> chinese2site = new HashMap<>();
		try {
			FileReader fileReader = new FileReader(filePath);
			CSVParser parser = new CSVParser(fileReader, CSVFormat.DEFAULT);
			
			parser.forEach(record->{
				chinese2site.put(record.get(0), record.get(1));
			});
			
			parser.close();
			fileReader.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}catch (IOException e) {
			e.printStackTrace();
		}
		return chinese2site;
	}
}
