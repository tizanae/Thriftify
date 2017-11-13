package theateam.thriftify;

@SuppressWarnings({ "unused", "WeakerAccess"})
public class User {
    private String userId;
    private String firstName;
    private String lastName;
    private String thumbnail;
    private double latitude;
    private double longitude;

    public User() {}

    public User(String userId, String firstName, String lastName, String thumbnail, double latitude, double longitude) {
        this.userId = userId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.thumbnail = thumbnail;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }
}
