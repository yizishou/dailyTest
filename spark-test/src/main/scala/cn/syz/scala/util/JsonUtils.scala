package cn.syz.scala.util

object JsonUtils {

  /**
   * 校验json对象是否合规
   * @author yizhu.sun 2016年10月20日
   */
  def maybeJson(src: String): Boolean = {
    if (src == null || src.length == 0) return false
    if (src.charAt(0) != '{' || src.charAt(src.length - 1) != '}') return false
    val keyStack = scala.collection.mutable.Stack[Char]()
    var currKey: Char = '<' // maybe '[', '{', '"'
    var ignore = false
    for (i <- 0 until src.length) {
      if (ignore) {
        ignore = false
      } else {
        val c = src.charAt(i)
        if (currKey == '<') {
          if (c == '{') {
            currKey = c
          } else {
            return false
          }
        } else if (currKey == '>') {
          return false
        } else {
          if ((currKey == '{' && c == '}') || (currKey == '[' && c == ']') || (currKey == '"' && c == '"')) {
            if (keyStack.isEmpty) { // 此时currKey一定是'{'
              currKey = '>' // 结束
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
    keyStack.isEmpty && currKey == '>'
  }

  /**
   * 仅展开一层JsonArray
   * @author yizhu.sun 2016年8月29日
   * @param src
   */
  def splitJsonArray(src: String): Seq[String] = {
    val result = scala.collection.mutable.ArrayBuffer[String]()
    var (jsonStart, jsonEnd) = (0, 0)
    val keyStack = scala.collection.mutable.Stack[Char]()
    var currKey: Char = 'X' // maybe '[', '{', '"'
    var ignore = false
    for (i <- 0 to src.length - 1) {
      if (ignore) {
        ignore = false
      } else {
        val c = src.charAt(i)
        if (currKey == 'X') {
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

  def main(args: Array[String]): Unit = {
    val s = """{"\"":[{}, {}, null], "{": "["}"""
    println(maybeJson(s))
  }

}