package safe.webshell;

/** 
 * WebShell 检测器
 * @author mindw
 */
public final class WebShellDetector {

	/** 是否是异常数据 */
	public static final boolean isWebShell(String postData){	
		// 分解参数 
		String[] parameters = postData.split("&");
	
		// 处理每一个参数 :key=value
		for(String parameter : parameters){
			
			// 寻找分隔符 = 
			int beginIndex = parameter.indexOf('=');
			if(beginIndex==-1) {
				continue;
			}
			
			// 获得参数
			parameter = parameter.substring(beginIndex+1);		
			
			// 顺序逆序都判断一下 
			if(isParameterDangerous(parameter) || isParameterDangerous(reverse(parameter))) {
				return true;
			}
		}
		return false;
	}
	
	
	/** 判断参数是否有问题 */
	private static boolean isParameterDangerous(String str){
	
		if(isDangerous(str)){
			return true;
		}
		
		// URL 解码
		if(str.contains("%")){
			str = Decoder.decodeURL(str);	
			// 解码后，判断字符串本身 和 ROT13 解码
			if(isDangerous(str) || isDangerous(Decoder.decodeRot13(str))){
				return true;
			}
			
		}

		str = Decoder.decodeBASE64(str);
		// 解码后，判断字符串本身 和 ROT13 解码
		if(isDangerous(str)||isDangerous(Decoder.decodeRot13(str))) {
			return true;
		}
		
		return false;
	}
	
	/** 判断内容是否危险 */
	private static boolean isDangerous(String content) {
		return TextChecker.isExecutable(content);
	}
	
	/** 字符串反转 */
	private static String reverse(String s) {
		return new StringBuilder(s).reverse().toString();
	}
	
}
