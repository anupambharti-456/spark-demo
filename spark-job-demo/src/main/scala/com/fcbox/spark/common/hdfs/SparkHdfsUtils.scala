package com.fcbox.spark.common.hdfs

import org.apache.spark.sql.{DataFrame, SparkSession}

object SparkHdfsUtils {

  def createOrReplaceTempView(sparkSession: SparkSession, table: String,  hdfsPath: String*): String = {
    loadDF(sparkSession,hdfsPath:_*).createOrReplaceTempView(table)
    table
  }

  def loadDF(sparkSession: SparkSession, hdfsPath: String*): DataFrame = {
    sparkSession.read.load(hdfsPath:_*)
  }

}
