package cn.syz.sorting;

import static org.apache.commons.lang3.StringUtils.join;

/**
 * 计数排序
 * 
 * @author yizhu.sun 2016年10月10日
 */
public class CountingSort {

  /**
   * 根据agent中数字的大小来排序src
   */
  public static int[] sort(int[] src, int[] agent, int max) {
    assert src.length > 0;
    assert src.length == agent.length;
    assert max >= 0;
    int[] result = new int[src.length];
    int[] count = new int[max + 1];
    for (int i = 0; i < agent.length; i++) {
      count[agent[i]]++;
    }
    for (int i = 1; i < count.length; i++) {
      count[i] += count[i - 1];
    }
    for (int i = src.length - 1; i >= 0; i--) {
      result[--count[agent[i]]] = src[i];
    }
    return result;
  }

  public static int[] sort(int[] src, int max) {
    return sort(src, src, max);
  }

  @org.junit.Test
  public void test() {
    int[] src = {11, 13, 17, 11, 18, 13, 17, 11, 15, 15, 14};
    int[] agent = {1, 3, 7, 1, 8, 3, 7, 1, 5, 5, 4};
    int max = 9;
    System.out.println(join(agent, ','));
    System.out.println(join(sort(agent, max), ','));
    System.out.println(join(src, ','));
    System.out.println(join(sort(src, agent, max), ','));
  }

}
