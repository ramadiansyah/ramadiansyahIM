package ymsg.network;

import java.util.Vector;

/**
 * Represents a single group of friend users in a Yahoo friends list.
 * @author FISH
 */
public class YahooGroup {

    /**
     *
     */
    protected String name;
    /**
     *
     */
    protected boolean open;
    /**
     *
     */
    protected Vector users;

	YahooGroup(String n,boolean o)
	{	name=n;  open=o;
		users = new Vector();
	}

	YahooGroup(String n)
	{	this(n,true);
	}

	void addUser(YahooUser yu) { users.addElement(yu); }
	void removeUserAt(int i) { users.removeElementAt(i); }
	boolean isEmpty() { return (users.size()<=0); }

    /**
     *
     * @return
     */
    public String getName() { return name; }
    /**
     *
     * @return
     */
    public boolean isOpen() { return open; }
    /**
     *
     * @param b
     */
    public void setOpen(boolean b) { open=b; }
    /**
     *
     * @return
     */
    public Vector getMembers() {
        //return (Vector)users.clone();
        return (Vector)users;
    }
    /**
     *
     * @param i
     * @return
     */
    public YahooUser getUserAt(int i) { return (YahooUser)users.elementAt(i); }

    /**
     *
     * @param id
     * @return
     */
    public synchronized int getIndexOfFriend(String id)
	{	for(int i=0;i<users.size();i++)
		{	if( ((YahooUser)users.elementAt(i)).getId().equals(id) )
				return i;
		}
		return -1;
	}

    /**
     *
     * @return
     */
    public String toString()
	{	return "name="+name+" open?="+open;
	}
}
