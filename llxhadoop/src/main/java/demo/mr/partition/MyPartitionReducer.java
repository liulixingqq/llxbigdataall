package demo.mr.partition;

import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.mapreduce.Reducer;

import java.io.IOException;

/**
 * description
 *
 * @author Llx
 * @version v1.0.0
 * @since 2018/4/4
 */
public class MyPartitionReducer extends Reducer<IntWritable, Employee, IntWritable, Employee> {
    @Override
    protected void reduce(IntWritable k3, Iterable<Employee> v3, Context context)
            throws IOException, InterruptedException {
        // 直接输出
        for(Employee e:v3){
            context.write(k3, e);
        }
    }
}
