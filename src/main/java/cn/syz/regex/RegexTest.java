package cn.syz.regex;

public class RegexTest {

  public static void main(String[] args) {
    f1();
    f2();
    f3();
    f4();
  }

  private static void f1() {
    String reg = "(?:(?<=[a-zA-Z])(?=\\d)|(?<=\\d)(?=[a-zA-Z]))(\\w+?)";
    System.out.println("3232qw3232e123qwe3232".replaceAll(reg, "_$1"));
  }

  private static void f2() {
    String reg = "((?<=[a-zA-Z])(?=\\d)|(?<=\\d)(?=[a-zA-Z]))";
    System.out.println("3232qw3232e123qwe3232".replaceAll(reg, "_$1"));
  }

  private static void f3() {
    System.out.println("aa".replaceAll("(?<=(?<a>.{1}))(\\k<a>)", "-${a}"));
  }

  private static void f4() {
    System.out.println("lower_case_under_score".replaceAll("_+([a-z])", "$1"));
  }
  
}
