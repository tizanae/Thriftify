package theateam.thriftify;

@SuppressWarnings({"unused", "WeakerAccess"})
public class Message {
    private String message;
    private String from;
    private long timestamp;

    Message() {}

    Message(String message, String from, long timestamp) {
        this.message = message;
        this.from = from;
        this.timestamp = timestamp;
    }


    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }
}