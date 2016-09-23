package cn.syz.game.bamboo;

public class Monitor {

  private boolean[] dead;
  private int nextPlayer;
  private int alive;

  Monitor(int playerCount) {
    alive = playerCount;
    dead = new boolean[playerCount];
    nextPlayer = org.apache.commons.lang3.RandomUtils.nextInt(0, dead.length);
//    nextPlayer = -1;
  }

  public int nextPlayer() {
    nextPlayer = (++nextPlayer) % dead.length;
    if (dead[nextPlayer]) {
      return nextPlayer();
    } else {
      return nextPlayer;
    }
  }

  public void die(int playerId) {
    if (!dead[playerId]) {
      alive--;
    }
    dead[playerId] = true;
  }

  public int getAliveCount() {
    return alive;
  }

}
