package DateFream.使用数据源

/**
  * description
  *
  * @author Llx
  * @version v1.0.0
  * @since 2018/7/11
  */
import java.util.Properties

import org.apache.spark.sql.SparkSession
import org.apache.spark.sql.Row
import org.apache.spark.sql.types.{IntegerType, StringType, StructField, StructType}

object SpecifyingSchema {
  def main(args: Array[String]): Unit = {
    //创建一个Spark Session对象
    val spark = SparkSession.builder().master("local").appName("SpecifyingSchema").getOrCreate()

    //在SparkSession中包含一个SparkContext，读取数据
    val data = spark.sparkContext.textFile("D:\\temp\\students.txt").map(_.split(" "))

    //创建Schema的结构
    val schema = StructType(
      List(
        StructField("id",IntegerType,true),
        StructField("name",StringType,true),
        StructField("age",IntegerType,true)
      )
    )

    //将数据RDD映射成Row
    val rowRDD = data.map(p => Row(p(0).toInt,p(1).trim,p(2).toInt))

    //关联schema
    val studentDF = spark.createDataFrame(rowRDD,schema)

    //生成表
    studentDF.createOrReplaceTempView("student")

    //执行SQL
    val result = spark.sql("select * from student")

    //显示
    //result.show()

    //保存到Oracle中
    val props = new Properties()
    props.setProperty("user","scott")
    props.setProperty("password","tiger")

    //不需要事先建表
    result.write.jdbc("jdbc:oracle:thin:@192.168.157.101:1521/orcl.example.com","scott.student",props)

    //如果表已经存在，可以采用append模式
    //result.write.mode("append").jdbc("jdbc:oracle:thin:@192.168.157.101:1521/orcl.example.com","scott.student",props)

    spark.stop()
  }
}
















