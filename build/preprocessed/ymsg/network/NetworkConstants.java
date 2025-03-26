package ymsg.network;
/**
 * Various constant settings for network code.
 * @author FISH , stripped down by Rizki Eka Saputra Ramadiansyah
 */
public interface NetworkConstants
{	// -----Header constants
	// -----Note: between versions 10 and 12 the version field appears to have 
	// -----swapped endian-ness, from 0x0a00 to 0x000c
    /**
     *
     */
    final static byte PROTOCOL = 0x0d;	// Protocol version 10 (YMSG 10)
    /**
     *
     */
    final static byte[] MAGIC = { 'Y','M','S','G' };
    /**
     *
     */
    final static byte[] VERSION = { 0x00,PROTOCOL,0x00,0x00 };
    /**
     *
     */
    final static byte[] VERSION_HTTP = { PROTOCOL,0x00,(byte)0xc8,0x00 };
    /**
     *
     */
    final static int YMSG9_HEADER_SIZE = 20;
    /**
     *
     */
    final static String CLIENT_VERSION = "8.1.0.209";

	
	// -----HTTP
    /**
     *
     */
    final static String USER_AGENT = "Mozilla/4.5 [en] (X11; U; FreeBSD 2.2.8-STABLE i386)";
    /**
     *
     */
    final static String END = "\n";	// Line terminator

	

	// -----Cookies in array (see Session.getCookies())
    /**
     *
     */
    final static int COOKIE_Y = 0;
    /**
     *
     */
    final static int COOKIE_T = 1;
    /**
     *
     */
    final static int COOKIE_C = 2;

	// -----Default timouts (seconds)
    /**
     *
     */
    final static int LOGIN_TIMEOUT = 60;

	// -----Ping timeout
    /**
     *
     */
    final static int PING_TIMEOUT = 1000*60*20;	// 20 minutes


	// -----Chat server

	// -----Buzz string
    /**
     *
     */
    final static String BUZZ = "<ding>";
}
