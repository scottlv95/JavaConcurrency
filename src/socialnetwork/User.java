package socialnetwork;

import socialnetwork.domain.Board;
import socialnetwork.domain.Message;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;

public class User extends Thread {

  private static final AtomicInteger nextId = new AtomicInteger(0);

  protected final SocialNetwork socialNetwork;
  private final int id;
  private final String name;

  public User(String username, SocialNetwork socialNetwork) {
    this.name = username;
    this.id = User.nextId.getAndIncrement();
    this.socialNetwork = socialNetwork;
  }

  public int getUserId() {
    return id;
  }

  @Override
  public void run() {
    // send random messages
    Set<User> allUsers = socialNetwork.getAllUsers();
    Random rand = new Random();
    List<User> sendList = new ArrayList<>();

    // randomly sending
    for (User user: allUsers) {
      boolean isSending = rand.nextBoolean();
      if (isSending) {
        sendList.add(user);
      }
    }
    socialNetwork.postMessage(this, sendList,"Hello");

    //get board snapshot
    Board userBoard = socialNetwork.getBoards().get(this);
    List<Message> userMessages = userBoard.getBoardSnapshot();

    //randomly deleting messages
    for (Message message: userMessages) {
      boolean isDeleting = rand.nextBoolean();
      if (isDeleting) {
        userMessages.remove(message);
      }
    }


  }

  @Override
  public String toString() {
    return "User{" + "id=" + id + ", name='" + name + '\'' + '}';
  }

  @Override
  public int hashCode() {
    return id;
  }
}
