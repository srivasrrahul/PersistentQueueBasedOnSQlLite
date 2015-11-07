package SqliteBasedPersistentQ;

import java.math.BigInteger;
import java.sql.SQLException;

/**
 * Created by Rahul on 11/7/15.
 */

class MessageReciever implements ISubscriber {

    @Override
    public void messageRecieved(BigInteger messageId, String message) throws SubscriberException {
        System.out.println("Message recieved with " + messageId);
    }
}

class PublisherTask implements Runnable {

    static void publisherTest() {
        PublisherFactory publisherFactory = new PublisherFactory();
        try {
            IPublisher publisher = publisherFactory.createPublisher("testing");
            publisher.publish(BigInteger.valueOf(System.currentTimeMillis()),"hello");
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (PublisherException e) {
            e.printStackTrace();
        }
    }
    @Override
    public void run() {
          while (true) {
              publisherTest();
              try {
                  Thread.sleep(100);
              } catch (InterruptedException e) {
                  e.printStackTrace();
              }
          }
    }
}
public class TestCode {


    static void subsciberTest() {
        SubscriberFactory subscriberFactory = new SubscriberFactory();
        subscriberFactory.createSubscriber(new MessageReciever(),"testing");
    }
    public static void main(String[] args) {
        Thread t = new Thread(new PublisherTask());
        t.start();
        System.out.println("subscriver test");
        subsciberTest();
    }
}
