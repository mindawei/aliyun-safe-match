package safe.webshell;

import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 验证文本是否可执行
 * @author mindw
 */
public class TextChecker {
	
	static String cmd = "([\\W]|^)("
			+ "eval"
			+ "|assert"
			+ "|exec"
			+ "|execute"
			+ "|curl_exec"
			+ "|system"
			+ "|shell_exec"
			+ "|pcntl_exec"
			+ "|popen"
			+ "|proc_open"
			+ "|passthru"
			+ "|call_user_func"
			+ "|ReflectionMethod"
			+ "|ReflectionFunction"
			+ "|ExecuteGlobal"
			+ "|escapeshellcmd"
			+ ")+";
	


	
	/** 正则表达式 */
	static List<String> regexs = Arrays
			.asList(
					
					"function_exists\\s*\\(\\s*[\'|\"](popen|exec|proc_open|system|passthru)+[\'|\"]\\s*\\)",
					cmd+"\\s*\\(\\s*\\$_(\\w+)\\[(.*)\\]\\s*\\)",
				    "((udp|tcp)://(.*);)+",
				    "preg_replace\\s*\\((.*)/e(.*),\\s*\\$_(.*),(.*)\\)",
				    "preg_replace\\s*\\((.*)\\(base64_decode\\(\\$",
				    "(eval|assert|include|require|include_once|require_once)+\\s*\\(\\s*(base64_decode|str_rot13|gz(\\w+)|file_(\\w+)_contents|(.*)php://input)+",
				    "(eval|assert|include|require|include_once|require_once|array_map|array_walk)+\\s*\\(\\s*\\$_(GET|POST|REQUEST|COOKIE|SERVER|SESSION)+\\[(.*)\\]\\s*\\)",
				    cmd+"\\s*\\(\\s*\\(\\s*\\$\\$(\\w+)",
				    "(include|require|include_once|require_once)+\\s*\\(\\s*[\'|\"](\\w+)\\.(jpg|gif|ico|bmp|png|txt|zip|rar|htm|css|js)+[\'|\"]\\s*\\)",
				    "\\$_(\\w+)(.*)(eval|assert|include|require|include_once|require_once)+\\s*\\(\\s*\\$(\\w+)\\s*\\)",
				    "\\(\\s*\\$_FILES\\[(.*)\\]\\[(.*)\\]\\s*,\\s*\\$_(GET|POST|REQUEST|FILES)+\\[(.*)\\]\\[(.*)\\]\\s*\\)",
				    "(fopen|fwrite|fputs|file_put_contents)+\\s*\\((.*)\\$_(GET|POST|REQUEST|COOKIE|SERVER)+\\[(.*)\\](.*)\\)",
				    "echo\\s*curl_exec\\s*\\(\\s*\\$(\\w+)\\s*\\)",
				    "new com\\s*\\(\\s*[\'|\"]shell(.*)[\'|\"]\\s*\\)",
				    "\\$(.*)\\s*\\((.*)/e(.*),\\s*\\$_(.*),(.*)\\)",
				    "\\$_=(.*)\\$_",
				    "\\$_(GET|POST|REQUEST|COOKIE|SERVER)+\\[(.*)\\]\\(\\s*\\$(.*)\\)",
				    "\\$(\\w+)\\s*\\(\\s*\\$_(GET|POST|REQUEST|COOKIE|SERVER)+\\[(.*)\\]\\s*\\)",
				    "\\$(\\w+)\\s*\\(\\s*\\$\\{(.*)\\}",
				    "\\$(\\w+)\\s*\\(\\s*chr\\(\\d+\\)",
				    
				    // --------------------
				    // eval request("sb")
				    cmd+"\\s+request\\s*\\(\\s*\"(.)*\"\\)",
				    // execute(request("sb")
				    cmd+"\\s*\\(\\s*request\\s*\\(\\s*\"(.)*\"\\)",
				    //eval (eval(chr(114)+chr(101)+chr(113)+chr(117)+chr(101)+chr(115)+chr(116))("a"))
				    cmd+"\\s*\\(\\s*chr\\s*\\(\\s*\\d+\\s*\\)\\s*\\+\\s*chr\\s*\\(",
				    // eval("e"&"v"&"a"&"l"&"("&"r"&"e"&"q"&"u"&"e"&"s"&"t"&"("&"0″&"-"&"2″&"-"&"5″&")"&")")
				    cmd+"\\s*\\(\\s*[\"|\'](\\w)+[\"|\']\\s*&\\s*[\"|\'](\\w)+[\"|\']",
				    // eval(Request.Item["pass"],"unsafe");
				    cmd+"\\s*\\(\\s*Request\\.Item\\[\\s*\"(\\w+)\"\\s*\\]\\s*",
				    // new ReflectionFunction($_GET[m])
				    "([\\W]|^)new\\s+ReflectionFunction\\s*\\(\\s*\\$_",
				    // eval($fatezero);
				    cmd+"\\s*\\(\\$\\w+\\)",
				    // eval^($_POST[c]^);
				    cmd+"\\W*\\(\\W*\\$_POST\\[",
				    // shell.php?m=system&c=wget http://xxx.xxx/igenus/images/suffix/test.php 
				    "([\\W]|^)wget\\s*http://",
				    // @eval(get_magic_quotes_gpc()
				    cmd+"\\s*\\(\\s*get_magic_quotes_gpc",
				    // new java.io.FileOutputStream().write(request.getParameter(  
				    "\\.write\\s*\\(\\s*request\\.(form|item|getparameter)",
				    "echo\\s+`(.)*`",
				    cmd+"\\s*\\(\\s*System\\.Text\\.Encoding.GetEncoding\\(",
				    cmd+"\\s*\\(\\s*System\\.Convert\\.FromBase64String\\(",
				    cmd+"(\\W)*\\((\\W)*\\$\\{(\\W)*[\"|\']_(GET|POST|REQUEST|COOKIE|SERVER)[\"|\']\\}"
					 
			);

	static List<Pattern> patterns = new LinkedList<>();

	static {
		for (String regex : regexs) {
			patterns.add(Pattern.compile(regex, Pattern.CASE_INSENSITIVE));
		}
	}		
	
	// >>> asp
	
	private static boolean _check(String input){
		
		for (Pattern pattern : patterns) {
			if (pattern.matcher(input).find()) {
				return true;
			}
		}
		
		return false;
	}
	
	static List<String> keys = Arrays.asList("eval", "assert", "exec",
			"execute", "curl_exec", "system", "shell_exec", "pcntl_exec",
			"popen", "proc_open", "passthru", "call_user_func",
			"ReflectionMethod", "ReflectionFunction", "ExecuteGlobal",
			"escapeshellcmd", "$_GET", "$_POST", "$_REQUEST", "$_COOKIE",
			"$_SERVER");

   static boolean isDangerous(String input){
		for(String key : keys){
			if(input.contains(key))
				return true;
		}
		return false;
	}

	
	
	static boolean isExecutable(String input){
		
		// 去注释
		input = removeZhushi(input);
		
		boolean isDangerous = isDangerous(input);
		
		// 字符串拼接
		input = input.replaceAll("\"\\s*\\.\\s*\"", "")
		.replaceAll("'\\s*\\.\\s*'", "")
		.replaceAll("\"\\s*\\+\\s*\"", "")
		.replaceAll("'\\s*\\+\\s*'", "")
		.replaceAll("'\\s*&\\s*'", "")
		.replaceAll("\"\\s*&\\s*\"", "");
		
		if(!isDangerous){
			if(isDangerous(input))
				return true;
		}
		
		
		// base64_decode|str_rot13|urldecode|gzinflate|gzdecode
		if(input.contains("base64_decode")
				||input.contains("str_rot13")
				||input.contains("urldecode")
				||input.contains("gzinflate")
				||input.contains("gzdecode")			
				||input.contains("Request.Files.Count")
				||input.contains("Request.Files[")
				||input.contains("System.Convert.FromBase64String")
				||input.contains("request.getParameter")
				||input.contains("Runtime.getRuntime().exec(")
				||input.contains("Class.forName("))
			
			return true;
		
		
//		if (input.contains("(") && input.contains(")")) {

			if (_check(input)) {
				return true;
			}

			for (int i = 0; i < 2; ++i) {
				// chr(99).chr(104).chr(114) -> 'c'.'h'.'r' -> 'chr'
				input = charNumberToChar(input);
				input = input.replace("'.'", "");
				input = variableNameToVariable(input);
				if (_check(input)) {
					return true;
				}
			}

//		}

		return false;
	}
	

	
	
	// chr(数字)
	private static Pattern pat_fun_char = Pattern.compile("chr\\(([\\d]{1,3})\\)");
	// chr(99).chr(104).chr(114) -> 'c'.'h'.'r'
	private static String charNumberToChar(String input){
		Matcher matcher = pat_fun_char.matcher(input);
		while(matcher.find()){
			try{
				String charStr = matcher.group(0);
				String number = matcher.group(1);
				input = input.replace(charStr,"'"+(char)Integer.parseInt(number)+"'");
			}catch(Exception e){
				continue;
			}
		}
		return input;
	}
	
	// $_uU(118) -> char(118)  $_uU='chr' ${_uU}='chr'
	private static Pattern pat_var_name = Pattern.compile("(\\$[_a-zA-Z]+[0-9a-zA-Z]*)='((.)*)';");
	private static String variableNameToVariable(String input){
		
		Map<String,String> variableMap = new HashMap<>();
		Matcher matcher = pat_var_name.matcher(input);
		while(matcher.find()){	
			String key  = matcher.group(1);
			variableMap.put(key, matcher.group(2));	
			
			// $_uU ${_uU}
			StringBuilder builder = new StringBuilder();
			builder.append("${");
			builder.append(key.substring(1));			
			builder.append("}");
			key = builder.toString();
			
			variableMap.put(key, matcher.group(2));	
		}
		
		for(Map.Entry<String, String> entry : variableMap.entrySet()){
			input = input.replace(entry.getKey(), entry.getValue());
		}
		return input;
	}
	
	
	// 去掉 /* */
		private static String removeZhushi(String str) {

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
	
}
