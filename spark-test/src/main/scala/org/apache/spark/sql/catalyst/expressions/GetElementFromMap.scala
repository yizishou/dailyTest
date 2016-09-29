package org.apache.spark.sql.catalyst.expressions

import org.apache.spark.sql.catalyst.analysis.TypeCheckResult
import org.apache.spark.sql.catalyst.expressions.codegen.CodeGenContext
import org.apache.spark.sql.catalyst.expressions.codegen.GeneratedExpressionCode
import org.apache.spark.sql.types.AbstractDataType
import org.apache.spark.sql.types.MapData
import org.apache.spark.sql.types.ArrayType
import org.apache.spark.sql.types.DataType
import org.apache.spark.sql.types.IntegerType
import org.apache.spark.sql.types.MapType

case class GetElementFromMap(left: Expression, right: Expression)
    extends BinaryExpression with ImplicitCastInputTypes {

  def keyType = left.dataType.asInstanceOf[MapType].keyType
  def valueType = left.dataType.asInstanceOf[MapType].valueType
  override def dataType: DataType = valueType

  override def inputTypes: Seq[AbstractDataType] = Seq(MapType, keyType)

  override def checkInputDataTypes(): TypeCheckResult = {
    if (!left.dataType.isInstanceOf[MapType]) {
      TypeCheckResult.TypeCheckFailure("The first input of get_element_from_map() should be MapType")
    } else if (right.dataType != keyType) {
      TypeCheckResult.TypeCheckFailure("The second input of get_element_from_map() should be key type of the first input")
    } else {
      TypeCheckResult.TypeCheckSuccess
    }
  }

  override def nullable: Boolean = true

  override def nullSafeEval(map: Any, kToFind: Any): Any = {
    map.asInstanceOf[MapData].foreach(keyType, valueType,
      (k, v) => if (k == kToFind) {
        return v
      })
    null
  }

  override def genCode(ctx: CodeGenContext, ev: GeneratedExpressionCode): String = {
    nullSafeCodeGen(ctx, ev, (map, kToFind) => {
      val i = ctx.freshName("i")
      val keyArray = ctx.freshName("keyArray")
      val valueArray = ctx.freshName("valueArray")
      val value = ctx.freshName("value")
      val getKey = ctx.getValue(keyArray, keyType, i)
      val getValue = ctx.getValue(valueArray, valueType, i)
      s"""
      ArrayData $keyArray = $map.keyArray();
      ArrayData $valueArray = $map.valueArray();
      ${ctx.javaType(valueType)} $value = null;
      for (int $i = 0; $i < $keyArray.numElements(); $i++) {
        if (${ctx.genEqual(keyType, kToFind, getKey)}) {
          $value = $getValue;
          break;
        }
      }
      if ($value == null) {
        ${ev.isNull} = true;
      } else {
        ${ev.value} = $value;
      }
     """
    })
  }

  override def prettyName: String = "get_element_from_map"
}
