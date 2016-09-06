package org.apache.spark.sql

import org.junit.Test
import org.scalatest.junit.JUnitSuite
import org.apache.spark.SparkContext
import org.apache.spark.SparkConf
import org.apache.spark.sql.types.StructType
import org.apache.spark.sql.types.StructField
import org.apache.spark.sql.types.StringType
import org.apache.spark.sql.types.IntegerType
import org.apache.spark.annotation.DeveloperApi
import org.apache.spark.annotation.Experimental
import org.apache.spark.sql.catalyst.analysis.FunctionRegistry
import org.apache.spark.sql.catalyst.expressions.Json

class JsonTest extends JUnitSuite {

  val subSchema = StructType(Array(
    StructField("a", StringType, true),
    StructField("b", StringType, true),
    StructField("c", IntegerType, true)))

  val schema = StructType(Array(
    StructField("x", subSchema, true)))

  @Test def test: Unit = {
    val sc = new SparkContext("local", "test", new SparkConf())
    val sqlContext = new SQLContext(sc)
    val (name, (info, builder)) = FunctionRegistry.expression[Json]("json")
    sqlContext.functionRegistry.registerFunction(name, info, builder)
    import sqlContext.sql
    val rdd = sc.makeRDD(Seq(Row(Row("12", null, 123)), Row(Row(null, "2222", null))))
    val df = sqlContext.createDataFrame(rdd, schema)
    df.registerTempTable("df")
    sql("select x, x.a from df").show(5, false)
    sql("select x, x.a from df").printSchema
    sql("select json(x), json(x.a) from df").show(5, false)
    sql("select json(x), json(x.a) from df").printSchema
    //    val df2 = sqlContext.read.parquet("file:///D:/tmp/logaa.parquet")
    //    df2.registerTempTable("df2")
    //    sql("select mAppProfile from df2").show(5, false)
    //    sql("select json(mAppProfile) from df2").show(5, false)
  }
  
}
