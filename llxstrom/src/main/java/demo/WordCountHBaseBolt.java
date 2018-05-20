package demo;
import java.util.Map;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.client.HTable;
import org.apache.hadoop.hbase.client.Put;
import org.apache.hadoop.hbase.util.Bytes;
import org.apache.storm.task.OutputCollector;
import org.apache.storm.task.TopologyContext;
import org.apache.storm.topology.OutputFieldsDeclarer;
import org.apache.storm.topology.base.BaseRichBolt;
import org.apache.storm.tuple.Tuple;
/*
 * 创建一个HBase的表： create 'result','info'
 */
public class WordCountHBaseBolt extends BaseRichBolt {

    //定义一个HBase的客户端
    private HTable table ;

    @Override
    public void execute(Tuple tuple) {
        //得到上一个组件处理的数据
        String word = tuple.getStringByField("word");
        int total = tuple.getIntegerByField("total");

        //创建一个Put对象
        Put put = new Put(Bytes.toBytes(word));
        put.add(Bytes.toBytes("info"), Bytes.toBytes("word"), Bytes.toBytes(word));
        put.add(Bytes.toBytes("info"), Bytes.toBytes("total"), Bytes.toBytes(String.valueOf(total)));
        try{
            table.put(put);
        }catch(Exception ex){
            ex.printStackTrace();
        }

    }

    @Override
    public void prepare(Map arg0, TopologyContext arg1, OutputCollector arg2) {
        // 初始化：指定HBase的相关信息
        //指定ZK的地址
        Configuration conf = new Configuration();
        conf.set("hbase.zookeeper.quorum", "192.168.157.11");
        try{
            table = new HTable(conf, "result");
        }catch(Exception ex){
            ex.printStackTrace();
        }
    }

    @Override
    public void declareOutputFields(OutputFieldsDeclarer arg0) {
        // TODO Auto-generated method stub

    }
}
