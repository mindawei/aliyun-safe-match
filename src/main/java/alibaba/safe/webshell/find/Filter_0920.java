package alibaba.safe.webshell.find;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Filter_0920 {
	
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
	private static String functionStr = "";
	static{
		functionStr+="(";
		
		// php
		functionStr+="eval|assert|call_user_func|system|passthru|popen|shell_exec|exec";
		functionStr+="|ReflectionMethod";
		functionStr+="|file_get_contents|file_put_contents";
		functionStr+="|array_map|array_walk|array_filter|array_reduce|usort|uksort";
		functionStr+="|preg_replace|str_replace";
		functionStr+="|ini_set|set_time_limit";
		functionStr+="|include|include_once|require|require_once";
		functionStr+="|fopen|fwrite|fputs|file_put_contents|opendir|basename|dirname|pathinfo|scandir";
		functionStr+="|mysql_query|mysqli_query|sqlite_query|sqlite_single_query";
		// 类似 $_POST['sa']($_POST['sb'])
		functionStr+="|\\$_(GET|POST|REQUEST|SESSION|SERVER|COOKIE)\\[(.){1,30}\\]";
		// $_VAR()
		//functionStr+="|\\$([_a-zA-Z][_a-zA-Z0-9]*)"; 09.15 ?
		
		
		// asp
		// <%execute request(\"sb\")%>
		// <%execute(request(\"sb\"))%>
		functionStr+="|(Eval|eval|Execute|execute|ExecuteGlobal)[\\s]{0,20}(\\()*[\\s]{0,20}(([_a-zA-Z][_a-zA-Z0-9]*)){0,1}";
		functionStr+="|Request|request|replace";
		
		
		functionStr+=")";
	}
	
	private static Pattern pat_function = Pattern.compile(functionStr+"[\\s]{0,20}\\((.){0,200}\\)[\\s]{0,20}");
		
	
	// >>> asp
	
	private static boolean _check(String input){
		
		if(pat_function.matcher(input).find()){
			return true;
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
		
		
		if (input.contains("(") && input.contains(")")) {

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

		}

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
