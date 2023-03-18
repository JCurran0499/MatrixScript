package Program;

public class Payload {

    public final String sessionToken;
    public final String command;

    public Payload(String t, String c) {
        sessionToken = t;
        command = c;
    }
}
