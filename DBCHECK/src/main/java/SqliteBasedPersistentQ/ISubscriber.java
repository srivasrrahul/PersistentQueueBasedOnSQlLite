package SqliteBasedPersistentQ;

import java.math.BigInteger;

/**
 * Created by rasrivastava on 11/7/15.
 */
public interface ISubscriber {
    void messageRecieved(BigInteger messageId,String message) throws SubscriberException;
}
