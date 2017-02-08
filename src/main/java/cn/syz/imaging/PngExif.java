package cn.syz.imaging;

import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.imaging.ImageFormats;
import org.apache.commons.imaging.Imaging;
import org.apache.commons.imaging.formats.png.PngConstants;
import org.apache.commons.imaging.formats.png.PngText;
import org.apache.commons.io.FileUtils;

public class PngExif {

  public static void main(String[] args) throws Exception {
    // test();
    addCreationTimeToPngs();
  }

  private static void addCreationTimeToPngs() throws Exception {
    File srcDir = new File("C:\\Users\\yizhu.sun\\Pictures\\mi\\pic\\截屏");
    File tarDir = new File("C:\\Users\\yizhu.sun\\Pictures\\mi\\pic\\截屏'");
    FileUtils.deleteQuietly(tarDir);
    FileUtils.forceMkdir(tarDir);
    for (File src : srcDir.listFiles()) {
      String fileName = src.getName();
      File tar = new File(tarDir, fileName);
      Map<String, Object> params = new HashMap<>();
      List<PngText> texts = new ArrayList<>();
      texts.add(new PngText.Text("Creation Time", getTimeFromFileName(fileName)));
      params.put(PngConstants.PARAM_KEY_PNG_TEXT_CHUNKS, texts);
      Imaging.writeImage(Imaging.getBufferedImage(src), tar, ImageFormats.PNG, params);
    }
  }

  private static String getTimeFromFileName(String fileName) {
    Pattern p = Pattern.compile("(?<y>\\d{4})-(?<mon>\\d{2})-(?<d>\\d{2})-(?<h>\\d{2})-(?<min>\\d{2})-(?<s>\\d{2})");
    Matcher m = p.matcher(fileName);
    if (m.find()) {
      StringBuilder sb = new StringBuilder();
      sb.append(m.group("y")).append(':').append(m.group("mon")).append(':').append(m.group("d")).append(' ');
      sb.append(m.group("h")).append(':').append(m.group("min")).append(':').append(m.group("s"));
      return sb.toString();
    } else {
      throw new IllegalArgumentException("illegal file name: " + fileName);
    }
  }

  private static void test() throws Exception {
    File png = new File("D://Screenshot_2014-08-06-21-22-28.png");
    File tar = new File("D://Screenshot_2014-08-06-21-22-28''.png");
    // PngImageInfo info = (PngImageInfo) Imaging.getImageInfo(png);
    Map<String, Object> params = new HashMap<>();
    List<PngText> texts = new ArrayList<>();
    /**
     * Title 图像名称或者标题
     * Author 图像作者名
     * Description 图像说明
     * Copyright 版权声明
     * CreationTime 原图创作时间
     * Software 创作图像使用的软件
     * Disclaimer 弃权
     * Warning 图像内容警告
     * Source 创作图像使用的设备
     * Comment 各种注释
     */
    // texts.add(new PngText.Text("Title", "hh"));
    // texts.add(new PngText.Text("Author", "yizishou"));
    // texts.add(new PngText.Text("Description", "desc"));
    // texts.add(new PngText.Text("Copyright", "yizishou@2017"));
    texts.add(new PngText.Text("Creation Time", "2014:08:06 21:22:28"));
    // texts.add(new PngText.Text("Software", "screenshot"));
    // texts.add(new PngText.Text("Disclaimer", "what's this"));
    // texts.add(new PngText.Text("Warning", "!!!"));
    // texts.add(new PngText.Text("Source", "mi3"));
    // texts.add(new PngText.Text("Comment", "something"));
    params.put(PngConstants.PARAM_KEY_PNG_TEXT_CHUNKS, texts);
    BufferedImage src = Imaging.getBufferedImage(png);
    Imaging.writeImage(src, tar, ImageFormats.PNG, params);
  }

}
