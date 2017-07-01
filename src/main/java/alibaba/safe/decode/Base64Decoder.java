package alibaba.safe.decode;

import Decoder.BASE64Decoder;

public class Base64Decoder implements Decoder {

	@Override
	public String decode(String str) {
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
	
}
