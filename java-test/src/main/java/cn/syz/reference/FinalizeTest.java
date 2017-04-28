package cn.syz.reference;

public class FinalizeTest {
    
    public static void main(String[] args) throws Exception {
        for (int i = 0; i < 10; i++) {
            f();
        }
        Thread.sleep(1000);
        System.gc();
        Thread.sleep(1000);
    }

    private static void f() {
        Foo foo = new Foo();
        System.out.println(foo);
    }

    public static class Foo {
        @Override
        protected void finalize() throws Throwable {
            super.finalize();
            System.out.println("finalized: " + this);
        }
    }

}
