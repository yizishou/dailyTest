package cn.syz.game.bamboo;

import java.util.ArrayList;

import org.apache.commons.lang3.RandomUtils;

import cn.syz.util.SplicableQueue;
import cn.syz.util.SplicableQueue.Node;

public class SpliceBambooPoles {

  /** 牌库 **/
  private final Card[] library = new Card[52];
  {
    // 将 A - K 复制四份，作为初始牌堆
    System.arraycopy(Card.values(), 0, library, 0, 13);
    System.arraycopy(library, 0, library, 13, 13);
    System.arraycopy(library, 0, library, 26, 26);
  }

  public int play(int playerCount) {
    if (library.length % playerCount != 0) {
      throw new IllegalArgumentException("分牌不均无法开始");
    }
    assert playerCount >= 2;
    final Card[] library = initLibrary(); // 初始化52张牌
    shuffle(library); // 洗牌
    ArrayList<SplicableQueue<Card>> hands = deal(library, playerCount); // 发牌
    SplicableQueue<Card> pile = new SplicableQueue<>(); // 桌面牌堆
    // 开始游戏
    Monitor monitor = new Monitor(playerCount);
    int player = monitor.nextPlayer();
    while (monitor.getAliveCount() > 1) {
      SplicableQueue<Card> hand = hands.get(player);
      System.out.print("player" + player + ": ");
      if (discard(hand, hand.poll(), pile)) { // 出牌
        // 如果赢下此轮（即桌面牌堆中发现相同点数手牌），则本人继续出牌
      } else {
        // 如果输掉此轮，并手牌数为零，则出局
        if (hand.isEmpty()) {
          monitor.die(player);
        }
        player = monitor.nextPlayer();
      }
    }
    return player;
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

  private static <T> ArrayList<SplicableQueue<T>> deal(T[] library, int players) {
    ArrayList<SplicableQueue<T>> hands = new ArrayList<>(players);
    for (int i = 0; i < players; i++) {
      hands.add(new SplicableQueue<T>());
    }
    for (int i = 0; i < library.length; i++) {
      hands.get(i % players).add(library[i]);
    }
    return hands;
  }

  private boolean discard(SplicableQueue<Card> hand, Card discard, SplicableQueue<Card> pile) {
    System.out.println(pile.toString() + " <- " + discard + " ramain: " + hand.size());
    Node<Card> node = pile.findAndCutBefore(discard);
    if (node == null) {
      pile.add(discard);
    } else {
      hand.append(node);
      hand.add(discard);
    }
    return node != null;
  }

  private static <T> void swap(T[] arr, int pos1, int pos2) {
    if (pos1 != pos2) {
      T tmp = arr[pos1];
      arr[pos1] = arr[pos2];
      arr[pos2] = tmp;
    }
  }

  public static void main(String[] args) {
    System.out.println("Winner: player" + new SpliceBambooPoles().play(2));
  }

}
