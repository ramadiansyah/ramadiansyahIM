package ymsg.network;

/**
 *
 * @author 7406030021
 */
public class LoginRefusedException extends YahooException
{	private long status=-1;

	LoginRefusedException(String m) { super(m); }
	LoginRefusedException(String m,long st) { this(m);  status=st; }

    /**
     *
     * @return
     */
    public long getStatus() { return status; }
}
