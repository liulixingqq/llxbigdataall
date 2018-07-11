package sparkRdd

/**
  * description
  *
  * @author Llx
  * @version v1.0.0
  * @since 2018/7/11
  */

/*
提交
bin/spark-submit --master spark://bigdata11:7077 --class mydemo.MyWordCount /root/temp/MyWordCount.jar hdfs://bigdata11:9000/input/data.txt hdfs://bigdata11:9000/output/spark/day0209/wc1
 */

import org.apache.spark.{SparkConf, SparkContext}

//开发一个Scala版本的WordCount
object MyWordCount {
  def main(args: Array[String]): Unit = {
    //创建一个Config
    val conf = new SparkConf().setAppName("MyScalaWordCount")

    //核心创建SparkContext对象
    val sc = new SparkContext(conf)

    //使用sc对象执行相应的算子（函数）
    sc.textFile(args(0))
      .flatMap(_.split(" "))
      .map((_,1))
      .reduceByKey(_+_)
      .saveAsTextFile(args(1))

    //停止SparkContext对象
    sc.stop()

  }
}