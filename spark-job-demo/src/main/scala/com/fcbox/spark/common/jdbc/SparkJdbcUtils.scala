package com.fcbox.spark.common.jdbc

import java.sql.{Connection, DriverManager, Timestamp}

import com.fcbox.spark.common.jdbc.DatabaseType.DatabaseType
import com.fcbox.spark.common.util.ResourceUtils
import org.apache.spark.sql.{DataFrame, SparkSession}

/**
  * 数据源配置
  */
object SparkJdbcUtils {


  /**
    * mysql的upsert
    *
    * @param dataFrame    处理后的dataFrame,里面的字段需要和表字段名称保持一致
    * @param databaseType 库名
    * @param tableName    表名,需要对where条件字段建唯一索引
    */
  def upsert(dataFrame: DataFrame, databaseType: DatabaseType, tableName: String): Unit = {

    val structFields = dataFrame.schema.fields

    val sb1 = new StringBuilder
    val sb2 = new StringBuilder
    val sb3 = new StringBuilder

    val nameIndex = scala.collection.mutable.Map[String, Int]()

    for (i <- structFields.indices) {
      val s = structFields(i)
      val name = s.name

      nameIndex += (name -> (i + 1))

      sb1.append(name).append(",")
      sb2.append("?,")
      sb3.append(name).append("=").append("VALUES(").append(name).append("),")
    }

    val s1 = sb1.toString().substring(0, sb1.toString().length - 1)
    val s2 = sb2.toString().substring(0, sb2.toString().length - 1)
    val s3 = sb3.toString().substring(0, sb3.toString().length - 1)

    val sql = s"INSERT INTO $tableName ($s1) VALUES($s2) ON DUPLICATE KEY UPDATE $s3"

    dataFrame.foreachPartition(rows => {
      SparkJdbcUtils.invoke(databaseType, conn => {
        rows.foreach(row => {
          val upsertP = conn.prepareStatement(sql)
          row.schema.fields.foreach(f => {
            val name = f.name
            val typeName = f.dataType.typeName
            typeName match {
              case "string" => upsertP.setString(nameIndex(name), row.getAs[String](name))
              case "long" => upsertP.setLong(nameIndex(name), row.getAs[Long](name))
              case "int" => upsertP.setInt(nameIndex(name), row.getAs[Int](name))
              case "double" => upsertP.setDouble(nameIndex(name), row.getAs[Double](name))
              case "float" => upsertP.setFloat(nameIndex(name), row.getAs[Float](name))
              case "short" => upsertP.setShort(nameIndex(name), row.getAs[Short](name))
              case "timestamp" => upsertP.setTimestamp(nameIndex(name), row.getAs[Timestamp](name))
            }
          })
          upsertP.execute()
        })
      })
    })
  }


  def invoke[T](databaseType: DatabaseType, call: Connection => T): T = {
    val jdbcOptions = getJdbcOptions("", databaseType)
    if (Driver.ORACLE.toString.equals(jdbcOptions("driver"))) {
      Class.forName("oracle.jdbc.driver.OracleDriver")
    } else {
      Class.forName("com.mysql.jdbc.Driver")
    }
    val conn = DriverManager.getConnection(jdbcOptions("url") + "&useServerPrepStmts=false&rewriteBatchedStatements=true", jdbcOptions("user"), jdbcOptions("password"))
    try {
      call(conn)
    } catch {
      case ex: Exception => {
        println("Exception:" + ex)
        throw ex
      }
    } finally {
      conn.close()
    }
  }

  //**********************************************数据源连接配置BEGIN**********************************************
  /**
    * 获取数据库连接配置
    *
    * @param dbtable      表名
    * @param databaseType 数据源类型
    * @return Map[String, String]
    */
  def getJdbcOptions(dbtable: String, databaseType: DatabaseType): Map[String, String] = {
    import DatabaseType._
    databaseType match {
      case COURIER => getCourierJdbcOptions(dbtable)
      case OP => getOpJdbcOptions(dbtable)
      case EDMS => getEdmsJdbcOptions(dbtable)
      case SCAM => getScamJdbcOptions(dbtable)
      case DWAPP => getDwAppJdbcOptions(dbtable)
      case EDMSREPORT => getEdmsreportJdbcOptions(dbtable)
      case STATISTIC => getStatisticJdbcOptions(dbtable)
      case _ => null
    }
  }

  def getCourierJdbcOptions(dbtable: String): Map[String, String] = {
    Map(
      "url" -> ResourceUtils.getConfig("courier.database.url"),
      "user" -> ResourceUtils.getConfig("courier.database.user"),
      "password" -> ResourceUtils.getConfig("courier.database.password"),
      "driver" -> Driver.MYSQL.toString,
      "dbtable" -> dbtable
    )
  }

  def getOpJdbcOptions(dbtable: String): Map[String, String] = {
    Map(
      "url" -> ResourceUtils.getConfig("op.database.url"),
      "user" -> ResourceUtils.getConfig("op.database.user"),
      "password" -> ResourceUtils.getConfig("op.database.password"),
      "driver" -> Driver.MYSQL.toString,
      "dbtable" -> dbtable
    )
  }

  def getScamJdbcOptions(dbtable: String): Map[String, String] = {
    Map(
      "url" -> ResourceUtils.getConfig("scam.database.url"),
      "user" -> ResourceUtils.getConfig("scam.database.user"),
      "password" -> ResourceUtils.getConfig("scam.database.password"),
      "driver" -> Driver.MYSQL.toString,
      "dbtable" -> dbtable
    )
  }

  def getEdmsJdbcOptions(dbtable: String): Map[String, String] = {
    Map(
      "url" -> ResourceUtils.getConfig("edms.database.url"),
      "user" -> ResourceUtils.getConfig("edms.database.user"),
      "password" -> ResourceUtils.getConfig("edms.database.password"),
      "driver" -> Driver.ORACLE.toString,
      "dbtable" -> dbtable
    )
  }

  def getEdmsreportJdbcOptions(dbtable: String): Map[String, String] = {
    Map(
      "url" -> ResourceUtils.getConfig("edmsreport.database.url"),
      "user" -> ResourceUtils.getConfig("edmsreport.database.user"),
      "password" -> ResourceUtils.getConfig("edmsreport.database.password"),
      "driver" -> Driver.ORACLE.toString,
      "dbtable" -> dbtable
    )
  }

  def getStatisticJdbcOptions(dbtable: String): Map[String, String] = {
    Map(
      "url" -> ResourceUtils.getConfig("statistic.database.url"),
      "user" -> ResourceUtils.getConfig("statistic.database.user"),
      "password" -> ResourceUtils.getConfig("statistic.database.password"),
      "driver" -> Driver.MYSQL.toString,
      "dbtable" -> dbtable
    )
  }

  def getDwAppJdbcOptions(dbtable: String): Map[String, String] = {
    Map(
      "url" -> ResourceUtils.getConfig("dw_app.database.url"),
      "user" -> ResourceUtils.getConfig("dw_app.database.user"),
      "password" -> ResourceUtils.getConfig("dw_app.database.password"),
      "driver" -> Driver.MYSQL.toString,
      "dbtable" -> dbtable
    )
  }

  //**********************************************数据源连接配置END**********************************************

  def createOrReplaceTempView(sparkSession: SparkSession, databaseType: DatabaseType, dbtable: String): String = {
    loadDF(sparkSession, databaseType, dbtable).createOrReplaceTempView(dbtable)
    dbtable
  }

  def loadDF(sparkSession: SparkSession, databaseType: DatabaseType, dbtable: String): DataFrame = {
    sparkSession.read.format("jdbc").options(getJdbcOptions(dbtable, databaseType)).load()
  }

  def loadDF(sparkSession: SparkSession, databaseType: DatabaseType, dbtable: String, predicates: Array[String]): DataFrame = {
    val jdbcMap = getJdbcOptions(dbtable, databaseType)
    val prop = new java.util.Properties
    prop.setProperty("user", jdbcMap("user"))
    prop.setProperty("password", jdbcMap("password"))
    prop.setProperty("driver", jdbcMap("driver"))
    sparkSession.read.jdbc(jdbcMap("url"), dbtable, predicates, prop)
  }

}

/**
  * Jdbc数据库驱动类型枚举
  */
object Driver extends Enumeration {
  type Driver = Value
  val MYSQL: Driver.Value = Value("com.mysql.jdbc.Driver")
  val ORACLE: Driver.Value = Value("oracle.jdbc.driver.OracleDriver")
}

/**
  * 连接数据库类型枚举
  */
object DatabaseType extends Enumeration {
  type DatabaseType = Value
  val COURIER, OP, EDMS, SCAM, STATISTIC, DWAPP, EDMSREPORT = Value
}
