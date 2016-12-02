package cn.syz.common;

public class SynchronizedTest {
  
  public synchronized void instanceFunc(String from) {
    try { Thread.sleep(1000); } catch (InterruptedException e) { e.printStackTrace(); }
    System.out.println(from + " called instanceFunc");
  }

  public synchronized static void classFunc() { System.out.println("classFunc"); }
  
  public static void main(String[] args) {
    new Thread(new Runnable() {
      public void run() {
        synchronized(SynchronizedTest.class) {
          try { Thread.sleep(5000); System.out.println("a wake up"); } catch (InterruptedException e) { e.printStackTrace(); }
        }
      }
    }).start();
    new Thread(new Runnable() {
      public void run() {
        SynchronizedTest test = new SynchronizedTest();
        synchronized(test) {
          try { Thread.sleep(5000); System.out.println("b wake up"); } catch (InterruptedException e) { e.printStackTrace(); }
        }
      }
    }).start();
    new Thread(new Runnable() {
      public void run() {
        try { Thread.sleep(1000); } catch (InterruptedException e) { e.printStackTrace(); }
        SynchronizedTest.classFunc();
      }
    }).start();
    new Thread(new Runnable() {
      public void run() {
        try { Thread.sleep(1000); } catch (InterruptedException e) { e.printStackTrace(); }
        new SynchronizedTest().instanceFunc("d");
      }
    }).start();
    new Thread(new Runnable() {
      public void run() {
        try { Thread.sleep(1000); } catch (InterruptedException e) { e.printStackTrace(); }
        new SynchronizedTest().instanceFunc("e");
      }
    }).start();
  }

}
