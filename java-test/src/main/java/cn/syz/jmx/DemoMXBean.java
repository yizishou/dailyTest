package cn.syz.jmx;

import java.lang.management.ManagementFactory;

import javax.management.MBeanServer;
import javax.management.ObjectName;

// needs jmxtools.jar
import com.sun.jdmk.comm.HtmlAdaptorServer;

public interface DemoMXBean {

  void setName(String name);

  String getName();

  default void gc() {
    System.gc();
  }

  default void shutdown() {
    System.exit(0);
  }

  static class Demo implements DemoMXBean {

    static {
      try {
        MBeanServer server = ManagementFactory.getPlatformMBeanServer();
        server.registerMBean(new Demo(), new ObjectName("cn.syz.jmx:type=Demo"));
        System.out.println("DemoMXBean registered");
        // HtmlAdaptorServer needs jmxtools.jar
        HtmlAdaptorServer adapter = new HtmlAdaptorServer();
        adapter.setPort(9797);
        server.registerMBean(adapter, new ObjectName("cn.syz.jmx:name=htmladapter,port=9797"));
        adapter.start();
        System.out.println("HtmlAdaptorServer started");
      } catch (Exception e) {
        e.printStackTrace();
      }
    }

    private String name;

    @Override
    public void setName(String name) {
      this.name = name;
    }

    @Override
    public String getName() {
      return name;
    }

    public static void main(String[] args) throws Exception {
      System.out.println("waiting...");
      Thread.sleep(1000000);
    }

  }

}


