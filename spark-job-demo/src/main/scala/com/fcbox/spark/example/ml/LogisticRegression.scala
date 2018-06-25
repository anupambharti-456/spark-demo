package com.fcbox.spark.example.ml

import org.apache.spark.mllib.classification.{LogisticRegressionWithSGD, LogisticRegressionWithLBFGS, SVMWithSGD}
import org.apache.spark.mllib.feature.StandardScaler
import org.apache.spark.mllib.linalg.Vectors
import org.apache.spark.mllib.optimization.{SquaredL2Updater, L1Updater}
import org.apache.spark.mllib.regression.LabeledPoint
import org.apache.spark.mllib.util.MLUtils
import org.apache.spark.{SparkConf, SparkContext}

object LogisticRegression {
  val conf = new SparkConf()
  conf.setAppName("spark")
  conf.setMaster("local[3]")
  val sc = new SparkContext(conf)

  def main(args: Array[String]): Unit = {
    //获取原始数据
    val inputData = MLUtils.loadLibSVMFile(sc, "E:\\ml\\随堂资料--20160707\\随堂资料--20160707\\健康状况训练集.txt")
    //val inputData = MLUtils.loadLibSVMFile(sc, "D:\\spark数据分析\\分类\\线性不可分数据集.txt")
      //val inputData = MLUtils.loadLibSVMFile(sc, "D:\\spark数据分析\\分类\\w0测试数据.txt")
      .map { labelpoint =>
      val label = labelpoint.label
      val feature = labelpoint.features
      val array = Array(feature(0), feature(1), feature(0) * feature(1))
      val convertFeature = Vectors.dense(array)
      new LabeledPoint(label, convertFeature)
    }
    val splits = inputData.randomSplit(Array(0.7, 0.3))
    val (trainingData, testData) = (splits(0), splits(1))
    val lr = new LogisticRegressionWithLBFGS()
    // val lr=new LogisticRegressionWithSGD()
    // lr.optimizer.setMiniBatchFraction(0.3)
    //设置有无w0
    lr.setIntercept(true)
    //lr.optimizer.setUpdater(new L1Updater)
    //lr.optimizer.setUpdater(new SquaredL2Updater)
    //lr.optimizer.setRegParam(0.1)
    val model = lr.run(trainingData)
    val result = testData
      .map { point => Math.abs(point.label - model.predict(point.features)) }
    println("正确率=" + (1.0 - result.mean()))
    println(model.weights.toArray.mkString(" "))
    println(model.intercept)
    //    val result=testData.foreach{p=>
    //    val score=model.clearThreshold.predict(p.features)
    //   println(score)
    //   }
    //    println("阈值为"+model.getThreshold)
  }

}
