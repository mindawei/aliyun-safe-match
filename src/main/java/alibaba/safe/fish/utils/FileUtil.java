package alibaba.safe.fish.utils;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
/**
 * @author 闵大为
 * @date 2016年2月18日
 * @Description 文件相关工具类
 */
public class FileUtil {
	
	/** 输出覆盖 */
	public static PrintWriter getPrintWriter(String outFilePath) {
		PrintWriter out = null;
		try {
			out = new PrintWriter(outFilePath);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			System.out.println(outFilePath+" not found!");
			System.exit(0);
		}
		return out;
	}
}
