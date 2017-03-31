package cn.syz.regex;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.junit.Test;

public class RegexTest {

  // @Test
  // public void f1() {
  // String reg = "(?:(?<=[a-zA-Z])(?=\\d)|(?<=\\d)(?=[a-zA-Z]))(\\w+?)";
  // System.out.println("3232qw3232e123qwe3232".replaceAll(reg, "_$1"));
  // }
  //
  // @Test
  // public void f2() {
  // String reg = "((?<=[a-zA-Z])(?=\\d)|(?<=\\d)(?=[a-zA-Z]))";
  // System.out.println("3232qw3232e123qwe3232".replaceAll(reg, "_$1"));
  // }

  // @Test
  // public void f3() {
  // System.out.println("aa".replaceAll("(?<=(?<a>.{1}))(\\k<a>)", "-${a}"));
  // }

  // @Test
  // public void f4() {
  // System.out.println("lower_case_under_score".replaceAll("_+([a-z])", "$1"));
  // }

  /*
   * h3. collect_set usage: *collect_set(col_1)
   */
  // @Test
  // public void f5() {
  // System.out.println("usage: collect_set(col_1)".replaceAll("(?<usg>usage: )(?<nm>\\w+)", "h3.
  // ${nm}\n*${usg}*${nm}"));
  // }

  @Test
  public void f6() {
    String src = "{ \"num-executors\": \"3\", \"driver-memory\": \"2g\", \"executor-memory\": \"4g\", \"executor-cores\": \"1\" }";
    String regex = "(?:(?:\"num-executors\":\\s*\"(?<numexecutors>\\d+)\"|\"executor-cores\":\\s*\"(?<executorcores>\\d+)\"|\"driver-memory\":\\s*\"(?<drivermemory>\\w+)\"|\"executor-memory\":\\s*\"(?<executormemory>\\w+)\")[,\\s]+)+";
    Matcher matcher = Pattern.compile(regex).matcher(src);
    if (matcher.find()) {
      System.out.println(matcher.group("numexecutors"));
      System.out.println(matcher.group("executorcores"));
      System.out.println(matcher.group("drivermemory"));
      System.out.println(matcher.group("executormemory"));
    }
  }

}
