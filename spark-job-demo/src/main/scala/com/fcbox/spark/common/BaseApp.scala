package com.fcbox.spark.common

import com.fcbox.spark.common.util.ResourceUtils
import org.apache.spark.internal.Logging

trait BaseApp extends App with Logging{

  /**
    * get配置文件内容
    *
    * @param args
    * @return
    */
  def ^(args: String): String = {
    val content = ResourceUtils.getFileContent(args)
    if (content == null) {
      ResourceUtils.getConfig(args)
    } else {
      content
    }
  }

}
