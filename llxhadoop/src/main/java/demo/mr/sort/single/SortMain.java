package demo.mr.sort.single;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.NullWritable;
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
public class SortMain {
    public static void main(String[] args) throws Exception{

        Job job = Job.getInstance(new Configuration());

        job.setJarByClass(SortMain.class);

        job.setMapperClass(sortMapper.class);
        job.setMapOutputKeyClass(IntWritable.class);
        job.setMapOutputValueClass(NullWritable.class);

        job.setSortComparatorClass(MySort.class);

        FileInputFormat.setInputPaths(job,new Path(args[0]));
        FileOutputFormat.setOutputPath(job,new Path(args[1]));

        job.waitForCompletion(true);
    }
}
