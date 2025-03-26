package ymsg.network.util;

import java.io.IOException;
import java.io.OutputStream;

/**
 *
 * @author 7406030021
 */
public class FilterOutputStream extends OutputStream
{
    /**
     *
     */
    protected OutputStream out;

    /**
     *
     * @param underlying
     */
    protected FilterOutputStream(OutputStream underlying)
    {
        out = underlying;
    }

    /**
     *
     * @param b
     * @throws java.io.IOException
     */
    public void write(int b) throws IOException
    {
        out.write(b);
    }

    /**
     *
     * @param b
     * @throws java.io.IOException
     */
    public void write(byte[] b) throws IOException
    {
        write(b, 0, b.length);
    }

    /**
     *
     * @param b
     * @param offset
     * @param length
     * @throws java.io.IOException
     */
    public void write(byte[] b, int offset, int length) throws IOException
    {
        for (int i = 0; i < length; i++)
        {
            write(b[offset + i]);
        }
    }

    /**
     *
     * @throws java.io.IOException
     */
    public void flush() throws IOException
    {
        out.flush();
    }

    /**
     *
     * @throws java.io.IOException
     */
    public void close() throws IOException
    {
        out.close();
    }
}
