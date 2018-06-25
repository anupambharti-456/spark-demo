package com.fcbox.spark.example

import com.fcbox.spark.common.BaseApp
import com.fcbox.spark.common.constant.CommonConstants
import com.fcbox.spark.common.hdfs.SparkHdfsUtils
import com.fcbox.spark.common.jdbc.{DatabaseType, SparkJdbcUtils}
import org.apache.spark.sql.SparkSession

object DemoSta extends BaseApp {
  val sparkSession = SparkSession.builder().master(CommonConstants.MASTER).appName(this.getClass.getName).getOrCreate()

  sparkSession.sqlContext.setConf("spark.sql.parquet.binaryAsString", "true")

  //dim_edm_activity
  SparkHdfsUtils.createOrReplaceTempView(sparkSession, "dim_edm_activity", ^("hdfs.dim.dim_edm_activity.path"))

  //fact_edm_activity_coupon_pool和fact_edm_activity_coupon_pool_his
  SparkHdfsUtils.createOrReplaceTempView(sparkSession, "fact_edm_activity_coupon_pool", ^("hdfs.pdw.fact_edm_activity_coupon_pool.path"), ^("hdfs.pdw.fact_edm_activity_coupon_pool_his.path") + "/*/")

  //dim_edm_activity_coupon
  SparkHdfsUtils.createOrReplaceTempView(sparkSession, "dim_edm_activity_coupon", ^("hdfs.dim.dim_edm_activity_coupon.path"))

  //dim_edm_code
  SparkHdfsUtils.createOrReplaceTempView(sparkSession, "dim_edm_code", ^("hdfs.dim.dim_edm_code.path"))

  //统计
  val activityDataFrame = sparkSession.sql(^("/sql/example/select_demo_sta.sql"))
  activityDataFrame.show()
  SparkJdbcUtils.upsert(activityDataFrame, DatabaseType.DWAPP, "edm_activity_sta")


}