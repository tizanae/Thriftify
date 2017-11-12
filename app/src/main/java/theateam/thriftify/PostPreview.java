package theateam.thriftify;

public class PostPreview {
    private String name;
    private String thumbnail;
    private String key;
    private String category;

    public PostPreview() {}

    public PostPreview(String name, String thumbnail, String key, String category) {
        this.name = name;
        this.thumbnail = thumbnail;
        this.key = key;
        this.category = category;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }

    public String getKey() { return key; }

    public void setKey(String key) { this.key = key; }

    public String getCategory() { return this.category; }

    public void setCategory(String category) { this.category = category; }
}
