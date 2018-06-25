package com.fcbox.spark.example

import com.fcbox.spark.common.jdbc.{DatabaseType, SparkJdbcUtils}
import org.apache.spark.sql._
import org.apache.spark.sql.types.{IntegerType, StringType, StructField, StructType}


object ParquetSource extends App{

  def echo(args: String*) =
    for (arg <- args) println(arg)

  val arr = Array("What's", "up", "doc?")
  echo(arr: _*)

//  val sparkSession = SparkSession.builder().appName("LoadAndSave").master("local").getOrCreate()
//  sparkSession.sqlContext.setConf("spark.sql.parquet.binaryAsString", "true")
//  import sparkSession.implicits._
//
//  val dataFrame = sparkSession.read.load("D:\\Users\\000936\\桌面\\pay_bill_20180101.parquet")
//
//  dataFrame.printSchema()
//  dataFrame.show()
}
