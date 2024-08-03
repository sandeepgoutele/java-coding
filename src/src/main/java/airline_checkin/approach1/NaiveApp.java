package airline_checkin.approach1;

import airline_checkin.airline.*;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class NaiveApp {
    private static final Logger log = LoggerFactory.getLogger(Main.class);
    Connection conn;

    public NaiveApp(Connection conn) {
        this.conn = conn;
    }

    public Seat bookSeat(User user) {
        Seat seat = new Seat();
        try {
            conn.setAutoCommit(false); // Start transaction
            String selectQry = "SELECT id, name, trip_id, user_id from seats " +
                    "WHERE trip_id = 1 AND user_id is null " +
                    "ORDER BY id LIMIT 1";
            PreparedStatement selectStatement = conn.prepareStatement(selectQry);
            ResultSet resultSet = selectStatement.executeQuery();
            seat = new Seat();
            while (resultSet.next()) {
                seat.setId(resultSet.getInt("id"));
                seat.setName(resultSet.getString("name"));
                seat.setTripId(resultSet.getInt("trip_id"));
                seat.setUserId((Integer)resultSet.getObject("user_id"));
            }

            String updateQry = "UPDATE seats SET user_id = " + user.getId() + " WHERE id = " + seat.getId();
            PreparedStatement updateStatement = conn.prepareStatement(updateQry);
            updateStatement.executeUpdate();

            conn.commit(); // Commit transaction
        } catch (SQLException ex) {
            log.error("Seat booking failed with exception: {}.", ex.getMessage());
        }
        return seat;
    }
}
