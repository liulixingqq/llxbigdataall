package demo.wc;

import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.io.ImmutableBytesWritable;
import org.apache.hadoop.hbase.mapreduce.TableMapper;
import org.apache.hadoop.hbase.util.Bytes;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;

import java.io.IOException;

/**
 * description
 *
 * @author Llx
 * @version v1.0.0
 * @since 2018/4/15
 *
 * 注意：export HADOOP_CLASSPATH=$HBASE_HOME/lib/*:$CLASSPATH
 */
public class WordCountMapper extends TableMapper<Text,IntWritable> {

    @Override
    protected void map(ImmutableBytesWritable key, Result value, Context context) throws IOException, InterruptedException {


        String data = Bytes.toString(value.getValue(Bytes.toBytes("content"), Bytes.toBytes("info")));
        String[] words = data.split(" ");
        for(String w : words){
            context.write(new Text(w),new IntWritable(1));
        }

    }
}
