package com.fcbox.spark.example

import com.fcbox.spark.common.jdbc.{DatabaseType, SparkJdbcUtils}
import org.apache.spark.sql._
import org.apache.spark.sql.types.{IntegerType, StringType, StructField, StructType}


object JDBCDataSource extends App{

  val sparkSession = SparkSession.builder().appName("LoadAndSave").master("local").getOrCreate()
  val sqlContext = sparkSession.sqlContext

  sqlContext.setConf("logLevel","debug")

  val edDataFrame = sparkSession.read.format("jdbc").options(SparkJdbcUtils.getJdbcOptions("test1", DatabaseType.OP)).load()

  val districtDataFrame = sparkSession.read.format("jdbc").options(SparkJdbcUtils.getJdbcOptions("test2", DatabaseType.OP)).load()



  val edRdd = edDataFrame.rdd.map(x => (x.getString(0),x.getInt(1)))
  val districtRdd = districtDataFrame.rdd.map(x => (x.getString(0),x.getInt(1)))

  val join1 = edRdd.join(districtRdd)

  // 将PairRDD转换为RDD<Row>
  val studentsRowRDD = join1.map(x => Row(x._1, x._2._1, x._2._2))
//  val goodStudentsRDD = studentsRowRDD.filter(x => x.getInt(2)==11)

  val structFields = Array(StructField("name", StringType, true)
    ,StructField("age", IntegerType, true)
    ,StructField("score", IntegerType, true))

  val studentsDF = sqlContext.createDataFrame(studentsRowRDD, StructType(structFields))
  for(row <- studentsDF){
    println(row)
  }

//  val join = edDataFrame.join(districtDataFrame,"name")
//  join.show()
}
