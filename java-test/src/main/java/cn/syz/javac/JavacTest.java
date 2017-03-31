package cn.syz.javac;

import java.io.File;
import java.lang.reflect.Method;
import java.net.URI;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Arrays;
import java.util.Random;

import javax.tools.JavaCompiler;
import javax.tools.JavaCompiler.CompilationTask;
import javax.tools.JavaFileObject;
import javax.tools.SimpleJavaFileObject;
import javax.tools.ToolProvider;

public class JavacTest {

  public static void main(String[] args) throws Exception {
    File tmpDir = new File(System.getProperty("java.io.tmpdir"),
        "msgpackCodeGenerationTempDir" + new Random().nextLong());
    File targetDir = new File(tmpDir, "target/classes");
    targetDir.mkdirs();
    String className = "syz.test.z.javac.FileGeneratorTest";
    StringBuffer sb = new StringBuffer();
    sb.append("package syz.test.z.javac;").append('\n');
    sb.append("import org.apache.commons.lang.StringUtils;").append('\n');
    sb.append("public class FileGeneratorTest {").append('\n');
    sb.append("  public static void f() {").append('\n');
    sb.append("    System.out.println(\"FileGeneratorTest\");").append('\n');
    sb.append("    System.out.println(FileGeneratorTest.class.getClassLoader().getResource(\"\"));").append('\n');
    sb.append("    System.out.println(\"java.class.path = \" + System.getProperty(\"java.class.path\"));").append('\n');
    sb.append("    System.out.println(\"java.library.path = \" + System.getProperty(\"java.library.path\"));").append('\n');
    sb.append("  }").append('\n');
    sb.append("}").append('\n');
    JavaCompiler javaCompiler = ToolProvider.getSystemJavaCompiler();
    JavaFileObject javaFileContent = new StringJavaFileObject(className, sb.toString());
    CompilationTask task =
        javaCompiler.getTask(null, null, null, Arrays.asList("-d", targetDir.getCanonicalPath()
         // , "-cp", System.getProperty("java.class.path")
          ), null, Arrays.asList(javaFileContent));
    boolean success = task.call();
    if (success) {
      System.out.println("编译成功");
    } else {
      System.out.println("编译失败");
      return;
    }
    System.out.println("JavacTest");
    System.out.println(JavacTest.class.getClassLoader().getResource(""));
    System.out.println("java.class.path = " + System.getProperty("java.class.path"));
    System.out.println("java.library.path = " + System.getProperty("java.library.path"));
    URL[] urls = new URL[] {targetDir.toURI().toURL()};
    URLClassLoader classLoader = new URLClassLoader(urls);
    Class<?> clazz = classLoader.loadClass(className);
    classLoader.close();
    Method method = clazz.getDeclaredMethod("f");
    method.invoke(null);
  }

  static class StringJavaFileObject extends SimpleJavaFileObject {

    private final String sourceCode;

    public StringJavaFileObject(String className, String sourceCode) {
      super(URI.create("string:///" + className.replace('.', '/') + Kind.SOURCE.extension),
          Kind.SOURCE);
      this.sourceCode = sourceCode;
    }

    @Override
    public CharSequence getCharContent(boolean ignoreEncodingErrors) {
      return sourceCode;
    }

  }

}
