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
import org.apache.spark.sql.catalyst.util.ArrayData

/**
 * <pre>1. get_element(array, index)</pre>
 *   从Array中获取第index个元素<br>
 *   如果Array为空，返回null；如果指针越界，返回null<br>
 * <pre>2. get_element(map, key)</pre>
 *   从Map中获取key对应的value<br>
 *   如果Map为空，返回null；如果不包含key，返回null<br>
 * @author yizhu.sun 2016年8月17日
 */
case class GetElement(left: Expression, right: Expression)
    extends BinaryExpression with ImplicitCastInputTypes {

  override def dataType: DataType = left.dataType match {
    case arrType: ArrayType => arrType.elementType
    case mapType: MapType => mapType.valueType
  }

  override def inputTypes: Seq[AbstractDataType] = left.dataType match {
    case arrType: ArrayType => Seq(ArrayType, IntegerType)
    case mapType: MapType => Seq(MapType, mapType.keyType)
  }

  override def checkInputDataTypes(): TypeCheckResult = left.dataType match {
    case arrType: ArrayType =>
      if (right.dataType != IntegerType) {
        TypeCheckResult.TypeCheckFailure(s"The second input of $prettyName(array, index) should be IntegerType")
      } else {
        TypeCheckResult.TypeCheckSuccess
      }
    case mapType: MapType =>
      if (right.dataType != mapType.keyType) {
        TypeCheckResult.TypeCheckFailure(s"The second input of $prettyName(map, key) should be key type of the Map")
      } else {
        TypeCheckResult.TypeCheckSuccess
      }
    case _ => TypeCheckResult.TypeCheckFailure(s"The first input of $prettyName() should be ArrayType or MapType")
  }

  override def nullable: Boolean = true

  override def nullSafeEval(arrOrMap: Any, indOrKey: Any): Any = left.dataType match {
    case arrType: ArrayType =>
      arrOrMap.asInstanceOf[ArrayData].foreach(arrType.elementType,
        (i, v) => if (i == indOrKey) {
          return v
        })
      null
    case mapType: MapType =>
      arrOrMap.asInstanceOf[MapData].foreach(mapType.keyType, mapType.valueType,
        (k, v) => if (k == indOrKey) {
          return v
        })
      null
  }

  override def genCode(ctx: CodeGenContext, ev: GeneratedExpressionCode): String =
    left.dataType match {
      case arrType: ArrayType =>
        nullSafeCodeGen(ctx, ev, (arr, index) => {
          val getValue = ctx.getValue(arr, arrType.elementType, s"$index")
          s"""
      if ($arr.numElements() > $index) {
        ${ev.value} = $getValue;
      } else {
        ${ev.isNull} = true;
      }
     """
        })
      case mapType: MapType =>
        nullSafeCodeGen(ctx, ev, (map, kToFind) => {
          val i = ctx.freshName("i")
          val keyArray = ctx.freshName("keyArray")
          val valueArray = ctx.freshName("valueArray")
          val value = ctx.freshName("value")
          val getKey = ctx.getValue(keyArray, mapType.keyType, i)
          val getValue = ctx.getValue(valueArray, mapType.valueType, i)
          s"""
      ArrayData $keyArray = $map.keyArray();
      ArrayData $valueArray = $map.valueArray();
      ${ctx.javaType(mapType.valueType)} $value = null;
      for (int $i = 0; $i < $keyArray.numElements(); $i++) {
        if (${ctx.genEqual(mapType.keyType, kToFind, getKey)}) {
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
