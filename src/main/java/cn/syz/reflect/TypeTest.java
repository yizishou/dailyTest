package cn.syz.reflect;

import java.lang.reflect.*;

import org.apache.commons.lang3.StringUtils;

public class TypeTest {

  public static void main(String[] args) throws Exception {
    Class<?> clazz = new TypeTestInner<>().getClass();
    Type t = clazz.getDeclaredField("a").getGenericType();
    typeAnalysis(t, 0);
    TypeVariable<?>[] tp = TypeTestInner.class.getTypeParameters();
    for (TypeVariable<?> typeVariable : tp) {
      typeAnalysis(typeVariable, 0);
    }
  }

  public static void typeAnalysis(Type t, int depth) {
    System.out.println(StringUtils.repeat(' ', depth)
        + StringUtils.rightPad(t.getClass().getSimpleName(), 25 - depth) + t);
    if (t instanceof GenericArrayType) {
      typeAnalysis(((GenericArrayType) t).getGenericComponentType(), depth + 1);
    } else if (t instanceof ParameterizedType) {
      typeAnalysis(((ParameterizedType) t).getRawType(), depth + 1);
      for (Type tmp : ((ParameterizedType) t).getActualTypeArguments()) {
        typeAnalysis(tmp, depth + 1);
      }
    } else if (t instanceof WildcardType) {
      for (Type tmp : ((WildcardType) t).getLowerBounds()) {
        typeAnalysis(tmp, depth + 1);
      }
      for (Type tmp : ((WildcardType) t).getUpperBounds()) {
        typeAnalysis(tmp, depth + 1);
      }
    } else if (t instanceof TypeVariable) {
      for (Type tmp : ((TypeVariable<?>) t).getBounds()) {
        typeAnalysis(tmp, depth + 1);
      }
    }
  }

}


class TypeTestInner<T> {

  java.util.List<? extends T[]>[] a;

}
