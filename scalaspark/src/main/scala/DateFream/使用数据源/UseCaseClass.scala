package DateFream.使用数据源

/**
  * description
  *
  * @author Llx
  * @version v1.0.0
  * @since 2018/7/11
  */
import org.apache.spark.sql.SparkSession

object UseCaseClass {
  def main(args: Array[String]): Unit = {
    //创建一个Spark Session对象
    val sparkSession = SparkSession.builder().master("local").appName("SpecifyingSchema").getOrCreate()

    //读取数据，生成对应的RDD
    val lineRDD = sparkSession.sparkContext.textFile("D:\\temp\\students.txt").map(_.split(" "))
    val studentRDD = lineRDD.map(x => Student(x(0).toInt,x(1),x(2).toInt))

    //生成DataFrame, 需要导入隐式转换
    import sparkSession.sqlContext.implicits._
    val studentDF = studentRDD.toDF

    //生成表
    studentDF.createOrReplaceTempView("mystudent")

    //执行SQl
    sparkSession.sql("select * from mystudent").show()

    sparkSession.stop()
  }
  //定义样本类: schema
  case class Student(stuID:Int,stuName:String,stuAge:Int)
}

