package cn.syz.util;

public class MathUtils {

  public static int log2(int i) {
    if (i <= 0) {
      return -1;
    }
    return log2(i >> 1) + 1;
  }

}
