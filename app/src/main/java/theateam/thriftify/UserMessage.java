package theateam.thriftify;

/**
 * Created by TC on 11/11/17.
 */

public class UserMessage {
    private String message;
    private String sender;

    public UserMessage(String message, String sender) {
        this.message = message;
        this.sender = sender;
    }

    public UserMessage() {
    }

    public String getMessage() {
        return message;
    }

    public String getSender() {
        return sender;
    }
}
