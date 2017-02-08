package cn.syz.scala.util

import org.apache.commons.lang.math.NumberUtils

/**
 * @author yizhu.sun 2016年12月13日
 */
object JsonStringUtils {

  /**
   * 仅展开一层JsonArray
   * @author yizhu.sun 2016年8月29日
   * @param src
   */
  def splitJsonArray(src: String): Seq[String] = {
    val result = scala.collection.mutable.ArrayBuffer[String]()
    val keyStack = scala.collection.mutable.Stack[Char]()
    val BEGIN = '^'
    var (jsonStart, jsonEnd, ignore, currKey) = (0, 0, false, BEGIN)
    for (i <- 0 to src.length - 1) {
      if (ignore) {
        ignore = false
      } else {
        val c = src.charAt(i)
        if (currKey == BEGIN) {
          if (c == '[') { // 开始
            currKey = c
          } else {
            return Seq.empty[String]
          }
        } else {
          if ((currKey == '{' && c == '}') || (currKey == '[' && c == ']') || (currKey == '"' && c == '"')) {
            if (keyStack.isEmpty) { // 此时currKey一定是'['
              return result // 结束
            } else {
              currKey = keyStack.pop
              if (c == '}' && keyStack.isEmpty) {
                jsonEnd = i + 1
                result += src.substring(jsonStart, jsonEnd)
              }
            }
          } else {
            if (c == '{' && keyStack.isEmpty) {
              jsonStart = i
            }
            if (c == '\\' && currKey == '"') {
              ignore = true
            } else if (c == '"' || (currKey != '"' && (c == '[' || c == '{'))) {
              keyStack.push(currKey)
              currKey = c
            }
          }
        }
      }
    }
    result
  }

  def isJsonObject(src: String): Boolean = isJsonObject(src, 0)._1

  def isJsonArray(src: String): Boolean = isJsonArray(src, 0)._1

  /**
   * 返回值为Tuple2，其中_1代表校验是否通过，_2代表校验结束时指针位置。如果_1为false则_2表示json出错位置
   */
  private def isJsonArray(src: String, index: Int): (Boolean, Int) = {
    var i = index
    if (i >= src.length || src.charAt(i) != '[') return (false, i)
    var arrSize = 0
    i += 1
    while (i < src.length) {
      i = skipWhiteChars(src, i)
      if (i >= src.length) return (false, i)
      var c = src.charAt(i)
      if (c == ']') {
        return (true, i + 1) // 唯一为true的出口
      }
      if (arrSize > 0) {
        if (c == ',') {
          i = skipWhiteChars(src, i + 1)
          if (i >= src.length) return (false, i)
          c = src.charAt(i)
        } else {
          return (false, i)
        }
      }
      var f: (String, Int) => (Boolean, Int) = null
      if (c == '[') {
        f = isJsonArray
      } else if (c == '{') {
        f = isJsonObject
      } else if (c == '"') {
        f = isString
      } else if (Seq('n', 'N').contains(c)) {
        f = isNull
      } else if (Seq('t', 'T', 'f', 'F').contains(c)) {
        f = isBool
      } else if ((('0' to '9') ++ Seq('-', '.')).contains(c)) {
        f = isNumber
      } else {
        return (false, i)
      }
      var (found, newIndex) = f(src, i)
      if (found) {
        i = newIndex
        arrSize += 1
      } else {
        return (false, newIndex)
      }
    }
    return (false, i)
  }

  /**
   * 返回值为Tuple2，其中_1代表校验是否通过，_2代表校验结束时指针位置。如果_1为false则_2表示json出错位置
   */
  private def isJsonObject(src: String, index: Int): (Boolean, Int) = {
    var i = index
    if (i >= src.length || src.charAt(i) != '{') return (false, i)
    var paramSize = 0
    i += 1
    while (i < src.length) {
      i = skipWhiteChars(src, i)
      if (i >= src.length) return (false, i)
      var c = src.charAt(i)
      if (c == '}') {
        return (true, i + 1) // 唯一为true的出口
      }
      if (paramSize > 0) {
        if (c == ',') {
          i = skipWhiteChars(src, i + 1)
          if (i >= src.length) return (false, i)
          c = src.charAt(i)
        } else {
          return (false, i)
        }
      }
      if (c == '"') {
        var (found, newIndex) = isString(src, i)
        if (found) {
          i = newIndex
        } else {
          return (false, newIndex)
        }
      }
      i = skipWhiteChars(src, i)
      if (i >= src.length) return (false, i)
      c = src.charAt(i)
      if (c != ':') {
        return (false, i)
      }
      i = skipWhiteChars(src, i + 1)
      if (i >= src.length) return (false, i)
      c = src.charAt(i)
      var f: (String, Int) => (Boolean, Int) = null
      if (c == '[') {
        f = isJsonArray
      } else if (c == '{') {
        f = isJsonObject
      } else if (c == '"') {
        f = isString
      } else if (Seq('n', 'N').contains(c)) {
        f = isNull
      } else if (Seq('t', 'T', 'f', 'F').contains(c)) {
        f = isBool
      } else if ((Set('-', '.') ++ ('0' to '9')).contains(c)) {
        f = isNumber
      } else {
        return (false, i)
      }
      var (found, newIndex) = f(src, i)
      if (found) {
        i = newIndex
        paramSize += 1
      } else {
        return (false, newIndex)
      }
    }
    return (false, i)
  }

  private def isString(src: String, index: Int): (Boolean, Int) = {
    var i = index
    if (i >= src.length || src.charAt(i) != '"') return (false, i)
    i += 1
    while (i < src.length) {
      var c = src.charAt(i)
      if (c == '"') {
        return (true, i + 1) // 唯一为true的出口
      } else if (c == '\\') {
        i += 2
      } else {
        i += 1
      }
    }
    return (false, i)
  }

  private def isNull(src: String, index: Int): (Boolean, Int) = {
    val bol = index + 3 < src.length && src.substring(index, index + 4).equalsIgnoreCase("null")
    (bol, if (bol) index + 4 else index)
  }

  private def isBool(src: String, index: Int): (Boolean, Int) = {
    val isTrue = index + 3 < src.length && src.substring(index, index + 4).equalsIgnoreCase("true")
    val isFalse = !isTrue && index + 4 < src.length && src.substring(index, index + 5).equalsIgnoreCase("false")
    (isTrue || isFalse, if (isTrue) index + 4 else if (isFalse) index + 5 else index)
  }

  private def isNumber(src: String, index: Int): (Boolean, Int) = {
    val validChars = Set('-', '.') ++ ('0' to '9')
    var i = index
    while (i < src.length && validChars.contains(src.charAt(i))) i += 1
    val numString = src.substring(index, i)
    if (NumberUtils.isNumber(numString)) (true, i) else (false, index)
  }

  private def skipWhiteChars(src: String, index: Int): Int = {
    var i = index
    while (i < src.length && src.charAt(i) <= ' ') { i += 1 }
    i
  }

  /**
   * 校验json对象是否合规
   * @author yizhu.sun 2016年10月20日
   */
  @deprecated(message = "use isJsonObject(String)")
  private def maybeJsonObject(src: String): Boolean = {
    if (src == null || src.length == 0) return false
    if (src.charAt(0) != '{' || src.charAt(src.length - 1) != '}') return false
    val keyStack = scala.collection.mutable.Stack[Char]()
    val BEGIN = '^'
    val END = '$'
    var (ignore, currKey) = (false, BEGIN)
    for (i <- 0 until src.length) {
      if (ignore) {
        ignore = false
      } else {
        val c = src.charAt(i)
        if (currKey == BEGIN) {
          if (c == '{') {
            currKey = c
          } else {
            return false
          }
        } else if (currKey == END) {
          return false
        } else {
          if ((currKey == '{' && c == '}') || (currKey == '[' && c == ']') || (currKey == '"' && c == '"')) {
            if (keyStack.isEmpty) { // 此时currKey一定是'{'
              currKey = END // 结束
            } else {
              currKey = keyStack.pop
            }
          } else {
            if (c == '\\' && currKey == '"') {
              ignore = true
            } else if (c == '"' || (currKey != '"' && (c == '[' || c == '{'))) {
              keyStack.push(currKey)
              currKey = c
            }
          }
        }
      }
    }
    keyStack.isEmpty && currKey == END
  }

}