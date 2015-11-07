package SqliteBasedPersistentQ;

import java.math.BigInteger;
import java.sql.*;

/**
 * Created by Rahul on 11/7/15.
 */
class ListeneerTask implements Runnable {

    void updateQueueRowAsRead(Long msgId,Connection connection) throws SQLException {
        PreparedStatement preparedStatement = connection.prepareStatement("update queue_publisher set status = ? where id=?");
        preparedStatement.setInt(1,CommonConstants.STATUS_PROCESSED);
        preparedStatement.setLong(2, msgId);
        preparedStatement.executeUpdate();
    }
    @Override
    public void run() {
        while (true) {
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
                break; //Exit thread
            }

            //2. retrieve from SQL DB one logic
            Connection connection = null;
            try {
                //System.out.println("Getting connection");
                connection = DriverManager.getConnection(new CommonUtils().getConnectionString(queueName));
                connection.setAutoCommit(false);
                PreparedStatement preparedStatement =
                        connection.prepareStatement("select * from queue_publisher where status = ? order by time_inserted ASC limit 1");
                preparedStatement.setInt(1,CommonConstants.STATUS_WRITTEN);
                ResultSet rs = preparedStatement.executeQuery();
                //System.out.println("Getting connection executing query");
                //Only one should be returned
                //boolean b = false;
                while (rs.next()) {
                    //b = true;
                    //System.out.println("Next rcvd");
                    String data = rs.getString("data");
                    Long msgId = rs.getLong("id");
                    try {
                        subscriber.messageRecieved(BigInteger.valueOf(msgId),data);
                        updateQueueRowAsRead(msgId,connection);
                    } catch (SubscriberException e) {
                        e.printStackTrace();
                    }
                }

                //System.out.println(b);
                rs.close();
                connection.commit();
                connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
                if (connection != null) {
                    try {
                        connection.close();
                    } catch (SQLException e1) {
                        e1.printStackTrace();
                    }
                }
            }
        }
    }

    public ListeneerTask(String queueName, ISubscriber subscriber) {
        this.queueName = queueName;
        this.subscriber = subscriber;
    }

    private String queueName;
    private ISubscriber subscriber;
}
public class DBSubscriber  {


    public DBSubscriber(ISubscriber consumer,String subscriberQueue) {
        this.subscriberQueue = subscriberQueue;
        this.consumer = consumer;
        addListenerThread();
    }

    void addListenerThread() {
       thread = new Thread(new ListeneerTask(subscriberQueue,consumer));
        thread.start();
    }

    private String subscriberQueue;
    private ISubscriber consumer;
    private Thread thread;


}
