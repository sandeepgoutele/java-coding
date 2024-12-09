package connection_pool;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.BlockingDeque;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.LinkedBlockingQueue;

public class ConnectionPool {
    private BlockingDeque<Connection> connectionPool;

    ConnectionPool(int capacity) {
        connectionPool = new LinkedBlockingDeque<>(capacity);
        for (int idx = 0; idx < capacity; idx++) {
            connectionPool.add(createNewConnection());
        }
    }

    private Connection createNewConnection() {
        try {
            return DBConnection.getInstance().getConnection();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public Connection getConnection() {
        try {
            return connectionPool.take();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public void releaseConnection(Connection conn) {
        if (conn != null) {
            connectionPool.add(conn);
        }
    }

    public void shutdown() {
        for (Connection conn: connectionPool) {
            try {
                conn.close();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
