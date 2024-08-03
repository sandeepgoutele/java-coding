package airline_checkin.airline;

import java.sql.SQLException;
import java.util.List;

public class Main {
    public void printAllUsers() {
        try {
            UserDAO userDAO = new UserDAO(DatabaseConnection.getInstance().getConnection());
            List<User> users = userDAO.getUsers();
            for (User user : users) {
                System.out.println(user);
            }
        } catch (SQLException ex) {
            System.out.println("PrintUsers failed with exception: " + ex.getMessage());
        }
    }

    public void printAllSeats() {
        try {
            SeatDAO seatDAO = new SeatDAO(DatabaseConnection.getInstance().getConnection());
            List<Seat> seats = seatDAO.getSeats();
            for (Seat seat : seats) {
                System.out.println(seat);
            }
        } catch (SQLException ex) {
            System.out.println("PrintSeats failed with exception: " + ex.getMessage());
        }
    }

    public static void main(String[] args) {
        Main obj = new Main();
        obj.printAllSeats();
//        obj.printAllUsers();
    }
}