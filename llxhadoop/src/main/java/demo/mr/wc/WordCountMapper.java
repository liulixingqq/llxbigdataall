package demo.mr.wc;

import java.io.IOException;

import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

//public class WordCountMapper extends Mapper<k1, v1, k2, v2> {
public class WordCountMapper extends Mapper<LongWritable, Text, Text, IntWritable> {

	@Override
	protected void map(LongWritable key1, Text value1, Context context)
			throws IOException, InterruptedException {
		/*
		 * context 代表Mapper的上下文
		 * 上文：HDFS
		 * 下文：Reducer
		 */

		//取出数据:  I love Beijing
		String data = value1.toString();

		//分词
		String[] words = data.split(" ");

		//输出
		for(String word:words){
			//            k2 就是 单词                          v2: 记一次数
			context.write(new Text(word), new IntWritable(1));
		}
	}
}
