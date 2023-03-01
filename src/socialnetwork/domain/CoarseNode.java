package socialnetwork.domain;

public class CoarseNode<T> implements Node<T> {

  private T item;
  private int key;
  private Node<T> next;

  public CoarseNode(T item) {
    this.item = item;
    this.key = item.hashCode();
    this.next = null;
  }

  public CoarseNode(T item, Node<T> next) {
    this.item = item;
    this.key = item.hashCode();
    this.next = next;
  }

  public CoarseNode(T item, int key, Node<T> next){
    this.item = item;
    this.key = key;
    this.next = next;
  }

  @Override
  public T item() {
    return item;
  }

  @Override
  public int key() {
    return key;
  }

  @Override
  public Node<T> next() {
    return next;
  }

  @Override
  public void setItem(T item) {
    this.item = item;
  }

  @Override
  public void setKey(int key) {
    this.key = key;
  }

  @Override
  public void setNext(Node<T> next) {
    this.next = next;
  }
}
