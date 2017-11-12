package theateam.thriftify;


public class ActiveChatUser {
    private String user_id;
    private String name;
    private String thumb;

    public ActiveChatUser(String user_id, String name, String thumb) {
        this.user_id = user_id;
        this.name = name;
        this.thumb = thumb;
    }

    public String getUser_id() { return this.user_id; }
    public String getName() { return this.name; }
    public String getThumb() { return this.thumb; }
}
