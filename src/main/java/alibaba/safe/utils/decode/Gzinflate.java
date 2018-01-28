package alibaba.safe.utils.decode;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.util.zip.GZIPInputStream;

/**
 * GZIP 压缩
 * 
 * @author mindw
 */
public class Gzinflate implements Decoder {

	public String decode(String str) {
		throw new UnsupportedOperationException("");
	}

	/** 解析 byte 数组*/
	public String decode(byte[] strBytes) {

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
