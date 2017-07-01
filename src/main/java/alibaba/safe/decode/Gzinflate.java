package alibaba.safe.decode;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.zip.GZIPInputStream;
import java.util.zip.Inflater;
import java.util.zip.InflaterInputStream;

public class Gzinflate implements Decoder{

	@Override
	@Deprecated
	public String decode(String str) {
	
		
		//InputStream inflInstream = new InflaterInputStream(new ByteArrayInputStream(str.getBytes()), 
        //        new Inflater());

		StringBuilder builder = new StringBuilder();
		
		try {
			
		InputStream inflInstream = new GZIPInputStream(new ByteArrayInputStream( str.getBytes() ));

		byte bytes[] = new byte[1024];

		int length;
		while (true) {
				length = inflInstream.read(bytes, 0, 1024);
				if (length == -1)  
					break;
				for(int i=0;i<length;++i)
					builder.append(bytes[i]);
		}
		
		} catch (IOException e) {}
	
		return builder.toString();
	}

}
