

package ymsg.network;

import ymsg.network.util.digest.MD5Digest;
//import ymsg.network.util.digest.UnixMD5Crypt;

class ChallengeResponseUtility{
    private static final String Y64 = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789._";
    protected static final MD5Digest md5Obj = new MD5Digest();
    ChallengeResponseUtility(){
    }

    
    static String yahoo64(byte buffer[]){
        int limit = buffer.length - buffer.length % 3;
        String out = "";
        int buff[] = new int[buffer.length];
        for(int i = 0; i < buffer.length; i++)
            buff[i] = buffer[i] & 0xff;

        for(int i = 0; i < limit; i += 3){
            // -----Top 6 bits of first byte
            out = out + "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789._".charAt(buff[i] >> 2);
            // -----Bottom 2 bits of first byte append to top 4 bits of second
            out = out + "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789._".charAt(buff[i] << 4 & 0x30 | buff[i + 1] >> 4);
            // -----Bottom 4 bits of second byte appended to top 2 bits of third
            out = out + "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789._".charAt(buff[i + 1] << 2 & 0x3c | buff[i + 2] >> 6);
            // -----Bottom six bits of third byte
            out = out + "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789._".charAt(buff[i + 2] & 0x3f);
        }
        // -----Do we still have a remaining 1 or 2 bytes left?
        switch(buff.length - limit){
        case 1: // '\001'
            // -----Top 6 bits of first byte
            out = out + "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789._".charAt(buff[limit] >> 2);
            // -----Bottom 2 bits of first byte
            out = out + "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789._".charAt(buff[limit] << 4 & 0x30);
            out = out + "--";
            break;

        case 2: // '\002'
            // -----Top 6 bits of first byte
            out = out + "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789._".charAt(buff[limit] >> 2);
            // -----Bottom 2 bits of first byte append to top 4 bits of second
            out = out + "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789._".charAt(buff[limit] << 4 & 0x30 | buff[limit + 1] >> 4);
            // -----Bottom 4 bits of second byte
            out = out + "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789._".charAt(buff[limit + 1] << 2 & 0x3c);
            out = out + "-";
            break;
        }
        return out;
    }

    /*

    
	static byte[] md5(String s) throws NoSuchAlgorithmException
	{	return md5(s.getBytes());
	}
	static byte[] md5(byte[] buff) throws NoSuchAlgorithmException
	{	return MessageDigest.getInstance("MD5").digest(buff);
	}
	static byte[] md5Singleton(byte[] buff) throws NoSuchAlgorithmException
	{	md5Obj.reset();  return md5Obj.digest(buff);
	}

     */

    // -----------------------------------------------------------------
    // Return the MD5 or a string and byte array (note: md5Singleton()
    // is easier on the object heap, but is NOT thread safe.  It's ideal
    // for doing lots of hashing inside a tight loop - but remember to
    // mutex lock 'md5Obj' before using it!)
    // -----------------------------------------------------------------
    static byte[] md5(String s){
        return md5(s.getBytes());
    }

    static byte[] md5(byte buff[]){
        MD5Digest md5Digest = new MD5Digest();
        md5Digest.update(buff, 0, buff.length);
        byte result[] = new byte[md5Digest.getDigestSize()];
        md5Digest.doFinal(result, 0);
        return result;
    }

    static byte[] md5Singleton(byte buff[]){
        md5Obj.reset();
        md5Obj.update(buff, 0, buff.length);
        byte result[] = new byte[md5Obj.getDigestSize()];
        md5Obj.doFinal(result, 0);
        return result;
    }
    // -----------------------------------------------------------------
    // Return the MD5Crypt of a string and salt
    // -----------------------------------------------------------------
    static byte[] md5Crypt(String k, String s){
        return UnixMD5Crypt.crypt(k, s).getBytes();
    }
}