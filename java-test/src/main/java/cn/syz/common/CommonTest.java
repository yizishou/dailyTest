package cn.syz.common;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.collections4.IterableUtils;
import org.junit.Test;

public class CommonTest {

  @Test
  public void listGeneric() {
    List<Object> l = new ArrayList<>();
    l.add(12);
    l.add("123");
    l.add(l.get(0));
    List<?> l2 = l;
    System.out.println(l2.get(0));
    System.out.println(l2.get(1));
    // l2.add(l2.get(0));
  }

  @Test
  public void f1() {
    if (new Object() {
      public boolean foo() {
        System.out.print('a');
        return false;
      }
    }.foo()) {
      System.out.print('a');
    } else {
      System.out.print('b');
    }
  }

  @Test
  public void iterableZipTest() {
    ArrayList<String> a = new ArrayList<>();
    ArrayList<String> b = new ArrayList<>();
    a.add("a1");
    a.add("a2");
    b.add("b1");
    b.add("b2");
    b.add("b3");
    Iterable<String> c = IterableUtils.zippingIterable(a, b);
    for (String s : c) {
      System.out.println(s);
    }
  }

//  @Test
//  public void f() {
//    foo(new java.util.HashMap(), new java.util.TreeMap());
//  }
//  
//  private <T> void foo(T... ts) {
//    for (T t : ts) {
//      System.out.println(t.getClass());
//    }
//  }

}
