package common;

/**
 * Message listener interface that provides methods to
 * allow us to listen for a message from a source.
 *
 * @author Jameson Burchette
 * @author Nicholas Widener
 * @version November 2015
 */
public interface MessageListener {
    /**
     * Once implemented, messageReceived will print the message
     * that is received.
     *
     * @param message the message that is received.
     * @param source the source the message is received from.
     */
    public void messageReceived(String message, MessageSource source);

    /**
     * Once implemented, sourceClosed will close the source the message
     * is received from.
     * @param source the source that the message is received from.
     */
    public void sourceClosed(MessageSource source);
}
