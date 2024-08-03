package airline_checkin;

import airline_checkin.airline.*;
import airline_checkin.approach1.NaiveApp;

import java.sql.Connection;
import java.sql.SQLException;
import java.time.Duration;
import java.time.Instant;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import airline_checkin.approach2.AppWithExLock;
import airline_checkin.approach3.AppWithSkipLock;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Main {
    private static final Logger log = LoggerFactory.getLogger(airline_checkin.airline.Main.class);
    public static void main(String[] args) {
        Instant start = Instant.now();
        log.debug("Starting the airline booking simulation");

        Connection conn = null;
        try {
            conn = DatabaseConnection.getInstance().getConnection();
            UserDAO userDAO = new UserDAO(conn);
            List<User> users = userDAO.getUsers();
            log.debug("Simulating airline booking for {} users", users.size());
            ExecutorService executorService = null;
            try {
                executorService = Executors.newFixedThreadPool(users.size());
//                NaiveApp app = new NaiveApp(conn);
//                AppWithExLock app = new AppWithExLock(conn);
                AppWithSkipLock app = new AppWithSkipLock(conn);
                for (User user : users) {
                    executorService.submit(() -> {
                        String userName = user.getFirstName() + " " + user.getLastName();
                        try {
                            Seat seat = app.bookSeat(user);
                            log.info("{} was assigned the seat {}", userName, seat.getName());
                        } catch (Exception ex) {
                            log.error("We couldn't assign seat to user {}", userName, ex);
                        }
                    });
                }
            } catch (IllegalArgumentException ex) {
                log.error("Executor failed with exception: {}.", ex.getMessage());
            } finally {
                if (executorService != null) {
                    executorService.shutdown();
                    log.info("Terminating executor: {}", executorService.awaitTermination(1, TimeUnit.HOURS));
                }
            }

            SeatDAO seatDAO = new SeatDAO(conn);
            List<Seat> seats = seatDAO.getSeats();
            String[] seatPlot = new String[6];
            Arrays.fill(seatPlot, "");

            for (Seat seat : seats) {
                String label = seat.getName().split("-")[1];
                int index = label.charAt(0) - 'A';
                String occupied = seat.getUserId()!=null ? "x" : ".";
                seatPlot[index] += " " + occupied + " ";
            }

            for (int i = 0; i < seatPlot.length; i++) {
                if (i % 3 == 0) {
                    System.out.println();
                }
                System.out.println(seatPlot[i]);
            }

            System.out.println();
            seatDAO.resetUserIds();
        } catch (SQLException ex) {
            log.error("NaiveApp failed with exception: {}", ex.getMessage());
        } catch (InterruptedException | IllegalArgumentException ex) {
            throw new RuntimeException(ex);
        } finally {
            if (conn != null) {
                try {
                    conn.close();
                    Instant end = Instant.now();
                    log.info("Time taken for full execution: {}", Duration.between(start, end));
                } catch (SQLException ex) {
                    log.error("Connection closing failed with exception: {}", ex.getMessage());
                }
            }
        }
    }
}
