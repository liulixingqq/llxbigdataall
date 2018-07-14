package sparkStreaming

/**
  * description
  *
  * @author Llx
  * @version v1.0.0
  * @since 2018/7/11
  */
import org.apache.spark.SparkConf
import org.apache.spark.rdd.RDD
import org.apache.spark.streaming.{Seconds, StreamingContext}

import scala.collection.mutable.Queue

object MyRDDQueueDStream {

  def main(args: Array[String]): Unit = {
    val sparkconf = new SparkConf().setAppName("MyNetworkWordCount").setMaster("local[2]")

    //创建StreamingContext，表示每隔3秒采集一次数据
    val ssc = new StreamingContext(sparkconf,Seconds(3))

    //创建一个队列，把生成RDD放入队列
    val rddQueue = new Queue[RDD[Int]]()

    //初始化
    for(i<- 1 to 3){
      rddQueue += ssc.sparkContext.makeRDD(1 to 10)

      //让线程睡几秒
      Thread.sleep(3000)
    }

    //创建一个RDD的DStream
    val inputStream = ssc.queueStream(rddQueue)
    //处理：乘以10
    val result = inputStream.map(x=> (x,x*10))
    result.print()

    ssc.start()
    ssc.awaitTermination()
  }
}

















