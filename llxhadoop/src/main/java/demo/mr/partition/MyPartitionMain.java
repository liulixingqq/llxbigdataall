package demo.mr.partition;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;

/**
 * description
 *
 * @author Llx
 * @version v1.0.0
 * @since 2018/4/4
 */
public class MyPartitionMain {

    public static void main(String[] args) throws Exception {
        Job job = Job.getInstance(new Configuration());
        job.setJarByClass(MyPartitionMain.class);

        job.setMapperClass(EmployeeMapper.class);
        job.setMapOutputKeyClass(IntWritable.class);
        job.setMapOutputValueClass(Employee.class);

        //指定分区的规则
        job.setPartitionerClass(MyPartitioner.class);
        //指定分区的个数
        job.setNumReduceTasks(3); //3代表三个分区

        job.setReducerClass(MyPartitionReducer.class);
        job.setOutputKeyClass(IntWritable.class);
        job.setOutputValueClass(Employee.class);

        FileInputFormat.setInputPaths(job, new Path(args[0]));
        FileOutputFormat.setOutputPath(job, new Path(args[1]));

        job.waitForCompletion(true);
    }
}