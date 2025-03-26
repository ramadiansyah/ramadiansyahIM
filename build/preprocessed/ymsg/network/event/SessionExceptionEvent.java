package ymsg.network.event;

// *********************************************************************
// This class is used to pass exceptions from the input thread to the
// main application.
//
//							Message		Exception
// inputExceptionThrown		y			y
// *********************************************************************
/**
 *
 * @author 7406030021
 */
public class SessionExceptionEvent extends SessionEvent {

    /**
     *
     */
    protected Exception exception;

	// -----------------------------------------------------------------
	// CONSTRUCTORS
	// -----------------------------------------------------------------
    /**
     *
     * @param o
     * @param m
     * @param e
     */
    public SessionExceptionEvent(Object o,String m,Exception e)
	{	super(o);  message=m;  exception=e;
	}

    /**
     *
     * @return
     */
    public Exception getException() { return exception; }

	public String toString()
	{	return "Exception: message=\""+message+"\" type="+exception.toString();
	}

}
