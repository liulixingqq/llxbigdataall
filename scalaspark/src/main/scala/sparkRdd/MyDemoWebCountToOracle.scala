package sparkRdd

/**
  * description
  *
  * @author Llx
  * @version v1.0.0
  * @since 2018/7/11
  */
import java.sql.{Connection, DriverManager, PreparedStatement}

import org.apache.spark.{SparkConf, SparkContext}

//把每个jsp的访问量，直接保存到Oracle数据中
object MyDemoWebCountToOracle {
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

    //将数据保存到Oracle: create table mydemo1(jspName varchar2(40),countNumber number)
    //    rdd1.map 有一个返回值
    //    rdd1.foreach 没有返回值
    //错误的原因： rdd1.foreach方法，对里面的每条数据都进行操作 ---> 创建一个Connection对象

    //var conn:Connection = DriverManager.getConnection("jdbc:oracle:thin:@192.168.157.101:1521/orcl.example.com","scott","tiger")

    //    rdd1.foreach(f =>{
    //      //就是JDBC的程序 数据: (hadoop.jsp,1)
    //      var conn:Connection = null
    //      var pst:PreparedStatement = null
    //      try{
    //        //获取链接
    //        conn = DriverManager.getConnection("jdbc:oracle:thin:@192.168.157.101:1521/orcl.example.com","scott","tiger")
    //        pst = conn.prepareStatement("insert into mydemo1 values(?,?)")
    //
    //        //赋值
    //        pst.setString(1,f._1)  //jsp网页
    //        pst.setInt(2,f._2)   //一次访问量：1
    //        pst.executeUpdate()
    //      }catch {
    //        case e1:Exception => println("Some Error : " + e1.getMessage)
    //      }finally {
    //        if(pst != null) pst.close()
    //        if(conn != null) conn.close()
    //      }
    //
    //    }
    //    )

    rdd1.foreachPartition(saveToOracle)
  }

  //定义一个函数，将每个分区中的数据保存到Oracle中
  def saveToOracle(it:Iterator[(String,Int)]) = {
    var conn:Connection = null
    var pst:PreparedStatement = null
    try{
      //获取链接
      conn = DriverManager.getConnection("jdbc:oracle:thin:@192.168.157.101:1521/orcl.example.com","scott","tiger")
      pst = conn.prepareStatement("insert into mydemo1 values(?,?)")

      //保存数据
      it.foreach(data=>{
        pst.setString(1,data._1)
        pst.setInt(2,data._2)
        pst.executeUpdate()
      })
    }catch {
      case e1:Exception => println("Some Error : " + e1.getMessage)
    }finally {
      if(pst != null) pst.close()
      if(conn != null) conn.close()
    }
  }
}


