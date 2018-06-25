fcbox-spark
===================================
job运行命令
-

### 打包命令
mvn clean package -P prd

### 派件数据修复

    spark2-submit\
    --class com.fcbox.spark.dw.pdw.FactEdmsPost\
    --master yarn\
    --num-executors 6\
    --driver-memory 1g\
    --executor-memory 4g\
    --executor-cores 2\
    --conf spark.yarn.executor.memoryOverhead=3072\
    fcbox-spark-job.jar 20180520

### 柜机点击日志统计

    spark2-submit\
    --class com.fcbox.spark.edlog.EdClickStaApp\
    --master yarn\
    --num-executors 2\
    --driver-memory 1g\
    --executor-memory 1g\
    --executor-cores 1\
    --conf spark.yarn.executor.memoryOverhead=3072\
    fcbox-spark-job.jar 20180529
    
### edms活动以及优惠券统计

    spark2-submit\
    --class com.fcbox.spark.dw.app.EdmActivitySta\
    --master yarn\
    --num-executors 2\
    --driver-memory 1g\
    --executor-memory 1g\
    --executor-cores 1\
    --conf spark.yarn.executor.memoryOverhead=3072\
    fcbox-spark-job.jar
 
