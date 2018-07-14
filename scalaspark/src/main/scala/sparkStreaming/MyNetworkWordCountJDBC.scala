package sparkStreaming

/**
  * description
  *
  * @author Llx
  * @version v1.0.0
  * @since 2018/7/11
  */
import java.sql.{Connection, DriverManager, PreparedStatement}

import org.apache.spark.SparkConf
import org.apache.spark.storage.StorageLevel
import org.apache.spark.streaming.{Seconds, StreamingContext}

object MyNetworkWordCountJDBC {
  def main(args: Array[String]): Unit = {
    //创建一个Context对象: StreamingContext  (SparkContext, SQLContext)
    //指定批处理的时间间隔
    val conf = new SparkConf().setAppName("MyNetworkWordCount").setMaster("local[2]")
    val ssc = new StreamingContext(conf,Seconds(5))

    //创建一个DStream，处理数据
    val lines  = ssc.socketTextStream("192.168.157.81",7788,StorageLevel.MEMORY_AND_DISK_SER)

    //执行wordcount
    val words = lines.flatMap(_.split(" "))

    val wordPair = words.map(x => (x,1))

    val wordCountResult = wordPair.reduceByKey(_ + _)
    //val wordCountResult = wordPair.reduceByKeyAndWindow((a:Int,b:Int) => (a+b), Seconds(30),Seconds(10))

    //输出结果
    //wordCountResult.print()
    wordCountResult.foreachRDD(rdd =>{
      rdd.foreachPartition(partitionRecord =>{

        var conn:Connection = null
        var pst:PreparedStatement = null

        try {
          conn = DriverManager.getConnection("jdbc:oracle:thin:@192.168.157.101:1521/orcl.example.com", "SCOTT", "tiger")

          partitionRecord.foreach(record => {
            pst = conn.prepareStatement("insert into myresult values(?,?)")
            pst.setString(1, record._1)
            pst.setInt(2, record._2)
            //执行
            pst.executeUpdate()
          })
        }catch{
          case e1:Exception => println("Some Error: " + e1.getMessage)
        }finally {
          if(pst != null) pst.close()
          if(conn != null) conn.close()
        }
      })
    })

    //启动StreamingContext
    ssc.start()

    //等待计算完成
    ssc.awaitTermination()
  }
}