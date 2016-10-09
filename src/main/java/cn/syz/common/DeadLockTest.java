package cn.syz.common;

public class DeadLockTest {

	public static void main(String[] args) {
		final Object a = new Object();
		final Object b = new Object();
		new Thread(new Runnable() {
			@Override
			public void run() {
				synchronized (a) {
					System.out.println("thread 1 locked a");
					try {
						Thread.sleep(1000);
					} catch (InterruptedException e) {
					}
					synchronized (b) {
						System.out.println("thread 1 locked b");
					}
				}
			}
		}).start();
		new Thread(new Runnable() {
			@Override
			public void run() {
				synchronized (b) {
					System.out.println("thread 2 locked b");
					try {
						Thread.sleep(1000);
					} catch (InterruptedException e) {
					}
					synchronized (a) {
						System.out.println("thread 2 locked a");
					}
				}
			}
		}).start();
		
	}
	
}
