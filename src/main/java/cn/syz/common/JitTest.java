package cn.syz.common;

public class JitTest {
  
  private static int staticSum;

  private static int add(int a, int b) {
    int aa = a;
    int bb = b;
    return aa + bb;
  }
  
  public static void main(String[] args) {
    int sum = 0;
    for (int i = 0; i < 100000; i++) {
      int k = add(i, 1);
      sum += k;
    }
    for (int i = 0; i < 100000; i++) {
      int k = i + 1;
      sum += k;
    }
    for (int i = 0; i < 100000; i++) {
      int k = i + 1;
      JitTest.staticSum += k;
    }
    System.out.println(sum);
    System.out.println(JitTest.staticSum);
  }

}
