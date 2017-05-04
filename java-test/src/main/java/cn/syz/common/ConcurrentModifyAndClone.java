package cn.syz.common;

public class ConcurrentModifyAndClone {

  final static int[] ints = new int[1000000];

  /**
   * 主线程拷贝数组，子线程从后向前修改数组
   * 
   * @param args
   * @throws Exception
   */
  public static void main(String[] args) throws Exception {
    Thread t = new Thread(() -> {
      // synchronized (ints) {
      System.out.println("modify start.");
      for (int i = ints.length - 1; i >= 0; i--) {
        ints[i] = 1;
      }
      System.out.println("modify finished.");
      // }
    });
    t.start();
    int[] copy;
    // synchronized (ints) {
    System.out.println("copy start.");
    copy = ints.clone();
    System.out.println("copy finished.");
    // }
    t.join(); // 等待子线程执行完成
    for (int i = 1; i < copy.length; i++) {
      if (copy[i] != copy[i - 1]) {
        System.out.println("这个位置copy的数据发生变化：" + i);
        return;
      }
    }
    System.out.println("copy的数组内容全部相同.");
  }

}
