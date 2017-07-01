package alibaba.safe.fish.find;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;

public class TestHtmlContentFilter {
	public static void main(String[] args) throws IOException {
		
		FishFilter filter = new FishFilter();
			
		File file = new File("5.html");
		BufferedReader bufferedReader = new BufferedReader( new InputStreamReader(new FileInputStream(file),"utf-8"));
		//BufferedReader bufferedReader = new BufferedReader( new InputStreamReader(new FileInputStream(file),"gb2312"));

		StringBuilder builder = new StringBuilder();
		
		String htmlLine = null;
		while((htmlLine=bufferedReader.readLine()) != null){
			builder.append(htmlLine);
			builder.append("\n");
		}
		String htmlContent = builder.toString();
		
//		htmlContent = htmlContent.trim().toLowerCase();
//		int index = htmlContent.indexOf("icp备");
//		if(index!=-1){
//			String icp = htmlContent.substring(index+4,index+12);
//			System.out.println(icp);
//		}
		
		
		//System.out.println(htmlContent);
		System.out.println(filter.isFish("http://wx.weixincms.top",htmlContent));
		
		
		
//		System.out.println(htmlContentFilter.getTitle(htmlContent));
		
		bufferedReader.close();
		
//		System.out.println(isNetOk());
	
		
	}
	
	private static  boolean isNetOk(){ 
	    try {  
	    	  URL url = new URL("http://www.baidu.com");  
	         InputStream in = url.openStream();  
	         return true;
	    } catch (Exception e1) {    
	    }  
	    return false;
	}
	
}
