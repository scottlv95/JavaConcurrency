package socialnetwork.domain;

import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;

public class HandOverHandSyncBacklog implements Backlog{

  LockableNode<Task> head, tail;
  AtomicInteger size = new AtomicInteger(0);

  public HandOverHandSyncBacklog() {
    head = new LockableNode<>(null, Integer.MIN_VALUE, null);
    tail = new LockableNode<>(null, Integer.MAX_VALUE, null);
    head.setNext(tail);
  }

  private Position<Task> find(LockableNode<Task> start, int key) {
    LockableNode<Task> pred, curr;
    pred = start;
    pred.lock();
    curr = start.next();
    curr.lock();
    while (curr.key() < key) {
      pred.unlock();
      pred = curr;
      curr = curr.next();
      curr.lock();
    }
    return new Position<>(pred,curr);
  }


  @Override
  public boolean add(Task task) {
    LockableNode<Task> node = new LockableNode<>(task);
    LockableNode<Task> pred, curr;
    pred = null;
    curr = null;
    try {
      Position<Task> where = find(head, node.key());
      pred = (LockableNode) where.pred;
      curr = (LockableNode) where.curr;
      if (curr.key() == node.key()) {
        return false;
      }
      else {
        node.setNext(where.curr);
        where.pred.setNext(node);
        size.incrementAndGet();
        return true;
      }
    } finally {
      pred.unlock();
      curr.unlock();
    }
  }

  @Override
  public Optional<Task> getNextTaskToProcess() {
    head.lock();
    LockableNode<Task> secondNode = head.next();
    secondNode.lock();
    try {
      if (secondNode.key() == tail.key()) {
        return Optional.empty();
      } else {
        head.setNext(secondNode.next());
        size.decrementAndGet();
        return Optional.of(secondNode.item());
      }
    } finally {
      head.unlock();
      secondNode.unlock();
    }
  }

  @Override
  public int numberOfTasksInTheBacklog() {
    return size.get();
  }
}
