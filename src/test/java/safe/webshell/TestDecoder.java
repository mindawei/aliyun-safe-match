package safe.webshell;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.zip.GZIPOutputStream;
import org.junit.Assert;
import org.junit.Test;

import safe.webshell.Decoder;

/**
 * 测试解码
 * @author mindw
 */
public class TestDecoder {

	@Test
	public void testDecodeBASE64() {
		String decodeStr = Decoder.decodeBASE64("6L+Z5pivQkFTRee8lueggQ==");
		Assert.assertEquals("这是BASE编码", decodeStr);
	}
	
	@Test
	public void testDecodeRot13() {
		String decodeStr = Decoder.decodeRot13("Guvf vf Ebg13 rapbqr.");
		Assert.assertEquals("This is Rot13 encode.", decodeStr);
	}
	
	@Test
	public void testDecodeURL() {
		String decodeStr = Decoder.decodeURL("%d5%e2%ca%c7URL%b1%e0%c2%eb%a3%a1");
		Assert.assertEquals("这是URL编码！", decodeStr);
	}
	
	@Test
	public void testDecodeGzip() {
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
		String decodeStr =  Decoder.decodeGzip(bytes);
		Assert.assertEquals(str, decodeStr);
	}
	
}
