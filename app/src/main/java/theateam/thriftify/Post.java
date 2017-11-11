package theateam.thriftify;

import android.net.Uri;

import java.util.ArrayList;

/**
 * Created by oflyingturtleo on 11/11/17.
 */

public class Post {
    public String user_id;
    public String category_key;
    public String title;
    public String price;
    public String description;
    public double latitude;
    public double longitude;
    public ArrayList<String> picture_uris;

    public Post(
            String user_id,
            String category_key,
            String title,
            String price,
            String description,
            double latitude,
            double longitude,
            ArrayList<String> picture_uris
    ) {
        this.user_id = user_id;
        this.category_key = category_key;
        this.title = title;
        this.price = price;
        this.description = description;
        this.latitude = latitude;
        this.longitude = longitude;
        this.picture_uris = picture_uris;
    }
}
