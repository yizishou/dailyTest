package cn.syz.common;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

public class AnonymousInnerClassTest implements Serializable {

  private static final long serialVersionUID = -5032281474441314286L;
  String s = "=========================";

  public static void main(String[] args) throws IOException, ClassNotFoundException {
    final Map<String, String> dict1 = new HashMap<String, String>();
    Map<String, Object> dict2 = new HashMap<String, Object>() {
      private static final long serialVersionUID = 2376516129342076861L;

      {
        put("apple", "苹果");
        put("banana", "香蕉");
        put("dict1", dict1);
      }

      private void writeObject(ObjectOutputStream out) throws IOException {
        System.out.println("HashMap serialized");
      }

      private void readObject(ObjectInputStream in) throws IOException,
          ClassNotFoundException {
        System.out.println("HashMap deserialized");
      }

    };
    dict1.put("apple", "苹果");
    dict1.put("banana", "香蕉");
    System.out.println(dict1.getClass());
    System.out.println(dict2.getClass());
    ByteArrayOutputStream bout = new ByteArrayOutputStream();
    ObjectOutputStream out = new ObjectOutputStream(bout);
    out.writeObject(dict2);
    bout.writeTo(System.err);
    ByteArrayInputStream bin = new ByteArrayInputStream(bout.toByteArray());
    ObjectInputStream in = new ObjectInputStream(bin);
    Object o = in.readObject();
    System.out.println(o.getClass());
  }

  private void writeObject(ObjectOutputStream out) throws IOException {
    throw new RuntimeException("AnonymousInnerClassTest serialized");
  }

  private void readObject(ObjectInputStream in) throws IOException,
      ClassNotFoundException {
    throw new RuntimeException("AnonymousInnerClassTest deserialized");
  }

}
