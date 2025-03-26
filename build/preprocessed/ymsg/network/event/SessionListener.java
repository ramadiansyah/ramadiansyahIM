package ymsg.network.event;

/**
 *
 * @author 7406030021
 */
public interface SessionListener
{	
	// Yahoo has logged us off the system, or the connection was lost
    /**
     *
     * @param ev
     */
    public void connectionClosed(SessionEvent ev);

	// A list (friends and groups) update has been received
    /**
     *
     * @param ev
     */
    public void listReceived(SessionEvent ev);

	// Someone has sent us a message
	//  to - the target (us!)
	//  from - the user who sent the message
	//  message - the message text
    /**
     *
     * @param ev
     */
    public void messageReceived(SessionEvent ev);

	// Someone has sent us a buzz message
	//  to - the target (us!)
	//  from - the user who sent the message
	//  message - the message text
    /**
     *
     * @param ev
     */
    public void buzzReceived(SessionEvent ev);

	// Yahoo tells us about a message sent while we were away
	//  to - the target (us!)
	//  from - the user who sent the message
	//  message - the message text
	//  datestamp - the date the message was sent (?)
    /**
     *
     * @param ev
     */
    public void offlineMessageReceived(SessionEvent ev);

	// Message contained tag 16.  (These are commonly sent when trying
	// to ignore/unignore users already ignored/unignored).
    /**
     *
     * @param ev
     */
    public void errorPacketReceived(SessionErrorEvent ev);

	// The input thread has thrown an exception.  (Of course, this
	// should *never* happen :-)
    /**
     *
     * @param ev
     */
    public void inputExceptionThrown(SessionExceptionEvent ev);

	// Yahoo tells us we have unread Yahoo mail
	//  mail - number of unread mails
    /**
     *
     * @param ev
     */
    public void newMailReceived(SessionNewMailEvent ev);

	// Yahoo server wants to notify us of something
	//  service - the type of request
	//  to - the target (us!)
	//  from - the user who sent the message
	//  mode - 0=off/1=on (for typing)
    /**
     *
     * @param ev
     */
    public void notifyReceived(SessionNotifyEvent ev);

	// Someone wants to add us to their friends list
	//  to - the target (us!)
	//  from - the user who wants to add us
	//  message - the request message text
    /**
     *
     * @param ev
     */
    public void contactRequestReceived(SessionEvent ev);

	// Someone has rejected our attempts to add them to our friends list
	//  from - the user who rejected us
	//  message - rejection message text
    /**
     *
     * @param ev
     */
    public void contactRejectionReceived(SessionEvent ev);
	// Friend's details have been updated
	//  friends - vector of updated YahooUser's
    /**
     *
     * @param ev
     */
    public void friendsUpdateReceived(SessionFriendEvent ev);

	// Successfully added a friend
	//  friend - YahooUser of friend
	//  group - name of group added to
    /**
     *
     * @param ev
     */
    public void friendAddedReceived(SessionFriendEvent ev);

	// Successfully removed a friend
	//  friend - YahooUser of friend
	//  group - name of group removed from
    /**
     *
     * @param ev
     */
    public void friendRemovedReceived(SessionFriendEvent ev);

}
