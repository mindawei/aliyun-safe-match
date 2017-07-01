package alibaba.safe.webshell.analyze;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import javax.print.attribute.IntegerSyntax;

import org.apache.commons.csv.*;

public final class Analyze_Old {
	
	class IntSummaryStatistics{
		long sum = 0;
		int count = 0;
		int min = Integer.MAX_VALUE;
		int max = Integer.MIN_VALUE;
	
		void accept(int v){
			sum+=v;
			count++;
			if(v<min)
				min = v;
			if(v>max)
				max = v;
		}

		
		int avg(){
			int avg = 0 ;
			if(count>0){
				avg = (int)sum / count;
			}
			return avg;
		}
		
		@Override
		public String toString() {
			int avg = 0 ;
			if(count>0){
				avg = (int)sum / count;
			}
			return "[count=" + count + ", avg=" + avg
					+ ", min=" + min + ", max=" + max + "]";
		}
		
	}

	void run(String filePath) throws IOException{
		
		final String ID = "id";
		final String URL = "url";
		final String POST_DATA = "post_data";
		final String RESULT = "result";
		
		/** 正常标志 */
		final String RESULT_GOOD = "good";
		/** 后门标志*/
		final String RESULT_WEBSHELL = "webshell";
		
		IntSummaryStatistics goodPostDataLengthStatistics = new IntSummaryStatistics();
		IntSummaryStatistics webshellPostDataLengthStatistics = new IntSummaryStatistics();
		
		
		for (CSVRecord record : CSVFormat.EXCEL.withDelimiter(',')
				.withHeader(ID,URL,POST_DATA,RESULT)
				.withSkipHeaderRecord() // 去除首行
				.parse(new FileReader(filePath))) {
		
			try{
				String url = record.get(URL);
				String postData = record.get(POST_DATA);
				String result = record.get(RESULT);
				
				int postDataLength = postData.length();
				
				if(result.equals(RESULT_GOOD)){
					goodPostDataLengthStatistics.accept(postDataLength);
				}else if(result.equals(RESULT_WEBSHELL)){
					webshellPostDataLengthStatistics.accept(postDataLength);
				}
			
			}catch (Throwable e){
				continue;
			}
			
			
		}
		
		System.out.println("goodPostDataLengthStatistics: "+goodPostDataLengthStatistics);
		System.out.println("webshellPostDataLengthStatistics: "+webshellPostDataLengthStatistics);
		
	}
	
	
	
	
	// 分析 webshell
	public static void main(String[] args) throws IOException {
		String filePath = "webshell.csv";
		new Analyze_Old().run(filePath);
	}
}
