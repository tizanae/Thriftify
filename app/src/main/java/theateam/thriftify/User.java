package theateam.thriftify;

/**
 * Created by oflyingturtleo on 11/10/17.
 */

    public class User {
        public String first_name;
        public String last_name;
        public String profile_picture_uri;
        public String user_id;

        public User() {}

    public User(String first_name, String last_name, String profile_picture_uri) {
        this.first_name = first_name;
        this.last_name = last_name;
        this.profile_picture_uri = profile_picture_uri;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }
    public String getUser_id() { return this.user_id; }
//
//    public String getFirst_name() {
//        return this.first_name;
//    }
//
//    public String getLast_name() {
//        return this.last_name;
//    }
//
//    public String getProfile_picture_uri { return this.profile_picture_uri; }
//
//    public void setFirst_name(String first_name) {
//        this.first_name = first_name;
//    }
//
//    public void setLast_name(String last_name) {
//        this.last_name = last_name;
//    }
//
//    public void setProfile_picture_uri(String profile_picture_uri) { this.profile_picture_uri = profile_picture_uri; }
}
