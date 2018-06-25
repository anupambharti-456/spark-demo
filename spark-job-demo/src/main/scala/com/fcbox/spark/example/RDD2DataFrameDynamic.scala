package com.fcbox.spark.example

import org.apache.spark.sql.types._
import org.apache.spark.sql.{Row, SparkSession}


object RDD2DataFrameDynamic extends App {

  val sparkSession = SparkSession.builder().master("local").appName("RDD2DataFrameDynamic").getOrCreate()

  val sqlContext = sparkSession.sqlContext
  val sparkContext = sparkSession.sparkContext

  var fields = List[StructField]()
  fields = fields :+ StructField("id", IntegerType, nullable = false)
  fields = fields :+ StructField("name", StringType, nullable = true)
  fields = fields :+ StructField("age", IntegerType, nullable = true)

  var schema = StructType(fields)

  val rdd = sparkContext.
    textFile("C:\\000936\\gitlab\\DP\\DSG\\fcbox-spark\\fcbox-spark-test\\src\\main\\resources\\students.txt").
    map(_.split(",")).
    map(attr => {
      if(attr.length == 3) {
        Row(attr(0).toInt,attr(1),attr(2).toInt)
      }else {
        Row(attr(0).toInt,attr(1),null)
      }
    })

  val studentDF = sqlContext.createDataFrame(rdd,schema)
  studentDF.show

  studentDF.createOrReplaceTempView("student")

  sqlContext.sql("select * from student where name = 'yasaka'").show




}
