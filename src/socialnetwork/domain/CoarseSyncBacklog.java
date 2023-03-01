package socialnetwork.domain;

import java.util.Optional;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class CoarseSyncBacklog implements Backlog{

  private Lock lock = new ReentrantLock();
  int size = 0;
  private CoarseNode<Task> head, tail;

  public CoarseSyncBacklog() {
    head = new CoarseNode<>(null, Integer.MIN_VALUE, null);
    tail = new CoarseNode<>(null, Integer.MAX_VALUE, null);
    head.setNext(tail);
  }

  private Position<Task> find(Node<Task> start, int key) {
    Node<Task> pred, curr;
    curr = start;
    do {
      pred = curr;
      curr = curr.next();
    } while (curr.key() < key);
    return new Position<Task>(pred, curr);
  }

//  public boolean contains(Task task) {
//    lock.lock();
//    try {
//      CoarseNode<Task> taskCoarseNode = new CoarseNode<>(task);
//      Position<Task> expectedPos = find(head, taskCoarseNode.key());
//      return expectedPos.curr.key() == taskCoarseNode.key();
//    } finally {
//      lock.unlock();
//    }
//  }
  @Override
  public boolean add(Task task) {
    lock.lock();
    try {
      CoarseNode<Task> taskCoarseNode = new CoarseNode<>(task);
      Position<Task> where = find(head, taskCoarseNode.key());
      if (where.curr.key() == taskCoarseNode.key()){
        return false;
      } else {
        taskCoarseNode.setNext(where.curr);
        where.pred.setNext(taskCoarseNode);
        size+=1;
        return true;
      }
    }
    finally {
      lock.unlock();
    }
  }

  @Override
  public Optional<Task> getNextTaskToProcess() {
    if (head.next().key() == tail.key()) {
      return Optional.empty();
    }
    else {
      Optional<Task> result = Optional.of(head.next().item());
      head.setNext(head.next().next());
      size -= 1;
      return result;
    }
  }

  @Override
  public int numberOfTasksInTheBacklog() {
    return size;
  }
}
