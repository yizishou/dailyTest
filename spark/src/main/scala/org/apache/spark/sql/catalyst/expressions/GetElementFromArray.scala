package org.apache.spark.sql.catalyst.expressions

import org.apache.spark.sql.catalyst.analysis.TypeCheckResult
import org.apache.spark.sql.catalyst.expressions.codegen.CodeGenContext
import org.apache.spark.sql.catalyst.expressions.codegen.GeneratedExpressionCode
import org.apache.spark.sql.types._

case class GetElementFromArray(left: Expression, right: Expression)
    extends BinaryExpression with ImplicitCastInputTypes {

  override def dataType: DataType = left.dataType.asInstanceOf[ArrayType].elementType

  override def inputTypes: Seq[AbstractDataType] = Seq(ArrayType, IntegerType)

  override def checkInputDataTypes(): TypeCheckResult = {
    if (right.dataType != IntegerType) {
      TypeCheckResult.TypeCheckFailure("The second input of get_element_from_array() should be IntegerType")
    } else if (!left.dataType.isInstanceOf[ArrayType]) {
      TypeCheckResult.TypeCheckFailure("The first input of get_element_from_array() should be ArrayType")
    } else {
      TypeCheckResult.TypeCheckSuccess
    }
  }

  override def nullable: Boolean = true

  override def nullSafeEval(arr: Any, index: Any): Any = {
    arr.asInstanceOf[ArrayData].foreach(left.dataType.asInstanceOf[ArrayType].elementType,
      (i, v) => if (i == index) {
        return v
      })
    null
  }

  override def genCode(ctx: CodeGenContext, ev: GeneratedExpressionCode): String = {
    nullSafeCodeGen(ctx, ev, (arr, index) => {
      val getValue = ctx.getValue(arr, left.dataType.asInstanceOf[ArrayType].elementType, s"$index")
      s"""
      if ($arr.numElements() > $index) {
        ${ev.value} = $getValue;
      } else {
        ${ev.isNull} = true;
      }
     """
    })
  }

  override def prettyName: String = "get_element_from_array"
}
