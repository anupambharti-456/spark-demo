package com.fcbox.spark.example

import org.apache.spark.sql.SparkSession


case class Student(id: Int, name: String, age: Int)
object RDD2DataFrameReflection extends App {

  val sparkSession = SparkSession.builder().master("local").appName("RDD2DataFrameDynamic").getOrCreate()

  val sqlContext = sparkSession.sqlContext
  val sparkContext = sparkSession.sparkContext

  import sparkSession.implicits._
  val studentDF = sparkContext.
    textFile("C:\\000936\\gitlab\\DP\\DSG\\fcbox-spark\\fcbox-spark-test\\src\\main\\resources\\students.txt").
    map(_.split(",")).
    map(attr => Student(attr(0).toInt,attr(1),attr(2).toInt))//注意这里定义的string都是nullable为false,其他是true
    .toDF

  studentDF.createOrReplaceTempView("student")

  studentDF.printSchema()
  studentDF.show()

  sqlContext.sql("select * from student where name = 'yasaka'").show

  sparkSession.close()


}
