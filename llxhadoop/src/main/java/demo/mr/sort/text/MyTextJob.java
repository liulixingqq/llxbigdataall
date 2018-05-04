package demo.mr.sort.text;

import java.io.IOException;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;

public class MyTextJob {

	public static void main(String[] args) throws Exception {
		// 创建一个job
		Job job = Job.getInstance(new Configuration());

		//指定任务的入口
		job.setJarByClass(MyTextJob.class);

		//指定任务的mapper和输出的数据类型
		job.setMapperClass(MyTextMapper.class);
		job.setMapOutputKeyClass(Text.class);
		job.setMapOutputValueClass(NullWritable.class);

		//指定自己的比较器
		job.setSortComparatorClass(MyTextComparator.class);

		//指定任务的输入和输出
		FileInputFormat.setInputPaths(job, new Path(args[0]));
		FileOutputFormat.setOutputPath(job, new Path(args[1]));

		//执行任务
		job.waitForCompletion(true);
	}

}
