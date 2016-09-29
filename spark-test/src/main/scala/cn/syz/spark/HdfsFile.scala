package cn.syz.spark

import java.io._
import java.net.URI
import java.util.Locale

import org.apache.hadoop.conf.Configuration
import org.apache.hadoop.fs.{ FSDataInputStream, Path, FileSystem, FileStatus }

import scala.collection.mutable.ArrayBuffer

object HdfsFile extends Serializable {

  def getLastModified(dsf: String) = {
    var last: Long = 0
    var fs: FileSystem = null
    var ft: FileStatus = null
    try {
      val conf: Configuration = new Configuration
      fs = FileSystem.get(URI.create(dsf), conf)
      if (fs.exists(new Path(dsf))) {
        ft = fs.getFileStatus(new Path(dsf))
        last = ft.getModificationTime
      }

    } catch {
      case e: IOException => {
        e.printStackTrace
      }
    } finally {

      if (fs != null) {
        fs.close
      }
    }
    last
  }

  def isExist(dsf: String) = {

    var fs: FileSystem = null

    try {
      val conf: Configuration = new Configuration
      fs = FileSystem.get(URI.create(dsf), conf)
      fs.exists(new Path(dsf))
    } catch {
      case e: IOException => {
        e.printStackTrace
        false
      }
    } finally {

      if (fs != null) {
        fs.close
      }
    }

  }

  def readFile(dsf: String) = {
    val sb: StringBuffer = new StringBuffer
    var fs: FileSystem = null
    var hdfsInStream: FSDataInputStream = null
    try {
      val conf: Configuration = new Configuration
      fs = FileSystem.get(URI.create(dsf), conf)
      if (!fs.isFile(new Path(dsf))) {
        System.out.println(dsf + " is not file")

      }
      hdfsInStream = fs.open(new Path(dsf))
      val ioBuffer: Array[Byte] = new Array[Byte](1024)
      var readLen: Int = hdfsInStream.read(ioBuffer)
      while (readLen != -1) {
        sb.append(new String(ioBuffer, 0, readLen, "UTF-8"))
        readLen = hdfsInStream.read(ioBuffer)
      }
    } catch {
      case e: IOException => {
        e.printStackTrace
      }
    } finally {
      if (hdfsInStream != null) {
        hdfsInStream.close
      }
      if (fs != null) {
        fs.close
      }
    }
    sb.toString
  }

  /**
   * 遍历hdfs目录文件
   *
   * @param hdfshost hdfs://ip:port
   * @param hdfspath 如: /china,根目录 /
   * @return 所有文件路径的集合
   * @throws Exception
   */
  @throws(classOf[Exception])
  def listFiles(hdfshost: String, hdfspath: String): ArrayBuffer[String] = {

    val arrbuff1 = ArrayBuffer[String]()
    val conf = new Configuration
    val hdfs = FileSystem.get(URI.create(hdfshost), conf)
    val fs = hdfs.listStatus(new Path(hdfspath))
    if (fs.length > 0) {
      for (f <- fs) {
        showDir(f, hdfs, arrbuff1)
      }
    }
    return arrbuff1
  }

  /**
   * 列出目录下文件结构
   *
   * @param fs
   * @param hdfs
   * @throws Exception
   */
  @throws(classOf[Exception])
  private def showDir(fs: FileStatus, hdfs: FileSystem, arrbuff1: ArrayBuffer[String]) {
    val path: Path = fs.getPath
    val time: Long = fs.getModificationTime

    arrbuff1 += path.toString;
    //result.add(path.toString + (if (fs.isFile) " is file and size = " + fs.getLen else " is dectory and child sum = " + hdfs.listStatus(path).length))
    if (fs.isDir) {
      val f: Array[FileStatus] = hdfs.listStatus(path)
      if (f.length > 0) {
        for (file <- f) {
          showDir(file, hdfs, arrbuff1)
        }
      }
    }
  }

  def bytesToString(size: Long): String = {
    val TB = 1L << 40
    val GB = 1L << 30
    val MB = 1L << 20
    val KB = 1L << 10

    val (value, unit) = {
      if (size >= 2 * TB) {
        (size.asInstanceOf[Double] / TB, "TB")
      } else if (size >= 2 * GB) {
        (size.asInstanceOf[Double] / GB, "GB")
      } else if (size >= 2 * MB) {
        (size.asInstanceOf[Double] / MB, "MB")
      } else if (size >= 2 * KB) {
        (size.asInstanceOf[Double] / KB, "KB")
      } else {
        (size.asInstanceOf[Double], "B")
      }
    }
    "%.1f %s".formatLocal(Locale.CHINA, value, unit)
  }

  def listHadoopConfiguration(src: String): Array[String] = {
    val conf: Configuration = new Configuration
    val fs = FileSystem.get(URI.create(src), conf)
    import scala.collection.JavaConversions._
    conf.iterator.map(e => s"${e.getKey}=${e.getValue}").toArray
  }

  def listRoot(src: String): Array[String] = {
    val conf: Configuration = new Configuration
    val fs = FileSystem.get(URI.create(src), conf)
    fs.listStatus(new Path("/")).map(_.toString)
  }

  def main(args: Array[String]) {
    println(isExist("/"))
    println(isExist("/user"))
    println(isExist("/user/hadoop"))
    println(isExist("/user/hadoop/tmp"))
    println(isExist("/user/hadoop/tmp/preview"))
    println(isExist("/user/hadoop/tmp/preview/58AF5553DCB4B3C70C5A07DF4EEC19E7"))
    println(isExist("/user/hadoop/tmp/preview/58AF5553DCB4B3C70C5A07DF4EEC19E7.schema"))
  }

}
