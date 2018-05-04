package demo.mr.sort.single;

import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

import java.io.IOException;

/**
 * description
 *
 * @author Llx
 * @version v1.0.0
 * @since 2018/4/3
 */
public class sortMapper extends Mapper<LongWritable,Text,IntWritable,NullWritable> {
    @Override
    protected void map(LongWritable k1, Text v1, Context context) throws IOException, InterruptedException {
        String data = v1.toString().trim();
        int num = Integer.valueOf(data);

        context.write(new IntWritable(num),NullWritable.get());
    }
}
