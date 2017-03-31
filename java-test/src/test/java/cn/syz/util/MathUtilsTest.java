package cn.syz.util;

import static cn.syz.util.MathUtils.*;

public class MathUtilsTest {

  @org.junit.Test
  public void test() {
    assert log2(1) == 0;
    assert log2(2) == 1;
    assert log2(32) == 5;
  }

}
