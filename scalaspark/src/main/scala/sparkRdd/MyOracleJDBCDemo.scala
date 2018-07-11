package sparkRdd

/**
  * description
  *
  * @author Llx
  * @version v1.0.0
  * @since 2018/7/11
  */
import java.sql.DriverManager

import org.apache.spark.rdd.JdbcRDD
import org.apache.spark.{SparkConf, SparkContext}

object MyOracleJDBCDemo {
  val connection = () =>{
    //注册驱动
    Class.forName("oracle.jdbc.OracleDriver").newInstance()
    //获取链接
    DriverManager.getConnection("jdbc:oracle:thin:@192.168.157.101:1521/orcl.example.com","scott","tiger")
  }

  def main(args: Array[String]): Unit = {
    //定义SparkContext对象
    val conf = new SparkConf().setAppName("MyDemoWebCount").setMaster("local")

    //创建SparkContext对象
    val sc = new SparkContext(conf)

    //创建一个JdbcRDD
    //得到：员工姓名 薪水
    val oracleRDD = new JdbcRDD(sc,
      connection,
      "select * from emp where sal>? and deptno=?",
      2000,
      10,
      2,
      r =>{
        val ename = r.getString(2)
        val sal = r.getInt(6)
        //返回
        (ename,sal)
      }
    )

    val result = oracleRDD.collect()
    println(result.toBuffer)

    sc.stop()
  }
}
