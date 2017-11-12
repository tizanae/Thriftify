package theateam.thriftify;

public class Message {
    private String message;
    private String from;

    public Message() {

    }

    public Message(String message, String from) {
        this.message = message;
        this.from = from;
    }

    public String getFrom() {
        return this.from;
    }

    public String getMessage() {
        return this.message;
    }


}