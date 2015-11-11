package common;

public interface MessageListener {
    public void messageReceived(String message, MessageSource source);
    public void sourceClosed(MessageSource source);
}
