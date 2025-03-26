package ymsg.network.event;

import java.util.Date;
import ymsg.network.util.EventObject;

// *********************************************************************
// This class is the parent of all event classes in this package.
//
//							To		From	Message	Timestamp
// contactRejectionReceived	y		y		y		n
// contactRequestReceived	y		y		y		y
// messageReceived			y		y		y		n
// buzzReceived				y		y		y		n
// offlineMessageReceived	y		y		y		y
// listReceived				n		n		n		n
// logoffReceived			n		n		n		n
// *********************************************************************
/**
 *
 * @author 7406030021
 */
public class SessionEvent extends EventObject {

    /**
     * 
     */
    /**
     * 
     */
    /**
     * 
     */
    protected String to = null,  from = null,  message = null;
    /**
     *
     */
    protected Date timestamp;
    /**
     *
     */
    protected long status=0;

	// -----------------------------------------------------------------
	// CONSTRUCTORS
	// -----------------------------------------------------------------
    /**
     *
     * @param o
     */
    public SessionEvent(Object o)
	{	super(o);
	}

    /**
     *
     * @param o
     * @param t
     * @param f
     */
    public SessionEvent(Object o,String t,String f)
	{	this(o);  to=t;  from=f;
	}

    /**
     *
     * @param o
     * @param t
     * @param f
     * @param m
     */
    public SessionEvent(Object o,String t,String f,String m)	// Online message
	{	this(o,t,f);  message=m;
	}

    /**
     *
     * @param o
     * @param t
     * @param f
     * @param m
     * @param dt
     */
    public SessionEvent(Object o,String t,String f,String m,String dt) // Offline message
	{	this(o,t,f,m);
		try { timestamp = new Date(Long.parseLong(dt)*1000); }
			catch(NumberFormatException e) { timestamp = null; }
	}


	// -----------------------------------------------------------------
	// Accessors
	// -----------------------------------------------------------------
    /**
     *
     * @return
     */
    public String getTo() { return to; }
    /**
     *
     * @return
     */
    public String getFrom() { return from; }
    /**
     *
     * @return
     */
    public String getMessage() { return message; }
    /**
     *
     * @return
     */
    public Date getTimestamp() { return timestamp; }

    /**
     *
     * @return
     */
    public long getStatus() { return status; }
    /**
     *
     * @param s
     */
    public void setStatus(long s) { status=s; }

	public String toString()
	{	return "to:"+to+" from:"+from+" message:"+message+
			" timestamp:"+timestamp;
	}
}
