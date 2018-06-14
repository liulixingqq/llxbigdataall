package demo.mr.wc;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;


public class WordCountMain {

	public static void main(String[] args) throws Exception {
		// 创建一个job：job = map + reduce
		Job job = Job.getInstance(new Configuration());

		//指定任务的入口
		job.setJarByClass(WordCountMain.class);

		//指定任务的Mapper和输出的数据类型: k2  v2
		job.setMapperClass(WordCountMapper.class);
		job.setMapOutputKeyClass(Text.class);    //指定k2
		job.setMapOutputValueClass(IntWritable.class);  //指定v2

		//增加一个Combiner的class
		job.setCombinerClass(WordCountReducer.class);

		//指定任务的Reducer和输出的数据类型: k4 v4
		job.setReducerClass(WordCountReducer.class);
		job.setOutputKeyClass(Text.class);   //指定k4
		job.setOutputValueClass(IntWritable.class);   //指定v4

		//指定输入的路径（map）、输出的路径（reduce）
		FileInputFormat.setInputPaths(job, new Path(args[0]));
		FileOutputFormat.setOutputPath(job, new Path(args[1]));

		//执行任务
		job.waitForCompletion(true);
	}
}
















