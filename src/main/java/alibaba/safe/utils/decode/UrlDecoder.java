package alibaba.safe.utils.decode;

import java.net.URLDecoder;


/**
 * urlencode()函数原理就是首先把中文字符转换为十六进制，然后在每个字符前面加一个标识符%。
 * urldecode()函数与urlencode()函数原理相反，用于解码已编码的 URL 字符串。
 */
public class UrlDecoder implements Decoder {

	@Override
	public String decode(String str) {
		try {
			return URLDecoder.decode(str,"gb2312");
		} catch (Exception e) {
			return str;
		}
	}
	
}
