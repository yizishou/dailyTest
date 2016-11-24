package cn.syz.common;

import org.junit.Test;

public class OverloadTest {

    @Test
    public void test() {
        Integer i = 1;
        Object o = i;
        foo(i);
        foo(o);
    }

    private void foo(Integer i) {
        System.out.println("Integer " + i);
    }

    private void foo(Object o) {
        System.out.println("Object " + o);
    }

}
