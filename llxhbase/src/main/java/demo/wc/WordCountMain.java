package demo.wc;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.client.Scan;
import org.apache.hadoop.hbase.mapreduce.TableMapReduceUtil;
import org.apache.hadoop.hbase.util.Bytes;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;

/**
 * description
 *
 * @author Llx
 * @version v1.0.0
 * @since 2018/4/15
 *1、建立表
    create 'word','content'
    put 'word','1','content:info','I love Beijing'
    put 'word','2','content:info','I love China'
    put 'word','3','content:info','Beijing is the capital of China'

    2、结果：create 'stat','content'
 *
 *
 *注意：export HADOOP_CLASSPATH=$HBASE_HOME/lib/*:$CLASSPATH
 * export HADOOP_CLASSPATH=$HBASE_HOME/lib/*:$CLASSPATH
 */
public class WordCountMain {

    public static void main(String[] args) throws Exception{
        //获取ZK的地址
        //指定的配置信息: ZooKeeper
        Configuration conf = new Configuration();
        conf.set("hbase.zookeeper.quorum", "192.168.157.11");

        Job job = Job.getInstance(conf);
        job.setJarByClass(WordCountMain.class);


        Scan scan = new Scan();
        scan.addColumn(Bytes.toBytes("content"), Bytes.toBytes("info"));
        TableMapReduceUtil.initTableMapperJob(Bytes.toBytes("word"),    //输入的表
                scan,    //扫描器，只读取想要处理的数据
                WordCountMapper.class,
                Text.class,
                IntWritable.class,
                job);

        TableMapReduceUtil.initTableReducerJob("stat", WordCountReducer.class, job);

        job.waitForCompletion(true);
    }

}
