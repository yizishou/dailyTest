package cn.syz.common;

public class JavapTest {
  
  private int i;
  private int j = 3;

  public JavapTest() { i = 2; }

  private int iadd(int n) { i += n; return i; }

  private int jreduce() { return --j; }

  public static void main(String[] args) {
    JavapTest test = new JavapTest();
    System.out.println(test.iadd(1 << 10));
    System.out.println(test.jreduce());
    int a = 1;
    int i = a * 12 + a + 2 * a;
    System.out.println(i);
  }

}
