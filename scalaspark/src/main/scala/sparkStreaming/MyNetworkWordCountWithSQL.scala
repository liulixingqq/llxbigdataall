package sparkStreaming

/**
  * description
  *
  * @author Llx
  * @version v1.0.0
  * @since 2018/7/11
  */
import org.apache.spark.SparkConf
import org.apache.spark.sql.SparkSession
import org.apache.spark.storage.StorageLevel
import org.apache.spark.streaming.{Seconds, StreamingContext}

object MyNetworkWordCountWithSQL {
  def main(args: Array[String]): Unit = {
    val sparkconf = new SparkConf().setAppName("MyNetworkWordCount").setMaster("local[2]")

    //创建StreamingContext，表示每隔3秒采集一次数据
    val ssc = new StreamingContext(sparkconf,Seconds(3))

    //创建DStream，看成是一个输入流
    val lines = ssc.socketTextStream("192.168.157.11",7788,StorageLevel.MEMORY_AND_DISK_SER)

    //得到的所有的单词
    val words = lines.flatMap(_.split(" "))

    //使用Spark SQL来处理Spark Streaming的数据
    words.foreachRDD(rdd =>{
      //可以使用SparkSession来创建
      val spark = SparkSession.builder().config(rdd.sparkContext.getConf).getOrCreate()

      //需要把RDD转成一个DataFrame
      import spark.implicits._
      val wordCountDF = rdd.toDF("word")

      //注册成一个表
      wordCountDF.createOrReplaceTempView("words")

      //执行SQL
      //val result = spark.sql("select * from words")
      val result = spark.sql("select word,count(*) as total from words group by word")
      result.show()

      Thread.sleep(5000)
    }
    )


    //启动StreamingContext
    ssc.start()

    //等待计算完成
    ssc.awaitTermination()
  }
}

