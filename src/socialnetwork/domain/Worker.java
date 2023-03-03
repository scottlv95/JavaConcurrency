package socialnetwork.domain;

import java.util.Optional;

public class Worker extends Thread {

  private final Backlog backlog;
  private boolean interrupted = false;

  public Worker(Backlog backlog) {
    this.backlog = backlog;
  }

  @Override
  public void run() {
    while (!interrupted) {
      Optional<Task> optionalTask = backlog.getNextTaskToProcess();
      if (optionalTask.isEmpty()) {
        try {
          Thread.sleep(1);
        } catch (InterruptedException e) {
          throw new RuntimeException(e);
        }
      } else {
        process(optionalTask.get());
      }

    }
  }

  public void interrupt() {
    this.interrupted = true;
  }

  public void process(Task nextTask) {
    Board board = nextTask.getBoard();
    Task.Command command = nextTask.getCommand();
    switch (command) {
      case POST -> board.addMessage(nextTask.getMessage());
      case DELETE -> {
        if (!board.deleteMessage(nextTask.getMessage())) {
          backlog.add(nextTask);
        }
      }
    }
  }
}
