package theateam.thriftify;

import java.util.HashMap;

@SuppressWarnings({"unused", "WeakerAccess"})
public class Post {
    private String postKey;
    private String userId;
    private String categoryKey;
    private String title;
    private String price;
    private String description;
    private double latitude;
    private double longitude;
    private HashMap<String, String> pictures;

    public Post() {}

    public Post(
            String postKey,
            String userId,
            String categoryKey,
            String title,
            String price,
            String description,
            double latitude,
            double longitude,
            HashMap<String, String> pictures
    ) {
        this.postKey = postKey;
        this.userId = userId;
        this.categoryKey = categoryKey;
        this.title = title;
        this.price = price;
        this.description = description;
        this.latitude = latitude;
        this.longitude = longitude;
        this.pictures = pictures;
    }

    public String getPostKey() {
        return postKey;
    }

    public String getUserId() {
        return userId;
    }

    public String getCategoryKey() {
        return categoryKey;
    }

    public String getTitle() {
        return title;
    }

    public String getPrice() {
        return price;
    }

    public String getDescription() {
        return description;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public HashMap<String, String> getPictures() {
        return pictures;
    }

    public void setPostKey(String postKey) {
        this.postKey = postKey;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public void setCategoryKey(String categoryKey) {
        this.categoryKey = categoryKey;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public void setPictures(HashMap<String, String> pictures) {
        this.pictures = pictures;
    }
}
