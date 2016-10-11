package cn.syz.sorting;

import static org.apache.commons.lang3.StringUtils.join;

import org.apache.commons.math3.util.ArithmeticUtils;

import cn.syz.util.MathUtils;

/**
 * 基数排序
 * 
 * @author yizhu.sun 2016年10月10日
 */
public class RadixSort {

  public static int[] sort(int[] src) {
    return sort(src, 16, 8, 1); // int多八位十六进制数
  }

  /**
   * @param radix 基数。推荐二的幂作为基数，比如16
   * @param digit src中可能的最大数字位数。比如，整数0x1FF在基数为16时位数为3
   */
  public static int[] sort(int[] src, int radix, int digit) {
    return sort(src, radix, digit, 1);
  }

  /**
   * @param currentDigit 当前递归计算到的位数。最低位为1
   */
  private static int[] sort(int[] src, int radix, int digit, int currentDigit) {
    assert radix >= 2; // 至少二进制
    assert currentDigit >= 1; // 最低位次序为1
    if (currentDigit > digit) {
      return src;
    }
    int[] agent = agent(src, radix, currentDigit);
    if (agent == null) {
      return src;
    }
    int[] sorted = CountingSort.sort(src, agent, radix - 1);
    return sort(sorted, radix, digit, currentDigit + 1); // tail recursion
  }

  /**
   * @return src中指定位数上的数字。如果指定的位数超出src中所有数字的位数，则返回null
   */
  private static int[] agent(int[] src, int radix, int currentDigit) {
    int[] agent = new int[src.length];
    boolean overflow = true;
    int shift = ArithmeticUtils.isPowerOfTwo(radix) ? MathUtils.log2(radix) : 0;
    for (int i = 0; i < src.length; i++) {
      int truncToDigit = src[i];
      if (shift > 0) {
        if (currentDigit > 1) {
          truncToDigit = truncToDigit >> (shift * (currentDigit - 1));
        }
        agent[i] = truncToDigit & ~(~0 << shift);
      } else {
        if (currentDigit > 1) {
          truncToDigit = truncToDigit / ArithmeticUtils.pow(radix, currentDigit - 1);
        }
        agent[i] = truncToDigit % radix;
      }
      if (truncToDigit > 0) {
        overflow = false;
      }
    }
    return overflow ? null : agent;
  }

  @org.junit.Test
  public void test() {
    int[] src = {9009, 222, 131, 213, 123, 432, 4321, 76, 0, 9, 50};
    System.out.println(join(src, ','));
    System.out.println(join(sort(src), ','));
  }

}
