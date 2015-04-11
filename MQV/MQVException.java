package MQV;

/**
 * Created by mike on 11.04.15.
 */
public class MQVException extends Exception {
    public MQVException(Exception e) {
        e.getMessage();
    }
    public MQVException(String e) {
        super(e);
    }
}
