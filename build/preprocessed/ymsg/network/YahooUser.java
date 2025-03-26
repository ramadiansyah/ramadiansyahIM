package ymsg.network;
// This class represents a single canonical represenation of a user on
// Yahoo.  For each user there is only ever one single instance of this
// class - as updated details arrive, an existing object is sought for
// and updated.  This is to prevent code holding onto YahooUser references
// from pointing to stale data.
//
// The groupCount integer is used to service the isFriend() method.  When
// added to a group it is incremented, when removed it is decremented.  
// When zero, it means this user is not part of the client's friends list.
//
// Note: this API cannot guarantee the accuracy of details held on users
// who's contact with you has expired.  So... if you leave a chatroom
// in which 'fred' was a member, 'fred's object will continue to be in
// the hashtable - BUT his status will almost certainly not be updated
// any longer (unless, of course, you have a link with him via some other
// route - like if he is on your friends list too!)
// *********************************************************************
/**
 *
 * @author 7406030021
 */
public class YahooUser {

    /**
     *
     */
    protected String id;							// Yahoo id
    /**
     *
     */
    protected long status;							// Status (away, etc)
    /**
     *
     */
    /**
     *
     */
    /**
     *
     */
    protected boolean onChat,onPager,ignored;		// Flags
    /**
     *
     */
    protected String customStatusMessage;			// Custom away
    /**
     *
     */
    protected boolean customStatusBusy;				// Ditto
	private int groupCount=0;						// Friends group count

	// -----------------------------------------------------------------
	// CONSTRUCTORS
	// Do not called these manually.  UserStore is a cache of canonical 
	// YahooUser objects - use the getOrCreate() methods in that class so
	// the user objects are correctly added to that cache.
	// -----------------------------------------------------------------
	YahooUser(String i,long st,boolean ch,boolean pg)
	{	update(i,st,ch,pg);
	}

	YahooUser(String i,String st,String ch,String pg)
	{	update(i,st,ch,pg);
	}

	YahooUser(String i)
	{	this(i,StatusConstants.STATUS_OFFLINE,false,false);
	}


	// -----------------------------------------------------------------
	// Accessors
	// -----------------------------------------------------------------
	void setIgnored(boolean i)
	{	ignored=i;
	}
	void setCustom(String m,String a)
	{	customStatusMessage=m;  customStatusBusy=a.charAt(0)=='1';
	}

	void adjustGroupCount(int inc) { groupCount+=inc; }

	// -----------------------------------------------------------------
	// Public accessors
	// -----------------------------------------------------------------
    /**
     *
     * @return
     */
    public String getId() { return id; }
    /**
     *
     * @return
     */
    public long getStatus() { return status; }
    /**
     *
     * @return
     */
    public boolean isOnChat() { return onChat; }
    /**
     *
     * @return
     */
    public boolean isOnPager() { return onPager; }
    /**
     *
     * @return
     */
    public boolean isLoggedIn() { return (onChat||onPager); }
    /**
     *
     * @return
     */
    public boolean isIgnored() { return ignored; }
    /**
     *
     * @return
     */
    public String getCustomStatusMessage() { return customStatusMessage; }
    /**
     *
     * @return
     */
    public boolean isCustomBusy() { return customStatusBusy; }
    /**
     *
     * @return
     */
    public boolean isFriend() { return (groupCount>0); }

    /**
     *
     * @return
     */
    public String toString()
	{
        /*
         int cStatus = (int) status;
        return "id="+id+" status=0x"+Integer.toHexString(cStatus)+
			" chat?="+onChat+" pager?="+onPager+" ignored?="+ignored+
			" custMesg="+customStatusMessage+" custBusy?="+customStatusBusy+
			" friend?="+isFriend();
         */
        String strStatus = Long.toString(status);
        int intStatus = Integer.parseInt(strStatus);
        return "id="+id+" status=0x"+
                Integer.toHexString(intStatus)+
			" chat?="+onChat+" pager?="+onPager+" ignored?="+ignored+
			" custMesg="+customStatusMessage+" custBusy?="+customStatusBusy+
			" friend?="+isFriend();
	}

	// -----------------------------------------------------------------
	// Update this object's details
	// -----------------------------------------------------------------
	// -----This is the new version, where 13=combined pager/chat
	void update(String i,String st,String vs)
	{	int visibility=0;
		visibility = (vs==null) ? 0 : Integer.parseInt(vs);
		update( i , Long.parseLong(st) , (visibility&2)>0 , (visibility&1)>0 );
	}
	// -----This is the old version, when 13=pager and 17=chat
	void update(String i,String st,String ch,String pg)
	{	update( i , Long.parseLong(st) , 
			(ch!=null && ch.charAt(0)=='1') , (pg!=null && pg.charAt(0)=='1') );
	}
    //Harus Di Bedah di sini biar bisa custom status
	void update(String i,long st,boolean ch,boolean pg)
	{	id=i;  status=st;  onChat=ch;  onPager=pg;
		if(status != StatusConstants.STATUS_CUSTOM)
		{	customStatusMessage=null;  customStatusBusy=false;
		}
	}
}
