package airline_checkin.airline;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class SeatDAO {
    private final Connection connection;

    public SeatDAO(Connection connection) {
        this.connection = connection;
    }

    public List<Seat> getSeats() {
        List<Seat> seats = new ArrayList<>();
        try {
            String query = "SELECT * FROM seats";
            PreparedStatement statement = connection.prepareStatement(query);
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                Seat seat = new Seat();
                seat.setId(resultSet.getInt("id"));
                seat.setName(resultSet.getString("name"));
                seat.setTripId(resultSet.getInt("trip_id"));
                Integer userId = (Integer) resultSet.getObject("user_id");
                seat.setUserId(userId);
                seats.add(seat);
            }
        } catch (SQLException ex) {
            System.out.println("GetSeats failed with exception: " + ex.getMessage());
        }
        return seats;
    }

    public void resetUserIds() {
        try {
            String updateQuery = "UPDATE seats SET user_id = NULL";
            PreparedStatement statement = connection.prepareStatement(updateQuery);
            int rowsAffected = statement.executeUpdate();
            System.out.println("Number of rows updated: " + rowsAffected);
            connection.commit();
        } catch (SQLException ex) {
            System.out.println("ResetUserIds failed with exception: " + ex.getMessage());
        }
    }
}
