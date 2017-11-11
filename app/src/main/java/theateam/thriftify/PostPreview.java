package theateam.thriftify;

import android.net.Uri;

/**
 * Created by oflyingturtleo on 10/20/17.
 */

public class PostPreview {
    private String name;
    private String thumbnail;

    public PostPreview() {}

    public PostPreview(String name, String thumbnail) {
        this.name = name;
        this.thumbnail = thumbnail;
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
}
