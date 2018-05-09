package Protocol;

/**
 * Communication Protocol, information responses returned to client
 */
public class Pinfo {

    // inform client that action successed
    public final static String SUCCESS = "SUCCESS";

    // inform client that action failed
    public final static String FAILURE = "FAILURE";

    // unknown command
    public final static String UCOM = "UNKNOWN_COMMAND";

}
