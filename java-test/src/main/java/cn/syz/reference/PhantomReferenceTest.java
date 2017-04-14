package cn.syz.reference;

import java.lang.ref.PhantomReference;
import java.lang.ref.Reference;
import java.lang.ref.ReferenceQueue;

public class PhantomReferenceTest {

  public static void main(String[] args) throws Exception {
    final ReferenceQueue<Test> queue = new ReferenceQueue<>();
    Test test = new Test();
    PhantomReference<Test> phantomReference = new PhantomReference<Test>(test, queue);
    System.out.println("enqueued: " + phantomReference.isEnqueued());
    test = null;
    System.out.println("gc");
    System.gc();
    Thread.sleep(500);
    System.out.println("enqueued: " + phantomReference.isEnqueued());
    Reference<? extends Test> ref = queue.poll();
    System.out.println("reference: " + ref);
  }

  public static class Test {
    
    // 实现了finalize的类的对象的虚引用不会进入ReferenceQueue.

//    @Override
//    protected void finalize() throws Throwable {
//      super.finalize();
//      System.out.println("Test.finalize()");
//    }
    
  }

}
