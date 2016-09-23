package cn.syz.util;

public class SplicableQueue<E> {

  private int size;
  private Node<E> head;
  private Node<E> tail;

  public int size() {
    return size;
  }

  public boolean isEmpty() {
    return head == null;
  }

  public void add(E o) {
    if (isEmpty()) {
      tail = head = new Node<E>(null, null, o);
    } else {
      tail.next = new Node<E>(tail, null, o);
      tail = tail.next;
    }
    size++;
  }

  public E poll() {
    if (isEmpty()) {
      throw new NullPointerException();
    } else {
      E o = head.item;
      if (head == tail) {
        head = tail = null;
      } else {
        head = head.next;
        head.prev = null;
      }
      size--;
      return o;
    }
  }

  public Node<E> findAndCutBefore(E o) {
    if (!isEmpty()) {
      int remain = 0;
      Node<E> node = head;
      while (node != null) {
        if (node.item.equals(o)) {
          if (node.prev == null) {
            head = tail = null;
          } else {
            tail = node.prev;
            tail.next = null;
            node.prev = null;
          }
          size = remain;
          return node;
        }
        remain++;
        node = node.next;
      }
    }
    return null;
  }

  public void append(Node<E> node) {
    if (isEmpty()) {
      head = node;
      head.prev = null;
    } else {
      tail.next = node;
      node.prev = tail;
    }
    tail = node;
    size++;
    while (tail.next != null) {
      tail = tail.next;
      size++;
    }
  }

  @Override
  public String toString() {
    StringBuffer sb = new StringBuffer("[");
    Node<E> node = head;
    if (node != null) {
      sb.append(node.item);
      node = node.next;
    }
    while (node != null) {
      sb.append(',').append(node.item);
      node = node.next;
    }
    sb.append(']');
    return sb.toString();
  }

  public static class Node<E> {
    Node<E> prev;
    Node<E> next;
    E item;

    public Node(Node<E> prev, Node<E> next, E item) {
      this.prev = prev;
      this.next = next;
      this.item = item;
    }
  }

}
