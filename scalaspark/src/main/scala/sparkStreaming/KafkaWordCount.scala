package sparkStreaming


import org.apache.spark.SparkConf
import org.apache.spark.streaming.kafka.KafkaUtils
import org.apache.spark.streaming.{Seconds, StreamingContext}

object KafkaWordCount {
  def main(args: Array[String]) {
    val conf = new SparkConf().setAppName("SparkFlumeNGWordCount").setMaster("local[2]")
    val ssc = new StreamingContext(conf, Seconds(10))

    //创建topic名称，1表示一次从这个topic中获取一条记录
    val topics = Map("mydemo1" ->1)

    //创建Kafka的输入流，指定ZooKeeper的地址
    val kafkaStream = KafkaUtils.createStream(ssc,"192.168.157.81:2181","mygroup",topics)

    //处理每次接收到的数据
    val lineDStream = kafkaStream.map(e => {
      new String(e.toString())
    })
    //输出结果
    lineDStream.print()

    ssc.start()
    ssc.awaitTermination();
  }
}