package cn.syz.imaging;

import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.image.BufferedImage;
import java.awt.image.DataBuffer;
import java.io.File;

import javax.imageio.ImageIO;

public class ScreenshotAndCompare {

  static final int W = 900, H = 500, X_STEP = 30, Y_STEP = 3;
  static final int DELAY_MS = 50;

  public static void main(String[] args) throws Exception {
    final Robot robot = new Robot();
    final Rectangle screenRect = new Rectangle(0, 0, W, H);
    final File dir = new File("D:\\tmp\\img");
    DataBuffer prevImageContent = null;
    while (true) {
      BufferedImage bufferedImage = robot.createScreenCapture(screenRect);
      DataBuffer imageContent = bufferedImage.getData().getDataBuffer();
      if (prevImageContent != null && !compareImageContent(prevImageContent, imageContent)) {
        ImageIO.write(bufferedImage, "jpg", new File(dir, System.currentTimeMillis() + ".jpg"));
      }
      robot.delay(DELAY_MS);
      prevImageContent = imageContent;
    }
  }

  private static boolean compareImageContent(DataBuffer a, DataBuffer b) {
    for (int y = 0; y < H; y += Y_STEP) {
      for (int x = 0; x < W; x += X_STEP) {
        if (a.getElem(y * W + x) != b.getElem(y * W + x)) {
          return false;
        }
      }
    }
    return true;
  }

}
