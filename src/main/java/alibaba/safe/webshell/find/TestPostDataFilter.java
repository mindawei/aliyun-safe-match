package alibaba.safe.webshell.find;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import alibaba.safe.utils.decode.Base64Decoder;
import alibaba.safe.utils.decode.UrlDecoder;

public class TestPostDataFilter {
	public static void main(String[] args) throws IOException {
		
	    PostDataFilter postDataFilter = PostDataFilter.getInstance();
		
	    
//		String postData2 = "xise=edoced_46esab&xise:baidu.com....&z0=NjY2MDI1O0Bpbmlfc2V0KCJkaXNwbGF5X2Vycm9ycyIsIjAiKTtAc2V0X3RpbWVfbGltaXQoMCk7QHNldF9tYWdpY19xdW";
//		System.out.println(postDataFilter.isWebShell(postData2));
//		System.exit(0);
	
	    File file = new File("webshell.txt");
		//File file = new File("good.txt");
		
		FileReader reader = new FileReader(file);
		BufferedReader bufferedReader = new BufferedReader(reader);
		String postData = null;
		int num = 0;
		while((postData=bufferedReader.readLine()) != null){
			
			//postDataFilter.getRawData(postData);
			
			if(postDataFilter.isWebShell(postData)){
				//System.out.println(postData);
				//System.out.println();
				num++;
			}else{
				System.out.println(postData);
				System.out.println("-------------");
				String str = postData.substring(postData.lastIndexOf('=')+1);
				System.out.println(str);
				System.out.println(new Base64Decoder().decode(str));
				System.out.println();
			}
			
//			if(postData.contains("Execute++++")){
//				System.out.println(postData);
//				String str = postData.substring(postData.lastIndexOf('=')+1);
//				System.out.println(str);
//				System.out.println(new Base64Decoder().decode(str));
//				System.out.println();
//			}
		}
		System.out.println(num);
		
		bufferedReader.close();
		
	}
	
}
