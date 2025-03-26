package ymsg.network.event;

import ymsg.network.StatusConstants;

// *********************************************************************
// This event is used to convey Yahoo notification events, like typing
// on/off from other Yahoo users we're communicating with.
//
//					To		From	Message		Type	Mode
// notifyReceived	y		y		y			y		y
// *********************************************************************
/**
 *
 * @author 7406030021
 */
public class SessionNotifyEvent extends SessionEvent {

    /**
     *
     */
    protected String type;
    /**
     *
     */
    protected int mode;

	// -----------------------------------------------------------------
	// CONSTRUCTOR
	// -----------------------------------------------------------------
    /**
     *
     * @param o
     * @param t
     * @param f
     * @param m
     * @param ty
     * @param md
     */
    public SessionNotifyEvent(Object o,String t,String f,String m,String ty,String md)
	{	super(o,t,f,m);
		type=ty;  mode=Integer.parseInt(md);
	}

    /**
     *
     * @return
     */
    public int getMode() { return mode; }
    /**
     *
     * @return
     */
    public String getType() { return type; }
    /**
     *
     * @return
     */
    public String getGame() { return getMessage(); }
    /**
     *
     * @return
     */
    public boolean isTyping() { return (type!=null && type.equalsIgnoreCase(StatusConstants.NOTIFY_TYPING)); }
    /**
     *
     * @return
     */
    public boolean isGame() { return (type!=null && type.equalsIgnoreCase(StatusConstants.NOTIFY_GAME)); }

	public String toString()
	{	return super.toString()+" type:"+type+" mode:"+mode;
	}
}
