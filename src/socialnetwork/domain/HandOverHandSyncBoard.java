package socialnetwork.domain;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class HandOverHandSyncBoard implements Board{

  LockableNode<Message> head, tail;
  AtomicInteger size = new AtomicInteger(0);

  public HandOverHandSyncBoard() {
    head = new LockableNode<>(null, Integer.MAX_VALUE, null);
    tail = new LockableNode<>(null, Integer.MIN_VALUE, null);
    head.setNext(tail);
  }



  private Position<Message> find(LockableNode<Message> start, int key) {
    LockableNode<Message> pred, curr;
    pred = start;
    pred.lock();
    curr = start.next();
    curr.lock();
    while (curr.key()> key) {
      pred.unlock();
      pred = curr;
      curr = curr.next();
      curr.lock();
    }
    return new Position<>(pred,curr);
  }



  @Override
  public boolean addMessage(Message message) {
    LockableNode<Message> node = new LockableNode<>(message);
    LockableNode<Message> pred, curr;
    pred = null;
    curr = null;
    try {
      Position<Message> where = find(head, node.key());
      pred = (LockableNode) where.pred;
      curr = (LockableNode) where.curr;
      if (curr.key() == node.key()) {
        return false;
      }
      else {
        node.setNext(curr);
        pred.setNext(node);
        size.incrementAndGet();
        return true;
      }
    } finally {
      pred.unlock();
      curr.unlock();
    }
  }

  @Override
  public boolean deleteMessage(Message message) {
    LockableNode<Message> node = new LockableNode<>(message);
    LockableNode<Message> curr, pred;
    curr = null;
    pred = null;
    try {
      Position<Message> where = find(head, node.key());
      pred = (LockableNode) where.pred;
      curr = (LockableNode) where.curr;
      if (curr.key() != node.key()) {
        return false;
      } else {
        pred.setNext(curr.next());
        size.decrementAndGet();
        return true;
      }
    } finally {
      curr.unlock();
      pred.unlock();
    }
  }

  @Override
  public int size() {
    return size.get();
  }

  @Override
  public List<Message> getBoardSnapshot() {
    // method 1, lock the first two node (head and head.next) (works but slow)
//    LockableNode<Message> secondNode = head.next();
//    head.lock();
//    secondNode.lock();
//    try {
//      List<Message> messageList = new ArrayList<>();
//      Node<Message> curr = head.next();
//      while (curr.key() != tail.key()) {
//        messageList.add(curr.item());
//        curr = curr.next();
//      }
//      return messageList;
//    } finally {
//      head.unlock();
//      secondNode.unlock();
//    }
    // method 2, the lock cost might be high
    LockableNode<Message> pred, curr;
    pred = head;
    curr = head.next();
    pred.lock();
    curr.lock();
    List<Message> messageList = new ArrayList<>();
    try {
      while (curr.key() != tail.key()) {
        messageList.add(curr.item());
        pred.unlock();
        pred = curr;
        curr = curr.next();
        curr.lock();
      }
    } finally {
      pred.unlock();
      curr.unlock();
    }
    return messageList;
  }
    // doesnt need to be consistent
//    List<Message> messageList = new ArrayList<>();
//    Node<Message> curr = head.next();
//    while (curr.key() != tail.key()) {
//      messageList.add(curr.item());
//      curr = curr.next();
//    }
//    return messageList;
//  }
}
