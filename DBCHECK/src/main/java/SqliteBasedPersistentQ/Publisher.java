package SqliteBasedPersistentQ;

import java.math.BigInteger;
import java.sql.*;

/**
 * Created by Rahul on 11/7/15.
 */
public class Publisher implements IPublisher {
    private String publisherName;

    public Publisher(String publisherName) throws SQLException {
        this.publisherName = publisherName;
        createQueue();
    }

    @Override
    public void publish(BigInteger messageId, String message) throws PublisherException {
        Connection connection = null;
        try {
            connection = DriverManager.getConnection(new CommonUtils().getConnectionString(this.publisherName));
            connection.setAutoCommit(false);
            PreparedStatement preparedStatement =
                    connection.prepareStatement("insert into queue_publisher values(?,?,?,?,?)");

            preparedStatement.setLong(1,messageId.longValue());
            preparedStatement.setString(2,message);
            preparedStatement.setInt(3,CommonConstants.STATUS_WRITTEN);
            preparedStatement.setLong(4,System.currentTimeMillis());
            preparedStatement.setLong(5,0);
            preparedStatement.execute();
            connection.commit();

        } catch (SQLException e) {
            e.printStackTrace();
            throw new PublisherException(e);
        } finally {
            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                    throw new PublisherException(e);
                }
            }
        }

    }



    boolean checkTableExists(Connection connection) throws SQLException {
        PreparedStatement preparedStatement = connection.prepareStatement("SELECT name FROM sqlite_master WHERE type='table' AND name='queue_publisher'");
        //preparedStatement.setString(1,"queue_publisher");
        ResultSet resultSet = preparedStatement.executeQuery();
        while (resultSet.next()) {
            resultSet.close();
            return true;
        }

        return false;



    }
    void createQueue() throws SQLException {
        Connection conn = null;
        try {
            conn = DriverManager.getConnection(new CommonUtils().getConnectionString(publisherName));
            if (checkTableExists(conn)) {
                System.out.println("Table exists");
                return;
            }
            Statement stat = conn.createStatement();

            stat.executeUpdate("create table queue_publisher (id INT ,data VARCHAR,status INT ,time_inserted INT ,time_read INT)");
            stat.executeUpdate("create unique index idindex on queue_publisher (id)");
            stat.executeUpdate("create index statusindex on queue_publisher (status)");
            stat.executeUpdate("create index time_inserted_order on queue_publisher (time_inserted ASC)");
        }finally {
            if (conn != null) {
                conn.close();
            }
        }


    }
}
