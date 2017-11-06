package theateam.thriftify;

/**
 * Created by oflyingturtleo on 10/20/17.
 */

public class PostPreview {
    private String name;
    private int thumbnail;

    public PostPreview() {}

    public PostPreview(String name, int thumbnail) {
        this.name = name;
        this.thumbnail = thumbnail;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(int thumbnail) {
        this.thumbnail = thumbnail;
    }
}