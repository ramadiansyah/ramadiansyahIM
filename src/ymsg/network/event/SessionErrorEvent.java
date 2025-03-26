package ymsg.network.event;

// *********************************************************************
//						Message		Service			Code
// errorPacketReceived	y			y (or null)		y (or -1)
// *********************************************************************
/**
 *
 * @author 7406030021
 */
public class SessionErrorEvent extends SessionEvent {

    /**
     * 
     */
    /**
     * 
     */
    protected int service,  code = -1;

	// -----------------------------------------------------------------
	// CONSTRUCTORS
	// -----------------------------------------------------------------
    /**
     *
     * @param o
     * @param m
     * @param sv
     */
    public SessionErrorEvent(Object o,String m,int sv)
	{	super(o);  message=m;  service=sv;
	}

    /**
     *
     * @param c
     */
    public void setCode(int c) { code=c; }

    /**
     *
     * @return
     */
    public int getService() { return service; }
    /**
     *
     * @return
     */
    public int getCode() { return code; }

	public String toString()
	{	return "Error: message=\""+message+"\" service=0x"+Integer.toHexString(service);
	}
}
