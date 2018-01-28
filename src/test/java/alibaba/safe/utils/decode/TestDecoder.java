package alibaba.safe.utils.decode;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.zip.GZIPOutputStream;
import org.junit.Assert;
import org.junit.Test;

import alibaba.safe.utils.decode.Base64Decoder;
import alibaba.safe.utils.decode.Decoder;
import alibaba.safe.utils.decode.Gzinflate;
import alibaba.safe.utils.decode.Rot13Decoder;
import alibaba.safe.utils.decode.UrlDecoder;

/**
 * 测试解码
 * @author mindw
 */
public class TestDecoder {

	@Test
	public void testBase64Decoder() {
		Decoder decoder = new Base64Decoder();
		String decodeStr = decoder.decode("6L+Z5pivQkFTRee8lueggQ==");
		Assert.assertEquals("这是BASE编码", decodeStr);
	}
	
	@Test
	public void testRot13Decoder() {
		Decoder decoder = new Rot13Decoder();
		String decodeStr = decoder.decode("Guvf vf Ebg13 rapbqr.");
		Assert.assertEquals("This is Rot13 encode.", decodeStr);
	}
	
	@Test
	public void testUrlDecoder() {
		Decoder decoder = new UrlDecoder();
		String decodeStr = decoder.decode("%d5%e2%ca%c7URL%b1%e0%c2%eb%a3%a1");
		Assert.assertEquals("这是URL编码！", decodeStr);
	}
	
	@Test
	public void testGzinflate() {
		String str = "这是GZIP压缩";

		// 用GZIP 压缩
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		GZIPOutputStream gzip;
		try {
			gzip = new GZIPOutputStream(out);
			gzip.write(str.getBytes());
			gzip.close();
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
		byte[] bytes = out.toByteArray();
	
		// 解压
		String decodeStr =  new Gzinflate().decode(bytes);
		Assert.assertEquals(str, decodeStr);
	}
	
}
