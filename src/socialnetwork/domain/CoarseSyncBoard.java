package socialnetwork.domain;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class CoarseSyncBoard implements Board {

  private Lock lock = new ReentrantLock();
  int size = 0;
  private CoarseNode<Message> head, tail;

  public CoarseSyncBoard() {
    head = new CoarseNode<>(null, Integer.MAX_VALUE, null);
    tail = new CoarseNode<>(null, Integer.MIN_VALUE, null);
    head.setNext(tail);
  }

  private Position<Message> find(Node<Message> start, int key) {
    Node<Message> pred, curr;
    curr = start;
    do {
      pred = curr;
      curr = curr.next();
    } while (curr.key() > key);
    return new Position<Message>(pred, curr);
  }
//
//  public boolean contains(Message message) {
//    lock.lock();
//    try {
//      CoarseNode<Message> messageCoarseNode = new CoarseNode<>(message);
//      Position<Message> expectedPos = find(head, messageCoarseNode.key());
//      return expectedPos.curr.key() == messageCoarseNode.key();
//    } finally {
//      lock.unlock();
//    }
//  }

  @Override
  public boolean addMessage(Message message) {
    lock.lock();
    try {
      CoarseNode<Message> messageCoarseNode = new CoarseNode<>(message);
      Position<Message> where = find(head, messageCoarseNode.key());
      if (where.curr.key() == messageCoarseNode.key()){
        return false;
      } else {
        messageCoarseNode.setNext(where.curr);
        where.pred.setNext(messageCoarseNode);
        size+=1;
        return true;
      }
    }
    finally {
      lock.unlock();
    }
  }

  @Override
  public boolean deleteMessage(Message message) {
    lock.lock();
    try {
      CoarseNode<Message> messageCoarseNode = new CoarseNode<>(message);
      Position<Message> where = find(head, messageCoarseNode.key());
      if (where.curr.key() > messageCoarseNode.key()) {
        return false;
      } else {
        where.pred.setNext(where.curr.next());
        size-=1;
        return true;
      }
    } finally {
      lock.unlock();
    }
  }

  @Override
  public int size() {
    return size;
  }

  @Override
  public List<Message> getBoardSnapshot() {
    List<Message> messageList = new ArrayList<>();
    Node<Message> curr = head.next();
    while (curr.key() != tail.key()) {
      messageList.add(curr.item());
      curr = curr.next();
    }
    return messageList;
  }
}
