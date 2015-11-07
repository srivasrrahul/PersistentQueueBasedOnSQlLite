package SqliteBasedPersistentQ;

import java.sql.SQLException;

/**
 * Created by Rahul on 11/7/15.
 */
public class PublisherFactory {
    IPublisher createPublisher(String publisherId) throws SQLException {
        Publisher publisher = new Publisher(publisherId);
        return publisher;
    }
}
