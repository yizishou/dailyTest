package cn.syz.common;

public class ArrayParameterMethodTest {
  
  public static void main(String[] args) {
    f("a");
    f("a", "b");
  }

  private static void f(String a, String ... b) {
    System.out.println(b);
  }
  
}
