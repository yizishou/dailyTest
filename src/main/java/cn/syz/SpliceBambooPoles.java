package cn.syz;

import java.util.ArrayDeque;
import java.util.ArrayList;

import org.apache.commons.lang3.RandomUtils;

public class SpliceBambooPoles {

  /** 牌库 **/
  private final Card[] library = new Card[52];
  {
    // 将 A - K 复制四份，作为初始牌堆
    System.arraycopy(Card.values(), 0, library, 0, 13);
    System.arraycopy(library, 0, library, 13, 13);
    System.arraycopy(library, 0, library, 26, 26);
  }

  public String play(String... players) {
    if (library.length % players.length != 0) {
      throw new IllegalArgumentException("分牌不均无法开始");
    }
    assert players.length >= 2;
    final Card[] library = initLibrary(); // 初始化52张牌
    shuffle(library); // 洗牌
    ArrayList<ArrayDeque<Card>> hands = deal(library, players.length); // 发牌
    ArrayDeque<Card> pile = new ArrayDeque<>(Card.values().length + 1); // 桌面牌堆
    // 开始游戏
    Monitor monitor = new Monitor(players.length);
    int player = monitor.nextPlayer();
    return null;
  }

  private static Card[] initLibrary() {
    Card[] library = new Card[52];
    System.arraycopy(Card.values(), 0, library, 0, 13);
    System.arraycopy(library, 0, library, 13, 13);
    System.arraycopy(library, 0, library, 26, 26);
    return library;
  }

  private static <T> void shuffle(T[] library) {
    for (int i = library.length; i > 1; i--) {
      swap(library, i - 1, RandomUtils.nextInt(0, i));
    }
  }

  private static <T> ArrayList<ArrayDeque<T>> deal(T[] library, int players) {
    ArrayList<ArrayDeque<T>> hands = new ArrayList<>(players);
    for (int i = 0; i < players; i++) {
      hands.add(new ArrayDeque<T>(library.length));
    }
    for (int i = 0; i < library.length; i++) {
      hands.get(i % players).add(library[i]);
    }
    return hands;
  }

  private static <T> void swap(T[] arr, int pos1, int pos2) {
    if (pos1 != pos2) {
      T tmp = arr[pos1];
      arr[pos1] = arr[pos2];
      arr[pos2] = tmp;
    }
  }

  public static void main(String[] args) {
    System.out.println("Winner: " + new SpliceBambooPoles().play("Player1", "Player2"));
  }

  enum Card {
    A, _2, _3, _4, _5, _6, _7, _8, _9, _10, J, Q, K
  }

  class Monitor {
    private boolean[] dead;
    private int nextPlayer = -1;

    Monitor(int playerCount) {
      dead = new boolean[playerCount];
    }

    public int nextPlayer() {
      nextPlayer = (++nextPlayer) % dead.length;
      if (dead[nextPlayer]) {
        return nextPlayer();
      } else {
        return nextPlayer;
      }
    }

    public boolean isWinner(int playerId) {
      for (int i = 0; i < dead.length; i++) {
        if (i != playerId && !dead[i]) {
          return false;
        }
      }
      return true;
    }
  }

}
