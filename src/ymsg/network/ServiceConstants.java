package ymsg.network;

/**
 *
 * @author 7406030021
 */
public interface ServiceConstants {

    /**
     * 
     */
    public final static int SERVICE_LOGON = 0x01;
    /**
     *
     */
    public final static int SERVICE_LOGOFF			= 0x02;

    /**
     *
     */
    public final static int SERVICE_ISAWAY			= 0x03;
    /**
     *
     */
    public final static int SERVICE_ISBACK			= 0x04;
    /**
     *
     */
    public final static int SERVICE_IDLE			= 0x05;

    /**
     *
     */
    public final static int SERVICE_MESSAGE			= 0x06;

    /**
     *
     */
    public final static int SERVICE_IDACT			= 0x07;

     /**
     *
     */
    public final static int SERVICE_IDDEACT			= 0x08;
    /**
     *
     */
    public final static int SERVICE_MAILSTAT		= 0x09;
    /**
     *
     */
    public final static int SERVICE_USERSTAT		= 0x0a;
    /**
     *
     */
    public final static int SERVICE_NEWMAIL			= 0x0b;

    /**
     *
     */
    public final static int SERVICE_CONTACTNEW		= 0x0f;
    /**
     *
     */
    public final static int SERVICE_ADDIGNORE		= 0x11;
    /**
     *
     */
    public final static int SERVICE_PING			= 0x12;
    
    /**
     *
     */
    public final static int SERVICE_NOTIFY			= 0x4b;
    
    /**
     *
     */
    public final static int SERVICE_AUTHRESP		= 0x54;
    /**
     *
     */
    public final static int SERVICE_LIST			= 0x55;
    /**
     *
     */
    public final static int SERVICE_AUTH			= 0x57;
    /**
     *
     */
    public final static int SERVICE_FRIENDADD		= 0x83;
    /**
     *
     */
    public final static int SERVICE_FRIENDREMOVE	= 0x84;
    /**
     *
     */
    public final static int SERVICE_CONTACTIGNORE	= 0x85;
    /**
     *
     */
    public final static int SERVICE_CONTACTREJECT	= 0x86;

	// -----Home made service numbers, used in event dispatch only
    /**
     *
     */
    final static int SERVICE_X_ERROR				= 0xf00;
    /**
     *
     */
    final static int SERVICE_X_OFFLINE				= 0xf01;
    /**
     *
     */
    final static int SERVICE_X_EXCEPTION			= 0xf02;
    /**
     *
     */
    final static int SERVICE_X_BUZZ					= 0xf03;
}
