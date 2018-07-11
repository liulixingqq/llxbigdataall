package sparkRdd

/**
  * description
  *
  * @author Llx
  * @version v1.0.0
  * @since 2018/7/11
  */
import org.apache.spark.{Partitioner, SparkConf, SparkContext}

import scala.collection.mutable

object MyDemoWebPartitioner {
  def main(args: Array[String]): Unit = {
    //设置Hadoop的环境变量
    System.setProperty("hadoop.home.dir", "D:\\dowload\\hadoop-2.4.1\\hadoop-2.4.1")

    //定义SparkContext对象
    val conf = new SparkConf().setAppName("MyDemoWebCount").setMaster("local")

    //创建SparkContext对象
    val sc = new SparkContext(conf)

    //读入数据
    //日志：192.168.88.1 - - [30/Jul/2017:12:54:38 +0800] "GET /MyDemoWeb/hadoop.jsp HTTP/1.1" 200 242
    val rdd1 = sc.textFile("D:\\temp\\localhost_access_log.2017-07-30.txt").map(
      line => {
        //解析字符串：找到hadoo.jsp
        //得到: .jsp位置
        val index1 = line.indexOf("\"")   //得到的是第一个双引号的位置
        val index2 = line.lastIndexOf("\"") //得到的是最后一个双引号的位置

        //子串：GET /MyDemoWeb/hadoop.jsp HTTP/1.1
        val line1 = line.substring(index1+1,index2)
        val index3 = line1.indexOf(" ") //子串中第一个空格
        val index4 = line1.lastIndexOf(" ") //子串中最后一个空格

        //子串：/MyDemoWeb/hadoop.jsp
        val line2 = line1.substring(index3+1,index4)

        //得到jsp名字: hadoop.jsp
        val jspPage = line2.substring(line2.lastIndexOf("/") + 1)

        //返回: 格式:  (网页,对应的日志)
        //举例 (hadoop.jsp,192.168.88.1 - - [30/Jul/2017:12:54:38 +0800] "GET /MyDemoWeb/hadoop.jsp HTTP/1.1" 200 242)
        (jspPage,line)
      }
    )

    //去掉重复分key(jsp网页)，得到所有的网页的名字（不重复）
    //实际：一个jsp网页对应一个分区
    val rdd2 = rdd1.map(_._1).distinct().collect()

    //创建分区的规则
    val myPartitioner = new MyWebPartitioner(rdd2)

    //进行分区: 对rdd1进行分区
    val rdd3 = rdd1.partitionBy(myPartitioner)

    //输出
    rdd3.saveAsTextFile("d:\\temp\\out4")

    sc.stop()
  }


  //分区规则：根据jsp的网页进行分区，接收的参数：所有jsp的名字
  class MyWebPartitioner(allJSPName: Array[String]) extends Partitioner{
    //定义一个Map集合，保存分区的条件
    //参数：String jsp名字  Int 对应的分区号
    val partitionMap = new mutable.HashMap[String,Int]()
    var partID = 0 //分区号
    for(name <- allJSPName){
      //有一个jsp，建立一个分区
      partitionMap.put(name,partID)

      //分区号加一
      partID += 1
    }

    //有多少个分区
    override def numPartitions = partitionMap.size

    //分区规则：根据jsp的名字建立，返回对应的分区号
    override def getPartition(key: Any) = {
      // 根据输入的key，返回分区号
      partitionMap.getOrElse(key.toString,0)
    }
  }
}


