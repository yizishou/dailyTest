package cn.syz.spark

import org.apache.spark.SparkContext
import org.apache.spark.SparkConf

object T1 {

  def main(args: Array[String]): Unit = {
    val sc = new SparkContext("local", "test", new SparkConf())
    val src = sc.textFile("file:///d:/downloads/part-00000")
    src.flatMap(s => {
      val ss = s.split("\t")
      val s1 = ss(0).split(",")(0)
      ss(1).split(",").map(s1 + ',' + _.trim)
    }).saveAsTextFile("file:///d:/downloads/tdid-time-00000.txt")
    //.foreach(println)
    sc.stop
  }

}