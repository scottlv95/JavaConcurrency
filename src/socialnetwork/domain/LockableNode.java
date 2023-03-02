package socialnetwork.domain;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class LockableNode<T> implements Node<T> {

  Lock lock = new ReentrantLock();
  private T item;
  private int key;
  private LockableNode<T> next;
  public LockableNode(T item) {
    this.item = item;
    this.key = item.hashCode();
    this.next = null;
  }
  public LockableNode(T item, LockableNode<T> next) {
    this.item = item;
    this.key = item.hashCode();
    this.next = next;
  }

  public LockableNode(T item, int key, LockableNode<T> next) {
    this.item = item;
    this.key = key;
    this.next = next;
  }

  public void lock() {
    lock.lock();
  }

  public void unlock() {
    lock.unlock();
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
  public LockableNode<T> next() {
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
    this.next = (LockableNode<T>) next;
  }
}