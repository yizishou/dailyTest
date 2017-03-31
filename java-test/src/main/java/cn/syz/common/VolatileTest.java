package cn.syz.common;

import java.util.concurrent.atomic.AtomicInteger;

public class VolatileTest {

  private static volatile int volatileI = 0;
  private static AtomicInteger atomicI = new AtomicInteger(0);

  private static void add() {
    volatileI++;
    atomicI.incrementAndGet();
  }

  static class Runnable1 implements Runnable {
    @Override
    public void run() {
      for (int k = 0; k < 500; k++) {
        add();
      }
    }
  }

  public static void main(String[] args) throws InterruptedException {
    Thread[] threads = new Thread[10];
    for (int i = 0; i < threads.length; i++) {
      threads[i] = new Thread(new Runnable1());
      threads[i].start();
    }
    for (int i = 0; i < threads.length; i++) {
      threads[i].join();
    }
    System.out.println(volatileI);
    System.out.println(atomicI.get());
  }

}
