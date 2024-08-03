package airline_checkin.airline;

public class Seat {
    private int id;
    private String name;
    private int tripId;
    private Integer userId;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getTripId() {
        return tripId;
    }

    public void setTripId(int tripId) {
        this.tripId = tripId;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    @Override
    public String toString() {
        return "Seat{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", tripId=" + tripId +
                ", userId=" + userId +
                '}';
    }
}
