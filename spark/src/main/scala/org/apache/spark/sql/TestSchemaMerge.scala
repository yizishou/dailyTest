package org.apache.spark.sql

import org.scalatest.junit.JUnitSuite
import org.junit.Test
import org.apache.spark.sql.types.DataType
import org.apache.spark.sql.types.StructType
import org.apache.commons.io.IOUtils
import java.io._

class TestSchemaMerge extends JUnitSuite {

  @Test def test() {
    val reader = new FileReader(new File("./data/tps.txt"))
    val strs = IOUtils.readLines(reader).toArray(Array[String]())
    IOUtils.closeQuietly(reader)
    val dfs = strs.map(DataType.fromJson(_))
    val map = dfs.zipWithIndex
    val result = map.tail.foldLeft(map.head._1) { (left, b) =>
      StructType.merge(left, b._1)
    }
    println(result.json)
    println("----")
  }

}

object TestSchemaMerge {

  def main(args: Array[String]): Unit = {
    new TestSchemaMerge().test
  }

}