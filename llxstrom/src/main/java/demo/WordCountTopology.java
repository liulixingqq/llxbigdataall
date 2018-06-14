package demo;

import org.apache.storm.Config;
import org.apache.storm.LocalCluster;
import org.apache.storm.StormSubmitter;
import org.apache.storm.generated.AlreadyAliveException;
import org.apache.storm.generated.AuthorizationException;
import org.apache.storm.generated.InvalidTopologyException;
import org.apache.storm.generated.StormTopology;
import org.apache.storm.hdfs.bolt.HdfsBolt;
import org.apache.storm.hdfs.bolt.format.DefaultFileNameFormat;
import org.apache.storm.hdfs.bolt.format.DelimitedRecordFormat;
import org.apache.storm.hdfs.bolt.rotation.FileSizeRotationPolicy;
import org.apache.storm.hdfs.bolt.rotation.FileSizeRotationPolicy.Units;
import org.apache.storm.hdfs.bolt.sync.CountSyncPolicy;
import org.apache.storm.redis.bolt.RedisStoreBolt;
import org.apache.storm.redis.common.config.JedisPoolConfig;
import org.apache.storm.redis.common.mapper.RedisDataTypeDescription;
import org.apache.storm.redis.common.mapper.RedisStoreMapper;
import org.apache.storm.topology.IRichBolt;
import org.apache.storm.topology.TopologyBuilder;
import org.apache.storm.tuple.Fields;
import org.apache.storm.tuple.ITuple;

public class WordCountTopology {

    public static void main(String[] args) throws Exception {
        // 创建一个任务：Topology = spout + bolt(s)

        TopologyBuilder builder = new TopologyBuilder();

        //设置任务的第一个组件：spout组件
        builder.setSpout("mywordcount_spout", new WordCountSpout());
        //builder.setSpout("mywordcount_spout", createKafkaSpout());

        //设置任务的第二个组件：bolt组件，拆分单词
        builder.setBolt("mywordcount_split", new WordCountSplitBolt()).shuffleGrouping("mywordcount_spout");

        //设置任务的第三个组件：bolt组件，单词计数
        builder.setBolt("mywordcount_total", new WordCountTotalBolt()).fieldsGrouping("mywordcount_split", new Fields("word"));

        //设置任务的第四个组件：bolt组件，将结果写到Redis中
        //builder.setBolt("mywordcount_redis", createRedisBolt()).shuffleGrouping("mywordcount_total");

        //设置任务的第四个组件：bolt组件，将结果写到HDFS中
        //builder.setBolt("mywordcount_hdfs", createHDFSBolt()).shuffleGrouping("mywordcount_total");

        //设置任务的第四个组件：bolt组件，将结果写到HBase中
        builder.setBolt("mywordcount_hdfs", new WordCountHBaseBolt()).shuffleGrouping("mywordcount_total");

        //创建任务
        StormTopology topology = builder.createTopology();

        //配置参数
        Config conf = new Config();

        //提交任务: 方式1：本地模式     方式2：集群模式
        //方式1：本地模式
        LocalCluster cluster = new LocalCluster();
        cluster.submitTopology("mywordcount", conf, topology);

        // 方式2：集群模式: storm jar temp/storm.jar demo.WordCountTopology MyStormWordCount
        //StormSubmitter.submitTopology(args[0], conf, topology);
    }

    private static IRichBolt createHDFSBolt() {
        // 创建一个HDFS的BOlt组件，写入到HDFS
        HdfsBolt bolt = new HdfsBolt();

        //指定HDFS的位置
        bolt.withFsUrl("hdfs://192.168.157.11:9000");

        //数据保存在HDFS的哪个目录
        bolt.withFileNameFormat(new DefaultFileNameFormat().withPath("/stormresult"));

        //指定key和value的分隔符：Beijing|10
        bolt.withRecordFormat(new DelimitedRecordFormat().withFieldDelimiter("|"));

        //生成文件的策略
        bolt.withRotationPolicy(new FileSizeRotationPolicy(5.0f,Units.MB));

        //与HDFS进行同步的策略：当tuple的数据达到1K?????
        bolt.withSyncPolicy(new CountSyncPolicy(1024));

        return bolt;
    }

    private static IRichBolt createRedisBolt() {
        // 创建一个Redis的bolt组件，将数据写到Redis中
        //创建一个Redis的连接池
        JedisPoolConfig.Builder builder = new JedisPoolConfig.Builder();
        builder.setHost("192.168.157.11");
        builder.setPort(6379);
        JedisPoolConfig poolConfig = builder.build();

        //storeMapper: 存入Redis中数据的格式
        return new RedisStoreBolt(poolConfig, new RedisStoreMapper() {

            @Override
            public RedisDataTypeDescription getDataTypeDescription() {
                // 申明存入Redi的数据的类型
                return new RedisDataTypeDescription(RedisDataTypeDescription.RedisDataType.HASH,"wordcount");
            }

            @Override
            public String getValueFromTuple(ITuple tuple) {
                // 从上一个组件接收的value
                return String.valueOf(tuple.getIntegerByField("total"));
            }

            @Override
            public String getKeyFromTuple(ITuple tuple) {
                // 从上一个组件接收的key
                return tuple.getStringByField("word");
            }
        });
    }
}
