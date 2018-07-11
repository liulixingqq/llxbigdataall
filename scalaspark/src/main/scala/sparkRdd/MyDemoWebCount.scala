package sparkRdd

/**
  * description
  *
  * @author Llx
  * @version v1.0.0
  * @since 2018/7/11
  */
import org.apache.spark.{SparkConf, SparkContext}

//需求：找到访问量最高的两个网页
//以本地模式运行：.setMaster("local")
object MyDemoWebCount {
  def main(args: Array[String]): Unit = {
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

        //返回: 格式:  (hadoop.jsp,1)
        (jspPage,1)
      }
    )

    //按照key(jspPage)进行聚合操作
    val rdd2 = rdd1.reduceByKey(_+_)

    //排序: 按照value排序（降序） 数据   (hadoop.jsp,10)
    val rdd3 = rdd2.sortBy(_._2,false)

    //取出排名最高的两个网页
    println(rdd3.take(2).toBuffer)

    sc.stop()
  }
}
