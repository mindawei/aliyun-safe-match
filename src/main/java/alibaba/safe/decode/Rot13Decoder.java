package alibaba.safe.decode;

public class Rot13Decoder implements Decoder {

	@Override
	public String decode(String str) {
		
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

}
