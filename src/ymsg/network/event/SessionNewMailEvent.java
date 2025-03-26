package ymsg.network.event;


// *********************************************************************
//						MailCount	From	EmailA.	Subject	Message
// newMailReceived		y (gt 0)	n		n(?)	n		n
// -- ditto --	(v2)	y (eq 0)	y		y		y		y
// *********************************************************************
/**
 *
 * @author 7406030021
 */
public class SessionNewMailEvent extends SessionEvent {

    /**
     *
     */
    protected int mail;
    /**
     *
     */
    /**
     *
     */
    protected String subject,address;

	// -----------------------------------------------------------------
	// CONSTRUCTORS
	// -----------------------------------------------------------------
    /**
     *
     * @param o
     * @param ml
     */
    public SessionNewMailEvent(Object o,String ml)
	{	super(o);
		mail = Integer.parseInt(ml);
	}

    /**
     *
     * @param o
     * @param fr
     * @param em
     * @param sb
     */
    public SessionNewMailEvent(Object o,String fr,String em,String sb)
	{	super(o,null,fr,null);
		mail=0;  address=em;  subject=sb;
	}

	// -----------------------------------------------------------------
	// Accessors
	// -----------------------------------------------------------------
    /**
     *
     * @return
     */
    public int getMailCount() { return mail; }
    /**
     *
     * @return
     */
    public String getSubject() { return subject; }
    /**
     *
     * @return
     */
    public String getEmailAddress() { return address; }
    /**
     *
     * @return
     */
    public boolean isWholeMail() { return (mail==0); }

	public String toString()
	{	return super.toString()+" mail:"+mail+" addr:"+address+" subject:"+subject;
	}
}
