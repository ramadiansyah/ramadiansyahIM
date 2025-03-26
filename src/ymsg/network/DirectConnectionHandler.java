package ymsg.network;

import java.io.DataOutputStream;
import java.io.IOException;
import javax.microedition.io.Connector;
import javax.microedition.io.SocketConnection;



/**
 *
 * @author 7406030021
 */
public class DirectConnectionHandler extends ConnectionHandler
implements NetworkConstants
{	private String host = "scs.msg.yahoo.com";					// Yahoo IM host
	private int port;						// Yahoo IM port
	private boolean dontUseFallbacks=false;	// Don't use fallback port numbersac
	//private Session session;				// Associated session object
	// -----I/O
	private SocketConnection socket;					// Network connection
	private YMSG9InputStream ips;			// For receiving messages
	private DataOutputStream ops;			// For sending messages

	// -----------------------------------------------------------------
	// CONSTRUCTORS
	// -----------------------------------------------------------------
    /**
     *
     * @param h
     * @param p
     */
    public DirectConnectionHandler(String h,int p)
	{	host=h;  port=p;  dontUseFallbacks=true;
	}
    /**
     *
     * @param p
     */
    public DirectConnectionHandler(int p)
	{	this(Util.directHost(),p);
	}
    /**
     *
     * @param fl
     */
    public DirectConnectionHandler(boolean fl)
	{	this();  dontUseFallbacks=fl;
	}
    /**
     *
     */
    public DirectConnectionHandler()
	{	this( Util.directHost() , Util.directPort() );
		dontUseFallbacks=false;
	}

    /**
     *
     * @return
     */
    public String getHost() { return host; }
    /**
     *
     * @return
     */
    public int getPort() { return port; }

	// -----------------------------------------------------------------
	// **ConnectionHandler methods start
	// -----------------------------------------------------------------

	// -----------------------------------------------------------------
	// Session calls this when a connection handler is installed
	// -----------------------------------------------------------------
    void install(Session ss,Thread tg)
	{	//session=ss;
	}

	// -----------------------------------------------------------------
	// Opens a socket to Yahoo IM, or throws an exception.  If fallback
	// ports are to be used, will attempt each port in turn - upon failure
	// will return the last exception (the one for the final port).
	// -----------------------------------------------------------------
    void open() throws IOException
	{
        if(dontUseFallbacks){
            socket = (SocketConnection)Connector.open("socket://"+host+":"+port);
		}
		else
		{	int[] fallbackPorts = Util.directPorts();
			int i=0;
			while(socket==null)
			{	try{	
                    socket = (SocketConnection)Connector.open("socket://"+host+":"+fallbackPorts[i]);
					port=fallbackPorts[i];
				}
                catch(IOException e){
                    socket=null;  i++;
					if(i>=fallbackPorts.length)  throw e;
				}
			}
		}
        ips = new YMSG9InputStream(socket.openDataInputStream());
        ops = socket.openDataOutputStream();
	}

	// -----------------------------------------------------------------
	// Break socket connection
	// -----------------------------------------------------------------
	void close() throws IOException
	{	if(socket!=null)  socket.close();
		socket=null;
	}

    /**
     *
     * Note: the term 'packet' here refers to a YMSG message, not a TCP packet
     * (although in almost all cases the two will be synonymous).  This is to
     * avoid confusion with a 'YMSG message' - the actual discussion packet.
     * @param body
     * @param service  - the Yahoo service number
     * @param status - the Yahoo status number (not sessionStatus!)
     * @param sessionId - the payload of the packet
     * @throws java.io.IOException
     *
     * Note: it is assumed that 'ops' will have been set by the time
	// this method is called.
     */
    protected void sendPacket(PacketBodyBuffer body,int service,long status,long sessionId)
	throws IOException
	{	byte[] b = body.getBuffer();
		// -----Because the buffer is held at class member level, this method
		// -----is not automatically thread safe.  Besides, we should be only
		// -----sending one message at a time!
		synchronized(ops)
		{	// -----20 byte header
			ops.write(MAGIC,0,4);						// Magic code 'YMSG'
			ops.write(VERSION,0,4);						// Version
			ops.writeShort(b.length & 0xFFFF);			// Body length (16 bit unsigned)
			ops.writeShort(service & 0xFFFF);			// Service ID (16 bit unsigned
			ops.writeInt((int)(status & 0xFFFFFFFF));	// Status (32 bit unsigned)
			ops.writeInt((int)(sessionId & 0xFFFFFFFF)); // Session id (32 bit unsigned)
			// -----Then the body...
			ops.write(b,0,b.length);
			// -----Now send the buffer
			ops.flush();
		}
	}

    /**
     * Return a Yahoo message
     * @return
     * @throws java.io.IOException
     */
    protected YMSG9Packet receivePacket() throws IOException{
        return ips.readPacket();
	}

	// -----------------------------------------------------------------
	// ** ConnectionHandler methods end
	// -----------------------------------------------------------------

    /**
     *
     * @return
     */
    public String toString()
	{	return "Direct connection: "+host+":"+port;
	}
}
