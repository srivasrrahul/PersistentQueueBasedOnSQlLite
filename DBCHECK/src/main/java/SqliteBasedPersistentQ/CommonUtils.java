package SqliteBasedPersistentQ;

/**
 * Created by Rahul on 11/7/15.
 */
public class CommonUtils {
    String getConnectionString(String queueName) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("jdbc:sqlite:");
        stringBuilder.append(queueName);
        stringBuilder.append(".db");
        return stringBuilder.toString();

    }
}
