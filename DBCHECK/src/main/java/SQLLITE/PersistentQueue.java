package SQLLITE;

import java.sql.*;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by Rahul on 10/25/15.
 */
public class PersistentQueue {
    private String queueName;

    public PersistentQueue(String queueName) {
        this.queueName = queueName;
    }

    String getConnectionString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("jdbc:sqlite:");
        stringBuilder.append(queueName);
        return stringBuilder.toString();

    }
    void createQueue()  {



        try {
            Connection conn = DriverManager.getConnection(getConnectionString());
            Statement stat = conn.createStatement();
            stat.executeUpdate("create table queue_publisher (id INT ,data VARCHAR,status INT ,time_inserted INT ,time_read INT)");
            stat.executeUpdate("create unique index idindex on queue_publisher (id)");
            stat.executeUpdate("create index statusindex on queue_publisher (status)");
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("No problem");
        }


    }
    void addMessage(long id,String data) throws SQLException{
        Connection conn = null;
        try {
            conn = DriverManager.getConnection(getConnectionString());

            conn.setAutoCommit(false);
            PreparedStatement prep = conn.prepareStatement(
                    "insert into queue_publisher values (?, ?,?,?,?);");
            prep.setLong(1, id);
            prep.setString(2, data);
            prep.setLong(3, 0);
            prep.setLong(4, 0);
            prep.setLong(5, 0);
            prep.execute();
            conn.commit();

        } catch (SQLException e) {
            e.printStackTrace();
            if (conn != null) {
                try {
                    conn.rollback();

                } catch (SQLException e1) {
                    e1.printStackTrace();
                }
            }

            throw e;
        } finally {
            if (conn != null) {
                try {
                    conn.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                    throw e;
                }
            }
        }
    }

    void retrieveMessage(){
        Connection conn = null;
        try {
            conn = DriverManager.getConnection(getConnectionString());
            conn.setAutoCommit(false);
            Statement stat = conn.createStatement();

            String updateStr = "update queue_publisher set status=1 where id = ?";
            PreparedStatement updateStatement = conn.prepareStatement(updateStr);
            ResultSet rs = stat.executeQuery("select * from queue_publisher where status = 0;");
            Set<Integer> idSet = new HashSet<>();
            while (rs.next()) {
                String data = rs.getString("data");
                System.out.println(data);
                idSet.add(rs.getInt("id"));
                updateStatement.setInt(1, rs.getInt("id"));
                updateStatement.execute();

            }





            conn.commit();
            rs.close();




        } catch (SQLException e) {
            e.printStackTrace();
            try {
                conn.rollback();
            } catch (SQLException e1) {
                e1.printStackTrace();
            }
        } finally {
            if (conn != null) {
                try {
                    conn.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static void main(String[] args) throws SQLException {
        PersistentQueue persistentQueue = new PersistentQueue("hello");
        persistentQueue.createQueue();
        persistentQueue.addMessage(System.currentTimeMillis(),"rahul");
        persistentQueue.addMessage(System.currentTimeMillis(),"shloak");
        persistentQueue.retrieveMessage();

    }
}
