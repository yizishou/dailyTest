package org.apache.spark.sql.catalyst.expressions

import java.io.CharArrayWriter

import org.apache.spark.sql.catalyst.InternalRow
import org.apache.spark.sql.catalyst.analysis.TypeCheckResult
import org.apache.spark.sql.catalyst.expressions.codegen.CodeGenContext
import org.apache.spark.sql.catalyst.expressions.codegen.GeneratedExpressionCode
import org.apache.spark.sql.execution.datasources.json.JacksonGenerator
import org.apache.spark.sql.types.DataType
import org.apache.spark.sql.types.Metadata
import org.apache.spark.sql.types.StringType
import org.apache.spark.sql.types.StructField
import org.apache.spark.sql.types.StructType

import com.fasterxml.jackson.core.JsonFactory

/**
 * 将StructType类型的字段转换为Json String
 * @author yizhu.sun 2016年8月30日
 */
case class Json(child: Expression) extends UnaryExpression with ImplicitCastInputTypes {

  override def dataType: DataType = StringType
  override def inputTypes: Seq[DataType] = Seq(child.dataType)

  val inputStructType: StructType = child.dataType match {
    case st: StructType => st
    case _ => StructType(Seq(
      child match {
        case ne: NamedExpression =>
          StructField(ne.name, ne.dataType, ne.nullable, ne.metadata)
        case _ =>
          StructField("col", child.dataType, child.nullable, Metadata.empty)
      }))
  }

  override def checkInputDataTypes(): TypeCheckResult = TypeCheckResult.TypeCheckSuccess

  // 参照 org.apache.spark.sql.DataFrame.toJSON
  // 参照 org.apache.spark.sql.execution.datasources.json.JsonOutputWriter.writeInternal
  protected override def nullSafeEval(data: Any): Any = {
    val writer = new CharArrayWriter
    val gen = new JsonFactory().createGenerator(writer).setRootValueSeparator(null)
    val internalRow = child.dataType match {
      case _: StructType => data.asInstanceOf[InternalRow]
      case _ => InternalRow(data)
    }
    JacksonGenerator(inputStructType, gen)(internalRow)
    gen.flush
    gen.close
    writer.toString
  }

  override def genCode(ctx: CodeGenContext, ev: GeneratedExpressionCode): String = {
    val writer = ctx.freshName("writer")
    val gen = ctx.freshName("gen")
    val st = ctx.freshName("st")
    val typeJson = inputStructType.json
    def getDataExp(data: Any) =
      child.dataType match {
        case _: StructType => s"$data"
        case _ => s"new org.apache.spark.sql.catalyst.expressions.GenericInternalRow(new Object[]{$data})"
      }
    nullSafeCodeGen(ctx, ev, (row) => {
      s"""
        | if ($row == null) {
        |   ${ev.isNull} = true;
        | } else {
        |   try {
        |     org.apache.spark.sql.types.StructType $st = (org.apache.spark.sql.types.StructType)
        |         org.apache.spark.sql.types.DataType.fromJson("${typeJson.replace("\"", "\\\"")}");
        |     java.io.CharArrayWriter $writer = new java.io.CharArrayWriter();
        |     com.fasterxml.jackson.core.JsonGenerator $gen = new com.fasterxml.jackson.core.JsonFactory().createGenerator($writer).setRootValueSeparator(null);
        |     org.apache.spark.sql.execution.datasources.json.JacksonGenerator.apply($st, $gen, ${getDataExp(row)});
        |     $gen.flush();
        |     $gen.close();
        |     ${ev.value} = UTF8String.fromString($writer.toString());
        |   } catch (Exception e) {
        |   }
        | }
       """.stripMargin
    })
  }

}
