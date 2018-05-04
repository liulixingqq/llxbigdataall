package demo.mr.sort.hadoop.number;

import java.io.IOException;

import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

//                                         k1            v1     k2        v2???是什么 ----> 空值
public class NumberMapper extends Mapper<LongWritable, Text, IntWritable, NullWritable> {

	@Override
	protected void map(LongWritable key1, Text value1,Context context)
			throws IOException, InterruptedException {
		// 数字: 10
		String data = value1.toString().trim();

		int number = Integer.parseInt(data);

		//输出：一定要把这个数字作为key2
		context.write(new IntWritable(number), NullWritable.get());
	}

}
