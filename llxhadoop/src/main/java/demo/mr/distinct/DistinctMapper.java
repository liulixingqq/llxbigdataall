package demo.mr.distinct;

import java.io.IOException;

import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;


public class DistinctMapper extends Mapper<LongWritable, Text, Text, NullWritable> {

	@Override
	protected void map(LongWritable key1, Text value1, Context context)
			throws IOException, InterruptedException {
		// 数据  7654,MARTIN,SALESMAN,7698,1981/9/28,1250,1400,30
		String data = value1.toString();

		//分词
		String[] words = data.split(",");

		//将job作为key2进行输出
		context.write(new Text(words[2]), NullWritable.get());
	}


}
