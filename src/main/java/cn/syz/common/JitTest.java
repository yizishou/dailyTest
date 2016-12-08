package cn.syz.common;

public class JitTest {
  
  private static int add(int a, int b) {
    return a + b;
  }
  
  public static void main(String[] args) {
    int sum = 0;
    for (int i = 0; i < 1000000; i++) {
      int k = add(i, 1);
      sum += k;
    }
    for (int i = 0; i < 1000000; i++) {
      int k = i + 1;
      sum += k;
    }
    System.out.println(sum);
  }

}
