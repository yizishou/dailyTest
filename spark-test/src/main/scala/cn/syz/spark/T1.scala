package cn.syz.spark

import org.apache.spark.SparkConf
import org.apache.spark.SparkContext
import org.apache.spark.annotation.Experimental
import org.apache.spark.sql.DataFrame
import org.apache.spark.sql.SQLContext
import org.apache.spark.sql.catalyst._
import org.junit.Test

import junit.framework.TestSuite
import org.apache.spark.sql.execution.QueryExecution

class T1 extends TestSuite {

  @Test def t1 {
    val sc = new SparkContext("local", "test", new SparkConf())
    val ssc = new SQLContext(sc)
    val df = ssc.read.parquet("file:///d:/tmp/logaa.parquet")
    df.registerTempTable("df")
//    val sql = "select mDeviceId, mDeviceProfile.mGis.* from df where aa"
    val sql = "select mDeviceId, mDeviceProfile.mGis.* from df where mDeviceId"
    CustomSqlParser.checkParse(sql) match {
      case ParseSucceed(parsed) =>
        println("SQL parse succeed")
        val execution = new QueryExecution(ssc, parsed)
        val analyzed = printResult("analyze")(execution.analyzed)
        val optimized = printResult("optimize")(execution.optimizedPlan)
        val physical = printResult("execute")(execution.executedPlan)
        println(execution)
      case ParseFailed(msg, line, column) =>
        println("SQL parse failed")
        println(msg)
        println(line)
        println(column)
    }
    sc.stop
  }

  def printResult[A](op: String)(f: => A) {
    try {
      f.toString
      println(s"SQL $op succeed")
    } catch {
      case t: Throwable =>
        println(s"SQL $op failed")
        println(t.getMessage)
    }
  }

}

object T1 {

  def main(args: Array[String]): Unit = {
    new T1().t1
  }

}
