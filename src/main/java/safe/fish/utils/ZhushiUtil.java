package safe.fish.utils;

/**
 * 处理注释
 */
public class ZhushiUtil {

	public static String removeZhushi(String htmlContent) {
		// 去掉注释 <!-- -->
		htmlContent = removeZhushi1(htmlContent);
		// 去掉注释 /**/
		htmlContent = removeZhushi2(htmlContent);
//		// 去掉注释 //  http://
//		htmlContent = removeZhushi3(htmlContent);
		return htmlContent;
	}

	/**
	 *  去掉注释 <!-- -->
	 */
	public static String removeZhushi1(String str) {

		while (true) {

			int start = str.indexOf("<!--");
			if (start == -1)
				return str;

			int end = str.indexOf("-->", start + 4);
			if (end == -1)
				return str;

			end += 4;
			StringBuilder builder = new StringBuilder();
			for (int i = 0; i < str.length(); ++i) {
				if (i >= start && i < end)
					continue;
				builder.append(str.charAt(i));
			}
			str = builder.toString();
		}
	}

	// 去掉注释 /**/
	public static String removeZhushi2(String str) {

		while (true) {

			int start = str.indexOf("/*");
			if (start == -1)
				return str;

			int end = str.indexOf("*/", start + 2);
			if (end == -1)
				return str;

			end += 2;
			StringBuilder builder = new StringBuilder();
			for (int i = 0; i < str.length(); ++i) {
				if (i >= start && i < end)
					continue;
				builder.append(str.charAt(i));
			}
			str = builder.toString();
		}
	}

	// 去掉注释 //  但是会误判： http://
	public static String removeZhushi3(String str) {

		while (true) {

			int start = str.indexOf("//");
			if (start == -1)
				return str;

			int end = str.indexOf("\n", start + 2);
			if (end == -1)
				return str;

			end += 2;
			StringBuilder builder = new StringBuilder();
			for (int i = 0; i < str.length(); ++i) {
				if (i >= start && i < end)
					continue;
				builder.append(str.charAt(i));
			}
			str = builder.toString();
		}
	}

}
