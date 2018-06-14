package demo.mr.sort.hadoop.number;

import java.io.IOException;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;

public class NumberMain {

	public static void main(String[] args) throws Exception {
		// 创建一个job：job = map + reduce
		Job job = Job.getInstance(new Configuration());

		//指定任务的入口
		job.setJarByClass(NumberMain.class);

		//指定任务的Mapper和输出的数据类型: k2  v2
		job.setMapperClass(NumberMapper.class);
		job.setMapOutputKeyClass(IntWritable.class);    //指定k2
		job.setMapOutputValueClass(NullWritable.class);  //指定v2: null值

		//指定自己的比较规则
		job.setSortComparatorClass(MyNumberComparator.class);

		//指定输入的路径（map）、输出的路径（reduce）
		FileInputFormat.setInputPaths(job, new Path(args[0]));
		FileOutputFormat.setOutputPath(job, new Path(args[1]));

		//执行任务
		job.waitForCompletion(true);
	}


}
