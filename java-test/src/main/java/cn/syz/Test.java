package cn.syz;

public class Test {}


class AB {}


final class AB_System {
  public static final AB a = nullAB();

  private static AB nullAB() {
    if (System.currentTimeMillis() > 0) {
      return null;
    }
    throw new NullPointerException();
  }

  static {
    System.out.println("ab_system static");
  }

  private AB_System() {}

  public static void print() {
    System.out.println("abaddd_dadfa");
  }
}


class Test_System {
  public static void main(String[] args) {
    System.out.println(AB_System.a == null);
  }
}
