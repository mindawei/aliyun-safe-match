package alibaba.safe.decode;

import java.net.URLDecoder;


/**
 * urlencode()函数原理就是首先把中文字符转换为十六进制，然后在每个字符前面加一个标识符%。
 * urldecode()函数与urlencode()函数原理相反，用于解码已编码的 URL 字符串，其原理就是把十六
 */
public class UrlDecoder implements Decoder {

	@Override
	public String decode(String str) {
		try {
			return URLDecoder.decode(str,"gb2312");
		} catch (Exception e) {
			//System.out.println("UrlDecoder Exception");
			//System.out.println(str);
			return str;
		}
	}
	
//	public static void main(String[] args) {
//		System.out.println(new UrlDecoder().decode("%40eval%2F%2A%CE%D2%C8%A5%C4%E3%C2%EE%C1%CB%B8%F4%B1%DA%2A%2F%01%28%24%7B%27%5FP%27.%27OST%27%7D%5Bz9%5D%2F%2A%CE%D2%C8%A5%C4%E3%C2%EE%C1%CB%B8%F4%B1%DA%2A%2F%01%28%24%7B%27%5FPOS%27.%27T%27%7D%5Bz0%5D%29%29%3B"));
//	}

}
