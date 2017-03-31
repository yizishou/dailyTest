package cn.syz.common;

public class DoubleCheckedLockTest {

  private static volatile DoubleCheckedLockTest singleton;

  public static DoubleCheckedLockTest getSingletonInstance() {
    if (singleton == null) {
      synchronized (DoubleCheckedLockTest.class) {
        if (singleton == null) {
          singleton = new DoubleCheckedLockTest();
        }
      }
    }
    return singleton;
  }

  public static void main(String[] args) {
    getSingletonInstance();
  }

}
