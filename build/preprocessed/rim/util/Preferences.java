package rim.util;

import javax.microedition.rms.*;
import java.io.*;
import java.util.Enumeration;
import java.util.Hashtable;

/**
 *
 * @author 7406030021
 */
public class Preferences {
	private RecordStore store;
    private String mRecordStoreName;
    private Hashtable mHashtable;

    /**
     *
     * @param recordStoreName
     */
    public void open(String recordStoreName) {
            mRecordStoreName = recordStoreName;//store = RecordStore.openRecordStore(recordStoreName, true);
    }
    /**
     *
     * @param password
     * @throws javax.microedition.rms.RecordStoreNotOpenException
     * @throws javax.microedition.rms.InvalidRecordIDException
     * @throws javax.microedition.rms.RecordStoreException
     */
    public void saveLockScreenPassword(String password) throws RecordStoreNotOpenException, InvalidRecordIDException, RecordStoreException {
        RecordEnumeration re = null;
        re = store.enumerateRecords(null, null, false);
         try{
        // First remove all records, a little clumsy.
        while (re.hasNextElement()) {
            int id = re.nextRecordId();
            store.deleteRecord(id);
        }
        byte[] raw = password.getBytes();
        store.addRecord(raw, 0, raw.length);
    }
        finally {
              if (re != null)
                  re.destroy();
             if (store != null) store.closeRecordStore();
        }
    }
    /**
     *
     * @return
     * @throws javax.microedition.rms.RecordStoreNotOpenException
     * @throws javax.microedition.rms.InvalidRecordIDException
     * @throws javax.microedition.rms.RecordStoreException
     */
    public String readLockScreenPassword() throws RecordStoreNotOpenException, InvalidRecordIDException, RecordStoreException {
              RecordEnumeration re = store.enumerateRecords(null, null, false);
             String pref = null;
              try{
              while (re.hasNextElement()) {
                byte[] raw = re.nextRecord();
                pref = new String(raw);
              }
             }
            //}
            finally {
              if (re != null)
                  re.destroy();
             if (store != null) store.closeRecordStore();
              return pref;
        }
              

    }
    /**
     *
     * @param key
     * @return
     */
    public String get(String key) {
        String str = (String)mHashtable.get(key);
    return str;
  }

  /**
   *
   * @param key
   * @param value
   */
  public void put(String key, String value) {
    if (value == null) value = "";
        mHashtable.put(key, value);
  }

  /**
   *
   * @throws javax.microedition.rms.RecordStoreException
   * @throws java.lang.NullPointerException
   */
  public void loadUsernameAndPassword() throws RecordStoreException, NullPointerException {
    RecordEnumeration re = null;
    mHashtable = new Hashtable();
    store = RecordStore.openRecordStore("rim_upp", true);
    try {
      re = store.enumerateRecords(null, null, false);
      while (re.hasNextElement()) {
        byte[] raw = re.nextRecord();
        String pref = new String(raw);
        // Parse out the name.
        int index = pref.indexOf('|');
        String name = pref.substring(0, index);
        String value = pref.substring(index + 1);
        put(name, value);
      }
    }
    finally {
      if (re != null) re.destroy();
      if (store != null) store.closeRecordStore();
    }
  }

  /**
   *
   * @param username
   * @param password
   * @throws javax.microedition.rms.RecordStoreException
   */
  public void saveUsernameAndPassword(String username, String password) throws RecordStoreException {
    mHashtable = new Hashtable();
    put("username",username);
    put("password",password);
    store = RecordStore.openRecordStore("rim_upp", true);
    RecordEnumeration re = null;
    try {
      re = store.enumerateRecords(null, null, false);
      while (re.hasNextElement()) {
        int id = re.nextRecordId();
        store.deleteRecord(id);
      }
      Enumeration keys = mHashtable.keys();
      while (keys.hasMoreElements()) {
        String key = (String)keys.nextElement();
        String value = get(key);
        String pref = key + "|" + value;
        byte[] raw = pref.getBytes();
        store.addRecord(raw, 0, raw.length);
      }
    }
    finally {
      if (re != null) re.destroy();
      if (store != null) store.closeRecordStore();
    }
  }

    public void writeStream(boolean[] bData) throws RecordStoreException, IOException{
        store = RecordStore.openRecordStore(mRecordStoreName, true);
        ByteArrayOutputStream strmBytes = new ByteArrayOutputStream();
        DataOutputStream strmDataType = new DataOutputStream(strmBytes);
        byte[] record;
        RecordEnumeration re = store.enumerateRecords(null, null, false);
        while (re.hasNextElement()) {
            int id = re.nextRecordId();
            store.deleteRecord(id);
        }
        for (int i = 0; i < bData.length; i++){
            strmDataType.writeBoolean(bData[i]);
            strmDataType.writeInt(i);
            strmDataType.flush();
            record = strmBytes.toByteArray();
            store.addRecord(record, 0, record.length);
            strmBytes.reset();
        }
        strmBytes.close();
        strmDataType.close();
    }

    public boolean[] readStream() throws RecordStoreException, IOException{
        boolean[] b = null;
        store = RecordStore.openRecordStore(mRecordStoreName, true);
        try{
            byte[] recData = new byte[50];
            ByteArrayInputStream strmBytes = new ByteArrayInputStream(recData);
            DataInputStream strmDataType = new DataInputStream(strmBytes);
            if (store.getNumRecords() > 0){
                Comparator comp = new Comparator();
                int i = 0;
                b = new boolean[store.getNumRecords()];
                RecordEnumeration re = store.enumerateRecords(null, comp, false);
                while (re.hasNextElement()){
                    store.getRecord(re.nextRecordId(), recData, 0);
                    b[i++] = strmDataType.readBoolean();
                    strmBytes.reset();
                }
                comp.compareClose();
                re.destroy();
            }
            strmBytes.close();
            strmDataType.close();
        }
        finally{
        if (store != null)
            store.closeRecordStore();
        }
        return b;
    }
}

class Comparator implements RecordComparator{
    private byte[] recData = new byte[10];
    private ByteArrayInputStream strmBytes = null;
    private DataInputStream strmDataType = null;
    public void compareClose(){
    try{
        if (strmBytes != null)
            strmBytes.close();
        if (strmDataType != null)
            strmDataType.close();
    }
    catch (Exception e){}
    }
    public int compare(byte[] rec1, byte[] rec2){
    int x1, x2;
    try{
        int maxsize = Math.max(rec1.length, rec2.length);
        if (maxsize > recData.length)
        recData = new byte[maxsize];
        strmBytes = new ByteArrayInputStream(rec1);
        strmDataType = new DataInputStream(strmBytes);
        strmDataType.readBoolean();
        x1 = strmDataType.readInt();
        strmBytes = new ByteArrayInputStream(rec2);
        strmDataType = new DataInputStream(strmBytes);
        strmDataType.readBoolean();
        x2 = strmDataType.readInt();
        if (x1 == x2)
            return RecordComparator.EQUIVALENT;
        else if (x1 < x2)
            return RecordComparator.PRECEDES;
        else
            return RecordComparator.FOLLOWS;
    }
    catch (Exception e){
        return RecordComparator.EQUIVALENT;
    }
    }
}
