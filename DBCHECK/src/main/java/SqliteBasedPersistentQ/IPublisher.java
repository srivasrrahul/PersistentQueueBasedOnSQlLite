package SqliteBasedPersistentQ;

import java.math.BigInteger;

/**
 * Created by Rahul on 11/7/15.
 */
public interface IPublisher {
  void publish(BigInteger messageId,String message) throws PublisherException;
}
