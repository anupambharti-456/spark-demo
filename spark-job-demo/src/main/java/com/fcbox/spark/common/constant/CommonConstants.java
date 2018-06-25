package com.fcbox.spark.common.constant;


import com.fcbox.spark.common.util.ResourceUtils;

/**
 * 一些公共常量
 */
public interface CommonConstants {

    /**
     * spark的master
     */
    String MASTER = ResourceUtils.getConfig("master");


}
