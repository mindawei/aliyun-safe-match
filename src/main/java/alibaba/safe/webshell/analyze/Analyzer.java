package alibaba.safe.webshell.analyze;

import java.io.IOException;
import java.util.Iterator;
import com.aliyun.odps.OdpsException;
import com.aliyun.odps.data.Record;
import com.aliyun.odps.data.TableInfo;
import com.aliyun.odps.mapred.JobClient;
import com.aliyun.odps.mapred.MapperBase;
import com.aliyun.odps.mapred.ReducerBase;
import com.aliyun.odps.mapred.conf.JobConf;
import com.aliyun.odps.mapred.utils.InputUtils;
import com.aliyun.odps.mapred.utils.OutputUtils;
import com.aliyun.odps.mapred.utils.SchemaUtils;

public class Analyzer {

	public static class TestMapper extends MapperBase {
		private Record key;
		private Record value;

		@Override
		public void setup(TaskContext context) throws IOException {
			key = context.createMapOutputKeyRecord();
			value = context.createMapOutputValueRecord();
		}

		@Override
		public void map(long recordNum, Record record, TaskContext context)
				throws IOException {
			// url,post_data,result
			key.set("result", record.getString(2));
			
			value.set("url", record.getString(0));
			value.set("post_data", record.getString(1));
			
			context.write(key, value);
		}

	}

	static class IntSummaryStatistics{
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
	}

	public static class TestReducer extends ReducerBase {

		Record output;

		@Override
		public void setup(TaskContext context) throws IOException {
			output = context.createOutputRecord();
		}

		// key:result value:url,post_data
		@Override
		public void reduce(Record key, Iterator<Record> values,
				TaskContext context) throws IOException {

			IntSummaryStatistics postDataLengthStatistics = new IntSummaryStatistics();
			while (values.hasNext()) {
				Record val = values.next();
				//String url = val.getString("url");
				String post_data = val.getString("post_data");
				
				postDataLengthStatistics.accept(post_data.length());
				
			}

		
			// post_data 最大长度
			output.set(0, key.getString("result"));
			output.set(1, "post_data_length_max");	
			output.set(2, ""+postDataLengthStatistics.max);
			context.write(output);
		
			// post_data 最小长度
			output.set(0, key.getString("result"));
			output.set(1, "post_data_length_min");	
			output.set(2, ""+postDataLengthStatistics.min);
			context.write(output);
			
			// post_data 平均长度
			output.set(0, key.getString("result"));
			output.set(1, "post_data_length_avg");	
			output.set(2, ""+postDataLengthStatistics.avg());
			context.write(output);
			
			// 个数
			output.set(0, key.getString("result"));
			output.set(1, "count");	
			output.set(2, ""+postDataLengthStatistics.count);
			context.write(output);
			
			
			
		}
		
	}
	
	public static void main(String[] args) throws OdpsException {

		if (args.length != 2) {
			System.err
					.println("Usage: PlaysCount <user_actions_combine> <user_actions_count_filter>");
			System.exit(2);
		}

		JobConf job = new JobConf();
		job.setMapperClass(TestMapper.class);
		job.setReducerClass(TestReducer.class);

		job.setMapOutputKeySchema(SchemaUtils.fromString("result:string"));
		job.setMapOutputValueSchema(SchemaUtils.fromString("url:string,post_data:string"));

		InputUtils.addTable(TableInfo.builder().tableName(args[0]).build(), job);
		OutputUtils.addTable(TableInfo.builder().tableName(args[1]).build(),job);

		JobClient.runJob(job);
	}

}
