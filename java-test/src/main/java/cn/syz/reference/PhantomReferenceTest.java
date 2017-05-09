package cn.syz.reference;

import java.lang.ref.PhantomReference;
import java.lang.ref.Reference;
import java.lang.ref.ReferenceQueue;
import java.lang.ref.WeakReference;

public class PhantomReferenceTest {

  public static void main(String[] args) throws Exception {
    ReferenceQueue<Test> queue = new ReferenceQueue<>();
    Thread moniterThread = new Thread(() -> {
      // 监视线程，随时检查引用队列，一旦发现引用就会打印出来
      for (;;) {
        Reference<? extends Test> ref = queue.poll();
        if (ref != null) {
          System.out.printf("%s加入引用队列%n", ref.getClass().getSimpleName());
        }
        try {
          Thread.sleep(0);
        } catch (InterruptedException e) {
          break;
        }
      }
    });
    moniterThread.start();
    Test test = new Test();
    WeakReference<Test> weakReference = new WeakReference<Test>(test, queue);
    PhantomReference<Test> phantomReference = new PhantomReference<Test>(test, queue);
    // 去除强引用
    test = null;
    // 第一次gc，执行finalize()方法，弱引用进入队列。对象虽然不可达，但是还在堆中，虚引用也没有进入队列
    System.out.println(">> 第一次gc <<");
    System.gc();
    Thread.sleep(100); // 这里等待一段时间，保证引用进入队列，和finalize()方法执行
    // 第二次gc，被垃圾回收，然后进入虚引用队列
    System.out.println("\n>> 第二次gc <<");
    System.gc();
    assert weakReference != null && phantomReference != null;
    moniterThread.interrupt();
  }

  public static class Test {

    @Override
    protected void finalize() throws Throwable {
      System.out.println("== finalize() ==");
    }

  }

}
