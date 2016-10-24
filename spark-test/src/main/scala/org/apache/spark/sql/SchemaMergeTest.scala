package org.apache.spark.sql

import org.scalatest.junit.JUnitSuite
import org.junit.Test
import org.apache.spark.sql.types.DataType
import org.apache.spark.sql.types.StructType
import org.apache.commons.io.IOUtils
import java.io._
import java.nio.charset.Charset

class SchemaMergeTest extends JUnitSuite {

  @Test def test() {
    val strs = readLinesFromFile("schema.json")
    val tps = strs.map(DataType.fromJson(_).asInstanceOf[StructType])
    val map = tps.zipWithIndex
    val result = map.tail.foldLeft(map.head._1) { (left, b) =>
      StructType.merge(left, b._1).asInstanceOf[StructType]
    }
    println(result.json)
    println("----")
    println(result.treeString)
  }

  def readLinesFromFile(fileName: String): Array[String] = {
    var is: InputStream = null
    try {
      is = Thread.currentThread.getContextClassLoader.getResourceAsStream(fileName)
      IOUtils.readLines(is, Charset.forName("UTF-8")).toArray(Array.empty[String])
    } finally {
      IOUtils.closeQuietly(is)
    }
  }

}

object SchemaMergeTest {

  def main(args: Array[String]): Unit = {
    new SchemaMergeTest().test
  }

}