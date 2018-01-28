package alibaba.safe.utils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
/**
 * @author 闵大为
 * @date 2016年2月18日
 * @Description 文件相关工具类
 */
public class FileUtil {
	/** 获得程序目录 */
	public static String getBaseFilePath() {
		String baseFilePath = null;
		try {
			baseFilePath = new File(".").getCanonicalPath();
		} catch (IOException e) {
			System.err.println("error in FileUtil.getBaseFilePath()!");
		}
		return baseFilePath;
	}
	
	/** 创建文件夹*/
	public static boolean makeDirs(String folderName) {
		if (folderName == null || folderName.isEmpty()) {
			return false;
		}

		File folder = new File(folderName);
		return (folder.exists() && folder.isDirectory()) ? true : folder
				.mkdirs();
	}
	
	/** 添加文件 */
	public static FileWriter getAppednFileWriter(String outFilePath) {
		FileWriter fileWriter = null;
		try {
			//如果文件存在，则追加内容；如果文件不存在，则创建文件
			File f=new File(outFilePath);
			fileWriter = new FileWriter(f, true);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return fileWriter;
	}

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
