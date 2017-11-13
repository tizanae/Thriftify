package theateam.thriftify;

@SuppressWarnings({"unused", "WeakerAccess"})
public class User {
    private String userId;
    private String firstName;
    private String lastName;
    private String thumbnail;

    public User() {}

    public User(String userId, String firstName, String lastName, String thumbnail) {
        this.userId = userId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.thumbnail = thumbnail;
    }

    public String getUserId() { return this.userId; }
    public String getFirstName() { return this.firstName; }
    public String getLastName() { return this.lastName; }
    public String getThumbnail() { return this.thumbnail; }

    public void setUserId(String userId) { this.userId = userId; }
    public void setFirstName(String firstName) { this.firstName = firstName; }
    public void setLastName(String lastName) { this.lastName = lastName; }
    public void setThumbnail(String thumbnail) { this.thumbnail = thumbnail; }
}
