package cn.syz.java8;

import java.util.Arrays;
import java.util.List;
import java.util.Spliterator;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Predicate;

public class Java8Test {

  public static void main(String[] args) throws Exception {
    // Runnable
    Runnable runnable = () -> System.out.println(123);
    new Thread(Java8Test::print).start();
    // Predicate
    Predicate<String> isNull = s -> s == null;
    Predicate<String> isEmpty = s -> s.isEmpty();
    Predicate<String> isNullOrEmpty = isNull.or(isEmpty);
    System.out.println(isNullOrEmpty.test(""));
    // Closure
    List<Integer> list = Arrays.asList(1, 2, 3, 2);
    AtomicInteger max = new AtomicInteger();
    list.forEach(n -> {
      if (n > max.get()) {
        max.set(n);
        System.out.println(n);
      }
    });
    Spliterator<Integer> sp = list.spliterator();
  }

  static void print() {
    System.out.println(123);
  }

}
