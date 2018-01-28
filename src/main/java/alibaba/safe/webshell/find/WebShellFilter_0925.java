package alibaba.safe.webshell.find;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import alibaba.safe.utils.FileUtil;

public class WebShellFilter_0925 {

	static String functionStr = "("
			+ "eval|assert|exec|execute|curl_exec|system|shell_exec|pcntl_exec|popen|proc_open|passthru"
			+ "|call_user_func|ReflectionMethod|ReflectionFunction|ExecuteGlobal|escapeshellcmd"
			+ "|strreverse|usort|uksort"
			+ "|function_exists"
			+ "|\\$_(GET|POST|REQUEST|COOKIE|SERVER|FILES|SERVER)\\s*\\[(.*)\\]"
			+ "|\\$\\s*\\{\\s*[\"|\']_(GET|POST|REQUEST|COOKIE|SERVER|FILES|SERVER)[\"|\']\\s*\\}\\s*\\[(.*)\\]"
			+ "|\\$[\\w|\"|\'|\\[|\\]|\\.|\\.{|\\}]+" // $ 任何变量形式
			+ "|System.Convert.FromBase64String"
			+ "|urldecode|json_decode|base64_decode|str_rot13|gzinflate"
			+ "|request.getParameter|request.querystring|request.form|request.getSession\\(\\).getAttribute"
			+ "|Server.CreateObject"
			+ "|include|require|include_once|require_once"
			+ "|mysql_query|mysqli_query|sqlite_query|sqlite_single_query"
			+ "|fopen|fwrite|fputs|file_get_contents|file_put_contents|opendir|basename|dirname|pathinfo|scandir"
			+ "|FileOutputStream|FileInputStream"
			+ "|System.Reflection.Assembly.Load|Request.BinaryRead"
			+ "|[\\w]+\\.write" + ")";

	static String cmdStr = "("
			+ "|\\$_(GET|POST|REQUEST|COOKIE|SERVER|FILES|SERVER)\\s*\\[(.*)\\]"
			+ "|\\$\\s*\\{\\s*[\"|\']_(GET|POST|REQUEST|COOKIE|SERVER|FILES|SERVER)[\"|\']\\s*\\}\\s*\\[(.*)\\]"
			+ "|\\$\\w+|\\$\\{\\w+\\}" + "|[\"|\'](.+)[\"|\']"
			+ "|(\\w+)\\s*\\((.*)\\)"
			+ "|request.form\\s*\\(\\s*[\"|\'](.+)[\"|\']\\s*\\)"
			+ "|request.item\\s*\\[\\s*[\"|\'](.+)[\"|\']\\s*\\]"
			+ "|Request\\.Item\\s*\\[\\s*\"(.+)\"\\s*\\](.)*" + ")";

	/** 正则表达式 */
	static List<String> regexs = Arrays
			.asList("([^\\w+]|^)" + functionStr + "\\s*\\(\\s*" + cmdStr
					+ "\\s*\\)",

					"\\(\\s*\\$_FILES\\[(.*)\\]\\[(.*)\\]\\s*,\\s*\\$_(GET|POST|REQUEST|FILES)+\\[(.*)\\]\\[(.*)\\]\\s*\\)",
					"([^\\w+]|^)new com\\s*\\(\\s*[\'|\"]shell(.*)[\'|\"]\\s*\\)",
					"\\$(.*)\\s*\\((.*)/e(.*),\\s*\\$_(.*),(.*)\\)",
					"\\$_=(.*)\\$_",
					"\\$(\\w+)\\s*\\(\\s*chr\\(\\d+\\)",
					"([^\\w+]|^)(execute|eval|ExecuteGlobal)\\s+request\\s*\\(\\s*\"(.)*\"\\)",
					"([^\\w+]|^)Request\\.Files[\\[|\\(](.*)[\\]|\\)]\\.SaveAs",
					"([^\\w+]|^)Server\\.MapPath\\s*\\(\\s*Request",
					"\\w+\\s*=\\s*request\\s*\\(\\s*[\"|\'](.+)[\"|\']\\s*\\)",
					"([^\\w+]|^)ini_set\\s*\\(\\s*\"(.*)\"\\s*,\\s*\"(.*)\"\\s*\\)",
					"([^\\w+]|^)(set_time_limit|error_reporting)\\s*\\(\\s*0\\s*\\)",
					"([^\\w+]|^)(array_map|array_walk|array_filter|array_reducepreg_replace|str_replace|replace|substr|preg_replace)\\s*\\(\\s*(.)+(,(.)+)+\\s*\\)",

					"([^\\w+]|^)" + functionStr + "\\s*\\(\\s*Request",
					"echo\\s*`(.)*`", "((udp|tcp)://(.*);)+"

			);

	static List<Pattern> patterns = new LinkedList<>();

	static {
		for (String regex : regexs) {
			patterns.add(Pattern.compile(regex, Pattern.CASE_INSENSITIVE));
		}
	}

	static boolean check(String input) {

		// 去注释
		input = removeZhushi2(input);

		// 字符串拼接
		input = input.replaceAll("\"\\s*\\.\\s*\"", "")
				.replaceAll("'\\s*\\.\\s*'", "")
				.replaceAll("\"\\s*\\+\\s*\"", "")
				.replaceAll("'\\s*\\+\\s*'", "").replaceAll("'\\s*&\\s*'", "")
				.replaceAll("\"\\s*&\\s*\"", "");

		// Execute("Execute("":Function bd(byVal s):For i=1 To Len(s) Step 2:c=Mid(s,i,2):If IsNumeric(Mid(s,i,1)) Then:Execute(""""bd=bd&chr(&H""""&c&"""")""""):Else:Execute(""""bd=bd&chr(&H""""&c&Mid(s,i+2,2)&"""")""""):i=i+2:End If""&chr(10)&""Next:End Function:Response.Write(""""->|""""):
		String lowerCase = input.toLowerCase();
		if (lowerCase.contains("request.getparameter")) {
			if (lowerCase.contains("outputstream")
					|| lowerCase.contains("writer"))
				return true;
		}

		if (lowerCase.contains("tseuqer lave")) {
			if (lowerCase.contains("execute") || lowerCase.contains("reverse"))
				return true;
		}

		if (lowerCase.contains("wget http://"))
			return true;

		if (lowerCase.contains("base64_decode")
				|| lowerCase.contains("str_rot13")
				|| lowerCase.contains("urldecode")
				|| lowerCase.contains("gzinflate")
				|| lowerCase.contains("gzdecode")
				|| lowerCase.contains("system.convert.frombase64string")
				|| lowerCase.contains("runtime.getruntime().exec(")
				|| lowerCase.contains("system.reflection.assembly.load"))
			return true;

		if (_check(input))
			return true;

		// 变量替换
		return false;
	}

	// 去掉 /* */
	private static String removeZhushi2(String str) {

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

	static boolean checkByRemove(String input, String rmvStr) {
		int len = input.length();
		String dealedStr = input.replaceAll(rmvStr, "");

		// System.out.println(dealedStr);

		if (dealedStr.length() == len)
			return false;
		else
			return _check(dealedStr);
	}

	static boolean _check(String input) {

		for (Pattern pattern : patterns) {
			if (pattern.matcher(input).find()) {
				return true;
			}
		}

		return false;
	}

//	public static void main(String[] args) throws IOException {
//
//		BufferedReader reader = new BufferedReader(new FileReader(new File(
//				FileUtil.getBaseFilePath() + "/in.txt")));
//		String line = null;
//		int num = 0;
//		while ((line = reader.readLine()) != null) {
//			if (!check(line)) {
//				System.out.println(line);
//				num++;
//			}
//		}
//		System.out.println(num);
//
//	}

}
