package org.apache.spark.sql

import org.apache.spark.SparkConf
import org.apache.spark.SparkContext

object GetElementTest {

  def main(args: Array[String]): Unit = {
    val sc = new SparkContext("local", "test", new SparkConf())
    val sqlContext = new SQLContext(sc)
    import sqlContext.sql
    sql("select split('100,200,255,-20', ',')").show
    sql("select get_element(split('100,200,255,-20', ','), 2)").show
    sql("select get_element(split('100,200,255,-20', ','), 4)").show
    val arr = sqlContext.read.json("file:///D:/tmp/arr.json")
    val map = sqlContext.read.parquet("file:///D:/tmp/map2.parquet")
    arr.registerTempTable("arr")
    map.registerTempTable("map")
    sql("""
        select v,
        get_element(v, 0),
        get_element(v, 1),
        get_element(v, 2),
        get_element(v, 3),
        get_element(v, 4)
        from arr
        """).show
    sql("""
        select v,
        get_element(v, 0),
        get_element(v, 1),
        get_element(v, 2),
        get_element(v, 3),
        get_element(v, 4)
        from map
        """).show
    sc.stop
  }

}
