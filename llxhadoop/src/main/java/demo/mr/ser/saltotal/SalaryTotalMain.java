package demo.mr.ser.saltotal;

import java.io.IOException;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;

public class SalaryTotalMain {

	public static void main(String[] args) throws Exception {
		// 创建一个job：job = map + reduce
		Job job = Job.getInstance(new Configuration());

		//指定任务的入口
		job.setJarByClass(SalaryTotalMain.class);

		//指定任务的Mapper和输出的数据类型: k2  v2
		job.setMapperClass(SalaryTotalMapper.class);
		job.setMapOutputKeyClass(IntWritable.class);    //指定k2
		job.setMapOutputValueClass(Employee.class);  //指定v2

		//指定任务的Reducer和输出的数据类型: k4 v4
		job.setReducerClass(SalaryTotalReducer.class);
		job.setOutputKeyClass(IntWritable.class);   //指定k4
		job.setOutputValueClass(IntWritable.class);   //指定v4

		//指定输入的路径（map）、输出的路径（reduce）
		FileInputFormat.setInputPaths(job, new Path(args[0]));
		FileOutputFormat.setOutputPath(job, new Path(args[1]));

		//执行任务
		job.waitForCompletion(true);
	}

}
