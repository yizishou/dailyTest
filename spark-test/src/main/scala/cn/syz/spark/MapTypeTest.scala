package cn.syz.spark

import org.apache.spark.SparkContext
import org.apache.spark.SparkConf
import org.apache.spark.sql.types._
import org.apache.spark.sql.SQLContext
import org.apache.spark.sql.Row

object MapTypeTest {

  val schema: StructType = StructType(Seq(
    StructField("m", DataTypes.createMapType(StringType, StringType), true),
    StructField("a", DataTypes.createArrayType(StringType), true)))

  def main(args: Array[String]): Unit = {
    val sc = new SparkContext("local", "test", new SparkConf())
    val sqlContext = new SQLContext(sc)
    import sqlContext.sql
    val rdd = sc.makeRDD(Seq(
      Row(Map("1" -> "a", "2" -> null), Seq("a", null)),
      Row(Map(("1", "a")), Seq("a")),
      Row(Map(), Seq()),
      Row(null, null)))
    val df = sqlContext.createDataFrame(rdd, schema)
    df.registerTempTable("tab")
    df.write.mode("overwrite").parquet("file:///D:/tmp/tab.parquet")
    sql("select m, m['1'], m['2'], a, a[0], a[1] from tab").show
    val df2 = sqlContext.read.parquet("file:///D:/tmp/tab.parquet")
    df.registerTempTable("tab2")
    sql("select m, m['1'], m['2'], a, a[0], a[1] from tab2").show
    sc.stop
  }

}