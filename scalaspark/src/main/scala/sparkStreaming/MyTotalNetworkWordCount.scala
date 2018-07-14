package sparkStreaming

/**
  * description
  *
  * @author Llx
  * @version v1.0.0
  * @since 2018/7/11
  */
import org.apache.spark.SparkConf
import org.apache.spark.storage.StorageLevel
import org.apache.spark.streaming.{Seconds, StreamingContext}

object MyTotalNetworkWordCount {

  def main(args: Array[String]): Unit = {
    val sparkconf = new SparkConf().setAppName("MyNetworkWordCount").setMaster("local[2]")

    //创建StreamingContext，表示每隔3秒采集一次数据
    val ssc = new StreamingContext(sparkconf,Seconds(3))

    //注意：如果累计，在执行计算的时候，需要保持之前的状态信息
    //设置检查点
    ssc.checkpoint("hdfs://192.168.157.11:9000/spark/checkpoint0228")

    //创建DStream，看成是一个输入流
    val lines = ssc.socketTextStream("192.168.157.11",1234,StorageLevel.MEMORY_AND_DISK_SER)

    //执行WordCount
    val words = lines.flatMap(_.split(" "))

    //每个单词记一次数
    val pairs = words.map((_,1))

    //定义一个函数，进行累加
    //参数：1、当前的值    2、之前的值
    val addFunc = (currentValues: Seq[Int],preValues:Option[Int]) =>{
      //得到当前的值
      val currentCount = currentValues.sum

      //先得到之前的值
      val preCount = preValues.getOrElse(0)

      //返回累加的结果
      Some(currentCount + preCount)
    }

    //统计每个单词出现的频率：累计
    val totalCount = pairs.updateStateByKey(addFunc)
    totalCount.print()

    //启动任务
    ssc.start()
    ssc.awaitTermination()
  }
}


