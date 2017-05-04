package cn.syz.reference;

import java.lang.ref.PhantomReference;
import java.lang.ref.Reference;
import java.lang.ref.ReferenceQueue;
import java.lang.ref.WeakReference;

public class PhantomReferenceTest {

  public static void main(String[] args) throws Exception {
    final ReferenceQueue<Test> queue = new ReferenceQueue<>();
    Test test = new Test();
    WeakReference<Test> weakReference = new WeakReference<Test>(test, queue);
    PhantomReference<Test> phantomReference = new PhantomReference<Test>(test, queue);
    System.out.println("weakReference enqueued: " + weakReference.isEnqueued());
    System.out.println("phantomReference enqueued: " + phantomReference.isEnqueued());
    test = null;
    System.out.println("gc");
    // 第一次gc，执行了finalize()方法，加入了弱引用队列，但是没有被垃圾回收，也没有进入虚引用队列
    System.gc();
    Thread.sleep(500);
    System.out.println("weakReference enqueued: " + weakReference.isEnqueued());
    System.out.println("phantomReference enqueued: " + phantomReference.isEnqueued());
    Reference<? extends Test> ref = queue.poll();
    System.out.println("reference: " + ref);
    ref = queue.poll();
    System.out.println("reference: " + ref);
    System.out.println("gc");
    // 第二次gc，被垃圾回收，然后进入虚引用队列
    System.gc();
    Thread.sleep(500);
    System.out.println("weakReference enqueued: " + weakReference.isEnqueued());
    System.out.println("phantomReference enqueued: " + phantomReference.isEnqueued());
    ref = queue.poll();
    System.out.println("reference: " + ref);
    ref = queue.poll();
    System.out.println("reference: " + ref);
  }

  public static class Test {
    
    @Override
    protected void finalize() throws Throwable {
      super.finalize();
      System.out.println("Test.finalize()");
    }
    
  }

}
