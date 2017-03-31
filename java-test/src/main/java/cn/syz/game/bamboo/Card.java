package cn.syz.game.bamboo;

public enum Card {

  /** A **/
  A("A"),
  /** 2 **/
  _2("2"),
  /** 3 **/
  _3("3"),
  /** 4 **/
  _4("4"),
  /** 5 **/
  _5("5"),
  /** 6 **/
  _6("6"),
  /** 7 **/
  _7("7"),
  /** 8 **/
  _8("8"),
  /** 9 **/
  _9("9"),
  /** 10 **/
  _10("10"),
  /** J **/
  J("J"),
  /** Q **/
  Q("Q"),
  /** K **/
  K("K");

  Card(String v) {
    this.v = v;
  }

  private String v;

  @Override
  public String toString() {
    return v;
  }

}
