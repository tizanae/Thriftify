package theateam.thriftify;

public class Message {
    private String message;
    private String from;
    private long timestamp;

    public Message() {

    }

    public Message(String message, String from, long timestamp) {
        this.message = message;
        this.from = from;
        this.timestamp = timestamp;
    }

    public String getFrom() {
        return this.from;
    }

    public String getMessage() {
        return this.message;
    }

    public long getTimestamp() { return this.timestamp; }

}