package cn.syz.game.bamboo;

public class SpliceBambooPolesThread extends Thread {

  private int playTimes;
  private int playerCount;

  public SpliceBambooPolesThread(int playTimes, int playerCount) {
    this.playTimes = playTimes;
    this.playerCount = playerCount;
  }

  @Override
  public void run() {
    int[] scores = new int[playerCount];
    for (int i = 0; i < playTimes; i++) {
      int winner = new SpliceBambooPoles().play(playerCount);
      scores[winner]++;
    }
    addScores(scores);
  }

  public static void main(String[] args) {
    for (int i = 0; i < 3; i++) {
      new SpliceBambooPolesThread(1000000, 2).start();
    }
  }

  private static int[] scores = new int[100];
  private static int total = 0;
  private static long startTime = System.currentTimeMillis();

  private static synchronized void addScores(int[] scores) {
    for (int i = 0; i < scores.length; i++) {
      SpliceBambooPolesThread.scores[i] += scores[i];
      total += scores[i];
    }
    System.out.println(
        "total: " + total + ", cost: " + (System.currentTimeMillis() - startTime) / 1000.0);
    for (int i = 0; i < scores.length; i++) {
      System.out.println("player" + i + " score: " + SpliceBambooPolesThread.scores[i]);
    }
  }

}
