package connection_pool;

import airline_checkin.airline.DatabaseConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class WithoutPool implements Runnable {
    @Override
    public void run() {
        Connection conn = null;
        try {
            conn = DBConnection.getInstance().getConnection();
            String query = "SELECT SLEEP(0.1);";
            PreparedStatement statement = conn.prepareStatement(query);
            statement.executeQuery();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            try {
                if (conn!=null) {
                    conn.close();
                }
            } catch (SQLException e) {
                System.out.println(e.getMessage());
            }
        }
    }

    public static void main(String[] args) throws InterruptedException {
        long startTime = System.currentTimeMillis();
        Thread[] threads = new Thread[500];
        for (int idx = 0; idx < threads.length; idx++) {
            threads[idx] = new Thread(new WithoutPool());
            threads[idx].start();
        }

        for (Thread thread : threads) {
            thread.join();
        }
        long endTime = System.currentTimeMillis();
        System.out.println("Time taken: " + (endTime-startTime) + " milliseconds.");
    }
}
