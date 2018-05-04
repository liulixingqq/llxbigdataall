package demo.wc;

import org.apache.hadoop.hbase.client.Put;
import org.apache.hadoop.hbase.io.ImmutableBytesWritable;
import org.apache.hadoop.hbase.mapreduce.TableReducer;
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
 *
 * 1、建立表
create 'word','content'
put 'word','1','content:info','I love Beijing'
put 'word','2','content:info','I love China'
put 'word','3','content:info','Beijing is the capital of China'

2、结果：create 'stat','content'
 *
 * 注意：export HADOOP_CLASSPATH=$HBASE_HOME/lib/*:$CLASSPATH
 */
public class WordCountReducer extends TableReducer<Text,IntWritable,ImmutableBytesWritable> {

    @Override
    protected void reduce(Text key, Iterable <IntWritable> values, Context context) throws IOException, InterruptedException {

        int total= 0;

        for(IntWritable i:values){
            total= total+ i.get();
        }

        Put put = new Put(Bytes.toBytes(key.toString()));
        put.addColumn(Bytes.toBytes("content"), //列族
                Bytes.toBytes("result"), //列
                Bytes.toBytes(String.valueOf(total))
        );
        context.write(new ImmutableBytesWritable(Bytes.toBytes(key.toString())),put);



    }
}
