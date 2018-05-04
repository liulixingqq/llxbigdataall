package demo.mr.sort.text;

import java.io.IOException;

import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

//                                       k1             v1     k2      v2
public class MyTextMapper extends Mapper<LongWritable, Text, Text, NullWritable> {

	@Override
	protected void map(LongWritable key1, Text value1, Context context)
			throws IOException, InterruptedException {
		//数据：7654,MARTIN,SALESMAN,7698,1981/9/28,1250,1400,30
		String data = value1.toString();

		//分词
		String[] words = data.split(",");

		//输出job列 作为key2
		context.write(new Text(words[2]), NullWritable.get());
	}

}
