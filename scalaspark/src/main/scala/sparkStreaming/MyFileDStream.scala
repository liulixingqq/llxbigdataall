package sparkStreaming

/**
  * description
  *
  * @author Llx
  * @version v1.0.0
  * @since 2018/7/11
  */
import org.apache.spark.SparkConf
import org.apache.spark.streaming.{Seconds, StreamingContext}

object MyFileDStream {
  def main(args: Array[String]): Unit = {
    val sparkconf = new SparkConf().setAppName("MyNetworkWordCount").setMaster("local[2]")

    //创建StreamingContext，表示每隔3秒采集一次数据
    val ssc = new StreamingContext(sparkconf,Seconds(3))

    //监听一个目录，当目录下的文件发生变化的时候，将变化的数据读入: DStream
    val lines = ssc.textFileStream("D:\\temp\\aaaa")

    //打印
    lines.print()

    ssc.start()
    ssc.awaitTermination()
  }
}