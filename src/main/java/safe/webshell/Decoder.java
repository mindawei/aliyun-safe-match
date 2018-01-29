package safe.webshell;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLDecoder;
import java.nio.ByteBuffer;
import java.util.zip.GZIPInputStream;

import Decoder.BASE64Decoder;

/**
 * 解码器
 * @author mindw
 */
public class Decoder {
	
	/**
	 * 解析 BASE64 编码
	 */
	public static String decodeBASE64(String str) {
		byte[] b = null;
		String result = null;
		if (str != null) {
			BASE64Decoder decoder = new BASE64Decoder();
			try {
				b = decoder.decodeBuffer(str);
				result = new String(b, "utf-8");
			} catch (Exception e) {
				e.printStackTrace();
				return str;
			}
		}
		return result;
	}
	
	/**
	 * ROT13（回转13位，rotateby13places，有时中间加了个减号称作ROT-13）是一种简易的置换暗码。
	 */
	public static String decodeRot13(String str) {
		
		StringBuilder builder = new StringBuilder();

		for (char c : str.toCharArray()) {
			if (c >= 'a' && c <= 'm')
				c += 13;
			else if (c >= 'A' && c <= 'M')
				c += 13;
			else if (c >= 'n' && c <= 'z')
				c -= 13;
			else if (c >= 'N' && c <= 'Z')
				c -= 13;
			builder.append(c);
		}
		return builder.toString();

	}
	

	/**
	 * 用于解码已编码（把中文字符转换为十六进制，然后在每个字符前面加一个标识符%）的 URL字符串。
	 */
	public static String decodeURL(String str) {
		try {
			return URLDecoder.decode(str,"gb2312");
		} catch (Exception e) {
			return str;
		}
	}
	
	/** 
	 * 解析 GZIP压缩数组
	 */
	public static String decodeGzip(byte[] strBytes) {

		try {

			InputStream inflInstream = new GZIPInputStream(new ByteArrayInputStream(strBytes));
			byte bytes[] = new byte[1024];
			ByteBuffer byteBuffer = ByteBuffer.allocate(1024 * 10);
			int length;
			while (true) {
				length = inflInstream.read(bytes, 0, 1024);
				if (length == -1)
					break;
				for (int i = 0; i < length; ++i)
					byteBuffer.put(bytes[i]);
			}

			byteBuffer.flip();
			byte[] data = new byte[byteBuffer.limit()];
			byteBuffer.get(data);

			return new String(data);

		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}

	}
}
