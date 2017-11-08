package theateam.thriftify;

/**
 * Created by TC on 11/7/17.
 */

public class UserInfo {
    private String fullname;
    private String email;
    private String username;
    private String phonenumber;

    public UserInfo() {
    }

    public UserInfo(String username, String fullname, String email, String phonenumber ){
        this.username = username;
        this.fullname = fullname;
        this.email = email;
        this.phonenumber = phonenumber;
    }

    public String getFullname() {
        return fullname;
    }

    public String getEmail() {
        return email;
    }

    public String getUsername() {
        return username;
    }

    public String getPhonenumber() {
        return phonenumber;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPhonenumber(String phonenumber) {
        this.phonenumber = phonenumber;
    }
}
