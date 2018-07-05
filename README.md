spark-demo
===================================

### 打包命令
mvn clean package -P prd

### demo

    spark2-submit\
    --class com.fcbox.spark.example.DemoSta\
    --master yarn\
    --num-executors 6\
    --driver-memory 1g\
    --executor-memory 4g\
    --executor-cores 2\
    --conf spark.yarn.executor.memoryOverhead=3072\
    fcbox-spark-job.jar 20180520


 
