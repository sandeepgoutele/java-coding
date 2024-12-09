package connection_pool;

import java.sql.Connection;
import java.sql.SQLException;

public class WithConnectionPool implements Runnable {
    private static final ConnectionPool connectionPool;

    static {
        connectionPool = new ConnectionPool(10); // Pool size of 10
    }

//    ConnectionPool connectionPool;
//    WithConnectionPool(ConnectionPool connectionPool) {
//        this.connectionPool = connectionPool;
//    }

    @Override
    public void run() {
        Connection conn = null;
        try {
            conn = connectionPool.getConnection();
            String query = "SELECT SLEEP(0.1);";
            conn.prepareStatement(query).executeQuery();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            if (conn!=null) {
                connectionPool.releaseConnection(conn);
            }
        }
    }

    public static void main(String[] args) throws InterruptedException {
        long startTime = System.currentTimeMillis();
        ConnectionPool connectionPool = new ConnectionPool(10);
        Thread[] threads = new Thread[500];
        for (int idx = 0; idx < 500; idx++) {
            threads[idx] = new Thread(new WithConnectionPool());
            threads[idx].start();
        }

        for (Thread thread : threads) {
            thread.join();
        }
        long endTime = System.currentTimeMillis();
        System.out.println("Time taken: " + (endTime-startTime) + " milliseconds.");
    }
}
