package spring.ch6_aop.g_factory_bean;

public class Message {

    String text;

    private Message(String text) {
        this.text = text;
    }

    public static Message of(String message) {
        return new Message(message);
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
