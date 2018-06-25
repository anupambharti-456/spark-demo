package com.fcbox.spark.common.jdbc

import java.sql.Types
import java.util.TimeZone

import org.apache.spark.sql.catalyst.util.DateTimeUtils
import org.apache.spark.sql.internal.SQLConf
import org.apache.spark.sql.jdbc.{JdbcDialect, JdbcType}
import org.apache.spark.sql.types._

/**
  * oracle表中如果是Date类型,则会丢失时分秒,注册该方言解决这个问题
  * JdbcDialects.registerDialect(OracleJdbcDialect)
  */
case object OracleJdbcDialect extends JdbcDialect {

  private[jdbc] val BINARY_FLOAT = 100
  private[jdbc] val BINARY_DOUBLE = 101



  override def canHandle(url: String): Boolean = url.startsWith("jdbc:oracle")

  override def getCatalystType(sqlType: Int, typeName: String, size: Int, md: MetadataBuilder): Option[DataType] = {
    sqlType match {
      case Types.NUMERIC =>
        val scale = if (null != md) md.build().getLong("scale") else 0L
        size match {
          // Handle NUMBER fields that have no precision/scale in special way
          // because JDBC ResultSetMetaData converts this to 0 precision and -127 scale
          // For more details, please see
          // https://github.com/apache/spark/pull/8780#issuecomment-145598968
          // and
          // https://github.com/apache/spark/pull/8780#issuecomment-144541760
          case 0 => Option(DecimalType(DecimalType.MAX_PRECISION, 10))
          // Handle FLOAT fields in a special way because JDBC ResultSetMetaData converts
          // this to NUMERIC with -127 scale
          // Not sure if there is a more robust way to identify the field as a float (or other
          // numeric types that do not specify a scale.
          case _ if scale == -127L => Option(DecimalType(DecimalType.MAX_PRECISION, 10))
          case _ => None
        }
      case BINARY_FLOAT => Some(FloatType) // Value for OracleTypes.BINARY_FLOAT
      case BINARY_DOUBLE => Some(DoubleType) // Value for OracleTypes.BINARY_DOUBLE
      case Types.DATE => Some(TimestampType) // Value for OracleTypes.BINARY_DOUBLE
      case _ => None
    }
  }

  override def getJDBCType(dt: DataType): Option[JdbcType] = dt match {
    // For more details, please see
    // https://docs.oracle.com/cd/E19501-01/819-3659/gcmaz/
    case BooleanType => Some(JdbcType("NUMBER(1)", java.sql.Types.BOOLEAN))
    case IntegerType => Some(JdbcType("NUMBER(10)", java.sql.Types.INTEGER))
    case LongType => Some(JdbcType("NUMBER(19)", java.sql.Types.BIGINT))
    case FloatType => Some(JdbcType("NUMBER(19, 4)", java.sql.Types.FLOAT))
    case DoubleType => Some(JdbcType("NUMBER(19, 4)", java.sql.Types.DOUBLE))
    case ByteType => Some(JdbcType("NUMBER(3)", java.sql.Types.SMALLINT))
    case ShortType => Some(JdbcType("NUMBER(5)", java.sql.Types.SMALLINT))
    case StringType => Some(JdbcType("VARCHAR2(255)", java.sql.Types.VARCHAR))
    case TimestampType => Some(JdbcType("DATE", java.sql.Types.DATE))
    case DateType => Some(JdbcType("DATE", java.sql.Types.DATE))
    case _ => None
  }

}
