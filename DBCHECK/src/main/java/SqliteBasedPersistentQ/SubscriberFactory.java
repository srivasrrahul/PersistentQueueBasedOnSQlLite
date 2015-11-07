package SqliteBasedPersistentQ;

import java.util.HashMap;

/**
 * Created by Rahul on 11/7/15.
 */
public class SubscriberFactory {
    void createSubscriber(ISubscriber client,String uniqueId) {
        DBSubscriber dbSubscriber = new DBSubscriber(client,uniqueId);
        dbSubscriberHashMap.put(uniqueId,dbSubscriber);
    }

    HashMap<String,DBSubscriber> dbSubscriberHashMap = new HashMap<>();
}
