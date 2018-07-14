package DateFream.使用数据源

import org.apache.spark.SparkConf
import org.apache.spark.storage.StorageLevel
import org.apache.spark.streaming.{Seconds, StreamingContext}
/**
  * description
  *
  * @author Llx
  * @version v1.0.0
  * @since 2018/7/11
  */
object MyNetworkWordCount {
  def main(args: Array[String]): Unit = {
    //核心：通过StreamingContext对象，去创建一个DStream
    //DStream从外部接收数据（使用的是Linux上的netcat工具）

    //创建一个SparkConf对象
    //local[2] : 相当于有CPU有两个核数，相当于有两个工作线程
    // 一个用于接收数据；另一个用于处理数据
    val sparkconf = new SparkConf().setAppName("MyNetworkWordCount").setMaster("local[2]")

    //创建StreamingContext，表示每隔3秒采集一次数据
    val ssc = new StreamingContext(sparkconf,Seconds(3))

    //创建DStream，看成是一个输入流
    val lines = ssc.socketTextStream("192.168.157.11",1234,StorageLevel.MEMORY_AND_DISK_SER)

    //执行WordCount
    val words = lines.flatMap(_.split(" "))
    val wordCount = words.map((_,1)).reduceByKey(_+_)

    //输出
    wordCount.print()

    //启动StreamingContext
    ssc.start()

    //等待计算完成
    ssc.awaitTermination()
  }
}
