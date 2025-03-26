package ymsg.network.util;

import java.io.IOException;
import java.io.InputStream;

/**
 *
 * @author 7406030021
 */
public class FilterInputStream extends InputStream
{
    /**
     *
     */
    protected InputStream in;

    /**
     *
     * @param underlying
     */
    protected FilterInputStream(InputStream underlying)
    {
        in = underlying;
    }

    /**
     *
     * @return
     * @throws java.io.IOException
     */
    public int read() throws IOException
    {
        return in.read();
    }

    /**
     *
     * @param b
     * @return
     * @throws java.io.IOException
     */
    public int read(byte[] b) throws IOException
    {
        return read(b, 0, b.length);
    }

    /**
     *
     * @param b
     * @param offset
     * @param length
     * @return
     * @throws java.io.IOException
     */
    public int read(byte[] b, int offset, int length) throws IOException
    {
        return in.read(b, offset, length);
    }

    /**
     *
     * @param n
     * @return
     * @throws java.io.IOException
     */
    public long skip(long n) throws IOException
    {
        return in.skip(n);
    }

    /**
     *
     * @return
     * @throws java.io.IOException
     */
    public int available() throws IOException
    {
        return in.available();
    }

    /**
     *
     * @throws java.io.IOException
     */
    public void close() throws IOException
    {
        in.close();
    }

    /**
     *
     * @param readlimit
     */
    public void mark(int readlimit)
    {
        in.mark(readlimit);
    }

    /**
     *
     * @throws java.io.IOException
     */
    public void reset() throws IOException
    {
        in.reset();
    }

    /**
     *
     * @return
     */
    public boolean markSupported()
    {
        return in.markSupported();
    }
}
