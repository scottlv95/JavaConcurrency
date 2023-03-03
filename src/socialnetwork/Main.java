package socialnetwork;

import socialnetwork.domain.*;

public class Main {

  public static void main(String[] args) {
    // Implement logic here following the steps described in the specs
    //TODO
    Backlog backlog = new CoarseSyncBacklog();
    SocialNetwork socialNetwork = new SocialNetwork(backlog);
    Worker worker1 = new Worker(backlog);
    Worker worker2 = new Worker(backlog);
    Worker worker3 = new Worker(backlog);
    worker1.run();
    worker2.run();
    worker3.run();

    Board board1 = new CoarseSyncBoard();
    Board board2 = new CoarseSyncBoard();
    Board board3 = new CoarseSyncBoard();
    User user1 = new User("user1",socialNetwork);
    User user2 = new User("user2",socialNetwork);
    User user3 = new User("user3",socialNetwork);

    socialNetwork.register(user1,board1);
    socialNetwork.register(user2,board2);
    socialNetwork.register(user3,board3);
    user1.run();
    user2.run();
    user3.run();



  }
}
