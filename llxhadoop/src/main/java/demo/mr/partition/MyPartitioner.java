package demo.mr.partition;

import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.mapreduce.Partitioner;

/**
 * description
 *
 * @author Llx
 * @version v1.0.0
 * @since 2018/4/4
 */
public class MyPartitioner extends Partitioner<IntWritable, Employee> {

    @Override
    public int getPartition(IntWritable intWritable, Employee employee, int i) {
        if(employee.getDeptno()%i == 10){
            return 1%i;
        }else if(employee.getDeptno()%i == 20){
            return 2%i;
        }else{
            return 3%i;
        }

    }

}
