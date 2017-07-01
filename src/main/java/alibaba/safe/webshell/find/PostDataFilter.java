package alibaba.safe.webshell.find;

import alibaba.safe.decode.Base64Decoder;
import alibaba.safe.decode.Rot13Decoder;
import alibaba.safe.decode.UrlDecoder;

/** 找到异常数据 */
public final class PostDataFilter {
	
	private static final PostDataFilter instance = new PostDataFilter();
	
	private PostDataFilter(){}
	
	public static PostDataFilter getInstance(){
		return instance;
	}
	
	
	private final UrlDecoder urlDecoder = new UrlDecoder();
	private final Base64Decoder base64Decoder = new Base64Decoder();
	private final Rot13Decoder rot13Decoder = new Rot13Decoder();
	
	/** 是否是异常数据 */
	final boolean isWebShell(String postData){	
		// 分解参数 
		String[] parameters = postData.split("&");
		
		//StringBuilder builder = new StringBuilder();
		// 处理每一个参数 :key=value
		for(String parameter : parameters){
			
			int beginIndex = parameter.indexOf('=');
			
			if(beginIndex==-1)
				continue;
			
			parameter = parameter.substring(beginIndex+1);		
			
			if(isParameterDangerous(parameter)
					||isParameterDangerous(reverse(parameter)))
				return true;
			
			//builder.append(parameter);
		}
		
		// 拼接
//		String parameter = builder.toString();
//		if(isParameterDangerous(parameter)
//				||isParameterDangerous(reverse(parameter)))
//			return true;
		
		
		return false;
	}
	
	
	private final boolean check(String parameters){
		//String lowerCaseParameters = removeZhushi(parameters.toLowerCase());
		
		if(WebShellFilter.check(parameters))
			return true;
		
//		if(php.check(lowerCaseParameters)){
//			return true;
//		}
//		
//		if(html.check(lowerCaseParameters))
//			return true;
//		
//		if(asp.check(lowerCaseParameters))
//			return true;
//		
//		
//		if(jsp.check(lowerCaseParameters)){
//			return true;
//		}
		
		
		return false;
	}
	
	
	private final String reverse(String s) {
		return new StringBuilder(s).reverse().toString();
	}
	
	
//	/** 判断参数是否有问题 */
	private final boolean isParameterDangerous(String parameter){
			
		if(check(parameter)||check(rot13Decoder.decode(parameter)))
			return true;
			
		if(parameter.contains("%")){
			parameter = urlDecoder.decode(parameter);
			if(check(parameter))
				return true;
		}
		
		parameter = base64Decoder.decode(parameter);
		if(check(parameter)||check(rot13Decoder.decode(parameter)))
			return true;
		
		return false;
	}
	

//	/** 判断参数是否有包含关键字 */
//	private final boolean isParameterContainsDangerousKeys(String parameter){
//		
//		
////		for(String key  : keysInHtml){
////			if(lowerCaseParameters.contains(key))
////				return true;
////		}
//	
//		
////		for(String key  : keysInAsp){
////			if(lowerCaseParameters.contains(key))
////				return true;
////		}
//		
//		return false;
//	}
	
//	public void getRawData(String postData) {
//		
//		System.out.println("-----------------");
//		
//		System.out.println("raw:");
//		System.out.println(postData);
//		
//		System.out.println("urlDecoder:");
//		postData = urlDecoder.decode(postData);
//		System.out.println(postData);
//		
//		System.out.println("remove /**/:");
//		postData = removeZhushi(postData);
//		System.out.println(postData);
//		
//		
//		System.out.println("base64Decoder:");
//		System.out.println(base64Decoder.decode(postData));
//		
//		System.out.println();
//		
//	}
	
//	public static void main(String[] args) {
//		
//		String str = "1234/*/hahah/我去你骂了隔壁*/exce(中间部分)/*我你骂了隔壁*/312";
//		
//		System.out.println(removeZhushi(str));
//		
//	}
	
	
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
	
	///////////////////
	
	
	
}
