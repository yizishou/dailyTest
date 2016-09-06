package cn.syz.spark.udf

import scala.reflect.runtime.universe._
import scala.reflect.runtime.{ universe => ru }

object TestUdf2Test {

  val METHOD_NAME = "call"
  val m = runtimeMirror(getClass.getClassLoader)

  def main(args: Array[String]): Unit = {
    val classFullName = "syz.test.spark.udf.TestUdf2"
    val classSymbol = m.staticClass(classFullName)
    val tpe = classSymbol.selfType
    val s = classSymbol.baseClasses.filter(_.fullName.startsWith("org.apache.spark.sql.api.java.UDF")).head
    val sss = s.asClass.typeParams.last.asType.toType
    val tp = sss.asSeenFrom(tpe, s)

    val ctor = tpe.declaration(ru.nme.CONSTRUCTOR).asMethod
    val cm = m.reflectClass(classSymbol)
    val ctorm = cm.reflectConstructor(ctor)
    val instance = ctorm()
  }

}