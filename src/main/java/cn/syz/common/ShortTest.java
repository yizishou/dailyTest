package cn.syz.common;

public class ShortTest {

  private int i = 1;
  private short s = 1;

  public static void main(String[] args) {
    short s0 = 1;
    int i0 = 1;
    if (s0 == i0) {
      s0 = (short) i0;
    }
  }

}
