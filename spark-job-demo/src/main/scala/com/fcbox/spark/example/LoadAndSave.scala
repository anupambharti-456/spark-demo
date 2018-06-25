package com.fcbox.spark.example

import org.apache.spark.sql.{SaveMode, SparkSession}

object LoadAndSave extends App{

  val sparkSession = SparkSession.builder().appName("LoadAndSave").master("local").getOrCreate()
  import sparkSession.implicits._

  val dataFrame = sparkSession.read.load("C:\\000936\\gitlab\\DP\\DSG\\fcbox-spark\\fcbox-spark-test\\src\\main\\resources\\users.parquet").filter($"name".equalTo("Ben"))

  dataFrame.printSchema()

  dataFrame.show()

  dataFrame.filter($"name".equalTo("Ben")).select($"name",$"name".equalTo("Ben")).write.mode(SaveMode.Overwrite).json("C:\\000936\\gitlab\\DP\\DSG\\fcbox-spark\\fcbox-spark-test\\src\\main\\resources\\ben.json")

  sparkSession.stop()
}
