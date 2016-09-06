package cn.syz.common;

public class InstanceofTest {

  public static void main(String[] args) {
    String s = null;
    System.out.println(s instanceof String);
    System.out.println(s instanceof Object);
    s = "123";
    System.out.println(s instanceof String);
    System.out.println(s instanceof java.io.Serializable);
    System.out.println(s instanceof Object);
  }

}
